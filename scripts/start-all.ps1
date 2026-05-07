param(
    [switch]$SkipBuild,
    [switch]$NoBrowser
)

$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent $PSScriptRoot
$RunDir = Join-Path $Root ".run"
$LogDir = Join-Path $RunDir "logs"
$PidFile = Join-Path $RunDir "pids.json"
$MavenSettings = Join-Path $Root "infra\maven\settings.xml"

$Services = @(
    @{ Name = "auth-service"; Port = 9001; Jar = "backend\auth-service\target\auth-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "user-service"; Port = 9003; Jar = "backend\user-service\target\user-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "product-service"; Port = 9004; Jar = "backend\product-service\target\product-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "inventory-service"; Port = 9005; Jar = "backend\inventory-service\target\inventory-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "cart-service"; Port = 9006; Jar = "backend\cart-service\target\cart-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "order-service"; Port = 9007; Jar = "backend\order-service\target\order-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "payment-service"; Port = 9008; Jar = "backend\payment-service\target\payment-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "seckill-service"; Port = 9009; Jar = "backend\seckill-service\target\seckill-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "merchant-service"; Port = 9010; Jar = "backend\merchant-service\target\merchant-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "admin-service"; Port = 9011; Jar = "backend\admin-service\target\admin-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "message-service"; Port = 9012; Jar = "backend\message-service\target\message-service-0.1.0-SNAPSHOT.jar" },
    @{ Name = "gateway-service"; Port = 9000; Jar = "backend\gateway-service\target\gateway-service-0.1.0-SNAPSHOT.jar" }
)

$CommonArgs = @(
    "--spring.profiles.active=local",
    "--spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration,org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration",
    "--spring.cloud.nacos.discovery.enabled=false",
    "--spring.cloud.nacos.config.enabled=false",
    "--spring.cloud.service-registry.auto-registration.enabled=false",
    "--spring.cloud.discovery.enabled=false",
    "--seata.enabled=false"
)

function Write-Step($Message) {
    Write-Host ""
    Write-Host "==> $Message" -ForegroundColor Cyan
}

function Test-Port($Port) {
    $client = New-Object System.Net.Sockets.TcpClient
    try {
        $async = $client.BeginConnect("127.0.0.1", $Port, $null, $null)
        if (-not $async.AsyncWaitHandle.WaitOne(300, $false)) {
            return $false
        }
        $client.EndConnect($async)
        return $true
    } catch {
        return $false
    } finally {
        $client.Close()
    }
}

function Wait-Port($Name, $Port, $Seconds) {
    $deadline = (Get-Date).AddSeconds($Seconds)
    while ((Get-Date) -lt $deadline) {
        if (Test-Port $Port) {
            Write-Host "    $Name is ready on port $Port" -ForegroundColor Green
            return
        }
        Start-Sleep -Milliseconds 800
    }
    throw "$Name startup timed out on port $Port. Check logs under $LogDir."
}

function Stop-RecordedProcesses {
    if (-not (Test-Path $PidFile)) {
        return
    }

    Write-Step "Stopping processes from previous run"
    $records = Get-Content $PidFile -Raw | ConvertFrom-Json
    foreach ($record in $records) {
        $process = Get-Process -Id $record.pid -ErrorAction SilentlyContinue
        if ($null -eq $process) {
            continue
        }
        try {
            $commandLine = (Get-CimInstance Win32_Process -Filter "ProcessId=$($record.pid)").CommandLine
            if ($commandLine -and $commandLine.Contains($Root)) {
                Stop-Process -Id $record.pid -Force
                Write-Host "    Stopped $($record.name) (PID $($record.pid))"
            }
        } catch {
            Write-Host "    Skipped $($record.name) (PID $($record.pid))"
        }
    }
    Remove-Item $PidFile -Force -ErrorAction SilentlyContinue
}

function Start-LoggedProcess($Name, $FilePath, $ArgumentList, $WorkingDirectory) {
    $stdout = Join-Path $LogDir "$Name.out.log"
    $stderr = Join-Path $LogDir "$Name.err.log"
    $process = Start-Process `
        -FilePath $FilePath `
        -ArgumentList $ArgumentList `
        -WorkingDirectory $WorkingDirectory `
        -RedirectStandardOutput $stdout `
        -RedirectStandardError $stderr `
        -WindowStyle Hidden `
        -PassThru
    Write-Host "    Started $Name, PID $($process.Id), log $stdout"
    return $process
}

function Save-ProcessRecords($Records) {
    $Records | ConvertTo-Json -Depth 3 | Set-Content -Path $PidFile -Encoding UTF8
}

New-Item -ItemType Directory -Force -Path $RunDir, $LogDir | Out-Null

Write-Step "Checking runtime tools"
java -version
mvn -version | Select-Object -First 1
npm --version | Out-Host

Stop-RecordedProcesses

if (-not $SkipBuild) {
    Write-Step "Packaging backend services"
    & mvn -s $MavenSettings -DskipTests package

    Write-Step "Preparing frontend dependencies"
    if (-not (Test-Path (Join-Path $Root "frontend\node_modules"))) {
        Push-Location (Join-Path $Root "frontend")
        try {
            & npm --cache .\.npm-cache install
        } finally {
            Pop-Location
        }
    }
}

$records = New-Object System.Collections.ArrayList

Write-Step "Starting backend services"
foreach ($service in $Services) {
    $jar = Join-Path $Root $service.Jar
    if (-not (Test-Path $jar)) {
        throw "Jar not found for $($service.Name): $jar. Run without -SkipBuild and retry."
    }
    if (Test-Port $service.Port) {
        throw "Port $($service.Port) is already in use; cannot start $($service.Name)."
    }
    $args = @("-jar", $jar) + $CommonArgs
    $process = Start-LoggedProcess $service.Name "java" $args $Root
    [void]$records.Add([ordered]@{ name = $service.Name; pid = $process.Id; port = $service.Port })
    Save-ProcessRecords $records
    Wait-Port $service.Name $service.Port 45
}

Write-Step "Starting frontend dev server"
if (Test-Port 5173) {
    throw "Port 5173 is already in use; cannot start frontend."
}
$frontend = Start-LoggedProcess "frontend" "npm.cmd" @("--cache", ".\.npm-cache", "run", "dev") (Join-Path $Root "frontend")
[void]$records.Add([ordered]@{ name = "frontend"; pid = $frontend.Id; port = 5173 })
Save-ProcessRecords $records
Wait-Port "frontend" 5173 45

Save-ProcessRecords $records

Write-Step "Checking gateway API"
$products = Invoke-RestMethod -Uri "http://localhost:9000/api/products" -TimeoutSec 10
if (-not $products.success) {
    throw "Gateway API returned failure: $($products | ConvertTo-Json -Depth 5)"
}

Write-Host ""
Write-Host "All services are running." -ForegroundColor Green
Write-Host "  Frontend: http://localhost:5173"
Write-Host "  Gateway:  http://localhost:9000"
Write-Host "  Accounts: user / password, merchant / password, admin / password"
Write-Host "  Logs:     $LogDir"
Write-Host ""
Write-Host "Stop services with: powershell -NoProfile -ExecutionPolicy Bypass -File scripts\stop-all.ps1"

if (-not $NoBrowser) {
    Start-Process "http://localhost:5173"
}

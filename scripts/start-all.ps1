param(
    [switch]$SkipBuild,
    [switch]$NoBrowser,
    [switch]$SmokeTest
)

$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent $PSScriptRoot
$RunDir = Join-Path $Root ".run"
$LogDir = Join-Path $RunDir "logs"
$PidFile = Join-Path $RunDir "pids.json"
$MavenSettings = Join-Path $Root "infra\maven\settings.xml"
$SqlInitFile = Join-Path $Root "infra\sql\init-local-mysql.sql"

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

$Frontend = @{ Name = "frontend"; Port = 5173 }
$AppPorts = @($Frontend.Port) + ($Services | ForEach-Object { $_.Port })
$InfraPorts = @(
    @{ Name = "mysql"; Port = 3306 },
    @{ Name = "redis"; Port = 6379 },
    @{ Name = "nacos"; Port = 8848 },
    @{ Name = "rabbitmq"; Port = 5672 }
)
$InfraContainers = @(
    "payment-mysql",
    "payment-redis",
    "payment-rabbitmq",
    "payment-nacos",
    "payment-sentinel",
    "payment-seata"
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

function Wait-MySql($Seconds) {
    $deadline = (Get-Date).AddSeconds($Seconds)
    while ((Get-Date) -lt $deadline) {
        docker exec -e MYSQL_PWD=root payment-mysql mysqladmin ping -h127.0.0.1 -uroot --silent | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "    mysql accepts SQL connections" -ForegroundColor Green
            return
        }
        Start-Sleep -Milliseconds 800
    }
    throw "mysql did not become ready for SQL connections."
}

function Get-PortProcessId($Port) {
    $line = netstat -ano | Select-String -Pattern ":$Port\s+.*LISTENING" | Select-Object -First 1
    if ($null -eq $line) {
        return $null
    }
    $parts = ($line.ToString().Trim() -split "\s+")
    return [int]$parts[$parts.Length - 1]
}

function Stop-WorkspaceProcess($ProcessId, $Name) {
    $process = Get-Process -Id $ProcessId -ErrorAction SilentlyContinue
    if ($null -eq $process) {
        return
    }
    Stop-Process -Id $ProcessId -Force
    Write-Host "    Stopped $Name (PID $ProcessId)"
}

function Stop-RecordedProcesses {
    if (-not (Test-Path $PidFile)) {
        return
    }

    $records = Get-Content $PidFile -Raw | ConvertFrom-Json
    foreach ($record in $records) {
        Stop-WorkspaceProcess $record.pid $record.name
    }
    Remove-Item $PidFile -Force -ErrorAction SilentlyContinue
}

function Stop-ProcessesOnAppPorts {
    foreach ($port in $AppPorts) {
        $processId = Get-PortProcessId $port
        if ($null -ne $processId) {
            Stop-WorkspaceProcess $processId "port-$port"
        }
    }
}

function Stop-InfrastructureContainers {
    foreach ($container in $InfraContainers) {
        $containerId = docker ps -a --filter "name=^/$container$" --format "{{.ID}}"
        if ($containerId) {
            docker rm -f $container | Out-Null
        }
    }
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

$records = New-Object System.Collections.ArrayList
$infraStarted = $false

try {
    New-Item -ItemType Directory -Force -Path $RunDir, $LogDir | Out-Null

    Write-Step "Checking runtime tools"
    java -version
    mvn -version | Select-Object -First 1
    npm --version | Out-Host
    docker --version | Out-Host

    Write-Step "Stopping previously started frontend and backend"
    Stop-RecordedProcesses
    Stop-ProcessesOnAppPorts

    Write-Step "Stopping previously started infrastructure"
    Stop-InfrastructureContainers

    Write-Step "Starting infrastructure"
    Push-Location (Join-Path $Root "infra")
    try {
        docker compose up -d
        if ($LASTEXITCODE -ne 0) {
            throw "docker compose up failed with exit code $LASTEXITCODE"
        }
        $infraStarted = $true
    } finally {
        Pop-Location
    }
    foreach ($infra in $InfraPorts) {
        Wait-Port $infra.Name $infra.Port 90
    }
    Wait-MySql 90
    Write-Step "Applying database schema"
    Get-Content $SqlInitFile -Raw | docker exec -i -e MYSQL_PWD=root payment-mysql mysql -h127.0.0.1 -uroot
    if ($LASTEXITCODE -ne 0) {
        throw "database schema initialization failed with exit code $LASTEXITCODE"
    }

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

    Write-Step "Starting backend services"
    foreach ($service in $Services) {
        $jar = Join-Path $Root $service.Jar
        if (-not (Test-Path $jar)) {
            throw "Jar not found for $($service.Name): $jar. Run without -SkipBuild and retry."
        }
        if (Test-Port $service.Port) {
            throw "Port $($service.Port) is still in use; cannot start $($service.Name)."
        }
        $args = @("-jar", $jar, "--spring.profiles.active=local")
        $process = Start-LoggedProcess $service.Name "java" $args $Root
        [void]$records.Add([ordered]@{ name = $service.Name; pid = $process.Id; port = $service.Port })
        Save-ProcessRecords $records
        Wait-Port $service.Name $service.Port 60
    }

    Write-Step "Starting frontend dev server"
    if (Test-Port $Frontend.Port) {
        throw "Port $($Frontend.Port) is still in use; cannot start frontend."
    }
    $frontendProcess = Start-LoggedProcess "frontend" "npm.cmd" @("--cache", ".\.npm-cache", "run", "dev") (Join-Path $Root "frontend")
    [void]$records.Add([ordered]@{ name = "frontend"; pid = $frontendProcess.Id; port = $Frontend.Port })
    Save-ProcessRecords $records
    Wait-Port "frontend" $Frontend.Port 45

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
    Write-Host "Keep this window open. Press Ctrl+C to stop frontend and backend."

    if (-not $NoBrowser) {
        Start-Process "http://localhost:5173/login"
    }

    if ($SmokeTest) {
        Write-Host "Smoke test completed; stopping services."
        return
    }

    while ($true) {
        Start-Sleep -Seconds 2
    }
} finally {
    if ($records.Count -gt 0 -or (Test-Path $PidFile)) {
        Write-Step "Stopping frontend and backend"
        Stop-RecordedProcesses
        Stop-ProcessesOnAppPorts
    }
    if ($infraStarted) {
        Write-Step "Stopping infrastructure"
        Push-Location (Join-Path $Root "infra")
        try {
            docker compose down
            Stop-InfrastructureContainers
        } catch {
            Write-Host "    Failed to stop infrastructure with Docker: $($_.Exception.Message)" -ForegroundColor Yellow
        } finally {
            Pop-Location
        }
    }
}

$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent $PSScriptRoot
$RunDir = Join-Path $Root ".run"
$PidFile = Join-Path $RunDir "pids.json"
$AppPorts = @(5173, 9000, 9001, 9003, 9004, 9005, 9006, 9007, 9008, 9009, 9010, 9011, 9012)
$InfraContainers = @(
    "payment-mysql",
    "payment-redis",
    "payment-rabbitmq",
    "payment-nacos",
    "payment-sentinel",
    "payment-seata"
)

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
    Write-Host "Stopped $Name (PID $ProcessId)."
}

if (Test-Path $PidFile) {
    $records = Get-Content $PidFile -Raw | ConvertFrom-Json
    foreach ($record in $records) {
        Stop-WorkspaceProcess $record.pid $record.name
    }
    Remove-Item $PidFile -Force -ErrorAction SilentlyContinue
}

foreach ($port in $AppPorts) {
    $processId = Get-PortProcessId $port
    if ($null -ne $processId) {
        Stop-WorkspaceProcess $processId "port-$port"
    }
}

if (Get-Command docker -ErrorAction SilentlyContinue) {
    Push-Location (Join-Path $Root "infra")
    try {
        docker compose down
        foreach ($container in $InfraContainers) {
            $containerId = docker ps -a --filter "name=^/$container$" --format "{{.ID}}"
            if ($containerId) {
                docker rm -f $container | Out-Null
            }
        }
    } catch {
        Write-Host "Failed to stop infrastructure with Docker: $($_.Exception.Message)"
    } finally {
        Pop-Location
    }
}

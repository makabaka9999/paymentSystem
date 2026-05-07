$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent $PSScriptRoot
$RunDir = Join-Path $Root ".run"
$PidFile = Join-Path $RunDir "pids.json"

if (-not (Test-Path $PidFile)) {
    Write-Host "No process record found."
    exit 0
}

$records = Get-Content $PidFile -Raw | ConvertFrom-Json
foreach ($record in $records) {
    $process = Get-Process -Id $record.pid -ErrorAction SilentlyContinue
    if ($null -eq $process) {
        Write-Host "$($record.name) is not running."
        continue
    }

    $commandLine = (Get-CimInstance Win32_Process -Filter "ProcessId=$($record.pid)").CommandLine
    if ($commandLine -and $commandLine.Contains($Root)) {
        Stop-Process -Id $record.pid -Force
        Write-Host "Stopped $($record.name) (PID $($record.pid))."
    } else {
        Write-Host "Skipped $($record.name) (PID $($record.pid)); it was not started from this workspace."
    }
}

Remove-Item $PidFile -Force -ErrorAction SilentlyContinue

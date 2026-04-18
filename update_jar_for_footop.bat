@echo off
setlocal enabledelayedexpansion

REM 1. Check if VS Code is running
tasklist /FI "IMAGENAME eq Code.exe" | find "Code.exe" >nul
if errorlevel 1 (
    echo VS Code is not running. Continuing...
) else (
    echo VS Code is running. Closing it...

    taskkill /IM Code.exe >nul 2>&1
    taskkill /IM Code.exe /F >nul 2>&1

    REM Reset exit code so PowerShell does NOT abort
    cmd /c exit /b 0

    timeout /t 1 /nobreak >nul
)

REM 2. Move to script directory
cd /d "%~dp0"
echo Current directory: %cd%

REM 3. Stop Gradle daemon
echo Stopping Gradle daemon...
call "%~dp0gradlew.bat" --stop

REM Reset exit code so PowerShell does NOT abort
cmd /c exit /b 0

REM 4. Clean project
echo Cleaning project...
call "%~dp0gradlew.bat" clean

REM 5. Build project
echo Building project...
call "%~dp0gradlew.bat" build



REM 6. Copy built JAR to FootOP dependency folder
echo Copying FootLib JAR to FootOP...
copy "build\libs\footlib-*.jar" ^
 "C:\Users\kolay\NeoForgeProjects\MC1.21.1\FootOrganicProcessing-MC1.21.1\libs\" /Y

REM 7. Reopen VS Code in your workspace
echo Reopening VS Code workspace...
start "" code "C:\Users\kolay\NeoForgeProjects\MC1.21.1\Workspaces-MC1.21.1\footorganicprocessing.code-workspace"

endlocal

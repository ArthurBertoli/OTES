@echo off
setlocal

cd /d "%~dp0"
echo Starting API Financeiro on http://localhost:8080
call mvnw.cmd spring-boot:run

endlocal
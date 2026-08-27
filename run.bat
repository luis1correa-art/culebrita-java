@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
dir /s /b src\main\java\*.java > sources.txt
javac --release 11 -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
  echo Fallo la compilacion.
  exit /b 1
)
java -cp out culebrita.CulebritaApp

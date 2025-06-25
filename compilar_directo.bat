@echo off
echo ========================================
echo     COMPILANDO CON GRADLE DIRECTO
echo ========================================

set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Usando Java: %JAVA_HOME%
java -version

echo.
echo Compilando proyecto...
gradle build

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===========================================
    echo     COMPILACION EXITOSA
    echo ===========================================
    echo APK generado en: app\build\outputs\apk\debug\
    dir /b app\build\outputs\apk\debug\*.apk 2>nul
) else (
    echo.
    echo ===========================================
    echo     ERROR EN LA COMPILACION
    echo ===========================================
    echo Codigo de error: %ERRORLEVEL%
)

pause

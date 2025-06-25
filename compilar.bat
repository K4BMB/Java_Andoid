@echo off
echo ========================================
echo     COMPILANDO APLICACION ANDROID
echo ========================================

echo.
echo Limpiando proyecto...
if exist "gradlew.bat" (
    gradlew clean
) else (
    gradle clean
)

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo al limpiar el proyecto
    pause
    exit /b 1
)

echo.
echo Compilando APK...
if exist "gradlew.bat" (
    gradlew assembleDebug
) else (
    gradle assembleDebug
)

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo al compilar el APK
    pause
    exit /b 1
)

echo.
echo ========================================
echo     COMPILACION EXITOSA
echo ========================================
echo.
echo El APK se encuentra en: app\build\outputs\apk\debug\app-debug.apk
echo.
echo Para instalar en dispositivo:
echo 1. Habilite "Opciones de desarrollador" en su dispositivo Android
echo 2. Active "Depuracion USB"
echo 3. Conecte el dispositivo via USB
echo 4. Ejecute: adb install app\build\outputs\apk\debug\app-debug.apk
echo.
pause

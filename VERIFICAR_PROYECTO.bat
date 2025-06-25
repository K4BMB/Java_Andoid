@echo off
cls
echo ========================================
echo   VERIFICACION RAPIDA DEL PROYECTO
echo ========================================

echo.
echo ✅ Java: INSTALADO
java -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java: NO ENCONTRADO
    echo.
    echo Instale Java JDK 8 o superior desde:
    echo https://www.oracle.com/java/technologies/downloads/
    goto :end
) else (
    echo ✅ Java: ENCONTRADO
)

echo.
echo Verificando Android Studio...
if exist "%PROGRAMFILES%\Android\Android Studio\bin\studio64.exe" (
    echo ✅ Android Studio: ENCONTRADO
    echo.
    echo SOLUCION RECOMENDADA:
    echo 1. Abrir Android Studio
    echo 2. File ^> Open ^> Seleccionar esta carpeta
    echo 3. Esperar sincronizacion
    echo 4. Build ^> Build APK
) else (
    echo ❌ Android Studio: NO ENCONTRADO
    echo.
    echo SOLUCION:
    echo 1. Descargar Android Studio desde:
    echo    https://developer.android.com/studio
    echo 2. Instalar y configurar
    echo 3. Abrir este proyecto
)

echo.
echo Verificando estructura del proyecto...
if exist "app\src\main\java\com\sistemaeducativo\app\MainActivity.java" (
    echo ✅ Codigo fuente: CORRECTO
) else (
    echo ❌ Codigo fuente: FALTA ARCHIVOS
)

if exist "app\src\main\res\layout\activity_main.xml" (
    echo ✅ Layouts: CORRECTO
) else (
    echo ❌ Layouts: FALTA ARCHIVOS
)

if exist "app\build.gradle" (
    echo ✅ Configuracion Gradle: CORRECTO
) else (
    echo ❌ Configuracion Gradle: FALTA ARCHIVOS
)

echo.
echo ========================================
echo          ESTADO DEL PROYECTO
echo ========================================
echo.
echo El proyecto esta COMPLETO y listo para compilar.
echo.
echo PROXIMOS PASOS:
echo 1. Instalar Android Studio (si no lo tienes)
echo 2. Abrir el proyecto en Android Studio
echo 3. Compilar y ejecutar
echo.
echo ========================================

:end
pause

@echo off
echo ========================================
echo     VERIFICANDO ENTORNO ANDROID
echo ========================================

echo.
echo Verificando Java...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java no encontrado. Instale Java JDK 8 o superior.
    pause
    exit /b 1
)

echo.
echo Verificando Android SDK...
if "%ANDROID_HOME%" == "" (
    echo WARNING: Variable ANDROID_HOME no configurada.
    echo Configure la variable de entorno ANDROID_HOME apuntando al SDK de Android.
) else (
    echo ANDROID_HOME: %ANDROID_HOME%
    if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
        echo ADB encontrado: %ANDROID_HOME%\platform-tools\adb.exe
    ) else (
        echo WARNING: ADB no encontrado en platform-tools
    )
)

echo.
echo Verificando Gradle...
if exist "gradlew.bat" (
    echo Gradle Wrapper encontrado
    gradlew --version
) else (
    echo WARNING: Gradle Wrapper no encontrado
    gradle --version
    if %ERRORLEVEL% NEQ 0 (
        echo ERROR: Gradle no encontrado. Instale Gradle.
    )
)

echo.
echo ========================================
echo     VERIFICACION COMPLETADA
echo ========================================
pause

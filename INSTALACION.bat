@echo off
echo ========================================
echo  GUIA DE INSTALACION - SISTEMA ANDROID
echo ========================================

echo.
echo PASO 1: INSTALAR ANDROID STUDIO (RECOMENDADO)
echo ============================================
echo.
echo 1. Descargar Android Studio desde:
echo    https://developer.android.com/studio
echo.
echo 2. Instalar Android Studio (incluye automaticamente):
echo    - Android SDK
echo    - Android SDK Platform-Tools
echo    - Android SDK Build-Tools
echo    - Android Emulator
echo    - Gradle
echo.
echo 3. Abrir Android Studio y configurar:
echo    - Aceptar licencias del SDK
echo    - Descargar componentes adicionales
echo.

echo.
echo PASO 2: CONFIGURAR VARIABLES DE ENTORNO
echo =======================================
echo.
echo Agregar las siguientes variables de entorno:
echo.
echo ANDROID_HOME = C:\Users\%USERNAME%\AppData\Local\Android\Sdk
echo JAVA_HOME = C:\Program Files\Java\jdk-17
echo.
echo Agregar al PATH:
echo %%ANDROID_HOME%%\platform-tools
echo %%ANDROID_HOME%%\tools
echo %%JAVA_HOME%%\bin
echo.

echo.
echo PASO 3: ALTERNATIVA SIN ANDROID STUDIO
echo =====================================
echo.
echo Si prefieres no usar Android Studio:
echo.
echo 1. Instalar Java JDK 8 o superior
echo 2. Descargar Android SDK Command Line Tools
echo 3. Instalar Gradle manualmente
echo 4. Configurar variables de entorno
echo.

echo.
echo PASO 4: COMPILAR CON ANDROID STUDIO
echo ===================================
echo.
echo 1. Abrir Android Studio
echo 2. Seleccionar "Open an existing project"
echo 3. Navegar a: %~dp0
echo 4. Seleccionar la carpeta del proyecto
echo 5. Esperar a que se sincronicen las dependencias
echo 6. Hacer click en Build ^> Build Bundle(s) / APK(s) ^> Build APK(s)
echo.

echo.
echo PASO 5: INSTALAR EN DISPOSITIVO
echo ===============================
echo.
echo Opcion A - Usando Android Studio:
echo 1. Conectar dispositivo Android via USB
echo 2. Habilitar "Depuracion USB" en el dispositivo
echo 3. En Android Studio: Run ^> Run 'app'
echo.
echo Opcion B - Instalacion manual:
echo 1. El APK se genera en: app\build\outputs\apk\debug\
echo 2. Copiar app-debug.apk al dispositivo
echo 3. Instalar desde el explorador de archivos
echo.

echo.
echo USUARIOS DE PRUEBA:
echo ==================
echo Username: estudiante  Password: 123  (Rol: Estudiante)
echo Username: profesor    Password: 123  (Rol: Profesor)  
echo Username: admin       Password: 123  (Rol: Administrador)
echo.

echo.
echo ========================================
echo Para continuar, instale Android Studio
echo o configure el entorno manualmente
echo ========================================
pause

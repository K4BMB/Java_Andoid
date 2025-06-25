# Sistema Educativo Android

Una aplicación Android desarrollada en Java que permite la gestión de un sistema educativo con tres tipos de usuarios: Estudiantes, Profesores y Administradores.

## Características

### 🎓 Para Estudiantes
- Ver cursos disponibles
- Matricularse en cursos
- Ver mis cursos matriculados
- Interfaz intuitiva y fácil de usar

### 👨‍🏫 Para Profesores  
- Crear nuevos cursos
- Ver lista de cursos creados
- Ver estudiantes matriculados por curso
- Eliminar cursos propios

### 👨‍💼 Para Administradores
- Ver estadísticas del sistema
- Gestionar todos los usuarios
- Gestionar todos los cursos
- Panel de control completo

## Requisitos del Sistema

### Para Desarrollo
- **Java JDK 8 o superior**
- **Android SDK** (API Level 21 o superior)
- **Android Studio** (recomendado) o herramientas de línea de comandos
- **Gradle 8.2** (incluido en el proyecto)

### Para Dispositivo
- **Android 5.0 (API 21)** o superior
- **20 MB** de espacio libre

## Instalación y Configuración

### ⚠️ PROBLEMA ACTUAL
El proyecto necesita que tengas instalado **Android Studio** o el **Android SDK** con Gradle para poder compilar.

### 🚀 SOLUCIÓN RÁPIDA (Recomendado)

#### Opción A: Usar Android Studio (MÁS FÁCIL)
1. **Descargar Android Studio**: https://developer.android.com/studio
2. **Instalar Android Studio** (incluye todo lo necesario automáticamente)
3. **Abrir el proyecto**:
   - Abrir Android Studio
   - Seleccionar "Open an existing project"
   - Navegar a la carpeta `Java_Android`
   - Hacer clic en "OK"
4. **Esperar sincronización** (Android Studio descargará las dependencias)
5. **Compilar**: Build > Build Bundle(s) / APK(s) > Build APK(s)

#### Opción B: Instalación Manual (Avanzado)
Si prefieres no usar Android Studio:
1. **Instalar Java JDK 8+** (ya tienes ✅)
2. **Descargar Android SDK Command Line Tools**
3. **Instalar Gradle** manualmente  
4. **Configurar variables de entorno**:
   ```batch
   set ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk
   set PATH=%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\tools
   ```

### 2. Verificar el Entorno
Ejecutar el script de verificación:
```batch
verificar_entorno.bat
```

### 3. Compilar la Aplicación
```batch
compilar.bat
```

O manualmente:
```batch
gradlew clean
gradlew assembleDebug
```

## Instalación en Dispositivo Android

### Método 1: Usando ADB (Android Debug Bridge)
1. **Habilitar Opciones de Desarrollador:**
   - Ir a `Configuración > Acerca del teléfono`
   - Tocar 7 veces en "Número de compilación"
   - Volver a Configuración y buscar "Opciones de desarrollador"

2. **Activar Depuración USB:**
   - En "Opciones de desarrollador", activar "Depuración USB"

3. **Conectar el dispositivo:**
   - Conectar via USB al computador
   - Autorizar la conexión en el dispositivo

4. **Instalar APK:**
   ```batch
   adb install app\build\outputs\apk\debug\app-debug.apk
   ```

### Método 2: Transferencia Manual
1. Copiar el archivo `app-debug.apk` al dispositivo
2. En el dispositivo, ir a `Configuración > Seguridad`
3. Activar "Fuentes desconocidas" o "Instalar aplicaciones desconocidas"
4. Usar un explorador de archivos para instalar el APK

## Usuarios de Prueba

La aplicación viene con usuarios predefinidos para testing:

| Usuario | Contraseña | Tipo | Descripción |
|---------|------------|------|-------------|
| `estudiante` | `123` | Estudiante | Puede matricularse en cursos |
| `profesor` | `123` | Profesor | Puede crear y gestionar cursos |
| `admin` | `123` | Administrador | Control total del sistema |

## Estructura del Proyecto

```
SistemaEducativo/
├── app/
│   ├── src/main/
│   │   ├── java/com/sistemaeducativo/app/
│   │   │   ├── MainActivity.java          # Pantalla de login
│   │   │   ├── EstudianteActivity.java    # Panel de estudiante
│   │   │   ├── ProfesorActivity.java      # Panel de profesor
│   │   │   ├── AdminActivity.java         # Panel de administrador
│   │   │   ├── adapter/                   # Adaptadores para listas
│   │   │   ├── data/                      # Gestión de datos
│   │   │   └── model/                     # Modelos de datos
│   │   ├── res/
│   │   │   ├── layout/                    # Diseños de pantallas
│   │   │   ├── values/                    # Colores, strings, temas
│   │   │   └── xml/                       # Configuraciones
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── verificar_entorno.bat                  # Script de verificación
├── compilar.bat                          # Script de compilación
└── README.md
```

## Funcionalidades Técnicas

### Arquitectura
- **Patrón MVC** (Model-View-Controller)
- **Singleton** para gestión de datos
- **RecyclerView** para listas eficientes
- **Material Design** para UI moderna

### Tecnologías Utilizadas
- **Java 8+**
- **Android SDK API 21-34**
- **Material Components**
- **RecyclerView**
- **ConstraintLayout**

## Solución de Problemas

### Error de Compilación
```bash
# Limpiar y reconstruir
gradlew clean
gradlew build
```

### Problemas de Dependencias
```bash
# Actualizar dependencias
gradlew --refresh-dependencies
```

### APK no se instala
- Verificar que "Fuentes desconocidas" esté habilitado
- Comprobar que el dispositivo tenga suficiente espacio
- Reintentar con `adb install -r app-debug.apk`

### Problemas de ADB
```bash
# Reiniciar ADB
adb kill-server
adb start-server
adb devices
```

## Desarrollo y Contribución

### Para añadir nuevas funciones:
1. Crear nuevas actividades en `java/com/sistemaeducativo/app/`
2. Añadir layouts correspondientes en `res/layout/`
3. Actualizar `AndroidManifest.xml` si es necesario
4. Probar en dispositivo o emulador

### Estructura de Base de Datos
La aplicación usa un sistema de datos en memoria con las siguientes entidades:
- **Usuario**: ID, username, password, nombre, tipo
- **Curso**: ID, nombre, descripción, profesorId, nombreProfesor  
- **Matricula**: ID, estudianteId, cursoId, fechaMatricula

## Versiones y Actualizaciones

### v1.0.0 (Actual)
- ✅ Sistema de login con 3 tipos de usuario
- ✅ Panel de estudiante con matriculación
- ✅ Panel de profesor con gestión de cursos
- ✅ Panel de administrador con estadísticas
- ✅ UI responsive con Material Design
- ✅ Datos persistentes durante la sesión

### Próximas Versiones
- 🔄 Base de datos SQLite permanente
- 🔄 Notificaciones push
- 🔄 Exportar reportes
- 🔄 Chat entre usuarios
- 🔄 Calificaciones y evaluaciones

## Soporte

Para soporte técnico o preguntas sobre el desarrollo:

1. **Verificar el entorno** con `verificar_entorno.bat`
2. **Revisar logs** de Android Studio o `adb logcat`
3. **Comprobar la documentación** de Android Developer
4. **Validar dependencias** en `build.gradle`

## Licencia

Este proyecto es de código abierto para fines educativos.

---

**¡Feliz desarrollo! 🚀📱**

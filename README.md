# Registro de empleados

Aplicación Android desarrollada para el examen final de **Introducción a la Programación para Dispositivos Móviles**. Permite registrar empleados de una empresa, consultar sus datos y eliminar registros individuales.

## Funcionalidades

- Formulario con nombre completo, cargo, departamento, salario y fecha de contratación.
- Validación de todos los campos antes de registrar un empleado.
- Selector de fecha nativo de Material 3.
- Lista vertical implementada con `LazyColumn`.
- Datos complementarios de cada empleado presentados con `LazyRow`.
- Eliminación individual mediante el botón **Eliminar**.
- Tema personalizado claro y oscuro, con cambio manual desde la barra superior.
- Diseño adaptable: el formulario usa dos columnas en pantallas de 600 dp o más.
- Logs de `onStart()`, `onStop()` y `onDestroy()` en `MainActivity`.

## Tecnologías

- Kotlin 2.2.21
- Jetpack Compose y Material 3
- Android Gradle Plugin 9.2.1
- Gradle 9.4.1
- SDK de compilación y objetivo 36
- SDK mínimo 26 (Android 8.0)
- Java 17

## Estructura del proyecto

```text
app/src/main/java/py/com/robs/registroempleados/
├── MainActivity.kt                 # Activity principal y logs del ciclo de vida
├── model/
│   └── Employee.kt                # Modelo de empleado
└── ui/
    ├── EmployeeScreen.kt          # Formulario, LazyColumn, LazyRow y tarjetas
    ├── EmployeeViewModel.kt       # Estado y operaciones de alta/eliminación
    └── theme/
        ├── Color.kt               # Paleta personalizada clara y oscura
        ├── Theme.kt               # Configuración de MaterialTheme
        └── Type.kt                # Tipografía de la aplicación
```

Los recursos XML se encuentran en `app/src/main/res`. Las pruebas unitarias del estado de empleados están en `app/src/test`.

## Cómo ejecutar

### Android Studio

1. Clonar este repositorio.
2. Abrir la carpeta raíz con Android Studio.
3. Esperar a que finalice la sincronización de Gradle.
4. Seleccionar un emulador con Android 8.0 o superior, o conectar un teléfono con depuración USB habilitada.
5. Ejecutar la configuración `app` con **Run**.

### Terminal

Con Java 17 y el SDK de Android configurados:

```bash
./gradlew assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.

Para ejecutar las pruebas unitarias:

```bash
./gradlew testDebugUnitTest
```

## Verificación del ciclo de vida

En Android Studio, filtrar Logcat por la etiqueta `MainActivity`. Al abrir, enviar al fondo y cerrar la Activity se registran mensajes como:

```text
I/MainActivity: onStart
I/MainActivity: onStop
I/MainActivity: onDestroy
```

## Autor

Robert Ruiz

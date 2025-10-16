# app_prueba
Prueba Android, intents implícitos y explícitos
# 📱 App Prueba — Intents implícitos y explícitos

Proyecto Android desarrollado como parte de una práctica de programación.  
El objetivo principal es demostrar el uso de **intents implícitos y explícitos**, manejo de **permisos en tiempo de ejecución** y navegación entre **activities**.

## Funcionalidades principales

###  HomeActivity
Es la pantalla principal del proyecto, que da la bienvenida al usuario y agrupa los distintos botones de prueba.

Incluye:
- **Botones con Intents Explícitos**
  - Ir al perfil (`PerfilActivity`)
  - Ver detalles (`DetallesActivity`)
  - Abrir configuración (`ConfiguracionActivity`)
  - Ayuda (`AyudaActivity`)
- **Botones con Intents Implícitos**
  - Abrir sitio web (https://www.minecraft.net/)
  - Enviar mensaje SMS
  - Compartir texto
  - Marcar un número telefónico
  - Seleccionar una imagen desde la galería
- **Linterna ON/OFF**
  - Utiliza `CameraManager` y `setTorchMode()` para encender o apagar el flash del dispositivo.
  - Solicita permiso de cámara en tiempo de ejecución.

##  Estructura de Código

### `HomeActivity.java`
Maneja toda la lógica de los botones:
- Crea los **Intents implícitos** con `Intent.ACTION_VIEW`, `Intent.ACTION_SEND`, etc.
- Controla la linterna con permisos dinámicos (`Manifest.permission.CAMERA`).
- Usa **ActivityResultLauncher** para manejar respuestas desde otras pantallas (por ejemplo, editar el perfil).

### `activity_home.xml`
Diseño de la pantalla principal.
- Utiliza un `CoordinatorLayout` con `AppBarLayout` y `MaterialToolbar`.
- Dentro del contenido se organiza un `LinearLayout` con los botones principales.
- Cada botón está comentado y claramente identificado por su función.


## Intents y Permisos

### Intents implícitos:
- **Abrir sitio web:** `Intent.ACTION_VIEW`
- **Enviar SMS:** `Intent.ACTION_SENDTO`
- **Marcar número:** `Intent.ACTION_DIAL`
- **Seleccionar imagen:** `Intent.ACTION_GET_CONTENT`
- **Compartir texto:** `Intent.ACTION_SEND`

### Intents explícitos:
- Se usan para abrir Activities propias dentro del proyecto (`new Intent(this, PerfilActivity.class)`).

### Permisos usados:
- `android.permission.CAMERA` → Para controlar la linterna.
- `android.permission.SEND_SMS` → (Solo si se implementa envío real de SMS).


## Login y diseño adaptable
El `LoginActivity` cuenta con diseño **adaptable a orientación vertical y horizontal**.  
Se utiliza `ConstraintLayout` y restricciones relativas para mantener la estructura centrada y flexible.



## Tecnologías y librerías
- Android SDK (Java)
- Material Design Components
- Camera2 API
- AndroidX
- Gradle


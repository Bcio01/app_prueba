# 📱 App Prueba — Intents Implícitos y Explícitos

Proyecto Android desarrollado como parte de una evaluación práctica de programación móvil. El objetivo es demostrar el uso de **Intents Implícitos** y **Explícitos**, la navegación entre Activities y el manejo básico de recursos dentro de una aplicación Android.

---

## 🏠 HomeActivity — Pantalla Principal

La `HomeActivity` funciona como **menú principal**. Desde esta pantalla se accede a todas las funcionalidades mediante botones organizados en dos categorías:

- **Intents Explícitos**: Navegación interna de la app
- **Intents Implícitos**: Interacción con apps del sistema

---

## ⚙️ Intents Explícitos Implementados

Los **Intents Explícitos** abren pantallas internas del proyecto.

| Botón | Activity destino | Descripción |
|-------|------------------|-------------|
| ⚙️ **Configuración** | `ConfiguracionActivity` | Abre la pantalla de ajustes/configuración de la app |
| 📄 **Ver detalles** | `DetallesActivity` | Muestra información enviada desde Home mediante `putExtra()` |
| ❓ **Ayuda / FAQ** | `AyudaActivity` | Abre una pantalla con preguntas frecuentes o información general |

### Ejemplo de código (explícito):
```java
Intent i = new Intent(this, ConfiguracionActivity.class);
startActivity(i);
```

---

## 🌐 Intents Implícitos Implementados

Los **Intents Implícitos** abren aplicaciones del sistema (navegador, teléfono, maps, etc.).

| Botón | Acción / URI | Descripción |
|-------|--------------|-------------|
| 🌍 **Ir a sitio web** | `Intent.ACTION_VIEW` + `https://www.minecraft.net/` | Abre el navegador en la web de Minecraft |
| ✉️ **Enviar correo/SMS** | `Intent.ACTION_SENDTO` + `Uri.parse("smsto:")` | Abre la app de mensajes con un texto predefinido (sin número fijo) |
| 📤 **Compartir texto** | `Intent.ACTION_SEND` | Abre el selector para compartir un texto (WhatsApp, Gmail, etc.) |
| 📞 **Teléfono** | `Intent.ACTION_DIAL` + `Uri.parse("tel:+569...")` | Abre el marcador con un número precargado |
| 📍 **Google Maps** | `Intent.ACTION_VIEW` + `geo:` | Abre Google Maps (o el navegador) en una ubicación específica |

### Ejemplo de código (implícito - Maps):
```java
Uri uri = Uri.parse("geo:-33.449291,-70.662279?q=Instituto ST");
Intent i = new Intent(Intent.ACTION_VIEW, uri);
i.setPackage("com.google.android.apps.maps");
startActivity(i);
```

---

## 📁 Estructura del Proyecto
```
app/
 ├─ java/com/devst/app/
 │   ├─ HomeActivity.java
 │   ├─ ConfiguracionActivity.java
 │   ├─ DetallesActivity.java
 │   └─ AyudaActivity.java
 │
 ├─ res/layout/
 │   ├─ activity_home.xml
 │   ├─ activity_configuracion.xml
 │   ├─ activity_detalles.xml
 │   └─ activity_ayuda.xml
 │
 └─ AndroidManifest.xml
```

---

## 📌 Descripción de Activities

| Activity | Descripción |
|----------|-------------|
| **HomeActivity** | Pantalla principal con todos los botones de navegación |
| **ConfiguracionActivity** | Pantalla de configuración de la aplicación |
| **DetallesActivity** | Muestra información detallada con datos enviados por extras |
| **AyudaActivity** | Contiene preguntas frecuentes e información de ayuda |
| **AndroidManifest.xml** | Registra todas las Activities y permisos necesarios |

---

## 🔒 Permisos Utilizados
```xml
<uses-permission android:name="android.permission.SEND_SMS" />
```

Este permiso es necesario para utilizar la funcionalidad de envío de SMS mediante intents implícitos.

---

## 🚀 Instalación y Ejecución

1. Clona este repositorio:
```bash
   git clone https://github.com/Bcio01/app_prueba
```

2. Abre el proyecto en **Android Studio**

3. Sincroniza las dependencias de Gradle

4. Conecta un dispositivo físico o inicia un emulador

5. Ejecuta la aplicación desde Android Studio

---

## 📱 Requisitos

- **Android Studio**: Arctic Fox o superior
- **SDK mínimo**: API 21 (Android 5.0 Lollipop)
- **SDK objetivo**: API 34 (Android 14)
- **Lenguaje**: Java

---

## 👨‍💻 Autor

- Karen Dayana
- Camilo Suarez

---

## 📄 Licencia

Este proyecto es de uso educativo.

## Tecnologías y librerías
- Android SDK (Java)
- Material Design Components
- Camera2 API
- AndroidX
- Gradle

---

# 📱 Vistas en GIF

### 🔐 Login
![Login](gif%20app/Login.gif)

---

### ⚙️ Configuración
![Configuracion](gif%20app/Configuracion.gif)

### 📄 Detalles
![Detalles](gif%20app/Detalles.gif)

### ❓ Ayuda
![Ayuda](gif%20app/Ayuda.gif)

---

### 🌍 Redirección a Sitio Web
![Sitio Web](gif%20app/SitioWeb.gif)

### ✉️ Enviar Mensaje
![Enviar Correo](gif%20app/Correo.gif)

### 🤝 Compartir Mensaje
![Compartir Texto](gif%20app/Compartir.gif)

### ☎️ Redirección a Teléfono
![Llamar Teléfono](gif%20app/Telefono.gif)

### 🗺️ Redirección a Google Maps
![Google Maps](gif%20app/Maps.gif)

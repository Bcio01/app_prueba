package com.devst.app;

// Importa las herramientas necesarias de Android para esta clase
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Librerías de Android
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

// Se declara la clase principal de esta pantalla (Activity)
public class HomeActivity extends AppCompatActivity {

    // Variable que guardará el email del usuario
    private String emailUsuario = "";

    // Elemento de texto donde se mostrará el saludo o correo
    private TextView tvBienvenida;

    // Variables para controlar la linterna del teléfono
    private Button btnLinterna; // El botón para encender o apagar la linterna
    private CameraManager camara; // Objeto que maneja la cámara
    private String camaraID = null; // Identificador de la cámara trasera
    private boolean luz = false; // Guarda si la linterna está encendida o apagada

    // Permite abrir otra pantalla y recibir un resultado al volver (por ejemplo, editar el perfil)
    private final ActivityResultLauncher<Intent> editarPerfilLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                // Si la otra pantalla terminó bien y devolvió datos
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Se obtiene el nombre editado
                    String nombre = result.getData().getStringExtra("nombre_editado");
                    // Si no es nulo, se muestra el nuevo saludo
                    if (nombre != null) {
                        tvBienvenida.setText("Hola, " + nombre);
                    }
                }
            });

    // Sirve para pedir permiso de cámara mientras la app está abierta
    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                // Si el usuario acepta el permiso
                if (granted) {
                    alternarluz(); // Enciende o apaga la linterna
                } else {
                    // Si el permiso fue denegado, muestra un mensaje
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                }
            });

    // Sirve para abrir la galería y seleccionar una imagen
    private final ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                // Si el usuario seleccionó una imagen
                if (uri != null) {
                    Toast.makeText(this, "Imagen seleccionada: " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
                } else {
                    // Si canceló la selección
                    Toast.makeText(this, "Selección cancelada", Toast.LENGTH_SHORT).show();
                }
            });

    // Método que se ejecuta al iniciar la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método original
        EdgeToEdge.enable(this); // Habilita el modo pantalla completa
        setContentView(R.layout.activity_home); // Conecta el diseño XML con el código

        // Botón: abrir ubicación en Google Maps (nuevo)
        // Cuando el usuario presione este botón, abrirá el mapa con una ubicación específica
        findViewById(R.id.btnAbrirMaps).setOnClickListener(v ->
                abrirMapsUbicacion(-33.449291, -70.662279, "Instituto ST"));

        // Encuentra la barra superior (Toolbar) y la activa
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Busca los elementos del diseño (botones, textos) y los conecta con el código
        tvBienvenida = findViewById(R.id.tvBienvenida);
        Button btnIrPerfil = findViewById(R.id.btnIrPerfil);
        Button btnAbrirWeb = findViewById(R.id.btnAbrirWeb);
        Button btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        Button btnCompartir = findViewById(R.id.btnCompartir);
        btnLinterna = findViewById(R.id.btnLinterna);
        Button btnCamara = findViewById(R.id.btnCamara);
        Button btnMarcarTelefono = findViewById(R.id.btnMarcarTelefono);
        Button btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        Button btnAbrirConfiguracion = findViewById(R.id.btnAbrirConfiguracion);
        Button btnVerDetalles = findViewById(R.id.btnVerDetalles);
        Button btnAbrirAyuda = findViewById(R.id.btnAbrirAyuda);

        // Obtiene el correo que se mandó desde el login
        emailUsuario = getIntent().getStringExtra("email_usuario");
        // Si no llega ningún correo, deja el texto vacío
        if (emailUsuario == null) emailUsuario = "";
        // Muestra un mensaje de bienvenida con el correo
        tvBienvenida.setText("Bienvenido: " + emailUsuario);

        // ===== Intents explícitos =====
        // Cada uno de estos botones abre una nueva Activity dentro de la app

        // Botón: Ir al perfil
        btnIrPerfil.setOnClickListener(v -> {
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i); // Espera un resultado al volver
        });

        // Botón: Abrir configuración
        btnAbrirConfiguracion.setOnClickListener(v ->
                startActivity(new Intent(this, ConfiguracionActivity.class)));

        // Botón: Ver detalles de un elemento (manda parámetros)
        btnVerDetalles.setOnClickListener(v -> {
            Intent i = new Intent(this, DetallesActivity.class);
            i.putExtra("titulo", "Detalle desde Home");
            i.putExtra("id", 42);
            startActivity(i);
        });

        // Botón: Abrir ayuda (nueva Activity que explica el uso de la app)
        btnAbrirAyuda.setOnClickListener(v ->
                startActivity(new Intent(this, AyudaActivity.class)));

        // Botón: Abrir cámara (navega a una pantalla que controla la cámara)
        btnCamara.setOnClickListener(v ->
                startActivity(new Intent(this, CamaraActivity.class)));

        // ===== Intents implícitos =====
        // Usan aplicaciones externas como navegador, teléfono o SMS

        // Botón: abrir sitio web
        btnAbrirWeb.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.minecraft.net/");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        // Botón: enviar SMS
        btnEnviarCorreo.setOnClickListener(v -> {
            Uri uri = Uri.parse("smsto:");
            Intent sms = new Intent(Intent.ACTION_SENDTO, uri);
            sms.putExtra("sms_body", "Hola, mensaje desde mi app.");
            startActivity(sms);
        });

        // Botón: compartir texto con otras apps
        btnCompartir.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hola desde mi app Android 😎");
            startActivity(Intent.createChooser(share, "Compartir usando:"));
        });

        // Botón: abrir marcador telefónico
        btnMarcarTelefono.setOnClickListener(v -> {
            Uri tel = Uri.parse("tel:+56912345678");
            startActivity(new Intent(Intent.ACTION_DIAL, tel));
        });

        // Botón: abrir galería y seleccionar una imagen
        btnSeleccionarImagen.setOnClickListener(v ->
                seleccionarImagenLauncher.launch("image/*"));

        // ===== Linterna =====
        // Se prepara el acceso a la cámara del dispositivo para encender el flash
        camara = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            for (String id : camara.getCameraIdList()) {
                CameraCharacteristics cc = camara.getCameraCharacteristics(id);
                Boolean disponibleFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
                // Busca una cámara trasera que tenga flash disponible
                if (Boolean.TRUE.equals(disponibleFlash)
                        && lensFacing != null
                        && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    camaraID = id;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "No se puede acceder a la cámara", Toast.LENGTH_SHORT).show();
        }

        // Botón: alternar la linterna (enciende o apaga)
        btnLinterna.setOnClickListener(v -> {
            if (camaraID == null) {
                Toast.makeText(this, "Este dispositivo no tiene flash disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean camGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;

            if (camGranted) {
                alternarluz();
            } else {
                permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }

    // Método para abrir Google Maps en una ubicación específica
    private void abrirMapsUbicacion(double lat, double lng, String etiqueta) {
        Uri uri = Uri.parse("geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + Uri.encode(etiqueta) + ")");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage("com.google.android.apps.maps");

        // Si el dispositivo tiene Google Maps, lo abre directamente; si no, abre en navegador web
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        } else {
            Uri web = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng);
            startActivity(new Intent(Intent.ACTION_VIEW, web));
        }
    }

    // Enciende o apaga la linterna del teléfono
    private void alternarluz() {
        try {
            luz = !luz; // Cambia el estado actual
            camara.setTorchMode(camaraID, luz); // Controla el flash
            btnLinterna.setText(luz ? "Apagar Linterna" : "Encender Linterna");
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error al controlar la linterna", Toast.LENGTH_SHORT).show();
        }
    }

    // Apaga la linterna automáticamente si la app se pausa
    @Override
    protected void onPause() {
        super.onPause();
        if (camaraID != null && luz) {
            try {
                camara.setTorchMode(camaraID, false);
                luz = false;
                if (btnLinterna != null) btnLinterna.setText("Encender Linterna");
            } catch (CameraAccessException ignored) {}
        }
    }

    // Menú superior de opciones (toolbar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Infla el menú desde XML
        return true;
    }

    // Define la acción cuando el usuario selecciona una opción del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_perfil) {
            // Abre el perfil
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
            return true;
        } else if (id == R.id.action_web) {
            // Abre la web oficial
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.minecraft.net/")));
            return true;
        } else if (id == R.id.action_salir) {
            // Cierra la actividad actual
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

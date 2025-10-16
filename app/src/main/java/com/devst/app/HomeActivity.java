// Indica en qué paquete (carpeta lógica) está este archivo dentro del proyecto
package com.devst.app;

// Importa las herramientas necesarias de Android para esta clase
import android.Manifest; // Para pedir permisos (como la cámara)
import android.content.Intent; // Para abrir otras pantallas o apps
import android.content.pm.PackageManager; // Para comprobar permisos
import android.hardware.camera2.CameraManager; // Para manejar la cámara del celular
import android.hardware.camera2.CameraAccessException; // Para manejar errores de cámara
import android.hardware.camera2.CameraCharacteristics; // Para saber datos de la cámara
import android.net.Uri; // Para abrir direcciones web, teléfonos o archivos
import android.os.Bundle; // Contiene los datos al crear la actividad
import android.view.Menu; // Para mostrar menús arriba
import android.view.MenuItem; // Cada opción del menú
import android.widget.Button; // Botones
import android.widget.TextView; // Textos en pantalla
import android.widget.Toast; // Mensajes cortos que aparecen abajo

// Librerías modernas de Android (AndroidX)
import androidx.activity.EdgeToEdge; // Para usar pantalla completa
import androidx.activity.result.ActivityResultLauncher; // Para recibir resultados de otras pantallas
import androidx.activity.result.contract.ActivityResultContracts; // Define el tipo de resultado esperado
import androidx.annotation.NonNull; // Indica que un valor no puede ser nulo
import androidx.appcompat.app.AppCompatActivity; // Clase base para pantallas modernas
import androidx.appcompat.widget.Toolbar; // Barra superior de la app
import androidx.core.content.ContextCompat; // Verifica permisos del sistema
import androidx.core.graphics.Insets; // Para márgenes de la pantalla
import androidx.core.view.ViewCompat; // Para vista completa
import androidx.core.view.WindowInsetsCompat; // Ajustes de bordes y espacio en pantalla

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

        // Encuentra la barra superior y la activa
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

        // Botón: Ir al perfil (pantalla explícita)
        btnIrPerfil.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i); // Lanza la actividad esperando un resultado
        });

        // Botón: Abrir sitio web (intent implícito)
        btnAbrirWeb.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.minecraft.net/");
            Intent viewWeb = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(viewWeb);
        });

        // Botón: Enviar SMS (reemplaza al correo)
        btnEnviarCorreo.setOnClickListener(v -> {
            Uri uri = Uri.parse("smsto:"); // Abre la app de mensajes sin número
            Intent sms = new Intent(Intent.ACTION_SENDTO, uri);
            sms.putExtra("sms_body", "Hola, mensaje desde mi app.");
            startActivity(sms);
        });

        // Botón: Compartir texto (intent implícito)
        btnCompartir.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hola desde mi app Android 😎");
            startActivity(Intent.createChooser(share, "Compartir usando:"));
        });

        // Inicializa el acceso a la cámara del teléfono
        camara = (CameraManager) getSystemService(CAMERA_SERVICE);

        // Busca la cámara trasera con flash disponible
        try {
            for (String id : camara.getCameraIdList()) {
                CameraCharacteristics cc = camara.getCameraCharacteristics(id);
                Boolean disponibleFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
                if (Boolean.TRUE.equals(disponibleFlash)
                        && lensFacing != null
                        && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    camaraID = id; // Guarda la cámara trasera con flash
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "No se puede acceder a la cámara", Toast.LENGTH_SHORT).show();
        }

        // Botón: Enciende o apaga la linterna
        btnLinterna.setOnClickListener(v -> {
            if (camaraID == null) {
                Toast.makeText(this, "Este dispositivo no tiene flash disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            // Verifica si tiene permiso de cámara
            boolean camGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;

            // Si tiene permiso, cambia el estado de la linterna
            if (camGranted) {
                alternarluz();
            } else {
                // Si no tiene permiso, lo pide
                permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        // Botón: Abre la cámara del teléfono (otra pantalla)
        btnCamara.setOnClickListener(v ->
                startActivity(new Intent(this, CamaraActivity.class))
        );

        // Botón: Marcar teléfono (abre el marcador con un número)
        btnMarcarTelefono.setOnClickListener(v -> {
            Uri tel = Uri.parse("tel:+56912345678");
            startActivity(new Intent(Intent.ACTION_DIAL, tel));
        });

        // Botón: Seleccionar una imagen de la galería
        btnSeleccionarImagen.setOnClickListener(v ->
                seleccionarImagenLauncher.launch("image/*")
        );

        // Botón: Abrir configuración
        btnAbrirConfiguracion.setOnClickListener(v ->
                startActivity(new Intent(this, ConfiguracionActivity.class)));

        // Botón: Ver detalles (manda datos extras)
        btnVerDetalles.setOnClickListener(v -> {
            Intent i = new Intent(this, DetallesActivity.class);
            i.putExtra("titulo", "Detalle desde Home");
            i.putExtra("id", 42);
            startActivity(i);
        });

        // Botón: Abrir ayuda
        btnAbrirAyuda.setOnClickListener(v ->
                startActivity(new Intent(this, AyudaActivity.class)));
    }

    // Método que enciende o apaga la linterna
    private void alternarluz() {
        try {
            luz = !luz; // Cambia el estado de la linterna
            camara.setTorchMode(camaraID, luz); // Activa o desactiva el flash
            btnLinterna.setText(luz ? "Apagar Linterna" : "Encender Linterna"); // Cambia el texto del botón
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error al controlar la linterna", Toast.LENGTH_SHORT).show();
        }
    }

    // Cuando la app se pausa, apaga la linterna si estaba encendida
    @Override
    protected void onPause() {
        super.onPause();
        if (camaraID != null && luz) {
            try {
                camara.setTorchMode(camaraID, false); // Apaga el flash
                luz = false; // Actualiza el estado
                if (btnLinterna != null) btnLinterna.setText("Encender Linterna");
            } catch (CameraAccessException ignored) {}
        }
    }

    // Crea el menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Muestra las opciones del menú
        return true;
    }

    // Detecta qué opción del menú se selecciona
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_perfil) {
            // Abre el perfil (intent explícito)
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
            return true;
        } else if (id == R.id.action_web) {
            // Abre la página web (intent implícito)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.minecraft.net/")));
            return true;
        } else if (id == R.id.action_salir) {
            // Cierra la pantalla actual
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


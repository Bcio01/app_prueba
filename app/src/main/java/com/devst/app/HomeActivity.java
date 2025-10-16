// Indica en qué paquete (carpeta lógica) está este archivo dentro del proyecto
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

// Librerías modernas de Android (AndroidX)
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

// Clase principal
public class HomeActivity extends AppCompatActivity {

    private String emailUsuario = "";
    private TextView tvBienvenida;
    private Button btnLinterna;
    private CameraManager camara;
    private String camaraID = null;
    private boolean luz = false;

    // Lanzador para editar perfil
    private final ActivityResultLauncher<Intent> editarPerfilLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String nombre = result.getData().getStringExtra("nombre_editado");
                    if (nombre != null) {
                        tvBienvenida.setText("Hola, " + nombre);
                    }
                }
            });

    // Lanzador para permiso de cámara (linterna)
    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    alternarluz();
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                }
            });

    // Lanzador para seleccionar imagen desde la galería
    private final ActivityResultLauncher<String> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    Toast.makeText(this, "Imagen seleccionada: " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Selección cancelada", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Botón: abrir ubicación en Google Maps (nuevo)
        findViewById(R.id.btnAbrirMaps).setOnClickListener(v ->
                abrirMapsUbicacion(-33.449291, -70.662279, "Instituto ST"));

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Elementos del layout
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

        // Email recibido desde Login
        emailUsuario = getIntent().getStringExtra("email_usuario");
        if (emailUsuario == null) emailUsuario = "";
        tvBienvenida.setText("Bienvenido: " + emailUsuario);

        // ===== Intents explícitos =====
        btnIrPerfil.setOnClickListener(v -> {
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
        });

        btnAbrirConfiguracion.setOnClickListener(v ->
                startActivity(new Intent(this, ConfiguracionActivity.class)));

        btnVerDetalles.setOnClickListener(v -> {
            Intent i = new Intent(this, DetallesActivity.class);
            i.putExtra("titulo", "Detalle desde Home");
            i.putExtra("id", 42);
            startActivity(i);
        });

        btnAbrirAyuda.setOnClickListener(v ->
                startActivity(new Intent(this, AyudaActivity.class)));

        btnCamara.setOnClickListener(v ->
                startActivity(new Intent(this, CamaraActivity.class)));

        // ===== Intents implícitos =====
        btnAbrirWeb.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.minecraft.net/");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });

        btnEnviarCorreo.setOnClickListener(v -> {
            Uri uri = Uri.parse("smsto:");
            Intent sms = new Intent(Intent.ACTION_SENDTO, uri);
            sms.putExtra("sms_body", "Hola, mensaje desde mi app.");
            startActivity(sms);
        });

        btnCompartir.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hola desde mi app Android 😎");
            startActivity(Intent.createChooser(share, "Compartir usando:"));
        });

        btnMarcarTelefono.setOnClickListener(v -> {
            Uri tel = Uri.parse("tel:+56912345678");
            startActivity(new Intent(Intent.ACTION_DIAL, tel));
        });

        btnSeleccionarImagen.setOnClickListener(v ->
                seleccionarImagenLauncher.launch("image/*"));

        // ===== Linterna =====
        camara = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            for (String id : camara.getCameraIdList()) {
                CameraCharacteristics cc = camara.getCameraCharacteristics(id);
                Boolean disponibleFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
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

    // Método para abrir Google Maps (Intent implícito)
    private void abrirMapsUbicacion(double lat, double lng, String etiqueta) {
        Uri uri = Uri.parse("geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + Uri.encode(etiqueta) + ")");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setPackage("com.google.android.apps.maps");

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        } else {
            Uri web = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng);
            startActivity(new Intent(Intent.ACTION_VIEW, web));
        }
    }

    // Enciende o apaga la linterna
    private void alternarluz() {
        try {
            luz = !luz;
            camara.setTorchMode(camaraID, luz);
            btnLinterna.setText(luz ? "Apagar Linterna" : "Encender Linterna");
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error al controlar la linterna", Toast.LENGTH_SHORT).show();
        }
    }

    // Apaga la linterna si la app se pausa
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

    // Menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_perfil) {
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
            return true;
        } else if (id == R.id.action_web) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.minecraft.net/")));
            return true;
        } else if (id == R.id.action_salir) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

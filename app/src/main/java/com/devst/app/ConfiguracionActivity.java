package com.devst.app;

// Importa librerías necesarias
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle; // Control del ciclo de vida
import android.widget.Button; // Botón para guardar configuración
import android.widget.Switch; // Interruptores (On/Off)
import android.widget.Toast; // Mensajes emergentes

// Clase principal de la pantalla de configuración
public class ConfiguracionActivity extends AppCompatActivity {

    // Declaración de variables para los elementos de la interfaz
    private Switch swNotificaciones; // Permite activar o desactivar notificaciones
    private Switch swTemaOscuro; // Permite activar o desactivar modo oscuro
    private Button btnGuardar; // Botón para guardar los cambios

    private Button btnVolverHome; // Botón para volver al menú principal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion); // Conecta el diseño XML

        // Habilita botón de volver en la ActionBar (si está disponible)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Configuración"); // Título en la parte superior
        }

        // Referencias a los elementos del XML
        swNotificaciones = findViewById(R.id.swNotificaciones);
        swTemaOscuro = findViewById(R.id.swTemaOscuro);
        btnGuardar = findViewById(R.id.btnGuardarConfig);
        btnVolverHome = findViewById(R.id.btnVolverHome);

        // Evento del botón "Guardar"
        btnGuardar.setOnClickListener(v -> {
            // Captura el estado actual de los switches
            boolean notificaciones = swNotificaciones.isChecked();
            boolean temaOscuro = swTemaOscuro.isChecked();

            // Muestra un mensaje indicando que se guardó la configuración
            Toast.makeText(this,
                    "Configuración guardada:\nNotificaciones: " +
                            (notificaciones ? "activadas" : "desactivadas") +
                            "\nTema oscuro: " + (temaOscuro ? "sí" : "no"),
                    Toast.LENGTH_LONG).show();

            // Aquí podrías guardar los valores en SharedPreferences si lo deseas
        });
        // Acción del botón: volver al menú principal
        btnVolverHome.setOnClickListener(v -> {
            // Crea un Intent para volver a HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent); // Abre HomeActivity
            finish(); // Cierra esta pantalla para no acumular en la pila
        });
    }

    // Permite volver atrás al presionar la flecha de la ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra la actividad actual
        return true;
    }
}

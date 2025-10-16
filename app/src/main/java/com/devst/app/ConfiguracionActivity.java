package com.devst.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;

public class ConfiguracionActivity extends AppCompatActivity {

    private Switch swNotificaciones, swTemaOscuro;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Evita error si el tema no tiene ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Configuración");
        }

        // Referencias
        swNotificaciones = findViewById(R.id.swNotificaciones);
        swTemaOscuro = findViewById(R.id.swTemaOscuro);
        btnGuardar = findViewById(R.id.btnGuardarConfig);

        // Cargar configuración guardada
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("darkMode", false);
        swTemaOscuro.setChecked(darkMode);

        // Aplicar modo oscuro actual
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Escuchar cambios en el switch
        swTemaOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // Guardar preferencia
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();
        });

        // Evento del botón Guardar
        btnGuardar.setOnClickListener(v -> {
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

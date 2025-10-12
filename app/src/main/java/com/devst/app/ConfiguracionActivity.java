package com.devst.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Button;
import android.widget.Toast;

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

        // Evento del botón
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

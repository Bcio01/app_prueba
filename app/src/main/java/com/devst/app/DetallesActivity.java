package com.devst.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetallesActivity extends AppCompatActivity {

    private TextView tvTitulo, tvId, tvDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        // Evitar NPE si el tema es NoActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Detalles");
        }

        tvTitulo = findViewById(R.id.tvTituloDetalle);
        tvId = findViewById(R.id.tvIdDetalle);
        tvDescripcion = findViewById(R.id.tvDescripcionDetalle);

        // Leer extras con defaults seguros
        String titulo = getIntent() != null ? getIntent().getStringExtra("titulo") : null;
        int id = getIntent() != null ? getIntent().getIntExtra("id", -1) : -1;
        String desc = getIntent() != null ? getIntent().getStringExtra("desc") : null;

        // Mostrar datos (con fallback)
        tvTitulo.setText(titulo != null ? titulo : "Sin título");

        // ¡CUIDADO! setText(int) interpreta como recurso -> usa String.valueOf
        tvId.setText("ID: " + (id >= 0 ? id : 0));

        tvDescripcion.setText(desc != null ? desc : "Sin descripción");

        // Si viniste sin extras, avisa (no crashea)
        if (titulo == null && id == -1 && desc == null) {
            Toast.makeText(this, "Sin datos recibidos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

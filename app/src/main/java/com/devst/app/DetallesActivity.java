package com.devst.app;

// Importa las librerías necesarias
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Para recibir datos de otra pantalla
import android.os.Bundle;
import android.widget.Button; // Botón para volver
import android.widget.TextView; // Para mostrar los datos
import android.widget.Toast; // Mensajes cortos

// Clase principal que muestra los detalles de un elemento
public class DetallesActivity extends AppCompatActivity {

    // Elementos visuales
    private TextView tvTituloDetalle; // Muestra el título del ítem
    private TextView tvIdDetalle; // Muestra el ID del ítem
    private TextView tvDescripcionDetalle; // Muestra una descripción
    private Button btnVolver; // Botón para regresar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles); // Enlaza con el diseño XML

        // Referencias a los elementos del layout
        tvTituloDetalle = findViewById(R.id.tvTituloDetalle);
        tvIdDetalle = findViewById(R.id.tvIdDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtiene los datos enviados desde HomeActivity
        Intent intent = getIntent();
        if (intent != null) {
            String titulo = intent.getStringExtra("titulo");
            int id = intent.getIntExtra("id", -1); // Valor por defecto si no se recibe
            // Muestra los valores recibidos
            tvTituloDetalle.setText(titulo != null ? titulo : "Lápiz ✏️");
            tvIdDetalle.setText("ID: #" + id);
            tvDescripcionDetalle.setText("Este es un Lápiz.\n Color: Negro");
        } else {
            Toast.makeText(this, "No se recibieron datos", Toast.LENGTH_SHORT).show();
        }

        // Acción del botón: volver al menú principal
        btnVolver.setOnClickListener(v -> {
            finish(); // Cierra la pantalla actual y vuelve a la anterior
        });
    }
}

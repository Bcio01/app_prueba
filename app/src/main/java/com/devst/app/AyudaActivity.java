// Indica el paquete donde está la clase
package com.devst.app;

// Importa las librerías necesarias de Android
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Para navegar entre pantallas
import android.os.Bundle; // Para manejar el ciclo de vida
import android.widget.Button; // Botones de la interfaz
import android.widget.TextView; // Textos visibles en la pantalla

// Clase principal de la pantalla "Ayuda"
public class AyudaActivity extends AppCompatActivity {

    // Elementos de la interfaz
    private Button btnVolverHome; // Botón para volver al menú principal
    private TextView tvTituloAyuda; // Título principal de la pantalla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llama al método base del ciclo de vida
        setContentView(R.layout.activity_ayuda); // Conecta el XML con esta clase

        // Referencias a los elementos del diseño
        tvTituloAyuda = findViewById(R.id.tvTituloAyuda);
        btnVolverHome = findViewById(R.id.btnVolverHome);

        // Cambia el título dinámicamente si se desea (opcional)
        tvTituloAyuda.setText("Ayuda / Preguntas Frecuentes");

        // Acción del botón: volver al menú principal
        btnVolverHome.setOnClickListener(v -> {
            // Crea un Intent para volver a HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent); // Abre HomeActivity
            finish(); // Cierra esta pantalla para no acumular en la pila
        });
    }
}

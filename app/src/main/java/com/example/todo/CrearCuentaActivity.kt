package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CrearCuentaActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)

        // Ajuste de insets para compatibilidad visual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de vistas
        val txtNombreNuevo: TextView = findViewById(R.id.edtNombre)
        val txtEmailNuevo: TextView = findViewById(R.id.edtEmailNuevo)
        val txtPassword1: TextView = findViewById(R.id.edtPasswordNuevo1)
        val txtPassword2: TextView = findViewById(R.id.edtPasswordNuevo2)
        val btnCrear: Button = findViewById(R.id.btnCrearCuentaNueva)

        // Inicialización de FirebaseAuth
        firebaseAuth = Firebase.auth

        // Listener para el botón de crear cuenta
        btnCrear.setOnClickListener {
            val nombre = txtNombreNuevo.text.toString().trim()
            val email = txtEmailNuevo.text.toString().trim()
            val pass1 = txtPassword1.text.toString().trim()
            val pass2 = txtPassword2.text.toString().trim()

            // Validación de campos vacíos
            if (nombre.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(
                    this,
                    "Todos los campos son obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validación de contraseñas coincidentes
            if (pass1 != pass2) {
                Toast.makeText(
                    this,
                    "Error: las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()
                txtPassword1.requestFocus()
                return@setOnClickListener
            }

            // Intentar crear cuenta
            createAccount(email, pass1)
        }
    }
    private fun createAccount(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    sendEmailVerification()
                    Toast.makeText(baseContext, "Bienvenido a la familia", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, InicioActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Algo salió mal, error: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun sendEmailVerification() {
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    baseContext,
                    "Verificación enviada con éxito",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext,
                    "Algo salió mal, error: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

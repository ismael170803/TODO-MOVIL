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

class RecordarPassActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recordar_pass)

        // Ajuste de insets para compatibilidad visual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de vistas
        val txtMail: TextView = findViewById(R.id.txtEmailCambio)
        val btnCambiar: Button = findViewById(R.id.btnCambiar)

        // Inicialización de FirebaseAuth
        firebaseAuth = Firebase.auth

        // Listener para el botón de cambio
        btnCambiar.setOnClickListener {
            val email = txtMail.text.toString().trim()

            // Validación de campo vacío
            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    "Por favor, ingresa un correo electrónico válido",
                    Toast.LENGTH_SHORT
                ).show()
                txtMail.requestFocus()
                return@setOnClickListener
            }

            // Enviar correo de restablecimiento de contraseña
            sendPasswordReset(email)
        }
    }

    private fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Correo de restablecimiento enviado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, InicioActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Error: no se pudo enviar el correo de restablecimiento",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
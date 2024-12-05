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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class InicioActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btningresar : Button = findViewById(R.id.btnIngresar)
        val txtemail : TextView = findViewById(R.id.edtEmail)
        val txtpass : TextView = findViewById(R.id.edtPassword)
        val btncrear_Cuenta_Nueva : TextView = findViewById(R.id.btnCrearCuenta)
        val btnRecordar : TextView = findViewById(R.id.btnOlvidar)
        firebaseAuth= Firebase.auth
        btningresar.setOnClickListener {
            val email = txtemail.text.toString().trim() // Elimina espacios extra
            val password = txtpass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                // Si alguno de los campos está vacío, muestra un mensaje
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Si los campos están llenos, intenta iniciar sesión
                sign(email, password)
            }
        }
        btncrear_Cuenta_Nueva.setOnClickListener()
        {
            val i = Intent(this, CrearCuentaActivity::class.java)
            startActivity(i)
        }
        btnRecordar.setOnClickListener()
        {
            val i = Intent(this, RecordarPassActivity::class.java)
            startActivity(i)
        }
    }
    private fun sign(email: String, password: String)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                val verifica = user?.isEmailVerified
                if (verifica == true) {
                    Toast.makeText(baseContext, "inicio de Sesión Correcto", Toast.LENGTH_SHORT)
                        .show()
                    //aquí vamos a la segunda página
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                else
                {
                    Toast.makeText(baseContext,"Verifique su Correo Primero", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(baseContext,"Error del Email y/o Contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
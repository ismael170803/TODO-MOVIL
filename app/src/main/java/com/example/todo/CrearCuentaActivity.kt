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
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtnombre_nuevo : TextView = findViewById(R.id.edtNombre)
        val txtemail_nuevo : TextView = findViewById(R.id.edtEmailNuevo)
        val txtpassword1 : TextView = findViewById(R.id.edtPasswordNuevo1)
        val txtpassword2 : TextView = findViewById(R.id.edtPasswordNuevo2)
        val btnCrear : Button = findViewById(R.id.btnCrearCuentaNueva)
        btnCrear.setOnClickListener()
        {
            var pass1 = txtpassword1.text.toString()
            var pass2 = txtpassword2.text.toString()
            if (pass1.equals(pass2))
            {
                createAccount(txtemail_nuevo.text.toString(),txtpassword1.text.toString())
            }
            else{
                Toast.makeText(baseContext, "Error: los password no coinciden", Toast.LENGTH_SHORT).show()
                txtpassword1.requestFocus()
            }
        }
        firebaseAuth = Firebase.auth
    }
    private fun createAccount(email: String, password: String)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener (this){ task->
            if (task.isSuccessful)
            {
                sendEmailVerification()
                Toast.makeText(baseContext, "Bienvenido a la familia", Toast.LENGTH_SHORT).show()
                val i = Intent (this, InicioActivity::class.java)
                startActivity(i)
            }
            else
            {
                Toast.makeText(baseContext, "Algo Salió Mal, Error:" + task.exception, Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun sendEmailVerification()
    {
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this){ taks ->
            if (taks.isSuccessful)
            {
                Toast.makeText(baseContext,"Verificación de manera Exitosa", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(baseContext, "Algo Salió Mal, Error:" + taks.exception, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
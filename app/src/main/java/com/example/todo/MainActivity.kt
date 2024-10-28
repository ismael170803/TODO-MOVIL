package com.example.todo

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración del binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del BottomNavigationView
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Configuración del ActionBar
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Manejo del mensaje de notificación
        val message = intent.getStringExtra("notification_message")
        if (message != null) {
            // Cargar HomeFragment y pasar el mensaje
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("notification_message", message)
                }
            }
            // Navegar al HomeFragment usando el NavController
            navController.navigate(R.id.navigation_home) // Navegar a HomeFragment
        }
    }
}
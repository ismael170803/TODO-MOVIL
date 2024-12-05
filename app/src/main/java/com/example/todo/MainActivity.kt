package com.example.todo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        binding.navView.setupWithNavController(navController)

        binding.fabAddTask.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mostrarDialogoAñadirTarea()
                } else {
                    solicitarPermisoNotificaciones()
                }
            } else {
                mostrarDialogoAñadirTarea()
            }
        }
    }

    private fun mostrarDialogoAñadirTarea() {
        AñadirTarea { nuevaTarea ->
            val currentFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_activity_main)
                ?.childFragmentManager
                ?.fragments
                ?.firstOrNull()
            if (currentFragment is HomeFragment) {
                currentFragment.agregarTareaDesdeDialog(nuevaTarea)
            }
        }.show(supportFragmentManager, "AñadirTarea")
    }

    private fun solicitarPermisoNotificaciones() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            // Mostrar una explicación al usuario si es necesario
            // (Puedes agregar un mensaje aquí antes de solicitar el permiso)
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mostrarDialogoAñadirTarea()
            } else {
                // El permiso fue denegado, manejar el caso aquí
            }
        }
    }
}

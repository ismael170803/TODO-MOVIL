package com.example.todo

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import com.example.todo.databinding.AddTareaBinding
import java.util.Calendar
import java.util.UUID

class AñadirTarea(private val onTaskAdded: (Tarea) -> Unit) : DialogFragment() {

    private var _binding: AddTareaBinding? = null
    private val binding get() = _binding!!
    private var tareaExistente: Tarea? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getString("id")
            val titulo = it.getString("titulo")
            val descripcion = it.getString("descripcion")
            val prioridad = it.getString("prioridad") ?: "Alta"
            val fechaLimite = it.getString("fechaLimite") ?: ""
            tareaExistente = Tarea(id ?: "", titulo ?: "", descripcion ?: "", prioridad, fechaLimite)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddTareaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar spinner de prioridades
        val prioridades = arrayOf("Alta", "Media", "Baja")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spPrioridad.adapter = adapter

        // Cambiar texto y cargar datos si es actualización
        if (tareaExistente != null) {
            binding.btnGuardar.text = "Actualizar"
            tareaExistente?.let { tarea ->
                binding.etTitulo.setText(tarea.titulo)
                binding.etDescripcion.setText(tarea.descripcion)
                binding.etFechaLimite.setText(tarea.fechaLimite)
                binding.spPrioridad.setSelection(prioridades.indexOf(tarea.prioridad))
            }
        } else {
            binding.btnGuardar.text = "Agregar"
        }

        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etTitulo.text.toString()
            val descripcion = binding.etDescripcion.text.toString()
            val prioridad = binding.spPrioridad.selectedItem.toString()
            val fechaLimite = binding.etFechaLimite.text.toString()

            if (titulo.isBlank() || descripcion.isBlank()) {
                Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val nuevaTarea = tareaExistente?.copy(
                    titulo = titulo,
                    descripcion = descripcion,
                    prioridad = prioridad,
                    fechaLimite = fechaLimite
                ) ?: Tarea(UUID.randomUUID().toString(), titulo, descripcion, prioridad, fechaLimite)

                onTaskAdded(nuevaTarea)

                // Crear el canal de notificación (solo la primera vez)
                createNotificationChannel()
                // Enviar notificación
                sendTaskCreatedNotification(nuevaTarea)

                dismiss()
            }
        }


        // Configurar selección de fecha límite
        binding.etFechaLimite.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val fecha = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etFechaLimite.setText(fecha)
            }, year, month, day).show()
        }

        // Botón Cancelar
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
    private val channelId = "task_creation_channel"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Creación de Tareas"
            val descriptionText = "Notificaciones sobre la creación de nuevas tareas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendTaskCreatedNotification(tarea: Tarea) {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        val intent = Intent(requireContext(), MainActivity::class.java) // Cambia según la actividad principal
        val pendingIntent = PendingIntent.getActivity(
            requireContext(), 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp) // Asegúrate de tener un icono en tu proyecto
            .setContentTitle("Nueva Tarea Creada")
            .setContentText("Se ha añadido la tarea: ${tarea.titulo}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(tarea.id.hashCode(), notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
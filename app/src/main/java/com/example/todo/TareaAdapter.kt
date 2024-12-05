package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(
    private var tareas: List<Tarea>,
    private val onDelete: (String) -> Unit,
    private val onUpdate: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    fun updateData(newTareas: List<Tarea>) {
        tareas = newTareas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_tarea, parent, false) // Usa tu layout personalizado
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.titulo.text = tarea.titulo
        holder.descripcion.text = tarea.descripcion
        holder.prioridad.text = "Prioridad: ${tarea.prioridad}"
        holder.fechaLimite.text = "Fecha límite: ${tarea.fechaLimite.ifBlank { "Sin asignar" }}"

        // Actualizar tarea al hacer clic en el ítem
        holder.itemView.setOnClickListener {
            onUpdate(tarea)
        }

        holder.borrar.setOnClickListener {
            onDelete(tarea.id)
        }
    }

    override fun getItemCount(): Int = tareas.size

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val descripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val prioridad: TextView = itemView.findViewById(R.id.tvPrioridad)
        val fechaLimite: TextView = itemView.findViewById(R.id.tvFechaLimite) // Nuevo campo
        val borrar: ImageButton = itemView.findViewById(R.id.ibtnBorrar)
    }
}

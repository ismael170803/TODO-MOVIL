package com.example.todo

data class Tarea(
    var id: String = "",
    var titulo: String = "",
    var descripcion: String = "",
    var prioridad: String = "Alta", // Prioridad predeterminada
    var fechaLimite: String = "", // Nuevo campo para la fecha límite
    var timestamp: Long = System.currentTimeMillis() // Tiempo de creación
) {
    init {
        require(prioridad in listOf("Alta", "Media", "Baja")) {
            "Prioridad debe ser 'Alta', 'Media' o 'Baja'"
        }
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "titulo" to titulo,
            "descripcion" to descripcion,
            "prioridad" to prioridad,
            "fechaLimite" to fechaLimite,
            "timestamp" to timestamp
        )
    }
}

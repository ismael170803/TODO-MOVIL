package com.xcheko51x.crud_firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Tarea
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TareaViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _listaTareas = MutableLiveData<List<Tarea>>(emptyList())
    val listaTareas: LiveData<List<Tarea>> = _listaTareas

    init {
        obtenerTareas()
    }

    fun obtenerTareas() {
        db.collection("tareas")
            .orderBy("timestamp", Query.Direction.ASCENDING) // Ordena por tiempo de creación
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }
                val tareas = snapshot?.documents?.mapNotNull { doc ->
                    val tarea = doc.toObject(Tarea::class.java)
                    tarea?.apply { id = doc.id }
                }.orEmpty()
                _listaTareas.value = tareas
            }
    }

    fun agregarTarea(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nuevaTarea = tarea.toMap() + ("timestamp" to System.currentTimeMillis())
                db.collection("tareas").document(tarea.id).set(nuevaTarea).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarTarea(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("tareas").document(tarea.id).set(tarea.toMap()).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun borrarTarea(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("tareas").document(id).delete().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

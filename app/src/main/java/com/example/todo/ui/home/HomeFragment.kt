package com.example.todo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.AñadirTarea
import com.example.todo.Tarea
import com.example.todo.TareaAdapter
import com.example.todo.databinding.FragmentHomeBinding
import com.xcheko51x.crud_firebase.TareaViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TareaAdapter
    private lateinit var viewModel: TareaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TareaViewModel::class.java]

        setupObservers()
        setupRecyclerView()

        return binding.root
    }

    fun agregarTareaDesdeDialog(nuevaTarea: Tarea) {
        viewModel.agregarTarea(nuevaTarea)
    }

    private fun setupObservers() {
        viewModel.listaTareas.observe(viewLifecycleOwner) { tareas ->
            adapter.updateData(tareas)
        }
    }
    private fun setupRecyclerView() {
        adapter = TareaAdapter(emptyList(), ::borrarTarea, ::actualizarTarea)
        binding.rvTareas.layoutManager = LinearLayoutManager(context)
        binding.rvTareas.adapter = adapter
    }

    private fun borrarTarea(id: String) {
        viewModel.borrarTarea(id)
    }
    private fun actualizarTarea(tarea: Tarea) {
        val editarTareaDialog = AñadirTarea { tareaEditada ->
            viewModel.actualizarTarea(tareaEditada) // Actualiza los datos en Firebase
        }
        val args = Bundle().apply {
            putString("id", tarea.id)
            putString("titulo", tarea.titulo)
            putString("descripcion", tarea.descripcion)
        }
        editarTareaDialog.arguments = args
        editarTareaDialog.show(parentFragmentManager, "EditarTarea")
        Toast.makeText(context, "Dialogo mostrado", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.todo.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.Tarea
import com.example.todo.TareaAdapter
import com.example.todo.databinding.FragmentDashboardBinding
import com.xcheko51x.crud_firebase.TareaViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TareaViewModel
    private lateinit var altaAdapter: TareaAdapter
    private lateinit var mediaAdapter: TareaAdapter
    private lateinit var bajaAdapter: TareaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TareaViewModel::class.java]

        setupAdapters()
        setupObservers()
        setupSearchBar()

        return binding.root
    }

    private fun setupAdapters() {
        altaAdapter = TareaAdapter(emptyList(), {}, {})
        mediaAdapter = TareaAdapter(emptyList(), {}, {})
        bajaAdapter = TareaAdapter(emptyList(), {}, {})

        binding.rvPrioridadAlta.layoutManager = LinearLayoutManager(context)
        binding.rvPrioridadAlta.adapter = altaAdapter

        binding.rvPrioridadMedia.layoutManager = LinearLayoutManager(context)
        binding.rvPrioridadMedia.adapter = mediaAdapter

        binding.rvPrioridadBaja.layoutManager = LinearLayoutManager(context)
        binding.rvPrioridadBaja.adapter = bajaAdapter
    }

    private fun setupObservers() {
        viewModel.listaTareas.observe(viewLifecycleOwner) { tareas ->
            altaAdapter.updateData(tareas.filter { it.prioridad == "Alta" })
            mediaAdapter.updateData(tareas.filter { it.prioridad == "Media" })
            bajaAdapter.updateData(tareas.filter { it.prioridad == "Baja" })
        }
    }

    private fun setupSearchBar() {
        binding.etBuscador.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filteredTasks = viewModel.listaTareas.value.orEmpty().filter {
                    it.titulo.lowercase().contains(query) || it.descripcion.lowercase().contains(query)
                }
                altaAdapter.updateData(filteredTasks.filter { it.prioridad == "Alta" })
                mediaAdapter.updateData(filteredTasks.filter { it.prioridad == "Media" })
                bajaAdapter.updateData(filteredTasks.filter { it.prioridad == "Baja" })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package br.com.todolist.presentation

import br.com.todolist.databinding.FragmentTaskListBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.todolist.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskListViewModel by viewModel()
    private lateinit var taskAdapter: TaskAdapter

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_task, null)
        val editText = dialogView.findViewById<EditText>(R.id.edit_text_task_title)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Adicionar") { dialog, _ ->
                val title = editText.text.toString()
                if (title.isNotBlank()) {
                    viewModel.addTask(title)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewTasks.adapter = null
        _binding = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskChecked = { task, isChecked ->
                viewModel.updateTask(task.copy(isCompleted = isChecked))
            },
            onTaskDeleted = { task ->
                viewModel.deleteTask(task)
            }
        )
        binding.recyclerViewTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collectLatest { tasks ->
                taskAdapter.submitList(tasks)
            }
        }
    }
}
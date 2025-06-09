package br.com.todolist.presentation

import br.com.todolist.data.local.model.Task
import br.com.todolist.databinding.ItemTaskBinding
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskDeleted: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.textViewTitle.text = task.title
            binding.checkboxCompleted.isChecked = task.isCompleted

            updateStrikeThrough(task.isCompleted)

            binding.checkboxCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onTaskChecked(getItem(adapterPosition), isChecked)
                    updateStrikeThrough(isChecked)
                }
            }

            binding.buttonDelete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onTaskDeleted(getItem(adapterPosition))
                }
            }
        }

        private fun updateStrikeThrough(isCompleted: Boolean) {
            if (isCompleted) {
                binding.textViewTitle.paintFlags =
                    binding.textViewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.textViewTitle.paintFlags =
                    binding.textViewTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    companion object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
package com.example.notesappfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappfinal.db.TodoX

class TodoRecyclerViewAdapter(private val clickListener: (TodoX) -> Unit): RecyclerView.Adapter<TodoViewHolder>() {

    private val todoList = ArrayList<TodoX>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return TodoViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }


    fun setList(todos: List<TodoX>){
        todoList.clear()
        todoList.addAll(todos)
    }
}

class TodoViewHolder(private val view: View): RecyclerView.ViewHolder(view){
    fun bind(todo: TodoX, clickListener: (TodoX) -> Unit){
        val todoTextView = view.findViewById<TextView>(R.id.tvName)
        val userTextView = view.findViewById<TextView>(R.id.tvUser)
        val checkView = view.findViewById<ImageView>(R.id.ivCompleted)
        todoTextView.text = todo.todo
        userTextView.text = todo.userId.toString()
        if(todo.completed == true){
            checkView.setImageResource(R.drawable.ic_completed)
        }
        else{
            checkView.setImageResource(R.drawable.ic_checkbox)
        }
        view.setOnClickListener {
            clickListener(todo)
        }
    }
}
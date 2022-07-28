package com.example.notesappfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappfinal.db.TodoDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var todoEditText: EditText
    private lateinit var userEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var adapter: TodoRecyclerViewAdapter
    private lateinit var viewModel: TodoViewModel

    private lateinit var selectedTodo: TodoX
    private var isListItemClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoEditText = findViewById(R.id.etName)
        userEditText = findViewById(R.id.etUser)
        saveButton = findViewById(R.id.btnSave)
        clearButton = findViewById(R.id.btnClear)
        todoRecyclerView = findViewById(R.id.rvTodo)

        val dao = TodoDatabase.getInstance(application).todoDao()
        val factory = TodoViewModelFactory(dao)
        viewModel = ViewModelProvider(this,factory).get(TodoViewModel::class.java)

        saveButton.setOnClickListener {
            if(todoEditText.text.toString() == "" || userEditText.text.toString() == ""){
                Toast.makeText(this,"Please enter all the required values",Toast.LENGTH_SHORT).show()
            }
            else if(userEditText.text.toString().toIntOrNull()  == null){
                Toast.makeText(this,"Please enter a valid numeric user id",Toast.LENGTH_SHORT).show()
            }
            else{
                if(isListItemClicked){
                    updateTodoData()
                    clearInput()
                }
                else{
                    saveTodoData()
                    clearInput()
                }
            }
        }
        clearButton.setOnClickListener {
            if(isListItemClicked){
                deleteTodoData()
                clearInput()
            }
            else{
                clearInput()
            }
        }

        initRecyclerView()
    }

    private fun saveTodoData(){
        viewModel.insertTodo(
            TodoX(
                false,
                0,
                todoEditText.text.toString(),
                userEditText.text.toString().toInt()
            )
        )
    }

    private fun updateTodoData(){
        viewModel.updateTodo(
            TodoX(
                selectedTodo.completed,
                selectedTodo.id,
                todoEditText.text.toString(),
                userEditText.text.toString().toInt()
            )
        )
        saveButton.text = "Save"
        clearButton.text = "Clear"
        isListItemClicked = false
    }

    private fun deleteTodoData(){
        viewModel.deleteTodo(
            TodoX(
                selectedTodo.completed,
                selectedTodo.id,
                todoEditText.text.toString(),
                userEditText.text.toString().toInt()
            )
        )
        saveButton.text = "Save"
        clearButton.text = "Clear"
        isListItemClicked = false
    }

    private fun clearInput(){
        todoEditText.setText("")
        userEditText.setText("")
    }

    private fun initRecyclerView(){
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TodoRecyclerViewAdapter{
            selectedItem: TodoX -> listItemClicked(selectedItem)
        }
        todoRecyclerView.adapter = adapter

        displayStudentsList()
    }

    private fun displayStudentsList(){
        viewModel.todos.observe(this,{
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }


    private fun listItemClicked(todo: TodoX){
        selectedTodo = todo
        saveButton.text = "Update"
        clearButton.text = "Delete"
        isListItemClicked = true
        todoEditText.setText(selectedTodo.todo)
        userEditText.setText(selectedTodo.userId.toString())
    }
}


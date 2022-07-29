package com.example.notesappfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappfinal.db.Todo
import com.example.notesappfinal.db.TodoDatabase
import com.example.notesappfinal.db.TodoX
import com.example.notesappfinal.retrofit.RetrofitInstance
import com.example.notesappfinal.retrofit.TodoService
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var todoEditText: EditText
    private lateinit var userEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button
    private lateinit var cloudButton: Button
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var adapter: TodoRecyclerViewAdapter
    private lateinit var viewModel: TodoViewModel
    private lateinit var cbCheckbox: CheckBox

    private lateinit var selectedTodo: TodoX
    private var isListItemClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cbCheckbox = findViewById(R.id.cbCompleted)
        todoEditText = findViewById(R.id.etName)
        userEditText = findViewById(R.id.etUser)
        saveButton = findViewById(R.id.btnSave)
        clearButton = findViewById(R.id.btnClear)
        todoRecyclerView = findViewById(R.id.rvTodo)

        cloudButton = findViewById(R.id.btnCloud)

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



        cloudButton.setOnClickListener {

            val retService = RetrofitInstance.getRetrofitInstance().create(TodoService::class.java)

            val responseLiveData: LiveData<Response<Todo>> = liveData{
                val response = retService.getTodos()
                emit(response)
            }
            responseLiveData.observe(this, Observer{
                val todosList: ListIterator<TodoX>? = it.body()?.todos?.listIterator()
                if(todosList != null){
                    while(todosList.hasNext()){
                        val todosItem : TodoX = todosList.next()
                    viewModel.insertTodo(
                        TodoX(
                            false,
                            0,
                            todosItem.todo,
                            todosItem.id
                        )
                    )
                    }
                }
            })

            Toast.makeText(this,"Todos have been added from the cloud. Scroll down to access them.",
                Toast.LENGTH_SHORT).show()
        }

        initRecyclerView()

        //Retrofit

    }

    private fun saveTodoData(){
        viewModel.insertTodo(
            TodoX(
                cbCheckbox.isChecked,
                0,
                todoEditText.text.toString(),
                userEditText.text.toString().toInt()
            )
        )
        cbCheckbox.isChecked = false
        cbCheckbox.visibility = View.GONE
    }

    private fun updateTodoData(){
        viewModel.updateTodo(
            TodoX(
                cbCheckbox.isChecked,
                selectedTodo.id,
                todoEditText.text.toString(),
                userEditText.text.toString().toInt()
            )
        )
        cbCheckbox.visibility = View.GONE
        cbCheckbox.isChecked = false
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
        cbCheckbox.isChecked = false
        saveButton.text = "Save"
        clearButton.text = "Clear"
        isListItemClicked = false
        cbCheckbox.visibility = View.GONE
    }

    private fun clearInput(){
        todoEditText.setText("")
        userEditText.setText("")
        cbCheckbox.isChecked = false

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
        viewModel.todos.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }


    private fun listItemClicked(todo: TodoX){
        selectedTodo = todo
        saveButton.text = "Update"
        clearButton.text = "Delete"
        isListItemClicked = true
        cbCheckbox.visibility = View.VISIBLE
        cbCheckbox.isChecked = selectedTodo.completed
        todoEditText.setText(selectedTodo.todo)
        userEditText.setText(selectedTodo.userId.toString())
    }
}


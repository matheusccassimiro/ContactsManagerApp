package com.matheus.contactsmanagerapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.matheus.contactsmanagerapp.viewModel.UserViewModel
import com.matheus.contactsmanagerapp.viewModel.UserViewModelFactory
import com.matheus.contactsmanagerapp.databinding.ActivityMainBinding
import com.matheus.contactsmanagerapp.room.User
import com.matheus.contactsmanagerapp.room.UserDatabase
import com.matheus.contactsmanagerapp.room.UserRepository
import com.matheus.contactsmanagerapp.viewUi.MyRecyclerViewAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //ROOM
        val dao = UserDatabase.getInstance(application).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)


        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        DisplayUsersList()

    }

    private fun DisplayUsersList() {
        userViewModel.users.observe(this, {
            binding.recyclerView.adapter = MyRecyclerViewAdapter(it, {selectedItem: User ->listItemClicled(selectedItem)})
        })
    }

    private fun listItemClicled(selectedItem: User) {
        Toast.makeText(this,"You clicked on ${selectedItem.name}",Toast.LENGTH_LONG).show()

        userViewModel.initUpdateAndDelete(selectedItem)
    }
}
package com.example.deadlinescheduler

import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadlinescheduler.adapter.ItemListAdapter
import com.example.deadlinescheduler.data.InMemoryCategoryRepository
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.databinding.ActivityMainBinding
import com.example.deadlinescheduler.databinding.ContentMainBinding
import com.example.deadlinescheduler.model.Category

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contentBinding: ContentMainBinding

    private lateinit var itemListAdapter: ItemListAdapter
    private lateinit var categoryAdapter: ArrayAdapter<Category>

    private var itemRepository: InMemoryItemRepository = InMemoryItemRepository()
    private var categoryRepository: InMemoryCategoryRepository = InMemoryCategoryRepository()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val item = menu.findItem(R.id.category_spinner)
        val layout = item.actionView as ConstraintLayout
        val spinner = layout.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = categoryAdapter
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        contentBinding = ContentMainBinding.inflate(layoutInflater, binding.content, true)
        setContentView(binding.root)

        // item list
        itemListAdapter = ItemListAdapter()
        itemListAdapter.items = itemRepository.getItems()

        contentBinding.list.apply {
            adapter = itemListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        // toolbar
        setSupportActionBar(binding.toolbar)

        // category spinner
        categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoryRepository.getCategories())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
package com.example.deadlinescheduler

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadlinescheduler.adapter.ItemListAdapter
import com.example.deadlinescheduler.data.InMemoryCategoryRepository
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.databinding.ActivityMainBinding
import com.example.deadlinescheduler.databinding.ContentMainBinding
import com.example.deadlinescheduler.databinding.UpdateProductBinding
import com.example.deadlinescheduler.model.Category
import com.example.deadlinescheduler.model.Item
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contentBinding: ContentMainBinding

    private lateinit var itemListAdapter: ItemListAdapter
    private lateinit var categoryAdapter: ArrayAdapter<Category>

    private var itemRepository: InMemoryItemRepository = InMemoryItemRepository()
    private var categoryRepository: InMemoryCategoryRepository = InMemoryCategoryRepository()


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        contentBinding = ContentMainBinding.inflate(layoutInflater, binding.listContainer, true)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        defineList()
        defineCategorySpinner(binding.spinner)
        edgeToEdge()
    }
    fun showCreateSheet() {
        UpdateItemSheet(this)
    }
    fun showEditSheet(item: Item) {
        UpdateItemSheet(this, item)
    }
    private fun defineList() {
        itemListAdapter = ItemListAdapter(this)
        itemListAdapter.items = itemRepository.getItems()

        contentBinding.list.apply {
            adapter = itemListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    fun defineCategorySpinner(spinner: Spinner) {
        categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoryRepository.getCategories()
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = categoryAdapter
    }
    private fun edgeToEdge() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    class UpdateItemSheet(private var mainActivity: MainActivity) {
        private var bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(mainActivity.binding.root.context)
        private var bottomSheetView: View =
            mainActivity.layoutInflater.inflate(R.layout.update_product, null)
        private var dismissMessage = "Changes were dismissed"
        private var updateBinding: UpdateProductBinding = UpdateProductBinding.bind(bottomSheetView)
        init {
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.setOnDismissListener {
                Toast.makeText(mainActivity.binding.root.context,
                    dismissMessage,
                    Toast.LENGTH_SHORT).show()
            }
            bottomSheetDialog.show()
        }
        constructor(mainActivity: MainActivity, listItem: Item) : this(mainActivity) {
            updateBinding.editName.setText(listItem.name)
            updateBinding.editQuantity.setText(listItem.number.toString())
            updateBinding.expirationDate.updateDate(
                listItem.expirationDate.year,
                listItem.expirationDate.monthValue - 1,
                listItem.expirationDate.dayOfMonth
            )
            mainActivity.defineCategorySpinner(updateBinding.editCategory.spinner)
            updateBinding.editCategory.spinner.setSelection(mainActivity.categoryAdapter.getPosition(listItem.category))
            updateBinding.save.root.setOnClickListener {
                saveItem()
                dismissMessage = "Item was edited successfully"
                bottomSheetDialog.dismiss()
            }
        }
        private fun saveItem() {
            // TODO: Implement saveItem
        }
    }
}
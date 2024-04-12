package com.example.deadlinescheduler

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deadlinescheduler.adapter.CategorySpinnerAdapter
import com.example.deadlinescheduler.data.InMemoryCategoryRepository
import com.example.deadlinescheduler.databinding.ActivityMainBinding
import com.example.deadlinescheduler.model.Category
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listFragment: ListFragment

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        defineSwitch(menu)
        defineSpinner()
        listFragment.itemListAdapter.applyFilters()
        updateResultsTextView()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        listFragment = supportFragmentManager.findFragmentById(R.id.include) as ListFragment

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        updateResultsTextView()

        edgeToEdge()
    }

    fun updateResultsTextView() {
        binding.results.text = "Entries found: " + listFragment.getNumberOfItems().toString()
    }

    private fun edgeToEdge() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun defineSwitch(menu: Menu) {
        val item = menu.findItem(R.id.category)
        val switch = item.actionView?.findViewById<SwitchMaterial>(R.id.expiredSwitch)
        listFragment = supportFragmentManager.findFragmentById(R.id.include) as ListFragment
        switch?.setOnCheckedChangeListener { _, isChecked ->
            listFragment.itemListAdapter.updateFilterParams(isChecked)
            listFragment.itemListAdapter.applyFilters()
            updateResultsTextView()
        }
    }

    private fun defineSpinner() {
        val spinner = binding.toolbar.findViewById<Spinner>(R.id.spinner)
        spinner?.adapter =
            CategorySpinnerAdapter(this, InMemoryCategoryRepository().getCategories())
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                listFragment.itemListAdapter.updateFilterParams(parent?.getItemAtPosition(position) as Category)
                listFragment.itemListAdapter.applyFilters()
                updateResultsTextView()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
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

/**
 * Main activity of the application.
 * This activity is responsible for managing the user interface and interactions.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listFragment: ListFragment

    /**
     * Called when the options menu is created.
     * This method inflates the menu, defines the switch and spinner behavior, applies filters and updates the results text view.
     * @param menu The options menu in which you place your items.
     * @return Boolean. You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        defineSwitch(menu)
        defineSpinner()
        listFragment.itemListAdapter.applyFilters()
        updateResultsTextView()
        return true
    }

    /**
     * Called when the activity is starting.
     * This method initializes the activity, sets the content view, sets the toolbar, and updates the results text view.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
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

    /**
     * Updates the results text view with the number of items found.
     */
    fun updateResultsTextView() {
        binding.results.text = "Entries found: " + listFragment.getNumberOfItems().toString()
    }

    /**
     * Enables edge-to-edge display and sets padding for system bars.
     */
    private fun edgeToEdge() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Defines the behavior of the switch in the options menu.
     * The switch updates the filter parameters and applies the filters when its state is changed.
     * @param menu The options menu.
     */
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

    /**
     * Defines the behavior of the spinner in the toolbar.
     * The spinner updates the filter parameters and applies the filters when an item is selected.
     */
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
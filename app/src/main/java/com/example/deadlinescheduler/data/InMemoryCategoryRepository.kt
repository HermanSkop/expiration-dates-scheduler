package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Category

class InMemoryCategoryRepository : CategoryRepository{
    private val categories = listOf(
        Category("Fruits and vegetables", android.R.color.holo_blue_dark),
        Category("Meat and fish", android.R.color.holo_green_dark),
        Category("Dairy", android.R.color.holo_orange_dark),
        Category("Bread and bakery", android.R.color.holo_red_dark),
        )

    override fun getCategories(): List<Category> {
        return categories
    }
}
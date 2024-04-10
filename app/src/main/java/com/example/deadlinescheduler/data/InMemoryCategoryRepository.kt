package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Category

class InMemoryCategoryRepository : CategoryRepository{
    private val categories = listOf(
        Category(Categories.NONE, android.R.color.holo_orange_dark),
        Category(Categories.FOOD, android.R.color.holo_orange_dark),
        Category(Categories.MEDICINE, android.R.color.holo_red_light),
        Category(Categories.COSMETICS, android.R.color.holo_purple),
        )

    override fun getCategories(): List<Category> {
        return categories
    }
}
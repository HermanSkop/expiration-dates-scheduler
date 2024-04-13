package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Category

class InMemoryCategoryRepository : CategoryRepository {
    private val categories = listOf(
        Category.NONE,
        Category.FOOD,
        Category.COSMETICS,
        Category.MEDICINE,
    )

    override fun getCategories(): List<Category> {
        return categories
    }
}
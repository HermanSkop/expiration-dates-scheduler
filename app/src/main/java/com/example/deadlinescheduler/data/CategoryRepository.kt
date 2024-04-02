package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Category

interface CategoryRepository {
    fun getCategories(): List<Category>
}
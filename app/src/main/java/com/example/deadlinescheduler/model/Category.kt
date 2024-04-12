package com.example.deadlinescheduler.model

import java.util.Locale

enum class Category {
    FOOD,
    MEDICINE,
    NONE,
    COSMETICS;
    override fun toString(): String {
        if (this == NONE) {
            return "Select a category"
        }
        return name.toString().lowercase().capitalize(Locale.getDefault())
    }
}
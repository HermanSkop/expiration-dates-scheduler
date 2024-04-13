package com.example.deadlinescheduler.model

import java.util.Locale

/**
 * Enum class representing the categories an item can belong to.
 * The categories are FOOD, MEDICINE, NONE, and COSMETICS.
 * The NONE category is used as a default value and is not meant to be assigned to an item.
 * The toString method is overridden to return a user-friendly string representation of the category.
 */
enum class Category {
    FOOD, MEDICINE, NONE, COSMETICS;

    /**
     * Returns a string representation of the category.
     * If the category is NONE, it returns "Select a category".
     * Otherwise, it returns the category name in lowercase with the first letter capitalized.
     * @return String. The string representation of the category.
     */
    override fun toString(): String {
        if (this == NONE) {
            return "Select a category"
        }
        return name.lowercase().capitalize(Locale.getDefault())
    }
}
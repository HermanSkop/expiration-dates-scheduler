package com.example.deadlinescheduler.model

import android.R
import androidx.annotation.ColorRes
import com.example.deadlinescheduler.data.Categories
import java.util.Locale

class Category(val name: Categories, @ColorRes val color: Int = R.color.holo_blue_dark) {
    override fun toString(): String {
        if (name == Categories.NONE) {
            return "Select a category"
        }
        return name.toString().lowercase().capitalize(Locale.getDefault())
    }
    override fun equals(other: Any?): Boolean {
        return name == (other as Category).name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + color
        return result
    }
}
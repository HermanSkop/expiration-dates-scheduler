package com.example.deadlinescheduler.model

import androidx.annotation.ColorRes

class Category(val name: String, @ColorRes val color: Int) {
    override fun toString(): String {
        return name
    }
}
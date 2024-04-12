package com.example.deadlinescheduler.model

import java.time.LocalDate

data class Item(val name: String, val number: Int, val expirationDate: LocalDate, val category: Category) {
    override fun toString(): String {
        return name
    }
    override fun equals(other: Any?): Boolean {
        return this.name == (other as Item).name
    }
    override fun hashCode(): Int {
        return name.hashCode()
    }
}
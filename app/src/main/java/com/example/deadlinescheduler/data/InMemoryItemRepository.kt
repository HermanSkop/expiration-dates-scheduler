package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Category
import com.example.deadlinescheduler.model.Item
import java.time.LocalDate

class InMemoryItemRepository : ItemRepository{
    private val categories = InMemoryCategoryRepository().getCategories()
    private val itemList = listOf(
        Item("Milk", 1, LocalDate.now().plusDays(7), categories[1]),
        Item("Bread", 1, LocalDate.now().plusDays(3), categories[1]),
        Item("Eggs", 12, LocalDate.now().plusDays(15), categories[1]),
        Item("Marijuana", 1, LocalDate.now().plusDays(10), categories[2]),
        Item("Cough Syrup", 1, LocalDate.now().plusDays(30), categories[2]),
        Item("Bandages", 10, LocalDate.now().plusDays(365), categories[2]),
        Item("Pills", 30, LocalDate.now().plusDays(30), categories[2]),
        Item("Shampoo", 1, LocalDate.now().plusDays(90), categories[3]),
        Item("Conditioner", 1, LocalDate.now().plusDays(90), categories[3]),
        Item("Lipstick", 1, LocalDate.now().plusDays(180), categories[3]),
        Item("Foundation", 1, LocalDate.now().plusDays(365), categories[3]),
        Item("Mascara", 1, LocalDate.now().plusDays(180), categories[3]),
    )

    override fun getItems(): List<Item> {
        return itemList
    }
}
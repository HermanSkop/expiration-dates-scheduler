package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Item
import java.time.LocalDate

class InMemoryItemRepository : ItemRepository{
    private val itemList = listOf(
        Item("Milk", 1, LocalDate.now().plusDays(7)),
        Item("Bread", 1, LocalDate.now().plusDays(3)),
        Item("Eggs", 12, LocalDate.now().plusDays(15)),
        Item("Butter", 1, LocalDate.now().plusDays(10)),
        Item("Cheese", 1, LocalDate.now().plusDays(20)),
        Item("Yogurt", 6, LocalDate.now().plusDays(10)),
        Item("Apple", 6, LocalDate.now().plusDays(7)),
        Item("Banana", 6, LocalDate.now().plusDays(5)),
        Item("Orange", 6, LocalDate.now().plusDays(7)),
        Item("Grapes", 6, LocalDate.now().plusDays(7)),
        Item("Strawberry", 6, LocalDate.now().plusDays(3)),
    )

    override fun getItems(): List<Item> {
        return itemList
    }
}
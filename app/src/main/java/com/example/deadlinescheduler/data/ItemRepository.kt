package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
    fun addItem(item: Item)
    fun removeItem(item: Item)
    fun updateItem(item: Item)
    fun getItemId(item: Item): Int
    fun insertItemAtIndex(item: Item, index: Int)
}
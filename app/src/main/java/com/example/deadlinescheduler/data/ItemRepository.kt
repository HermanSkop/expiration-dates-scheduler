package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Item

interface ItemRepository {
    fun getFilteredItems(isExpired: Boolean): List<Item>
    fun getItemsSortedByExpirationDate(): List<Item>
    fun addItem(item: Item)
    fun removeItem(item: Item)
    fun updateItem(item: Item)
    fun getItemId(item: Item): Int
    fun insertItemAtIndex(item: Item, index: Int)
    fun replaceItem(itemToInsert: Item, nameOfItemToReplace: String)
    fun getItemByName(name: String): Item
}
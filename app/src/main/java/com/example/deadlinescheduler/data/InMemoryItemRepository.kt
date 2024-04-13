package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Category
import com.example.deadlinescheduler.model.Item
import java.time.LocalDate

object InMemoryItemRepository : ItemRepository {
    private val itemList = Item.createSampleItems().toMutableList()

    override fun addItem(item: Item) {
        if (itemList.contains(item)) {
            throw IllegalArgumentException("Item already exists")
        }
        itemList.add(item)
        itemList.sortBy { it.expirationDate }
    }

    override fun removeItem(item: Item) {
        itemList.remove(item)
    }

    override fun updateItem(item: Item) {
        if (!itemList.contains(item)) {
            throw IllegalArgumentException("Item does not exist")
        }
        itemList[itemList.indexOf(item)] = item
    }

    override fun getItemId(item: Item): Int {
        return itemList.indexOf(item)
    }

    override fun insertItemAtIndex(item: Item, index: Int) {
        itemList.add(index, item)
    }

    override fun replaceItem(itemToInsert: Item, nameOfItemToReplace: String) {
        if (itemList.contains(itemToInsert) && itemToInsert.name != nameOfItemToReplace) {
            throw IllegalArgumentException("Item already exists")
        }
        val itemToReplace = getItemByName(nameOfItemToReplace)
        val index = getItemId(itemToReplace)
        removeItem(itemToReplace)
        insertItemAtIndex(itemToInsert, index)
    }

    override fun getItemByName(name: String): Item {
        return itemList.find { it.name == name }!!
    }

    override fun getItemsSortedByExpirationDate(): List<Item> {
        return itemList.sortedBy { it.expirationDate }
    }

    override fun getFilteredItems(isExpired: Boolean): List<Item> {
        return if (isExpired) {
            itemList.filter { it.expirationDate.isBefore(LocalDate.now()) }
        } else {
            itemList.filter { it.expirationDate.isAfter(LocalDate.now()) }
        }
    }
}
package com.example.deadlinescheduler.data

import com.example.deadlinescheduler.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
}
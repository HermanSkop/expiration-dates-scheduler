package com.example.deadlinescheduler.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.example.deadlinescheduler.R
import com.example.deadlinescheduler.model.Category

class CategorySpinnerAdapter(context: Context, categories: List<Category>) :
    ArrayAdapter<Category>(context, R.layout.category_spinner_item, categories) {
}
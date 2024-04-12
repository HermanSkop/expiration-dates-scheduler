package com.example.deadlinescheduler.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.deadlinescheduler.FormBottomSheetDialogFragment
import com.example.deadlinescheduler.MainActivity
import com.example.deadlinescheduler.model.Category
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.databinding.ItemBinding
import com.example.deadlinescheduler.model.Item
import java.time.LocalDate

class ItemView(private val itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun onBind(item: Item) {
        with(itemBinding) {
            name.text = item.name
            quantity.text = item.number.toString()
            expirationDate.text = item.expirationDate.toString()
            category.text = item.category.toString()
        }
    }
}

@SuppressLint("NotifyDataSetChanged")
class ItemListAdapter(
    private var context: MainActivity,
    private var isExpired: Boolean = false,
    private var category: Category = Category.NONE
) :
    RecyclerView.Adapter<ItemView>() {
    private var itemRepository: InMemoryItemRepository = InMemoryItemRepository
    var items: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)
        return ItemView(binding)
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        holder.itemView.setOnClickListener {
            if (items[position].expirationDate.isBefore(LocalDate.now()))
                showExpiredError(holder, position)
            else
                createFormSheet(position, holder)

        }
        holder.itemView.setOnLongClickListener {
            if (items[position].expirationDate.isBefore(LocalDate.now()))
                showExpiredError(holder, position)
            else
                showDeleteDialog(holder, position)
            true
        }
        holder.onBind(items[position])
    }

    private fun showDeleteDialog(
        holder: ItemView,
        position: Int
    ) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete item")
            .setMessage("Are you sure you want to delete ${items[position].name}?")
            .setPositiveButton("Yes") { _, _ ->
                removeItem(items[position])
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    private fun createFormSheet(
        position: Int,
        holder: ItemView
    ) {
        val formBottomSheetDialogFragment =
            FormBottomSheetDialogFragment(this, items[position])
        formBottomSheetDialogFragment.show(
            (holder.itemView.context as MainActivity).supportFragmentManager,
            "formBottomSheetDialogFragment"
        )
    }

    private fun showExpiredError(holder: ItemView, position: Int) {
        Toast.makeText(
            holder.itemView.context,
            "${items[position].name} is already expired",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun resetItems() {
        items = itemRepository.getItemsSortedByExpirationDate()
        notifyDataSetChanged()
    }

    fun updateFilterParams(category: Category, isExpired: Boolean) {
        this.category = category
        this.isExpired = isExpired
    }

    fun updateFilterParams(category: Category) {
        this.category = category
    }

    fun updateFilterParams(isExpired: Boolean) {
        this.isExpired = isExpired
    }

    private fun filterExpiredItems(isExpired: Boolean) {
        this.isExpired = isExpired
        filterExpiredItems()
    }

    private fun filterExpiredItems() {
        items = if (isExpired) {
            items.filter { it.expirationDate.isBefore(LocalDate.now()) }
        } else {
            items.filter { it.expirationDate.isAfter(LocalDate.now().minusDays(1)) }
        }
        notifyDataSetChanged()
    }

    private fun filterCategory(category: Category) {
        if (category == Category.NONE) return
        items = items.filter { it.category == category }
        notifyDataSetChanged()
    }

    private fun removeItem(item: Item) {
        itemRepository.removeItem(item)
        applyFilters()
    }

    fun applyFilters() {
        applyFilters(category, isExpired)
    }

    fun applyFilters(category: Category, isExpired: Boolean) {
        updateFilterParams(category, isExpired)
        resetItems()
        filterCategory(category)
        filterExpiredItems(isExpired)
        notifyDataSetChanged()
        context.updateResultsTextView()
    }

}
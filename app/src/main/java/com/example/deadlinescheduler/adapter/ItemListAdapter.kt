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

/**
 * ViewHolder class for the RecyclerView.
 * This class binds the item data to the item view.
 */
class ItemView(private val itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    /**
     * Binds the item data to the item view.
     * @param item The item to bind.
     */
    fun onBind(item: Item) {
        with(itemBinding) {
            name.text = item.name
            quantity.text = item.quantity.number?.toString() ?: ""
            unit.text = item.quantity.unit ?: ""
            expirationDate.text = item.expirationDate.toString()
            category.text = item.category.toString()
        }
    }
}

/**
 * Adapter class for the RecyclerView.
 * This class manages the item data and binds it to the views.
 */
@SuppressLint("NotifyDataSetChanged")
class ItemListAdapter(
    private var context: MainActivity,
    private var isExpired: Boolean = false,
    private var category: Category = Category.NONE
) : RecyclerView.Adapter<ItemView>() {
    private var itemRepository: InMemoryItemRepository = InMemoryItemRepository
    var items: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)
        return ItemView(binding)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = items.size

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the itemView to reflect the item at the given position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        holder.itemView.setOnClickListener {
            if (items[position].expirationDate.isBefore(LocalDate.now())) showExpiredError(
                holder, position
            )
            else createFormSheet(position, holder)

        }
        holder.itemView.setOnLongClickListener {
            if (items[position].expirationDate.isBefore(LocalDate.now())) showExpiredError(
                holder, position
            )
            else showDeleteDialog(holder, position)
            true
        }
        holder.onBind(items[position])
    }

    /**
     * Shows a dialog to confirm the deletion of an item.
     * @param holder The ViewHolder for the item.
     * @param position The position of the item in the list.
     */
    private fun showDeleteDialog(
        holder: ItemView, position: Int
    ) {
        AlertDialog.Builder(holder.itemView.context).setTitle("Delete item")
            .setMessage("Are you sure you want to delete ${items[position].name}?")
            .setPositiveButton("Yes") { _, _ ->
                removeItem(items[position])
            }.setNegativeButton("No") { _, _ -> }.show()
    }

    /**
     * Creates a FormBottomSheetDialogFragment for editing an item.
     * @param position The position of the item in the list.
     * @param holder The ViewHolder for the item.
     */
    private fun createFormSheet(
        position: Int, holder: ItemView
    ) {
        val formBottomSheetDialogFragment = FormBottomSheetDialogFragment(this, items[position])
        formBottomSheetDialogFragment.show(
            (holder.itemView.context as MainActivity).supportFragmentManager,
            "formBottomSheetDialogFragment"
        )
    }

    /**
     * Shows a toast message indicating that an item is expired.
     * @param holder The ViewHolder for the item.
     * @param position The position of the item in the list.
     */
    private fun showExpiredError(holder: ItemView, position: Int) {
        Toast.makeText(
            holder.itemView.context,
            "${items[position].name} is already expired",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Resets the item list to the original list from the repository.
     */
    private fun resetItems() {
        items = itemRepository.getItemsSortedByExpirationDate()
        notifyDataSetChanged()
    }

    /**
     * Updates the filter parameters.
     * @param category The category to filter by.
     * @param isExpired Whether to filter by expiration date.
     */
    fun updateFilterParams(category: Category, isExpired: Boolean) {
        this.category = category
        this.isExpired = isExpired
    }

    /**
     * Updates the filter parameters.
     * @param category The category to filter by.
     */
    fun updateFilterParams(category: Category) {
        this.category = category
    }

    /**
     * Updates the filter parameters.
     * @param isExpired Whether to filter by expiration date.
     */
    fun updateFilterParams(isExpired: Boolean) {
        this.isExpired = isExpired
    }

    /**
     * Sets the isExpired property and filters the items based on their expiration date.
     * @param isExpired If true, filters the items to only include those that are expired. If false, filters the items to only include those that are not expired.
     */
    private fun filterExpiredItems(isExpired: Boolean) {
        this.isExpired = isExpired
        filterExpiredItems()
    }

    /**
     * Filters the items based on their expiration date.
     * If isExpired is true, only items that are expired (expiration date is before the current date) are included.
     * If isExpired is false, only items that are not expired (expiration date is after the current date minus one day) are included.
     * After filtering, the adapter's data set is updated and the UI is notified of the change.
     */
    private fun filterExpiredItems() {
        items = if (isExpired) {
            items.filter { it.expirationDate.isBefore(LocalDate.now()) }
        } else {
            items.filter { it.expirationDate.isAfter(LocalDate.now().minusDays(1)) }
        }
        notifyDataSetChanged()
    }

    /**
     * Filters the item list by category.
     * @param category The category to filter by.
     */
    private fun filterCategory(category: Category) {
        if (category == Category.NONE) return
        items = items.filter { it.category == category }
        notifyDataSetChanged()
    }

    /**
     * Removes an item from the list and the repository.
     * @param item The item to remove.
     */
    private fun removeItem(item: Item) {
        itemRepository.removeItem(item)
        applyFilters()
    }

    /**
     * Applies the current filters to the item list.
     */
    fun applyFilters() {
        applyFilters(category, isExpired)
    }

    /**
     * Applies the specified filters to the item list.
     * @param category The category to filter by.
     * @param isExpired Whether to filter by expiration date.
     */
    fun applyFilters(category: Category, isExpired: Boolean) {
        updateFilterParams(category, isExpired)
        resetItems()
        filterCategory(category)
        filterExpiredItems(isExpired)
        notifyDataSetChanged()
        context.updateResultsTextView()
    }

}
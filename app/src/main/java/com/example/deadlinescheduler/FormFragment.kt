package com.example.deadlinescheduler

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.deadlinescheduler.adapter.CategorySpinnerAdapter
import com.example.deadlinescheduler.adapter.ItemListAdapter
import com.example.deadlinescheduler.data.InMemoryCategoryRepository
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.data.ItemRepository
import com.example.deadlinescheduler.databinding.FragmentFormBinding
import com.example.deadlinescheduler.model.Category
import com.example.deadlinescheduler.model.Item
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate

/**
 * Fragment that displays a form for creating or editing items.
 * This fragment is responsible for managing the user interface and interactions for the item form.
 */
class FormBottomSheetDialogFragment(private val listAdapter: ItemListAdapter) :
    BottomSheetDialogFragment() {
    private var item: Item? = null
    private lateinit var binding: FragmentFormBinding
    private var messageOnSave: String = "Item successfully created"
    private var messageOnDismiss: String = "Changes were discarded"
    private lateinit var itemRepository: ItemRepository

    /**
     * Secondary constructor for editing an existing item.
     * @param listAdapter The adapter for the list of items.
     * @param item The item to be edited.
     */
    constructor(listAdapter: ItemListAdapter, item: Item) : this(listAdapter) {
        this.item = item
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This method initializes the fragment, sets the content view, and initializes the item repository.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return View. Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called when the fragment is starting.
     * This method initializes the item repository.
     * @param savedInstanceState If the fragment is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemRepository = InMemoryItemRepository
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * This method initializes the form fields with the item data (if editing an item), and sets the click listener for the save button.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editCategory.spinner.adapter = CategorySpinnerAdapter(
            requireContext(), InMemoryCategoryRepository().getCategories()
        )

        if (item != null) {
            binding.editName.setText(item!!.name)
            binding.editQuantity.setText(item!!.quantity.number?.toString() ?: "")
            binding.editUnit.setText(item!!.quantity.unit ?: "")
            binding.expirationDate.updateDate(
                item!!.expirationDate.year,
                item!!.expirationDate.monthValue - 1,
                item!!.expirationDate.dayOfMonth
            )
            binding.editCategory.spinner.setSelection(
                (binding.editCategory.spinner.adapter as CategorySpinnerAdapter).getPosition(item!!.category)
            )
            messageOnSave = "Changes were saved successfully"
        }

        binding.save.root.setOnClickListener {
            saveItem()
        }
    }

    /**
     * Called when the fragment is no longer being displayed to the user.
     * This method displays a toast message indicating that the changes were discarded.
     * @param dialog The dialog that was dismissed will be passed into the method.
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Toast.makeText(binding.root.context, messageOnDismiss, Toast.LENGTH_SHORT).show()
    }

    /**
     * Saves the item data entered in the form.
     * This method validates the form data, creates or updates the item, updates the list adapter, and dismisses the fragment.
     */
    private fun saveItem() {
        try {
            val name = binding.editName.text.toString()
            val quantity = binding.editQuantity.text.toString().toIntOrNull()
            val unit = binding.editUnit.text.let { if (it.isNullOrBlank()) null else it.toString() }
            val expirationDate = LocalDate.of(
                binding.expirationDate.year,
                binding.expirationDate.month + 1,
                binding.expirationDate.dayOfMonth
            )
            val category = binding.editCategory.spinner.selectedItem as Category

            if (item == null) {
                itemRepository.addItem(
                    Item(
                        name, Item.Quantity(quantity, unit), expirationDate, category
                    )
                )
                listAdapter.notifyItemInserted(listAdapter.items.size)
            } else {
                itemRepository.replaceItem(
                    Item(
                        name, Item.Quantity(quantity, unit), expirationDate, category
                    ), item!!.name
                )
                listAdapter.notifyItemChanged(listAdapter.items.indexOf(item))
            }

            listAdapter.items = itemRepository.getItemsSortedByExpirationDate()
            listAdapter.applyFilters()

            messageOnDismiss = messageOnSave
            dismiss()
        } catch (e: Exception) {
            Toast.makeText(binding.root.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
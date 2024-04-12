package com.example.deadlinescheduler

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.deadlinescheduler.adapter.CategorySpinnerAdapter
import com.example.deadlinescheduler.adapter.ItemListAdapter
import com.example.deadlinescheduler.data.Categories
import com.example.deadlinescheduler.data.InMemoryCategoryRepository
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.data.ItemRepository
import com.example.deadlinescheduler.databinding.FragmentFormBinding
import com.example.deadlinescheduler.model.Category
import com.example.deadlinescheduler.model.Item
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate

class FormBottomSheetDialogFragment(private val listAdapter: ItemListAdapter) : BottomSheetDialogFragment() {
    private var item: Item? = null
    private lateinit var binding: FragmentFormBinding
    private var messageOnSave: String = "Item successfully created"
    private var messageOnDismiss: String = "Changes were discarded"
    private lateinit var itemRepository: ItemRepository
    constructor(listAdapter: ItemListAdapter, item: Item) : this(listAdapter) {
        this.item = item
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemRepository = InMemoryItemRepository
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editCategory.spinner.adapter = CategorySpinnerAdapter(
            requireContext(),
            InMemoryCategoryRepository().getCategories()
        )

        if (item != null) {
            binding.editName.setText(item!!.name)
            binding.editQuantity.setText(item!!.number.toString())
            binding.expirationDate.updateDate(item!!.expirationDate.year,
                item!!.expirationDate.monthValue, item!!.expirationDate.dayOfMonth)
            binding.editCategory.spinner.setSelection(
                (binding.editCategory.spinner.adapter as CategorySpinnerAdapter).getPosition(item!!.category)
            )
            messageOnSave = "Changes were saved successfully"
        }

        binding.save.root.setOnClickListener {
            saveItem()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Toast.makeText(binding.root.context, messageOnDismiss, Toast.LENGTH_SHORT).show()
    }

    private fun saveItem() {
        try {
            val name = binding.editName.text.toString()
            val quantity = binding.editQuantity.text.toString().toInt()
            val expirationDate = LocalDate.of(
                binding.expirationDate.year,
                binding.expirationDate.month + 1,
                binding.expirationDate.dayOfMonth)
            val category = binding.editCategory.spinner.selectedItem as Category

            validateItem(name, quantity, expirationDate, category)

            if (item == null) {
                itemRepository.addItem(Item(name, quantity, expirationDate, category))
                listAdapter.notifyItemInserted(listAdapter.items.size)
            } else {
                val index = itemRepository.getItemId(item!!)
                itemRepository.removeItem(item!!)
                itemRepository.insertItemAtIndex(Item(name, quantity, expirationDate, category), index)
                listAdapter.notifyItemChanged(index)
            }
            messageOnDismiss = messageOnSave
            dismiss()
        } catch (e: Exception) {
            Toast.makeText(binding.root.context, e.message, Toast.LENGTH_LONG).show()
        }
    }
    private fun validateItem(name: String, quantity: Int, expirationDate: LocalDate, category: Category) {
        if (name.isEmpty() || name.isBlank()) throw IllegalArgumentException("Name cannot be empty")
        if (category.name == Categories.NONE) throw IllegalArgumentException("Category must be selected")
        if (expirationDate.isBefore(LocalDate.now())) throw IllegalArgumentException("Expiration date must be in the future")
        if (quantity <= 0) throw IllegalArgumentException("Quantity must be greater than 0")
    }
}
package com.example.deadlinescheduler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadlinescheduler.adapter.ItemListAdapter
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.databinding.FragmentListBinding
import com.example.deadlinescheduler.model.Category

/**
 * Fragment that displays a list of items.
 * This fragment is responsible for managing the user interface and interactions for the list of items.
 */
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    lateinit var itemListAdapter: ItemListAdapter
    private var itemRepository: InMemoryItemRepository = InMemoryItemRepository

    /**
     * Called to have the fragment instantiate its user interface view.
     * This method initializes the fragment, sets the content view, and initializes the item list adapter.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return View. Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * This method initializes the item list adapter, sets the adapter and layout manager for the recycler view, and sets the click listener for the create item button.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemListAdapter = ItemListAdapter(view.context as MainActivity, false, Category.NONE)
        itemListAdapter.items = itemRepository.getItemsSortedByExpirationDate()

        binding.list.apply {
            adapter = itemListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.createItem.setOnClickListener {
            val formBottomSheetDialogFragment = FormBottomSheetDialogFragment(itemListAdapter)
            formBottomSheetDialogFragment.show(
                parentFragmentManager, "formBottomSheetDialogFragment"
            )
        }
    }

    /**
     * Returns the number of items in the item list adapter.
     * @return Int. The number of items in the item list adapter.
     */
    fun getNumberOfItems(): Int {
        return itemListAdapter.itemCount
    }
}
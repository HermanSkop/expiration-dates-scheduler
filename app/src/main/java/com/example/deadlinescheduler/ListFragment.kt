package com.example.deadlinescheduler

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadlinescheduler.adapter.ItemListAdapter
import com.example.deadlinescheduler.data.InMemoryItemRepository
import com.example.deadlinescheduler.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var itemListAdapter: ItemListAdapter
    private var itemRepository: InMemoryItemRepository = InMemoryItemRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemListAdapter = ItemListAdapter()
        itemListAdapter.items = itemRepository.getItemsSortedByExpirationDate()

        binding.list.apply {
            adapter = itemListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.createItem.setOnClickListener {
            val formBottomSheetDialogFragment = FormBottomSheetDialogFragment(itemListAdapter)
            formBottomSheetDialogFragment.show(
                parentFragmentManager,
                "formBottomSheetDialogFragment"
            )
        }
    }


}
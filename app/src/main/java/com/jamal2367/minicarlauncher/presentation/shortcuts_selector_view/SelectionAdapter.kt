package com.jamal2367.minicarlauncher.presentation.shortcuts_selector_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamal2367.minicarlauncher.databinding.ElementAppShortcutHorizontalBinding
import com.jamal2367.minicarlauncher.repository.entities.Shortcut

class SelectorAdapter(
    private val maxSelectionCount: () -> Int,
    private val onSelectedCountUpd: (Int) -> Unit
) : RecyclerView.Adapter<SelectorAdapter.Holder>() {

    private var items = ArrayList<Shortcut>()
    private var currentSelections = 0

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): Holder {
        val binding = ElementAppShortcutHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val shortcut = items[position]

        holder.apply {
            binding.ivIcon.setImageDrawable(shortcut.icon)
            binding.tvTitle.text = shortcut.title
            holderStateSelected = shortcut.selected

            itemView.setOnClickListener {
                if (holderStateSelected || isAllowMakeSelection()) {
                    items[position] = shortcut.copy(!holderStateSelected, position)
                    notifyItemChanged(position)
                    updateSelectedCount()
                }
            }
        }
    }

    private fun isAllowMakeSelection(): Boolean = currentSelections < maxSelectionCount.invoke()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: Collection<Shortcut>) {
        items.clear()
        items.addAll(data)
        updateSelectedCount()
        notifyDataSetChanged()
    }

    private fun updateSelectedCount() {
        currentSelections = items.count { it.selected }
        onSelectedCountUpd.invoke(currentSelections)
    }

    fun getUpdatedItems(): Collection<Shortcut> = items

    inner class Holder(val binding: ElementAppShortcutHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var holderStateSelected: Boolean
            get() = itemView.isSelected
            set(value) {
                itemView.isSelected = value
            }
    }
}
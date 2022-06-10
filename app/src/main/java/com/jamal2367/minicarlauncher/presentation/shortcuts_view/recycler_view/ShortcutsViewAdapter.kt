package com.jamal2367.minicarlauncher.presentation.shortcuts_view.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamal2367.minicarlauncher.databinding.ElementAppShortcutBinding
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.ChangeEvent
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.Reorder
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.SimpleClick
import com.jamal2367.minicarlauncher.repository.entities.Shortcut

class ShortcutsViewAdapter(private val eventsListener: (ChangeEvent) -> Unit) :
    RecyclerView.Adapter<ShortcutsViewAdapter.Holder>(), MutableList<Shortcut> by ArrayList() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ElementAppShortcutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = get(position)

        if (item.position == -1) {
            item.position = position
            eventsListener(Reorder(item))
        }

        holder.run {
            binding.ivShortcutIcon.setImageDrawable(item.icon)
            binding.tvShortcutLabel.text = item.title
        }

        holder.itemView.setOnClickListener { eventsListener(SimpleClick(item)) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: Collection<Shortcut>) {
        val sortedData = data.sortedBy(Shortcut::position)
        clear()
        addAll(sortedData)
        notifyDataSetChanged()
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        val firstItem = get(fromPosition)
        val secondItem = get(toPosition)

        set(fromPosition, secondItem)
        set(toPosition, firstItem)
        notifyItemMoved(fromPosition, toPosition)

        eventsListener(Reorder(firstItem.copy(position = toPosition)))
        eventsListener(Reorder(secondItem.copy(position = fromPosition)))
    }

    inner class Holder(val binding: ElementAppShortcutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
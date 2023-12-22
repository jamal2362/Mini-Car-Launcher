package com.jamal2367.minicarlauncher.presentation.shortcuts_selector_view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jamal2367.minicarlauncher.R
import com.jamal2367.minicarlauncher.repository.entities.Shortcut

class SelectorDialog : DialogFragment() {

    private var doOnItemsSelection: (Collection<Shortcut>) -> Unit = {}
    private var maxItemsSelectLimit: Int = 0
    private var currentSelectionsCount: Int = 0
    private val selectorAdapter =
        SelectorAdapter(this::maxItemsSelectLimit, this::onSelectionsCountUpdate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val selectorView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = selectorAdapter
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getTitleWithSelectedCount())
            .setView(selectorView)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                doOnItemsSelection.invoke(selectorAdapter.getUpdatedItems())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                dialog?.cancel()
            }
            .create()
    }

    fun setListData(data: Collection<Shortcut>): SelectorDialog {
        selectorAdapter.setData(data)
        return this
    }

    fun selectionCallback(callback: (Collection<Shortcut>) -> Unit): SelectorDialog {
        doOnItemsSelection = callback
        return this
    }

    fun selectionItemsLimit(limit: Int): SelectorDialog {
        maxItemsSelectLimit = limit
        return this
    }

    private fun onSelectionsCountUpdate(currentSelectionsCount: Int) {
        this.currentSelectionsCount = currentSelectionsCount
        dialog?.takeIf { it.isShowing }?.setTitle(getTitleWithSelectedCount())
    }

    private fun getTitleWithSelectedCount(): String =
        if (currentSelectionsCount < 1) {
            resources.getString(R.string.dialog_no_applications_selected_title)
        } else if (currentSelectionsCount > 1) {
            resources.getString(R.string.dialog_applications_selected_title, currentSelectionsCount)
        } else {
            resources.getString(R.string.dialog_application_selected_title)
        }
}
package com.jamal2367.minicarlauncher.presentation.shortcuts_view

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.jamal2367.minicarlauncher.R
import com.jamal2367.minicarlauncher.databinding.FragmentShortcutsBinding
import com.jamal2367.minicarlauncher.isResolvable
import com.jamal2367.minicarlauncher.presentation.helpers.NoDoubleClickGuard
import com.jamal2367.minicarlauncher.presentation.shortcuts_selector_view.SelectorDialog
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.recycler_view.ShortcutsTouchCallback
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.recycler_view.ShortcutsViewAdapter
import com.jamal2367.minicarlauncher.repository.entities.Shortcut
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LauncherFragment : DaggerFragment(), LauncherView {

    @Inject
    lateinit var presenter: LauncherPresenter

    private lateinit var binding: FragmentShortcutsBinding

    private val shortcutsViewAdapter = ShortcutsViewAdapter(eventsListener = { event ->
        when (event) {
            is SimpleClick -> presenter.onShortcutClick(event.shortcut)
            is Reorder -> presenter.onShortcutChanged(event.shortcut)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShortcutsBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAppsList.adapter = shortcutsViewAdapter
        ItemTouchHelper(ShortcutsTouchCallback(shortcutsViewAdapter::onItemMove))
            .attachToRecyclerView(binding.rvAppsList)
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unbindView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shortcuts_main, menu)

        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.setOnMenuItemClickListener(NoDoubleClickGuard())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_items -> presenter.onShowAppsClick()
            R.id.action_open_assistant -> presenter.onOpenAssistant(requireContext())
            R.id.action_open_settings -> presenter.onOpenSettings(requireContext())
            R.id.action_set_wallpaper -> presenter.onSetWallpaper(requireContext())
            R.id.action_web_search -> presenter.onWebSearch(requireContext())
        }
        return false
    }

    override fun setShortcuts(data: Collection<Shortcut>) {
        shortcutsViewAdapter.setData(data)
    }

    override fun toggleNoShortcutsPane(show: Boolean) {
        binding.llEmptyHint.visibility = if (show) View.VISIBLE else View.GONE
        binding.llEmptyHint1.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showAppsSelector(data: Collection<Shortcut>, maxItemsCount: Int) {
        val fm = parentFragmentManager

        SelectorDialog()
            .setListData(data)
            .selectionCallback(presenter::onAppsSelected)
            .selectionItemsLimit(maxItemsCount)
            .show(fm, "")
    }

    override fun launchExternalApp(shortcut: Shortcut) {
        if (shortcut.launchIntent?.isResolvable(context) == true) {
            startActivity(shortcut.launchIntent)
        } else {
            presenter.onAppUnavailable(shortcut)
        }
    }

    override fun getShortcutsPerViewCount(): Int = binding.rvAppsList.getItemsCountDesired()

    override fun notifyAppUnavailable() {
        Snackbar.make(binding.llEmptyHint, R.string.snackbar_app_unavailable, Snackbar.LENGTH_LONG)
            .show()
    }
}
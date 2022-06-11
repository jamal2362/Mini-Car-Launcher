package com.jamal2367.minicarlauncher.presentation.shortcuts_view

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.jamal2367.minicarlauncher.interactors.ShortcutsInteractor
import com.jamal2367.minicarlauncher.presentation.helpers.SingleViewPresenter
import com.jamal2367.minicarlauncher.presentation.helpers.untilUnbind
import com.jamal2367.minicarlauncher.repository.entities.Shortcut
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton


interface LauncherView {
    fun setShortcuts(data: Collection<Shortcut>)
    fun toggleNoShortcutsPane(show: Boolean)
    fun showAppsSelector(data: Collection<Shortcut>, maxItemsCount: Int)
    fun launchExternalApp(shortcut: Shortcut)
    fun getShortcutsPerViewCount(): Int
    fun notifyAppUnavailable()
}

@Singleton
class LauncherPresenter
@Inject constructor(
    private val shortcutsInteractor: ShortcutsInteractor
) : SingleViewPresenter<LauncherView>() {

    private val shortcutsCountAllowed = AtomicInteger()

    fun onAppUnavailable(shortcut: Shortcut) {
        removeFromSelected(shortcut)
        applyView { it.notifyAppUnavailable() }
    }

    private fun removeFromSelected(shortcut: Shortcut) {
        val unavailableShortcut = shortcut.copy(selected = false)
        onShortcutChanged(unavailableShortcut, true)
    }

    fun onShortcutChanged(shortcut: Shortcut, forceRefresh: Boolean = false) {
        shortcutsInteractor.updateShortcut(shortcut)
            .doOnComplete { if (forceRefresh) refreshShortcutsSelected() }
            .subscribe()
            .untilUnbind(this)
    }

    fun onShortcutClick(clicked: Shortcut) {
        applyView { it.launchExternalApp(clicked) }
    }

    fun onAppsSelected(selections: Collection<Shortcut>) {
        Observable.fromIterable(selections)
            .flatMapCompletable(shortcutsInteractor::updateShortcut)
            .doOnTerminate { refreshShortcutsSelected() }
            .subscribe()
            .untilUnbind(this)
    }

    override fun bindView(view: LauncherView) {
        super.bindView(view)
        shortcutsCountAllowed.set(view.getShortcutsPerViewCount())
        refreshShortcutsSelected()
    }

    private fun refreshShortcutsSelected() {
        shortcutsInteractor.getSelectedShortcuts()
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess { updateNoShortcutsPaneBy(it.isEmpty()) }
            .subscribe(this::setShortcuts)
            .untilUnbind(this)
    }

    private fun updateNoShortcutsPaneBy(noShortcutsSelected: Boolean) {
        applyView { it.toggleNoShortcutsPane(noShortcutsSelected) }
    }

    private fun setShortcuts(data: Collection<Shortcut>) {
        applyView { it.setShortcuts(data) }
    }

    fun onShowAppsClick() {
        shortcutsInteractor.getAllShortcuts()
            .sorted()
            .toList()
            .subscribe(this::callAppsSelectionDialog)
            .untilUnbind(this)
    }

    fun onOpenSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    fun onOpenAssistant(context: Context) {
        context.startActivity(Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun onWebSearch(context: Context) {
        context.startActivity(Intent(Intent.ACTION_WEB_SEARCH))
    }

    private fun callAppsSelectionDialog(shortcuts: Collection<Shortcut>) {
        applyView { it.showAppsSelector(shortcuts, shortcutsCountAllowed.get()) }
    }
}
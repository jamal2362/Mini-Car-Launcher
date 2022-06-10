package com.jamal2367.minicarlauncher.interactors

import com.jamal2367.minicarlauncher.repository.ShortcutsRepository
import com.jamal2367.minicarlauncher.repository.entities.Shortcut
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutsInteractor
@Inject constructor(private val shortcutsRepository: ShortcutsRepository) {

    fun getAllShortcuts(): Observable<Shortcut> = shortcutsRepository.getShortcutsUpdated()

    fun getSelectedShortcuts(): Observable<Shortcut> {
        return shortcutsRepository.getShortcutsUpdatedCached()
            .filter(Shortcut::selected)
    }

    fun updateShortcut(shortcut: Shortcut): Completable {
        return shortcutsRepository.updateSingleShortcut(shortcut)
    }
}
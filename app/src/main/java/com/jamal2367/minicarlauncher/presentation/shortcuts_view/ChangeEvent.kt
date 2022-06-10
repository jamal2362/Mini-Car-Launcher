package com.jamal2367.minicarlauncher.presentation.shortcuts_view

import com.jamal2367.minicarlauncher.repository.entities.Shortcut

sealed class ChangeEvent
class SimpleClick(val shortcut: Shortcut) : ChangeEvent()
class Reorder(val shortcut: Shortcut) : ChangeEvent()
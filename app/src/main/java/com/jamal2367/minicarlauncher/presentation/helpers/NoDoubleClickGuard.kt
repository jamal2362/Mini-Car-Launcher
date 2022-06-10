package com.jamal2367.minicarlauncher.presentation.helpers

import android.view.MenuItem

class NoDoubleClickGuard(private val lockingTimeoutMs: Int = DEFAULT_TIMEOUT_MILLIS) :
    MenuItem.OnMenuItemClickListener {

    companion object {
        const val DEFAULT_TIMEOUT_MILLIS = 500
    }

    private var lastClickTime = 0L

    override fun onMenuItemClick(p0: MenuItem): Boolean {
        val curTime = System.currentTimeMillis()
        val clickTimeCheckable = lastClickTime
        lastClickTime = curTime

        return curTime - clickTimeCheckable <= lockingTimeoutMs
    }
}
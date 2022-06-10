package com.jamal2367.minicarlauncher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.jamal2367.minicarlauncher.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

fun Intent.isResolvable(ctx: Context?): Boolean {
    val pm = ctx?.packageManager
    val appInfo = pm?.resolveActivity(this, PackageManager.MATCH_DEFAULT_ONLY)
    return appInfo != null
}

class LauncherApp : DaggerApplication() {

    private val appInjector = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appInjector
}

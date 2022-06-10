package com.jamal2367.minicarlauncher.injection.modules

import com.jamal2367.minicarlauncher.presentation.MainActivity
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.LauncherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [LauncherModule::class, RepositoryModule::class])
abstract class InjectorBindMainModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeLauncherFragment(): LauncherFragment
}
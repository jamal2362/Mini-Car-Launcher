package com.jamal2367.minicarlauncher.injection.modules

import com.jamal2367.minicarlauncher.interactors.ShortcutsInteractor
import com.jamal2367.minicarlauncher.presentation.shortcuts_view.LauncherPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [InteractorModule::class])
class LauncherModule {

    @Provides
    @Singleton
    fun provideLauncherPresenter(interactor: ShortcutsInteractor): LauncherPresenter {
        return LauncherPresenter(interactor)
    }
}
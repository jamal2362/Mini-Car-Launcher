package com.jamal2367.minicarlauncher.injection.modules

import com.jamal2367.minicarlauncher.interactors.ShortcutsInteractor
import com.jamal2367.minicarlauncher.repository.ShortcutsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
class InteractorModule {

    @Provides
    @Singleton
    fun provideShortcutsInteractor(shortcutsRepository: ShortcutsRepository): ShortcutsInteractor {
        return ShortcutsInteractor(shortcutsRepository)
    }
}
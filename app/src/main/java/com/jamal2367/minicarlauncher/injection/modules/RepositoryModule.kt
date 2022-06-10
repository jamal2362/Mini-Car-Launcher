package com.jamal2367.minicarlauncher.injection.modules

import com.jamal2367.minicarlauncher.repository.AppPackageStorage
import com.jamal2367.minicarlauncher.repository.SelectionItemStorage
import com.jamal2367.minicarlauncher.repository.ShortcutsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [StorageModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideShortcutsRepo(
        storage: SelectionItemStorage,
        packages: AppPackageStorage
    ): ShortcutsRepository {
        return ShortcutsRepository(packages, storage)
    }
}
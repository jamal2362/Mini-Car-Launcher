package com.jamal2367.minicarlauncher.injection.modules

import com.jamal2367.minicarlauncher.LauncherApp
import com.jamal2367.minicarlauncher.repository.AppPackageStorage
import com.jamal2367.minicarlauncher.repository.SelectionItemStorage
import com.jamal2367.minicarlauncher.repository.storage.AppPackageStorageImpl
import com.jamal2367.minicarlauncher.repository.storage.SelectionItemDB
import com.jamal2367.minicarlauncher.repository.storage.SelectionItemStorageImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun provideSelectionsStorage(app: LauncherApp): SelectionItemStorage {
        val dao = SelectionItemDB.getInstance(app).getSelectionItemsDAO()
        return SelectionItemStorageImpl(dao)
    }

    @Provides
    @Singleton
    fun provideAppsPackages(app: LauncherApp): AppPackageStorage = AppPackageStorageImpl(app)
}
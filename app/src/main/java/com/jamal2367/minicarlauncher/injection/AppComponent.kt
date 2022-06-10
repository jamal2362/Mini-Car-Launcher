package com.jamal2367.minicarlauncher.injection

import com.jamal2367.minicarlauncher.LauncherApp
import com.jamal2367.minicarlauncher.injection.modules.InjectorBindMainModule
import com.jamal2367.minicarlauncher.injection.modules.StorageModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        InjectorBindMainModule::class,
        StorageModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<LauncherApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: LauncherApp): Builder
        fun build(): AppComponent
    }
}
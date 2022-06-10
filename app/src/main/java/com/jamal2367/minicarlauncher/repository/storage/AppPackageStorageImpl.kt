package com.jamal2367.minicarlauncher.repository.storage

import android.app.Application
import android.content.Intent
import android.content.pm.ResolveInfo
import com.jamal2367.minicarlauncher.repository.AppPackageStorage
import com.jamal2367.minicarlauncher.repository.entities.AppPackage
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AppPackageStorageImpl
@Inject constructor(app: Application) : AppPackageStorage {

    private val pkgManager = app.packageManager
    private val currentPkgName = app.packageName
    private val listIntent =
        Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
    private val appPackagesCached = BehaviorSubject.createDefault(buildAppsList())

    override fun getAvailablePackages(): Observable<AppPackage> {
        return Observable.just(buildAppsList())
            .doOnNext(appPackagesCached::onNext)
            .flatMapIterable { it }
    }

    override fun getAvailablePackagesCached(): Observable<AppPackage> {
        return appPackagesCached
            .firstElement()
            .flatMapObservable { Observable.fromIterable(it) }
    }

    private fun buildAppsList(): List<AppPackage> {
        return pkgManager?.queryIntentActivities(listIntent, 0)
            ?.filter { it.activityInfo.packageName != currentPkgName }
            ?.map(this::resolveInfoToPackage)
            ?: throw IllegalStateException("Can't access package information")
    }

    private fun resolveInfoToPackage(resolveInfo: ResolveInfo): AppPackage {
        val pkgName = resolveInfo.activityInfo.packageName
        val title = resolveInfo.loadLabel(pkgManager).toString()
        val icon = resolveInfo.activityInfo.loadIcon(pkgManager)

        val launchIntent =
            pkgManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName)
                ?: throw IllegalStateException("Can't access launch intent for '$title'")

        return AppPackage(pkgName, title, icon, launchIntent)
    }
}
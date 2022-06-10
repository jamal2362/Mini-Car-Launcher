package com.jamal2367.minicarlauncher.presentation.helpers

import com.jamal2367.minicarlauncher.RxLifecycleDelegate
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicReference

fun Disposable.untilUnbind(presenter: SingleViewPresenter<*>) {
    presenter.disposer = this
}

abstract class SingleViewPresenter<VIEW> : RxLifecycleDelegate() {
    private val viewReference = AtomicReference<VIEW?>()

    open fun bindView(view: VIEW) {
        viewReference.set(view)
    }

    open fun unbindView() {
        clearSubscriptions()
        viewReference.set(null)
    }

    fun applyView(action: (VIEW) -> Unit) {
        viewReference.get()?.let { action.invoke(it) }
    }
}
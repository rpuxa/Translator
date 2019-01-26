package ru.rpuxa.translator

import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) {
    observe(owner, Observer<T> { t: T? -> observer(t) })
}

inline fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner) { observer(it!!) }
}

val handler = Handler()
inline fun ui(crossinline run: () -> Unit) {
    handler.post {
        run()
    }
}

fun <T> liveData(value: T) = MutableLiveData<T>().apply { this.value = value }
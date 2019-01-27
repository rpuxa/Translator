package ru.rpuxa.translator

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) {
    observe(owner, Observer<T> { t: T? -> observer(t) })
}

inline fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner) { observer(it!!) }
}


/**
 * Адаптация класса [Call] под Kotlin coroutines
 */
suspend fun <T> Call<T>.await(): Response<T> = suspendCoroutine {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            it.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            it.resume(response)
        }
    })
}

/**
 * Ручное обновление данных
 */
fun <T> MutableLiveData<T>.update() {
    val v = value
    if (Looper.myLooper() == null)
        postValue(v)
    else
        value = v
}

@Suppress("FunctionName")
fun <T> MutableLiveData(defaultValue: T) = MutableLiveData<T>().apply { value = defaultValue }
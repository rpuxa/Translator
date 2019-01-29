package ru.rpuxa.translator

import android.os.Looper
import androidx.lifecycle.*
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
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

fun <T> LiveData<T>.toObservable(owner: LifecycleOwner): Observable<T> {
    return Observable.fromPublisher(LiveDataReactiveStreams.toPublisher(owner, this))
}

fun <T1, T2, R> Pair<ObservableSource<out T1>, ObservableSource<out T2>>.combineLatest(function: (T1, T2) -> R): Observable<R> =
        Observable.combineLatest(first, second, BiFunction<T1, T2, R> { t1: T1, t2: T2 -> function(t1, t2) })


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
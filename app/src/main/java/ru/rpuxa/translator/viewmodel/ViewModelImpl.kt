package ru.rpuxa.translator.viewmodel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.liveData
import ru.rpuxa.translator.model.IModel
import ru.rpuxa.translator.model.Model
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.database.DataBaseImpl
import ru.rpuxa.translator.ui

class ViewModelImpl(private val model: IModel, private val res: Resources) : IViewModel {

    override fun onCreate() {
        model.onCreate()

        asyncRequest(object : AsyncRequest<Boolean>(5_000) {
            override suspend fun getValue(): Boolean {
                return model.updateLanguages()
            }

            override fun onValueGotten(value: Boolean) {
                loadingSuccessful.value = value
            }

            override fun onTimeout() {
                loadingSuccessful.value = false
            }
        })
    }

    override val fromLanguage = liveData(Language.RUSSIAN)

    override fun setFromLanguage(language: Language) {
        if (language == toLanguage.value) {
            swapLanguages()
        } else {
            fromLanguage.value = language
        }
    }

    override val toLanguage = liveData(Language.ENGLISH)

    override fun setToLanguage(language: Language) {
        if (language == fromLanguage.value) {
            swapLanguages()
        } else {
            toLanguage.value = language
        }
    }

    override val translatesHistory: LiveData<List<Language>> =
            MutableLiveData()

    override val allLanguages: List<Language> get() = model.allLanguages

    override val isTranslateLoading = liveData(false)

    override val phrase = liveData<Phrase?>(null)

    override val loadingSuccessful = liveData<Boolean?>(null)

    override fun swapLanguages() {
        val tmp = fromLanguage.value
        fromLanguage.value = toLanguage.value
        toLanguage.value = tmp
    }

    override fun onTranslate(text: String) {
        if (text.isEmpty()) {
            phrase.value = null
            return
        }
        isTranslateLoading.value = true
        asyncRequest(object : AsyncRequest<Phrase?>() {
            override suspend fun getValue(): Phrase? {
                return model.translate(fromLanguage.value!!, toLanguage.value!!, text)
            }

            override fun onValueGotten(value: Phrase?) {
                phrase.value = value
                isTranslateLoading.value = false
                if (value == null) {
                    sendToast(res.getString(R.string.check_connection))
                }
            }

            override fun onTimeout() {
                sendToast(res.getString(R.string.check_connection))
                phrase.value = null
                isTranslateLoading.value = false
            }
        })
    }

    override fun clearField() {
        phrase.value = null
        isTranslateLoading.value = false
    }

    private fun sendToast(msg: String, isShort: Boolean = true) {

    }

    private fun <T> asyncRequest(request: AsyncRequest<T>) {
        GlobalScope.launch {
            var wait: Job? = null
            val req = launch {
                val answer = request.getValue()
                wait?.cancel()
                if (isActive) {
                    ui {
                        request.onValueGotten(answer)
                    }
                }
            }
            wait = launch {
                delay(request.timeout)
                req.cancel()
                ui {
                    request.onTimeout()
                }
            }
        }
    }

    private abstract class AsyncRequest<T>(val timeout: Long = 3_000) {
        abstract suspend fun getValue(): T

        open fun onValueGotten(value: T) {
        }

        open fun onTimeout() {
        }
    }
}
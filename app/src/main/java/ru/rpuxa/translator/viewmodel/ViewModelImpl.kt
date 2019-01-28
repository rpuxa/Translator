package ru.rpuxa.translator.viewmodel

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.rpuxa.translator.MutableLiveData
import ru.rpuxa.translator.model.IModel
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslateStatus
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.update

class ViewModelImpl(private val model: IModel) : IViewModel {

    override fun onCreate() {
        GlobalScope.launch {
            val successfulUpdateLanguages = model.languageManager.loadLanguages()
            loadingSuccessful.postValue(successfulUpdateLanguages)
            if (successfulUpdateLanguages) {
                val allPhrases = model.dataBase.getAllPhrases()
                translatesHistory.postValue(ArrayList(allPhrases))
            }
        }
    }

    override val fromLanguage by lazy {
        MutableLiveData(allLanguages.find { it.code == "ru" }!!)
    }

    override fun setFromLanguage(language: Language) {
        if (language == toLanguage.value) {
            swapLanguages()
        } else {
            fromLanguage.value = language
        }
    }

    override val toLanguage by lazy {
        MutableLiveData(allLanguages.find { it.code == "en" }!!)
    }

    override fun setToLanguage(language: Language) {
        if (language == fromLanguage.value) {
            swapLanguages()
        } else {
            toLanguage.value = language
        }
    }

    override val translatesHistory = MutableLiveData(ArrayList<TranslatedPhrase>())

    override val allLanguages: List<Language> get() = model.languageManager.allLanguages

    override val translatedPhrase = MutableLiveData<TranslatedPhrase>()

    override val translateStatus = MutableLiveData(TranslateStatus.WAITING_TRANSLATE)

    override val loadingSuccessful = MutableLiveData<Boolean?>(null)

    override fun swapLanguages() {
        val tmp = fromLanguage.value
        fromLanguage.value = toLanguage.value
        toLanguage.value = tmp
    }

    override fun onTranslate(text: String) {
        if (text.isBlank()) {
            return
        }
        translateStatus.value = TranslateStatus.TRANSLATING

        GlobalScope.launch {
            val phrase = model.translator.translate(Phrase(fromLanguage.value!!, text), toLanguage.value!!)
            if (phrase == null) {
                translateStatus.postValue(TranslateStatus.TRANSLATE_ERROR)
                return@launch
            }
            translatedPhrase.postValue(phrase)
            translateStatus.postValue(TranslateStatus.SHOW_TRANSLATE_RESULT)
            translatesHistory.value!!.remove(phrase)
            translatesHistory.value!!.add(phrase)
            translatesHistory.update()
            model.dataBase.savePhrase(phrase)
        }
    }

    override fun removeTranslateError() {
        translateStatus.value = TranslateStatus.WAITING_TRANSLATE
    }

    override fun removeTranslate(phrase: TranslatedPhrase) {
        translatesHistory.value!!.remove(phrase)
        translatesHistory.update()
        GlobalScope.launch { model.dataBase.removePhrase(phrase) }
    }

    override fun textToTranslateChanged() {
        translateStatus.value = TranslateStatus.WAITING_TRANSLATE
    }

    override fun showTranslate(phrase: TranslatedPhrase) {
        translatedPhrase.value = phrase
        translateStatus.value = TranslateStatus.SHOW_TRANSLATE_RESULT
    }
}
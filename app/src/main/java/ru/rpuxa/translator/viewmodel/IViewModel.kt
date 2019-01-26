package ru.rpuxa.translator.viewmodel

import androidx.lifecycle.LiveData
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.TranslatedPhrase

interface IViewModel {

    fun onCreate()

    val loadingSuccessful: LiveData<Boolean?>

    val allLanguages: List<Language>

    val fromLanguage: LiveData<Language>

    fun setFromLanguage(language: Language)

    val toLanguage: LiveData<Language>

    fun setToLanguage(language: Language)

    val translatedPhrase: LiveData<TranslatedPhrase>

    val translateStatus: LiveData<TranslateStatus>

    val translatesHistory: LiveData<out List<TranslatedPhrase>>

    fun removeTranslate(phrase: TranslatedPhrase)

    fun swapLanguages()

    fun textToTranslateChanged()

    fun onTranslate(text: String)

    fun showTranslate(phrase: TranslatedPhrase)
}
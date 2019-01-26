package ru.rpuxa.translator.viewmodel

import androidx.lifecycle.LiveData
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase

interface IViewModel {

    fun onCreate()

    val fromLanguage: LiveData<Language>
    fun setFromLanguage(language: Language)

    val toLanguage: LiveData<Language>
    fun setToLanguage(language: Language)

    val allLanguages: List<Language>

    val phrase: LiveData<Phrase?>

    val translatesHistory: LiveData<List<Language>>

    val isTranslateLoading: LiveData<Boolean>

    val loadingSuccessful: LiveData<Boolean?>

    fun swapLanguages()

    fun clearField()

    fun onTranslate(text: String)
}
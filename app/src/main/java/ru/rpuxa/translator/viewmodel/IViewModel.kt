package ru.rpuxa.translator.viewmodel

import androidx.lifecycle.LiveData
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.TranslateStatus
import ru.rpuxa.translator.model.data.TranslatedPhrase


/**
 * API слоя ViewModel из шаблона проектирования MVVM
 */
interface IViewModel {

    /**
     * Вызывается при запуске приложения. Содержит какие то предворительные вычисления.
     */
    fun onCreate()

    /**
     * [onCreate] устанавливает значение true, если загрузка прошла удачно,
     * иначе false. Null - загрузка в процессе
     */
    val loadingSuccessful: LiveData<Boolean?>

    /**
     * Список всех доступных языков
     */
    val allLanguages: List<Language>


    /**
     * Показывает с какого языка переводить фразу
     */
    val fromLanguage: LiveData<Language>

    fun setFromLanguage(language: Language)


    /**
     * Показывает на какой язык переводить фразу
     */
    val toLanguage: LiveData<Language>

    fun setToLanguage(language: Language)


    /**
     * Результат перевода
     */
    val translatedPhrase: LiveData<TranslatedPhrase>

    /**
     * Статус перевода.
     */
    val translateStatus: LiveData<TranslateStatus>

    /**
     * История всех переводов
     */
    val translatesHistory: LiveData<out List<TranslatedPhrase>>

    fun removeTranslate(phrase: TranslatedPhrase)

    /**
     * Меняет местами [fromLanguage] и [toLanguage]
     */
    fun swapLanguages()

    /**
     * Вызывается в то время, когда пользователь изменил поле с текстом для перевода
     */
    fun textToTranslateChanged()

    /**
     * Вызывается в то время, как пользователь запроил перевод
     */
    fun onTranslate(text: String)

    /**
     * Показывает переведенную фразу
     */
    fun showTranslate(phrase: TranslatedPhrase)

    /**
     * Вызывается после того как View(слой) обработал ошибку [translateStatus]
     */
    fun removeTranslateError()
}
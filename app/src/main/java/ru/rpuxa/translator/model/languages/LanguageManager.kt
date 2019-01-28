package ru.rpuxa.translator.model.languages

import ru.rpuxa.translator.model.data.Language

/**
 * Интерфейс ответственный за загрузку и хранение языков
 */
interface LanguageManager {

    /**
     * Функция которая загружает все языки в [allLanguages]. Должна вызываться перед обращением
     * к [allLanguages], где-то на старте программы
     */
    suspend fun loadLanguages(): Boolean

    /**
     * Список всех доступных языков
     */
    val allLanguages: List<Language>

    fun getLanguageByCode(code: String): Language = allLanguages.find { it.code == code }
            ?: throw IllegalStateException("Language not found! Code: $code")
}
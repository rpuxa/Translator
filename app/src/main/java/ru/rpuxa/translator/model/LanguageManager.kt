package ru.rpuxa.translator.model

import ru.rpuxa.translator.model.data.Language

interface LanguageManager {
    suspend fun loadLanguages(): Boolean

    val allLanguages: List<Language>

    fun getLanguageByCode(code: String): Language = allLanguages.find { it.code == code }
            ?: throw IllegalStateException("Language not found! Code: $code")
}
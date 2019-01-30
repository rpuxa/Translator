package ru.rpuxa.translator.model.languages

import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.server.ServerTranslator

class LanguageManagerImpl(private val server: ServerTranslator) : LanguageManager {

    override suspend fun loadLanguages(): Boolean {
        allLanguages = server.getLanguages()?.sortedBy { it.name } ?: return false
        return true
    }

    override lateinit var allLanguages: List<Language>
}
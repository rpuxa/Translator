package ru.rpuxa.translator.model

import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase

interface IModel : LanguageManager {

    suspend fun translate(fromLanguage: Language, toLanguage: Language, text: String): Phrase?

    suspend fun addPhrase(phrase: TranslatedPhrase)

    suspend fun removePhrase(phrase: TranslatedPhrase)

    suspend fun getAllPhrases(): List<TranslatedPhrase>
}
package ru.rpuxa.translator.model

import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.server.YandexTranslator

class Model(private val dataBase: DataBase) : IModel {

    override fun onCreate() {
        allPhrases = dataBase.getAllPhrases(this)
    }

    override suspend fun translate(fromLanguage: Language, toLanguage: Language, text: String): Phrase? {
        try {
            val response = YandexTranslator.getTranslate(
                    text,
                    "${fromLanguage.code}-${toLanguage.code}"
            ).execute()
            if (response.isSuccessful) {
                return Phrase(toLanguage, response.body().text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun addPhrase(phrase: TranslatedPhrase) {
        dataBase.savePhrase(phrase)
    }

    override suspend fun removePhrase(phrase: TranslatedPhrase) {
        dataBase.removePhrase(phrase)
    }

    override lateinit var allPhrases: List<TranslatedPhrase>

    override var allLanguages: List<Language> = listOf(Language.NULL)

    override suspend fun updateLanguages(): Boolean {
        try {
            val response = YandexTranslator.getLanguages().execute()
            if (response.isSuccessful) {
                allLanguages = response.body().toLanguageList().sortedBy { it.name }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
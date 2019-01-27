package ru.rpuxa.translator.model

import ru.rpuxa.translator.await
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.server.YandexTranslator

class Model(private val dataBase: DataBase) : IModel {

    override suspend fun translate(fromLanguage: Language, toLanguage: Language, text: String): Phrase? {
        try {
            val response = YandexTranslator.getTranslate(
                    text,
                    "${fromLanguage.code}-${toLanguage.code}"
            ).await()
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

    override suspend fun getAllPhrases(): List<TranslatedPhrase> = dataBase.getAllPhrases(this)

    override var allLanguages: List<Language> = listOf(Language.NULL)

    override suspend fun loadLanguages(): Boolean {
        try {
            val response = YandexTranslator.getLanguages().await()
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
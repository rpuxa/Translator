package ru.rpuxa.translator.model.database

import ru.rpuxa.translator.model.data.TranslatedPhrase

interface DataBase {

    suspend fun savePhrase(phrase: TranslatedPhrase)

    suspend fun removePhrase(phrase: TranslatedPhrase)

    suspend fun getAllPhrases(): List<TranslatedPhrase>

    /**
     * Полная очистка базы данных
     */
    suspend fun clear()
}
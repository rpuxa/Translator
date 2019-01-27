package ru.rpuxa.translator.model.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import ru.rpuxa.translator.model.LanguageManager
import ru.rpuxa.translator.model.data.TranslatedPhrase

abstract class DataBase(context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    abstract fun savePhrase(phrase: TranslatedPhrase)

    abstract fun removePhrase(phrase: TranslatedPhrase)

    abstract fun getAllPhrases(manager: LanguageManager): List<TranslatedPhrase>

    /**
     * Полная очистка базы данных
     */
    abstract fun clear()
}
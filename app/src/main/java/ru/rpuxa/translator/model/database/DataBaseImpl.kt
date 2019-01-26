package ru.rpuxa.translator.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ru.rpuxa.translator.model.LanguageManager
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase

class DataBaseImpl(applicationContext: Context)
    : DataBase(applicationContext, DATABASE_NAME, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "translator.db"

        //translates table
        private const val TRANSLATES_TABLE_NAME = "translates"
        //fields
        private const val TRANSLATES_COLUMN_ID = "id"
        private const val TRANSLATES_COLUMN_FROM_LANGUAGE = "fl"
        private const val TRANSLATES_COLUMN_TO_LANGUAGE = "tl"
        private const val TRANSLATES_COLUMN_FROM_TEXT = "ft"
        private const val TRANSLATES_COLUMN_TO_TEXT = "tt"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TRANSLATES_TABLE_NAME, true,
                TRANSLATES_COLUMN_ID to INTEGER + PRIMARY_KEY,
                TRANSLATES_COLUMN_FROM_LANGUAGE to INTEGER,
                TRANSLATES_COLUMN_TO_LANGUAGE to INTEGER,
                TRANSLATES_COLUMN_FROM_TEXT to TEXT,
                TRANSLATES_COLUMN_TO_TEXT to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        clear(db)
    }

    override fun savePhrase(phrase: TranslatedPhrase) {
        writableDatabase.insert(TRANSLATES_TABLE_NAME,
                TRANSLATES_COLUMN_ID to phrase.id,
                TRANSLATES_COLUMN_FROM_LANGUAGE to phrase.from.language.code,
                TRANSLATES_COLUMN_TO_LANGUAGE to phrase.to.language.code,
                TRANSLATES_COLUMN_FROM_TEXT to phrase.from.text,
                TRANSLATES_COLUMN_TO_TEXT to phrase.to.text
        )
    }

    override fun removePhrase(phrase: TranslatedPhrase) {
        writableDatabase.delete(TRANSLATES_TABLE_NAME, "", TRANSLATES_COLUMN_ID to phrase.id)
    }

    override fun getAllPhrases(manager: LanguageManager): List<TranslatedPhrase> {
        return writableDatabase.select(TRANSLATES_TABLE_NAME).parseList(rowParser { _: Int, fromLanguage: String, toLanguage: String,
                                                                                     fromText: String, toText: String ->
            TranslatedPhrase(
                    Phrase(manager.getLanguageByCode(fromLanguage), fromText),
                    Phrase(manager.getLanguageByCode(toLanguage), toText)
            )
        })
    }

    private fun clear(db: SQLiteDatabase) {
        db.dropTable(TRANSLATES_TABLE_NAME, true)
        onCreate(db)
    }

    override fun clear() {
        clear(writableDatabase)
    }
}
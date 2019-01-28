package ru.rpuxa.translator.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.jetbrains.anko.db.*
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.model.languages.LanguageManager


@Deprecated("Use [MyRoomDataBase] instead")
class SQLiteDataBase(
        applicationContext: Context,
        private val manager: LanguageManager
) : SQLiteOpenHelper(applicationContext, DATABASE_NAME, null, DATABASE_VERSION), DataBase {
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
        private const val TRANSLATES_COLUMN_TIME = "t"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TRANSLATES_TABLE_NAME, true,
                TRANSLATES_COLUMN_ID to INTEGER + PRIMARY_KEY,
                TRANSLATES_COLUMN_FROM_LANGUAGE to INTEGER,
                TRANSLATES_COLUMN_TO_LANGUAGE to INTEGER,
                TRANSLATES_COLUMN_FROM_TEXT to TEXT,
                TRANSLATES_COLUMN_TO_TEXT to TEXT,
                TRANSLATES_COLUMN_TIME to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        clear(db)
    }

    override suspend fun savePhrase(phrase: TranslatedPhrase) {
        writableDatabase.replace(TRANSLATES_TABLE_NAME,
                TRANSLATES_COLUMN_ID to phrase.id,
                TRANSLATES_COLUMN_FROM_LANGUAGE to phrase.from.language.code,
                TRANSLATES_COLUMN_TO_LANGUAGE to phrase.to.language.code,
                TRANSLATES_COLUMN_FROM_TEXT to phrase.from.text,
                TRANSLATES_COLUMN_TO_TEXT to phrase.to.text,
                TRANSLATES_COLUMN_TIME to phrase.createdTime
        )
    }

    override suspend fun removePhrase(phrase: TranslatedPhrase) {
        writableDatabase.delete(TRANSLATES_TABLE_NAME, "$TRANSLATES_COLUMN_ID = ${phrase.id}")
    }

    override suspend fun getAllPhrases(): List<TranslatedPhrase> {
        return writableDatabase.select(TRANSLATES_TABLE_NAME).parseList(rowParser { _: Int, fromLanguage: String, toLanguage: String, fromText: String, toText: String, time: Int ->
            TranslatedPhrase(
                    Phrase(manager.getLanguageByCode(fromLanguage), fromText),
                    Phrase(manager.getLanguageByCode(toLanguage), toText),
                    time
            )
        }).sortedBy { it.createdTime }
    }

    private fun clear(db: SQLiteDatabase) {
        db.dropTable(TRANSLATES_TABLE_NAME, true)
        onCreate(db)
    }

    override suspend fun clear() {
        clear(writableDatabase)
    }
}
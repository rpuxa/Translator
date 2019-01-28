package ru.rpuxa.translator.model.database

import android.content.Context
import androidx.room.*
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.model.languages.LanguageManager

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "translator.db"
private const val TRANSLATES_TABLE_NAME = "translates"

@Database(
        entities = [TranslatePhraseItem::class],
        version = DATABASE_VERSION
)
abstract class MyRoomDataBase : RoomDatabase(), DataBase {

    companion object {
        fun create(applicationContext: Context, manager: LanguageManager): DataBase {
            return Room.databaseBuilder(applicationContext, MyRoomDataBase::class.java, DATABASE_NAME).build().apply {
                this.manager = manager
            }
        }
    }

    lateinit var manager: LanguageManager

    protected abstract fun translateItemDao(): TranslateItemDao

    override suspend fun savePhrase(phrase: TranslatedPhrase) {
        translateItemDao().insert(TranslatePhraseItem(phrase))
    }

    override suspend fun removePhrase(phrase: TranslatedPhrase) {
        translateItemDao().delete(TranslatePhraseItem(phrase))
    }

    override suspend fun getAllPhrases(): List<TranslatedPhrase> {
        return translateItemDao().getAll().map { it.toTranslatePhrase(manager) }
    }

    override suspend fun clear() {
        translateItemDao().clear()
    }

    @Dao
    protected interface TranslateItemDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: TranslatePhraseItem)

        @Delete
        fun delete(item: TranslatePhraseItem)

        @Query("SELECT * FROM $TRANSLATES_TABLE_NAME")
        fun getAll(): List<TranslatePhraseItem>

        @Query("DELETE FROM $TRANSLATES_TABLE_NAME")
        fun clear()
    }
}


@Entity(tableName = TRANSLATES_TABLE_NAME)
class TranslatePhraseItem(
        @PrimaryKey
        val id: Int,
        val createdTime: Int,
        val fromText: String,
        val toText: String,
        val fromLanguageCode: String,
        val toLanguageCode: String
) {
    constructor(phrase: TranslatedPhrase) : this(
            phrase.id,
            phrase.createdTime,
            phrase.from.text,
            phrase.to.text,
            phrase.from.language.code,
            phrase.to.language.code
    )

    fun toTranslatePhrase(manager: LanguageManager): TranslatedPhrase {
        return TranslatedPhrase(
                Phrase(manager.getLanguageByCode(fromLanguageCode), fromText),
                Phrase(manager.getLanguageByCode(toLanguageCode), toText),
                createdTime
        )
    }
}
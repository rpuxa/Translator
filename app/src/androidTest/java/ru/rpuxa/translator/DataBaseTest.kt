package ru.rpuxa.translator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Language.Companion.ENGLISH
import ru.rpuxa.translator.model.data.Language.Companion.RUSSIAN
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.database.MyRoomDataBase
import ru.rpuxa.translator.model.languages.LanguageManager

@RunWith(AndroidJUnit4::class)
class DataBaseTest {


    // Реализация базы данных
    private lateinit var database: DataBase


    private val phrase1 = TranslatedPhrase(Phrase(RUSSIAN, "Привет"), Phrase(ENGLISH, "Hello"))
    private val phrase2 = TranslatedPhrase(Phrase(RUSSIAN, "Пока"), Phrase(ENGLISH, "Goodbye"))
    private val fakeLanguageManager = object : LanguageManager {

        override suspend fun loadLanguages(): Boolean {
            return true
        }

        override val allLanguages: List<Language> = listOf(RUSSIAN, ENGLISH)
    }

    @Before
    fun before() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = MyRoomDataBase.create(context, fakeLanguageManager) //SQLiteDataBase(context, fakeLanguageManager)
    }

    @After
    fun after() = runBlocking {
        database.clear()
    }

    @Test
    fun saveAndLoadPhrase1() = runBlocking {
        database.clear()
        database.savePhrase(phrase1)
        val a = database.getAllPhrases()
        assertEquals(a.size, 1)
        assertEquals(a[0], phrase1)
    }

    @Test
    fun saveAndLoadPhrase2() = runBlocking {
        database.clear()
        database.savePhrase(phrase2)
        val a = database.getAllPhrases()
        assertEquals(a.size, 1)
        assertEquals(a[0], phrase2)
    }

    @Test
    fun saveAndLoadAll() = runBlocking {
        database.clear()
        database.savePhrase(phrase1)
        database.savePhrase(phrase2)
        val a = database.getAllPhrases()
        assertEquals(a.size, 2)
        assertEquals(a[0], phrase2)
        assertEquals(a[1], phrase1)
    }

    @Test
    fun sameValues() = runBlocking {
        database.clear()
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)

        val a = database.getAllPhrases()

        assertEquals(a.size, 1)
    }

    @Test
    fun delete() = runBlocking {
        database.clear()
        database.savePhrase(phrase1)
        database.savePhrase(phrase2)
        database.removePhrase(phrase1)
        val a = database.getAllPhrases()

        assertEquals(a.size, 1)
        assertEquals(a[0], phrase2)
    }
}

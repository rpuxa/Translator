package ru.rpuxa.translator

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.rpuxa.translator.model.LanguageManager
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.database.DataBaseImpl

@RunWith(AndroidJUnit4::class)
class DataBaseTest {

    private lateinit var context: Context
    private val phrase1 = TranslatedPhrase(Phrase(Language.RUSSIAN, "Привет"), Phrase(Language.ENGLISH, "Hello"))
    private val phrase2 = TranslatedPhrase(Phrase(Language.RUSSIAN, "Пока"), Phrase(Language.ENGLISH, "Goodbye"))
    private val fakeLanguageManager = object : LanguageManager {
        override fun updateLanguages(): Boolean {
        }

        override val allLanguages: List<Language> = listOf(Language.RUSSIAN, Language.ENGLISH)
    }

    private fun getClearedDataBase(): DataBase = DataBaseImpl(context).apply { clear() }

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }


    @Test
    fun saveAndLoadPhrase1() {
        val database = getClearedDataBase()
        database.savePhrase(phrase1)
        val a = database.getAllPhrases(fakeLanguageManager)
        assertEquals(a.size, 1)
        assertEquals(a[0], phrase1)
    }

    @Test
    fun saveAndLoadPhrase2() {
        val database =  getClearedDataBase()
        database.savePhrase(phrase2)
        val a = database.getAllPhrases(fakeLanguageManager)
        assertEquals(a.size, 1)
        assertEquals(a[0], phrase2)
    }

    @Test
    fun saveAndLoadAll() {
        val database =  getClearedDataBase()
        database.savePhrase(phrase1)
        database.savePhrase(phrase2)
        val a = database.getAllPhrases(fakeLanguageManager)
        assertEquals(a.size, 2)
        assertEquals(a[0], phrase2)
        assertEquals(a[1], phrase1)
    }

    @Test
    fun sameValues() {
        val database =  getClearedDataBase()
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)
        database.savePhrase(phrase1)

        val a = database.getAllPhrases(fakeLanguageManager)

        assertEquals(a.size, 1)
    }
}

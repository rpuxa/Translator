package ru.rpuxa.translator

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rpuxa.translator.model.server.YandexTranslator
import java.util.*

class ServerTest {

    @Test
    fun languagesList() {
        val list = YandexTranslator.getLanguages().execute().body().toLanguageList()
        assertEquals(Arrays.hashCode(list.toTypedArray()),1112849076)
    }

    @Test
    fun translate() {
        val execute = YandexTranslator.getTranslate("Hello world!", "en-ru").execute()
        val answer = execute.body().text
        assertEquals(answer, "Всем привет!")
    }

    @Test
    fun translateOtherLanguage() {
        val execute = YandexTranslator.getTranslate("Bonjour monsieur!", "fr-ru").execute()
        val answer = execute.body().text
        assertEquals(answer, "Здравствуйте, уважаемые!")
    }
}
package ru.rpuxa.translator

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rpuxa.translator.model.data.Language.Companion.ENGLISH
import ru.rpuxa.translator.model.data.Language.Companion.FRENCH
import ru.rpuxa.translator.model.data.Language.Companion.RUSSIAN
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.server.ServerTranslator
import ru.rpuxa.translator.model.server.YandexTranslator

class ServerTest {


    // Реализация сервера
    private val server: ServerTranslator = YandexTranslator


    @Test
    fun translate() = runBlocking {
        val phrase = Phrase(ENGLISH, "Hello world!")
        val answer = server.translate(phrase, RUSSIAN)
        assertEquals(answer?.to?.text, "Всем привет!")
    }

    @Test
    fun translateOtherLanguage() = runBlocking {
        val phrase = Phrase(FRENCH, "Bonjour messieurs!")
        val answer = server.translate(phrase, RUSSIAN)
        assertEquals(answer?.to?.text, "Здравствуйте господа!")
    }
}
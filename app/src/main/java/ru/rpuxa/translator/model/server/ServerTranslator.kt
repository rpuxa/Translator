package ru.rpuxa.translator.model.server

import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase

interface ServerTranslator {

    /**
     * Загружает с сервера доступные языки.
     * Возвращает null в случае ошибки
     */
    suspend fun getLanguages(): List<Language>?

    /**
     * Переводит текст
     * Возвращает null в случае ошибки
     */
    suspend fun translate(from: Phrase, to: Language): TranslatedPhrase?
}
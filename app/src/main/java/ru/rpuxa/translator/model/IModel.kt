package ru.rpuxa.translator.model

import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.model.data.TranslatedPhrase

/**
 * API слоя Model из шаблона проектирования MVVM
 */
interface IModel : LanguageManager {

    /**
     * Запрашивает перевод текста [text] с одного языка [fromLanguage] на другой [toLanguage].
     * Возвращает null в случае, если произошла какая-либо ошибка
     */
    suspend fun translate(fromLanguage: Language, toLanguage: Language, text: String): Phrase?

    /**
     * Добавление фразы в базу данных
     */
    suspend fun addPhrase(phrase: TranslatedPhrase)

    /**
     * Удаление фразы из базы данных
     */
    suspend fun removePhrase(phrase: TranslatedPhrase)

    /**
     * Получение всех фраз из базы данных
     */
    suspend fun getAllPhrases(): List<TranslatedPhrase>
}
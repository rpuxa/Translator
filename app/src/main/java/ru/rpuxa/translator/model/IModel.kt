package ru.rpuxa.translator.model

import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.languages.LanguageManager
import ru.rpuxa.translator.model.server.ServerTranslator

/**
 * API слоя Model из шаблона проектирования MVVM
 */
interface IModel {

    val translator: ServerTranslator

    val dataBase: DataBase

    val languageManager: LanguageManager
}
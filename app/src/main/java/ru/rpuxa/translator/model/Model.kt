package ru.rpuxa.translator.model

import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.languages.LanguageManager
import ru.rpuxa.translator.model.server.ServerTranslator

class Model(
        override val dataBase: DataBase,
        override val translator: ServerTranslator,
        override val languageManager: LanguageManager
) : IModel
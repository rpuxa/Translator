package ru.rpuxa.translator.model.data

import java.io.Serializable


/**
 *  Хранит текст [text] и язык текста [language]. Реализует Serializable для передачи через intent
 */
data class Phrase(val language: Language, val text: String) : Serializable
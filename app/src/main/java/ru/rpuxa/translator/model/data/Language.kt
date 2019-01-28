package ru.rpuxa.translator.model.data

import java.io.Serializable

/**
 *  Data класс языка. Реализует Serializable для передачи через intent
 */
data class Language(val code: String, val name: String) : Serializable {

    companion object {
        @JvmField
        val NULL = Language("null", "...")
        @JvmField
        val RUSSIAN = Language("ru", "Russian")
        @JvmField
        val ENGLISH = Language("en", "English")
        @JvmField
        val FRENCH = Language("fr", "French")
    }
}
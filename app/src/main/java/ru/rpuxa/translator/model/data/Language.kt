package ru.rpuxa.translator.model.data

import java.io.Serializable

data class Language(val code: String, val name: String) : Serializable {

    companion object {
        @JvmField
        val NULL = Language("null", "...")
    }
}
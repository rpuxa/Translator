package ru.rpuxa.translator.model.data

data class TranslatedPhrase(val from: Phrase, val to: Phrase) {
    val id: Int = hashCode()
}
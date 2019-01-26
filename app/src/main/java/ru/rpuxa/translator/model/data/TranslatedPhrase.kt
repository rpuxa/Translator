package ru.rpuxa.translator.model.data

data class TranslatedPhrase(
        val from: Phrase,
        val to: Phrase,
        val createdTime: Int = System.currentTimeMillis().toInt()
) {
    val id: Int = hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TranslatedPhrase) return false

        if (from != other.from) return false
        if (to != other.to) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + id
        return result
    }
}
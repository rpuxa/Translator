package ru.rpuxa.translator.model.data

/**
 * Класс хранящий фразу [from] и ее перевод [to]. Так же хранит время создания [createdTime]
 * для сортировки по порядку перевода, а [id] для ключа в базе данных
 */
data class TranslatedPhrase(
        val from: Phrase,
        val to: Phrase,
        val createdTime: Long = System.currentTimeMillis()
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
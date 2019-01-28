package ru.rpuxa.translator.model.data

/**
 * Показывается состояние перевода.
 * Обычно цикл такой:
 * [WAITING_TRANSLATE] -> [TRANSLATING] -> [SHOW_TRANSLATE_RESULT] -> [WAITING_TRANSLATE]
 * или
 * [WAITING_TRANSLATE] -> [TRANSLATING] -> [TRANSLATE_ERROR] -> [WAITING_TRANSLATE]
 */
enum class TranslateStatus {

    /**
     * Ожидаем пока пользователь введет тест и запросит перевод
     */
    WAITING_TRANSLATE,

    /**
     * Переводим...
     */
    TRANSLATING,

    /**
     * Показываем результат перевода
     */
    SHOW_TRANSLATE_RESULT,

    /**
     * Ошибка перевода. К примеру: Сервер не доступен
     */
    TRANSLATE_ERROR
}
package ru.rpuxa.translator.view

import android.app.Application
import ru.rpuxa.translator.model.Model
import ru.rpuxa.translator.model.database.MyRoomDataBase
import ru.rpuxa.translator.model.languages.LanguageManagerImpl
import ru.rpuxa.translator.model.server.YandexTranslator
import ru.rpuxa.translator.viewmodel.IViewModel
import ru.rpuxa.translator.viewmodel.ViewModelImpl

private lateinit var viewModel: IViewModel
object ViewModel : IViewModel by viewModel

@Suppress("unused")
class TranslatorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializingViewModel()
    }

    private fun initializingViewModel() {
        // Можно было бы использовать Dagger 2, но тут слишком мало зависимостей
        val serverTranslator = YandexTranslator
        val languageManager = LanguageManagerImpl(serverTranslator)
        val dataBase = MyRoomDataBase.create(applicationContext, languageManager) //SQLiteDataBase(applicationContext, languageManager)
        val model = Model(dataBase, serverTranslator, languageManager)
        viewModel = ViewModelImpl(model)
    }
}
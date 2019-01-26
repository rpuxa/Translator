package ru.rpuxa.translator.view

import android.app.Application
import ru.rpuxa.translator.model.IModel
import ru.rpuxa.translator.model.Model
import ru.rpuxa.translator.model.database.DataBase
import ru.rpuxa.translator.model.database.DataBaseImpl
import ru.rpuxa.translator.viewmodel.IViewModel
import ru.rpuxa.translator.viewmodel.ViewModelImpl

private lateinit var dataBase: DataBase
private lateinit var model: IModel
private lateinit var viewModel: IViewModel

object ViewModel : IViewModel by viewModel

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        dataBase = DataBaseImpl(this)
        model = Model(dataBase)
        viewModel = ViewModelImpl(model)
    }
}
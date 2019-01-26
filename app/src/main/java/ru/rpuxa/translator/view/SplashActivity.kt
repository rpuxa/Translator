package ru.rpuxa.translator.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import ru.rpuxa.translator.R
import ru.rpuxa.translator.observe

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModel.onCreate()

        ViewModel.loadingSuccessful.observe(this) {
            when (it) {
                true -> startActivity<TranslateActivity>()
                false -> longToast(getString(R.string.check_connection))
            }
        }

    }

}
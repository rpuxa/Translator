package ru.rpuxa.translator.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.Language

/**
 * Главное активити
 */
class TranslateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && (requestCode == CHANGE_TO_LANGUAGE || requestCode == CHANGE_FROM_LANGUAGE)) {
            val language = data!!.getSerializableExtra(LanguagesListActivity.ANSWER_TAG) as Language
            if (language != Language.NULL) {
                if (requestCode == CHANGE_FROM_LANGUAGE)
                    ViewModel.setFromLanguage(language)
                else
                    ViewModel.setToLanguage(language)
            }
        }

    }

    override fun onBackPressed() {
    }

    companion object {
        const val CHANGE_TO_LANGUAGE = 0
        const val CHANGE_FROM_LANGUAGE = 1
    }
}

package ru.rpuxa.translator.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_translate.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.Language


class TranslateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        translate_button.setOnClickListener {
            ViewModel.onTranslate(from_text_field.text.toString())
            hideKeyboard()
        }

        clear_button.setOnClickListener {
            ViewModel.clearField()
            from_text_field.setText("")

        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && (requestCode == CHANGE_TO_LANGUAGE || requestCode == CHANGE_FROM_LANGUAGE)) {
            val language = data!!.getSerializableExtra(LanguagesListActivity.ANSWER) as Language
            if (language != Language.NULL) {
                if (requestCode == CHANGE_FROM_LANGUAGE)
                    ViewModel.setFromLanguage(language)
                else
                    ViewModel.setToLanguage(language)
            }
        }

    }

    companion object {
        const val CHANGE_TO_LANGUAGE = 0
        const val CHANGE_FROM_LANGUAGE = 1
    }
}

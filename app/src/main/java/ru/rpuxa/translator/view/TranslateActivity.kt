package ru.rpuxa.translator.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_translate.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.viewmodel.TranslateStatus

/**
 * Главное активити
 */
class TranslateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        translate_button.setOnClickListener {
            ViewModel.onTranslate(translate_from_text.text.toString())
            hideKeyboard()
        }

        clear_button.setOnClickListener {
            if (ViewModel.translateStatus.value != TranslateStatus.TRANSLATING)
                translate_from_text.text.clear()
        }

        translate_from_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ViewModel.textToTranslateChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
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

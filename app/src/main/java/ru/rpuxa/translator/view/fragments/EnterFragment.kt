package ru.rpuxa.translator.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.enter_fragment.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.TranslateStatus
import ru.rpuxa.translator.observe
import ru.rpuxa.translator.observeNotNull
import ru.rpuxa.translator.view.ViewModel

class EnterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.enter_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        translate_button.setOnClickListener {
            if (translate_from_text.text.isNotBlank() &&
                    ViewModel.translateStatus != TranslateStatus.TRANSLATING &&
                    ViewModel.translatedPhrase.value?.from?.text != translate_from_text.text.toString()
            ) {
                ViewModel.onTranslate(translate_from_text.text.toString())
                hideKeyboard()
            }
        }

        clear_button.setOnClickListener {
            if (ViewModel.translateStatus.value != TranslateStatus.TRANSLATING)
                translate_from_text.text.clear()
        }

        translate_from_text.textChangedListener {
            ViewModel.textToTranslateChanged()
        }

        ViewModel.translateStatus.observeNotNull(this) { status ->
            translate_from_language.visibility =
                    if (status == TranslateStatus.SHOW_TRANSLATE_RESULT) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
        }

        ViewModel.translatedPhrase.observe(this) { phrase ->
            translate_from_language.text = phrase?.from?.language?.name
            translate_from_text.setText(phrase?.from?.text)
        }
    }

    private fun hideKeyboard() {
        val inputManager = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
                act.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
        )
        translate_from_text.clearFocus()
    }
}
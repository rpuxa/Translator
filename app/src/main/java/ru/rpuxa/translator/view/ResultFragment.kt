package ru.rpuxa.translator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_translate.*
import kotlinx.android.synthetic.main.result.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast
import ru.rpuxa.translator.R
import ru.rpuxa.translator.observe
import ru.rpuxa.translator.observeNotNull
import ru.rpuxa.translator.viewmodel.TranslateStatus

class ResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as ViewSwitcher
        ViewModel.translateStatus.observeNotNull(this) { status ->
            when (status) {
                TranslateStatus.WAITING_TRANSLATE -> {
                    view.visibility = View.GONE
                }
                TranslateStatus.TRANSLATING -> {
                    loading(true)
                }
                TranslateStatus.SHOW_TRANSLATE_RESULT -> {
                    act.translate_from_language.visibility = View.VISIBLE
                    loading(false)
                }
                TranslateStatus.TRANSLATE_ERROR -> {
                    view.visibility = View.GONE
                    toast(getString(R.string.check_connection))
                }
            }

            if (status != TranslateStatus.SHOW_TRANSLATE_RESULT) {
                act.translate_from_language.visibility = View.GONE
            }
        }

        ViewModel.translatedPhrase.observe(this) { phrase ->
            act.translate_from_language.text = phrase?.from?.language?.name
            act.translate_from_text.setText(phrase?.from?.text)
            result_language.text = phrase?.to?.language?.name
            result_text.text = phrase?.to?.text
        }
    }

    private fun loading(bFlag: Boolean) {
        val view = view as ViewSwitcher
        view.visibility = View.VISIBLE
        if ((view.currentView === progress_bar) != bFlag)
            view.showNext()
    }
}
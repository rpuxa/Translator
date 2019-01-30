package ru.rpuxa.translator.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.result.*
import org.jetbrains.anko.support.v4.longToast
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.TranslateStatus
import ru.rpuxa.translator.observe
import ru.rpuxa.translator.observeNotNull
import ru.rpuxa.translator.view.ViewModel


/**
 * Фрагмент для показа результата перевода
 */
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
                    loading(false)
                }
                TranslateStatus.TRANSLATE_ERROR -> {
                    view.visibility = View.GONE
                    longToast(getString(R.string.check_connection))
                    ViewModel.removeTranslateError()
                }
            }
        }

        ViewModel.translatedPhrase.observe(this) { phrase ->
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
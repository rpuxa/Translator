package ru.rpuxa.translator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.result.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.Phrase
import ru.rpuxa.translator.observe
import ru.rpuxa.translator.observeNotNull

class ResultFragment : Fragment() {

    private var isLoading = false
    private var phrase: Phrase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as ViewSwitcher
        ViewModel.isTranslateLoading.observeNotNull(this) { isLoading ->
            this.isLoading = isLoading
            update()
        }
        ViewModel.phrase.observe(this) { translateItem ->
            this.phrase = translateItem
            update()
        }
    }

    private fun update() {
        val view = view as ViewSwitcher
        view.visibility = if (!isLoading && phrase == null) {
            View.GONE
        } else {
            if ((view.currentView === progress_bar) != isLoading)
                view.showNext()
            result_text.text = phrase?.text
            result_language.text = phrase?.language?.name
            View.VISIBLE
        }

    }
}
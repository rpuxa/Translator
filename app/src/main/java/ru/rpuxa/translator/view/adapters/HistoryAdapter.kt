package ru.rpuxa.translator.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_item.view.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.TranslatedPhrase
import ru.rpuxa.translator.view.ViewModel

/**
 * Адаптер отвечающий за расположение истории переводов
 */
class HistoryAdapter : ListAdapter<TranslatedPhrase, HistoryAdapter.HistoryViewHolder>(Diff) {
    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fromText: TextView = view.history_from_text
        val toText: TextView = view.history_to_text
        val languages: TextView = view.history_languages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)

        return HistoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val phrase = getItem(position)

        holder.apply {
            fromText.text = phrase.from.text
            toText.text = phrase.to.text
            languages.text = "${phrase.from.language.code}-${phrase.to.language.code}"
            itemView.setOnClickListener {
                ViewModel.showTranslate(phrase)
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<TranslatedPhrase>() {
        override fun areItemsTheSame(oldItem: TranslatedPhrase, newItem: TranslatedPhrase): Boolean =
                oldItem == newItem

        override fun areContentsTheSame(oldItem: TranslatedPhrase, newItem: TranslatedPhrase): Boolean =
                oldItem == newItem
    }
}
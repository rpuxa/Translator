package ru.rpuxa.translator.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.android.synthetic.main.language_item.view.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.view.activities.LanguagesListActivity

/**
 * Адаптер для списка всех языков
 */
class LanguagesAdapter(private val activity: LanguagesListActivity) :
        ListAdapter<Language, LanguagesAdapter.LanguageViewHolder>(Diff) {
    class LanguageViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val text: TextView = view.language_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)

        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = getItem(position)
        holder.text.text = language.name

        holder.itemView.setOnClickListener {
            activity.finishWithResult(language)
        }
    }

    private object Diff : DiffUtil.ItemCallback<Language>() {
        override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean =
                oldItem == newItem

        override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean =
                oldItem == newItem
    }
}

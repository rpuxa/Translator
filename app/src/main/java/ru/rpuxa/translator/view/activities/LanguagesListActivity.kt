package ru.rpuxa.translator.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_languages_list.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.model.data.Language
import ru.rpuxa.translator.view.ViewModel
import ru.rpuxa.translator.view.adapters.LanguagesAdapter

/**
 * Активность для выбора языка из списка доступных.
 * Следует вызывать через startActivityForResult
 */
class LanguagesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages_list)
        val adapter = LanguagesAdapter(this)
        adapter.submitList(ViewModel.allLanguages)
        val manager = LinearLayoutManager(this)
        languages_recyclerview.addItemDecoration(DividerItemDecoration(this, manager.orientation))
        languages_recyclerview.adapter = adapter
        languages_recyclerview.layoutManager = manager

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.subtitle = getString(R.string.choose_language)
    }

    fun finishWithResult(language: Language) {
        val intent = Intent()
        intent.putExtra(ANSWER_TAG, language)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finishWithResult(Language.NULL)
    }

    companion object {
        const val ANSWER_TAG = "answer"
    }
}
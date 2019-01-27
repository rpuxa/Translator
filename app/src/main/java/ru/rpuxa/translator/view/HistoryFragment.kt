package ru.rpuxa.translator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_fragment.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.observeNotNull
import ru.rpuxa.translator.viewmodel.TranslateStatus


/**
 * Фрагмент содержащий историю переводов
 */
class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.history_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = HistoryAdapter()
        val manager = LinearLayoutManager(context)
        history_recycler_view.addItemDecoration(DividerItemDecoration(context, manager.orientation))
        history_recycler_view.adapter = adapter
        history_recycler_view.layoutManager = manager
        view.visibility = View.GONE

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(holder: RecyclerView.ViewHolder, swipeDir: Int) {
                ViewModel.removeTranslate(adapter.currentList[holder.layoutPosition])
            }

        }

        //разделение между элементами
        ItemTouchHelper(callback).attachToRecyclerView(history_recycler_view)

        ViewModel.translateStatus.observeNotNull(this) { status ->
            updateVisibility(view, adapter.currentList.isEmpty(), status)
        }

        ViewModel.translatesHistory.observeNotNull(this) { list ->
            updateVisibility(view, list.isEmpty(), ViewModel.translateStatus.value!!)
            adapter.submitList(list.reversed())
        }
    }

    /**
     * Обновляет видимость фрагмента в зависимотсти от содержимого списка [isEmpty] и статуса перевода [status]
     */
    private fun updateVisibility(view: View, isEmpty: Boolean, status: TranslateStatus) {
        view.visibility = if (
                status == TranslateStatus.SHOW_TRANSLATE_RESULT ||
                status == TranslateStatus.TRANSLATING ||
                isEmpty
        ) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
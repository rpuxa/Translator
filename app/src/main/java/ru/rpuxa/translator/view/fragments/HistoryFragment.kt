package ru.rpuxa.translator.view.fragments

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.history_fragment.*
import ru.rpuxa.translator.R
import ru.rpuxa.translator.combineLatest
import ru.rpuxa.translator.model.data.TranslateStatus
import ru.rpuxa.translator.observeNotNull
import ru.rpuxa.translator.toObservable
import ru.rpuxa.translator.view.ViewModel
import ru.rpuxa.translator.view.adapters.HistoryAdapter
import kotlin.math.abs
import kotlin.math.max


/**
 * Фрагмент содержащий историю переводов
 */
class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.history_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = HistoryAdapter()
        history_recycler_view.adapter = adapter
        history_recycler_view.layoutManager = LinearLayoutManager(context)
        history_recycler_view.addItemDecoration(MyItemDecoration())

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(holder: RecyclerView.ViewHolder, swipeDir: Int) {
                ViewModel.removeTranslate(adapter.currentList[holder.layoutPosition])
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(history_recycler_view)

        ViewModel.translatesHistory.observeNotNull(this) { list ->
            adapter.submitList(list.reversed())
        }

        (ViewModel.translateStatus.toObservable(this) to ViewModel.translatesHistory.toObservable(this))
                .combineLatest { status, list ->
                    if (status == TranslateStatus.SHOW_TRANSLATE_RESULT ||
                            status == TranslateStatus.TRANSLATING ||
                            list.isEmpty()
                    ) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }
                .subscribeBy(onNext = view::setVisibility)
    }

    /**
     *  Делает более красивые разделения чем [DividerItemDecoration]
     *  и плавно меняет alpha при удалении элемента
     */
    private class MyItemDecoration : RecyclerView.ItemDecoration() {

        private var startX: Float? = null

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val paint = Paint()
            paint.strokeWidth = 1f
            paint.color = parent.context.resources.getColor(R.color.gray)
            val (parentX, parentY) = parent.absoluteCoordinates
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                val (x, y) = child.absoluteCoordinates
                val childX = (x - parentX).toFloat()
                val childY = (y - parentY).toFloat()
                if (startX == null)
                    startX = childX
                val alpha = max(1 - abs(startX!! - childX) / child.width * 2, 0f)
                child.alpha = alpha
                if (i != 0) {
                    paint.alpha = (alpha * 255).toInt()
                    c.drawLine(childX, childY, childX + child.width, childY, paint)
                }
            }
        }

        private val View.absoluteCoordinates: IntArray
            get() {
                val c = IntArray(2)
                getLocationOnScreen(c)
                return c
            }
    }
}
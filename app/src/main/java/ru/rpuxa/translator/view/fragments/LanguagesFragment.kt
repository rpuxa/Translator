package ru.rpuxa.translator.view.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.languages.*
import kotlinx.android.synthetic.main.languages.view.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.translator.R
import ru.rpuxa.translator.observeNotNull
import ru.rpuxa.translator.view.ViewModel
import ru.rpuxa.translator.view.activities.LanguagesListActivity
import ru.rpuxa.translator.view.activities.TranslateActivity

/**
 * Фрагмент показывающий направление перевода
 */
class LanguagesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.languages, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as ConstraintLayout

        view.swap_languages.setOnClickListener {
            startAnimation()
        }

        ViewModel.fromLanguage.observeNotNull(this) { language ->
            from_language_textview.text = language.name
        }

        ViewModel.toLanguage.observeNotNull(this) { language ->
            to_language_textview.text = language.name
        }

        from_language_textview.setOnClickListener {
            act.startActivityForResult<LanguagesListActivity>(TranslateActivity.CHANGE_FROM_LANGUAGE)
        }

        to_language_textview.setOnClickListener {
            act.startActivityForResult<LanguagesListActivity>(TranslateActivity.CHANGE_TO_LANGUAGE)
        }
    }


    private val swapAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f, 0f).apply {
            duration = resources.getInteger(R.integer.language_swap_animation_duration).toLong()
        }
    }
    private val rotateAnimation: Animation by lazy {
        loadAnimation(context, R.anim.rotate_center)
    }

    private fun startAnimation() {
        val constraint = view as ConstraintLayout
        val set = ConstraintSet()
        set.clone(constraint)
        swapAnimator.cancel()
        rotateAnimation.cancel()

        var swapped = false
        swapAnimator.addUpdateListener {
            val fromLanguageValue = it.animatedValue as Float
            val toLanguageValue = 1 - fromLanguageValue
            set.setHorizontalBias(R.id.from_language_textview, fromLanguageValue)
            set.setHorizontalBias(R.id.to_language_textview, toLanguageValue)

            if (!swapped && it.animatedFraction > .5f) {
                ViewModel.swapLanguages()
                swapped = true
            }
            set.applyTo(constraint)
        }

        swap_languages.startAnimation(rotateAnimation)
        swapAnimator.start()
    }
}
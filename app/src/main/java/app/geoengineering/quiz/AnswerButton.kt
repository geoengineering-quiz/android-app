package app.geoengineering.quiz

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class AnswerButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {
    private val stateCorrect = intArrayOf(R.attr.state_correct)
    private val stateIncorrect = intArrayOf(R.attr.state_incorrect)

    var isCorrect = false
        set(value) {
            field = value
            refresh()
        }

    var isIncorrect = false
        set(value) {
            field = value
            refresh()
        }

    private fun refresh() {
        invalidate()
        refreshDrawableState()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 2)
        if (isCorrect) mergeDrawableStates(drawableState, stateCorrect)
        if (isIncorrect) mergeDrawableStates(drawableState, stateIncorrect)
        return drawableState
    }
}
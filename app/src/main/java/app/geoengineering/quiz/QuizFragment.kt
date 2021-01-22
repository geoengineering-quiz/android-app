package app.geoengineering.quiz

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.geoengineering.quiz.databinding.FragmentQuizBinding
import com.beust.klaxon.Klaxon

class QuizFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private var score = 0
    private var total = 0
    private var questions: Array<Question>? = null
    private var answerButtons: Array<AnswerButton>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            answerButtons = arrayOf(buttonAnswer1, buttonAnswer2, buttonAnswer3)
            explanationBody.movementMethod = ScrollingMovementMethod()
        }
        startQuiz()
    }

    private fun startQuiz() {
        // 1. load questions from json file
        // 2. initialize total
        // 3. render question + options
        // 4. button onClick: compare answers
        // 5.   correct:
        //          - set state to correct
        //          - inc score
        //      incorrect
        //          - set state to incorrect
        //      - render answer and description
        // 6. if next question: repeat step 3
        //    else: navigate to game over fragment and pass score / total

        val result = Klaxon().parse<Questions>(resources.openRawResource(R.raw.questions))
        questions = result!!.questions
        total = questions!!.size
        nextQuestion(0)
    }

    private fun nextQuestion(index: Int) {
        if (index + 1 == total) gameOver()
        binding.buttonNext.setOnClickListener { nextQuestion(index + 1) }
        renderQuestion(index)
    }

    private fun renderQuestion(index: Int) {
        val q = questions!![index]

        binding.apply {
            questionTitle.text = getString(R.string.frage, index + 1)
            questionBody.text = q.question
            for ((i, b) in answerButtons!!.withIndex()) {
                b.text = q.options[i]
                b.isCorrect = false
                b.isIncorrect = false
                b.setOnClickListener { handleAnswer(it as AnswerButton, index, i) }
            }

            correctAnswer.text = q.options[q.answer]
            explanationBody.text = q.info
            explanationBody.scrollY = 0
            buttonMoreInfo.visibility = when {
                q.info_url.isNullOrEmpty() -> View.INVISIBLE
                else -> View.VISIBLE
            }
        }

        binding.viewAnimator.displayedChild = 0
    }

    private fun handleAnswer(button: AnswerButton, question: Int, answer: Int) {
        for (b in answerButtons!!) {
            b.setOnClickListener { }
        }

        if (checkAnswer(question, answer)) {
            button.isCorrect = true
            score++
        } else button.isIncorrect = true

        binding.viewAnimator.displayedChild = 1
    }

    private fun checkAnswer(question: Int, answer: Int) = questions!![question].answer == answer

    private fun gameOver() {
        val action = QuizFragmentDirections.actionQuizFragmentToGameoverFragment(score, total)
        findNavController().navigate(action)
    }

    companion object {
        @JvmStatic
        fun newInstance() = QuizFragment()
    }
}
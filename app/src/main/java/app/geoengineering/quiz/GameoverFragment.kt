package app.geoengineering.quiz

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.geoengineering.quiz.databinding.FragmentGameoverBinding

private const val ARG_PARAM1 = "score"
private const val ARG_PARAM2 = "total"

/**
 * A simple [Fragment] subclass.
 * Use the [GameoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameoverFragment : Fragment() {
    private lateinit var binding: FragmentGameoverBinding

    private var score: Int = 0
    private var total: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getInt(ARG_PARAM1, 0)
            total = it.getInt(ARG_PARAM2, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGameoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonHome.setOnClickListener {
            val action = GameoverFragmentDirections.actionGameoverFragmentToMainFragment()
            findNavController().navigate(action)
        }

        binding.progressBar.progress = score
        binding.progressBar.max = total
        binding.resultScore.text = getString(R.string.final_score, score, total)

        val x = score / total * 100
        binding.feedbackText.text = when {
            x >= 70 -> getString(R.string.exzellent)
            x >= 50 -> getString(R.string.gratulation)
            else -> getString(R.string.versuchs_nochmal)
        }

        binding.shareButton.setOnClickListener {
            val bitmap = getBitmapFromView(binding.root)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, bitmap)
            }
            startActivity(Intent.createChooser(intent, "Share"))
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param score Correct questions.
         * @param total Total questions.
         * @return A new instance of fragment GameoverFragment.
         */
        @JvmStatic
        fun newInstance(score: Int, total: Int) =
            GameoverFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, score)
                    putInt(ARG_PARAM2, total)
                }
            }
    }
}
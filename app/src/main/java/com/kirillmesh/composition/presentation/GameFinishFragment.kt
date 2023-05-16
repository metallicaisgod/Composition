package com.kirillmesh.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.kirillmesh.composition.R
import com.kirillmesh.composition.databinding.FragmentGameFinishBinding
import com.kirillmesh.composition.domain.entity.GameResult

class GameFinishFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishBinding? = null
    private val binding: FragmentGameFinishBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
        setViews()
    }

    private fun setViews() {
        val smileId = if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
        with(binding) {
            emojiResult.setImageResource(smileId)
            tvScoreAnswers.text = getString(
                R.string.score_answers,
                gameResult.countOfRightAnswers.toString()
            )
            tvScorePercentage.text = getString(
                R.string.score_percentage,
                calculatePercentOfRightAnswers().toString()
            )
            tvRequiredAnswers.text = getString(
                R.string.required_score,
                gameResult.gameSettings.minCountOfRightAnswers.toString()
            )
            tvRequiredPercentage.text = getString(
                R.string.required_percentage,
                gameResult.gameSettings.minPercentOfRightAnswers.toString()
            )
        }
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (gameResult.countOfQuestions == 0)
            return 0
        return ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager
            .popBackStack(QuestionFragment.NAME, POP_BACK_STACK_INCLUSIVE)
    }

    private fun parseArgs() {
        val tempGameResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_GAME_RESULT, GameResult::class.java)
        } else {
            requireArguments().getParcelable(KEY_GAME_RESULT)
        }
        tempGameResult?.let {
            gameResult = it
        }
    }

    companion object {

        private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishFragment {
            return GameFinishFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}
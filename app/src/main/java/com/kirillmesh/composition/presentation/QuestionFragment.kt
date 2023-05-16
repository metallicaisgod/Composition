package com.kirillmesh.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.kirillmesh.composition.R
import com.kirillmesh.composition.databinding.FragmentQuestionBinding
import com.kirillmesh.composition.domain.entity.GameResult
import com.kirillmesh.composition.domain.entity.GameSettings
import com.kirillmesh.composition.domain.entity.Level
import com.kirillmesh.composition.domain.entity.Question

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionBinding == null")

    private var level: Level? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvOption1.setOnClickListener {
            launchGameFinishFragment(
                GameResult(
                true,
                0,
                0,
                GameSettings(
                    0,
                    0,
                    0,
                    0
                )
            )
            )
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        level = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requireArguments().getParcelable(KEY_LEVEL,  Level::class.java)
        } else {
            requireArguments().getParcelable(KEY_LEVEL)
        }
    }

    companion object {

        private const val KEY_LEVEL = "level"
        const val NAME = "QuestionFragment"

        fun newInstance(level: Level): QuestionFragment {
            return QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}
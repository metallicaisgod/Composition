package com.kirillmesh.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kirillmesh.composition.R
import com.kirillmesh.composition.databinding.FragmentChooseLevelBinding
import com.kirillmesh.composition.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLevelTest.setOnClickListener{
            launchQuestionFragment(Level.TEST)
        }
        binding.buttonLevelEasy.setOnClickListener{
            launchQuestionFragment(Level.EASY)
        }
        binding.buttonLevelNormal.setOnClickListener{
            launchQuestionFragment(Level.NORMAL)
        }
        binding.buttonLevelHard.setOnClickListener{
            launchQuestionFragment(Level.HARD)
        }

    }

    private fun launchQuestionFragment(level: Level) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(level))
            .addToBackStack(QuestionFragment.NAME)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "ChooseLevelFragment"

        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }
}
package com.kirillmesh.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kirillmesh.composition.databinding.FragmentQuestionBinding
import com.kirillmesh.composition.domain.entity.GameResult

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionBinding == null")

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private val args by navArgs<QuestionFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(requireActivity().application, args.level)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
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
        observeViewModel()
        setClickListeners()
    }

    private fun setClickListeners(){
        tvOptions.forEach{ textView ->
            textView.setOnClickListener {
                viewModel.chooseAnswer(textView.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            timeFormatted.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            question.observe(viewLifecycleOwner) {
                with(binding) {
                    tvSum.text = it.sum.toString()
                    tvLeftNumber.text = it.visibleNumber.toString()
                    tvOptions.forEachIndexed{ idx, textView ->
                        textView.text = it.options[idx].toString()
                    }
                }
            }
            progressAnswers.observe(viewLifecycleOwner){
                binding.tvAnswersProgress.text = it
            }
            percentOfRightAnswers.observe(viewLifecycleOwner){
                binding.progressBar.setProgress(it, true)
            }
            enoughCount.observe(viewLifecycleOwner){
                binding.tvAnswersProgress.setTextColor(getColorByState(it))
            }
            enoughPercent.observe(viewLifecycleOwner){
                val color = getColorByState(it)
                binding.progressBar.progressTintList = ColorStateList.valueOf(color)
            }
            minPercent.observe(viewLifecycleOwner){
                binding.progressBar.secondaryProgress = it
            }
            gameResult.observe(viewLifecycleOwner){
                launchGameFinishFragment(it)
            }
        }
    }

    private fun getColorByState(state: Boolean): Int {
        val colorResId = if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun launchGameFinishFragment(gameResult: GameResult){
        findNavController().navigate(
            QuestionFragmentDirections.actionQuestionFragmentToGameFinishFragment(gameResult)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
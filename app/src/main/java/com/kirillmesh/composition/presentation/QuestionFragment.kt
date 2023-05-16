package com.kirillmesh.composition.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kirillmesh.composition.R
import com.kirillmesh.composition.databinding.FragmentQuestionBinding
import com.kirillmesh.composition.domain.entity.GameResult
import com.kirillmesh.composition.domain.entity.Level

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionBinding == null")

    private lateinit var level: Level

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

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }

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
        viewModel.startGame(level)
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
        val tempLevel = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requireArguments().getParcelable(KEY_LEVEL,  Level::class.java)
        } else {
            requireArguments().getParcelable(KEY_LEVEL)
        }
        tempLevel?.let {
            level = it
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
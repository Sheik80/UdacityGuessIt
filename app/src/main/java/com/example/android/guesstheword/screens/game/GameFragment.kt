/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import java.util.regex.Pattern

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    //add new changeble to our viewmodel
    private lateinit var viewModel: GameViewModel

    /* we need to remove this to GameViewModel because ViewModel have had to contain our changable values
    // The current word
      private var word = ""

      // The current score
      private var score = 0

      // The list of words - the front of the list is the next word to guess
      private lateinit var wordList: MutableList<String>*/

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment", "Called ViewModelProvers.of!")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel
        //in example we see as
        //binding.setLifecycleOwner(this)
        binding.lifecycleOwner = this

/*      remove too in init block
        resetList()
        nextWord()*/
        //теперь нам не нужны слушатели на эти кнопки, потому что мы добавили их через DataBinding и Лямбду напрямую в xml код на кнопки onSkip и onCorrect
        /* binding.correctButton.setOnClickListener {
             viewModel.onCorrect()
             //update with ViewModel
             // updateScoreText() nor use because liveData
             //updateWordText()
         }
         binding.skipButton.setOnClickListener {
             viewModel.onSkip()
           //update with ViewModel
           // updateScoreText() nor use because liveData
             //updateWordText()
         }*/

        /** Setting up LiveData observation relationship **/
        /*when we use LiveData Binding на не нужно наблюдать за каждым объектом за ним у нас следит наш метод lifeCycleOwner
        viewModel.word.observe(this, Observer { newWord ->
             binding.wordText.text = newWord
         }
         )*/
        /* when we use LiveData Binding на не нужно наблюдать за каждым объектом за ним у нас следит наш метод lifeCycleOwner
        viewModel.score.observe(this, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })*/

        /*when we use LiveData Binding на не нужно наблюдать за каждым объектом за ним у нас следит наш метод lifeCycleOwner, но здесь мы должны трансформировать Лонг в СТринг
        viewModel.currentTime.observe(this, Observer { newTime ->
              binding.timerText.text = DateUtils.formatElapsedTime(newTime)
          })
  */
        //Получаем обсервер от нашей вьюмодели, потом вводим переменнную hasFinished т.к. наша eventGameFinishe Boolean
        // И если из вьюМодели наш евент true то мы вызываем функцию ИГРА ЗАКОНЧЕНА
        viewModel.eventGameFinished.observe(this, Observer { hasFinished ->

            if (hasFinished) {
                gameFinished()
                viewModel.onGameFinishedCompleate()
            }
        })



        return binding.root
        // updateScoreText() nor use because liveData
        //updateWordText()
        //return binding.root

    }

    /**
     * Resets the list of words and randomizes the order

    private fun resetList() {
    wordList = mutableListOf(
    "queen",
    "hospital",
    "basketball",
    "cat",
    "change",
    "snail",
    "soup",
    "calendar",
    "sad",
    "desk",
    "guitar",
    "home",
    "railway",
    "zebra",
    "jelly",
    "car",
    "crow",
    "trade",
    "bag",
    "roll",
    "bubble"
    )
    wordList.shuffle()
    }


     * Called when the game is finished
     */
    private fun gameFinished() {
        /*val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?:0)
        findNavController(this).navigate(action)*/
        Toast.makeText(this.activity, "Game has finished", Toast.LENGTH_SHORT).show()
    }


    /** Methods for buttons presses **/

    /*  private fun onSkip() {
          score--
          nextWord()
      }

      private fun onCorrect() {
          score++
          nextWord()
      }*/

    /** Methods for updating the UI

    private fun updateWordText() {
    binding.wordText.text = viewModel.word

    }*/

    /*remove to liveData
    private fun updateScoreText() {
          binding.scoreText.text = viewModel.score.toString()
      }*/

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}

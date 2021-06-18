package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.sql.Time

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel: ViewModel() {
    companion object{
        // These represent different important times
        // This is when the game is over
        const val  DONE =0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME= 10000L
    }

    private val timer : CountDownTimer


     //The currentTime
    private val _currentTime = MutableLiveData<Long>()
    val currentTime:LiveData<Long>
    get() = _currentTime

    //here we transformation our Time from long to String, and transfer this to our observer and show in UI
    val currentTimeString = Transformations.map(currentTime, {time ->
        DateUtils.formatElapsedTime(time)
    })

    // The current word
   private val _word = MutableLiveData<String>()
    val word:LiveData<String>
    get() = _word

    // The current score internal
    //external

   private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
    get() = _score


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinished = MutableLiveData<Boolean>()
            val eventGameFinished:LiveData<Boolean>
                    get() = _eventGameFinished

    init {
        /*Log.d("GameViewModel", "GameViewModel created")
        _eventGameFinished.value = false*/
        resetList()
        nextWord()
        _score.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {

                _currentTime.value = millisUntilFinished/ONE_SECOND

            }

            override fun onFinish() {

                _currentTime.value = DONE
                _eventGameFinished.value = true

            }
        }
        timer.start()
    }

   /* override fun onCleared() {
        super.onCleared()
        Log.d("GameViewModel", "GameViewModel created")
    }*/

    /**
     We remove this from Game Fragment, becouse list we need to store in ViewModel*/

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
    /** Called when the game is finished
     */

    /** Remove from GameFragment too, because we don't have to work with our word list in our UIC
     * Moves to the next word in the list
     * */

    private fun nextWord() {
    //Select and remove a word from the list
    if (wordList.isEmpty() ) {
            resetList()
    //_eventGameFinished.value = true
    //gameFinished()
    }//we do no need else now, потому что игра закончится когда закончится время

    _word.value = wordList.removeAt(0)

/*  we can remove this because we use it in GameFragment in binding our view
    updateWordText()
    updateScoreText()*/
    }
    //remove from fragment too, and change not private
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishedCompleate(){
        _eventGameFinished.value = false

        }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }
}
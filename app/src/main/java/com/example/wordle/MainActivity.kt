package com.example.wordle

import android.content.Context
import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wordle.FourLetterWordList.getRandomFourLetterWord
import com.github.jinatonic.confetti.CommonConfetti
import com.github.jinatonic.confetti.ConfettiManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checkOne = findViewById<TextView>(R.id.checkOne)
        val checkTwo = findViewById<TextView>(R.id.checkTwo)
        val checkThree = findViewById<TextView>(R.id.checkThree)
        val userWordOne = findViewById<TextView>(R.id.userWordOne)
        val userWordTwo = findViewById<TextView>(R.id.userWordTwo)
        val userWordThree = findViewById<TextView>(R.id.userWordThree)
        val userCheckOne = findViewById<TextView>(R.id.userCheckOne)
        val userCheckTwo = findViewById<TextView>(R.id.userCheckTwo)
        val userCheckThree = findViewById<TextView>(R.id.userCheckThree)
        val correctWordTextView = findViewById<TextView>(R.id.correctWordTextView)
        val keyboard = findViewById<EditText>(R.id.keyboard)
        val guessButton = findViewById<Button>(R.id.guessButton)
        val streakView = findViewById<TextView>(R.id.streakView)
        val filterButton = findViewById<Button>(R.id.button)
        var wordToGuess = correctWordTextView.text
        var userGuess = keyboard.text
        var checkGuessReturn = ""
        val viewGroup = findViewById<View>(android.R.id.content) as ViewGroup
        var attemptCounter = 0
        var streakCounter = 0
        var wordListID = 0
        var confetti : ConfettiManager? = null

        /**
         * Description: Hides soft keyboard
         * Parameters:
         *   view : View - View to be hidden
         * */
        fun hideSoftKeyboard(view: View) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Parameters / Fields:
         *   wordToGuess : String - the target word the user is trying to guess
         *   guess : String - what the user entered as their guess
         *
         * Returns a String of 'O', '+', and 'X', where:
         *   'O' represents the right letter in the right place
         *   '+' represents the right letter in the wrong place
         *   'X' represents a letter not in the target word
         */
        fun checkGuess(guess: String) : String {
            var result = ""
            for (i in 0..3) {
                if (guess[i] == wordToGuess[i]) {
                    result += "O"
                }
                else if (guess[i] in wordToGuess) {
                    result += "+"
                }
                else {
                    result += "X"
                }
            }
            return result
        }

        /**
         * Description: Creates a colored SpannableString for provided String.
         * Parameters:
         *   guess: String - string made up of O,X, and + that needs to be colored green, red, or
         *                   yellow
         * Returns a SpannableString of Green, Red, and Yellow where:
         *   O = Green
         *   X = Red
         *   + = Yellow
         * */
        fun createSpannable(guess: String) : SpannableString {
            val spanGuess = SpannableString(guess)
            for (i in 0..3 ) {
                if (checkGuessReturn[i].toString() == "O") {
                    // set color to green
                    spanGuess.setSpan(ForegroundColorSpan(rgb(0,255,0)),
                        i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else if (checkGuessReturn[i].toString() == "X") {
                    // set color to red
                    spanGuess.setSpan(ForegroundColorSpan(rgb(255,0,0)),
                        i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else if (checkGuessReturn[i].toString() == "+") {
                    // set color to yellow
                    spanGuess.setSpan(ForegroundColorSpan(rgb(220,220,0)),
                        i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            return spanGuess
        }

        /**
         * Descripton: Updates streak based on outcome. If user guessed correctly, then increment by
         *             1 and if user guessed incorrectly, set streak to 0
         * Parameters:
         *   outcome: String - the outcome of the game. Either "WIN" or "LOSS".
         * */
        fun updateStreak(outcome: String) {
            if (outcome == "WIN") {
                streakCounter++
            }
            else {
                streakCounter = 0
            }
            streakView.setText("Word Streak: " + streakCounter)
        }

        /**
         * Description: Resets the text in relavant TextViews to blank and sets visibiltiy of
         *              relevant TextViews to be invisible
         * */
        fun reset() {
            // reset TextViews to start another game
            correctWordTextView.setText(getRandomFourLetterWord(wordListID))
            userWordOne.setText("")
            userWordTwo.setText("")
            userWordThree.setText("")
            userCheckOne.setText("")
            userCheckTwo.setText("")
            userCheckThree.setText("")
            guessButton.setText("Guess")
            keyboard.setText("")
            attemptCounter = 0
            checkOne.visibility = View.INVISIBLE
            checkTwo.visibility = View.INVISIBLE
            checkThree.visibility = View.INVISIBLE
            correctWordTextView.visibility = View.INVISIBLE
        }

        /**
         * Description: Handles game of Wordle
         * */
        fun wordle() {
            userGuess = keyboard.getText()
            // if statement for checking that word is length 4
            if(userGuess.length != 4) {
                Toast.makeText(this, "Please enter a four letter word",
                    Toast.LENGTH_SHORT).show()
                return
            }
            hideSoftKeyboard(keyboard)
            wordToGuess = correctWordTextView.text
            checkGuessReturn = checkGuess(userGuess.toString().uppercase())

            // update related TextViews based on which attempt user is on
            if (attemptCounter == 0) {
                userWordOne.setText(keyboard.text)
                userCheckOne.setText(createSpannable(checkGuessReturn), TextView.BufferType.SPANNABLE)
                checkOne.visibility = View.VISIBLE
            } else if (attemptCounter == 1) {
                userWordTwo.setText(keyboard.text)
                userCheckTwo.setText(createSpannable(checkGuessReturn), TextView.BufferType.SPANNABLE)
                checkTwo.visibility = View.VISIBLE
            } else if (attemptCounter == 2) {
                userWordThree.setText(keyboard.text)
                userCheckThree.setText(createSpannable(checkGuessReturn), TextView.BufferType.SPANNABLE)
                checkThree.visibility = View.VISIBLE
            }

            keyboard.setText("")
            attemptCounter++

            // User guessed correct word
            if (checkGuessReturn == "OOOO" && attemptCounter <= 3) {
                Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
                //Store ConfettiManager object to terminate confetti animation when reset button pressed
                confetti = CommonConfetti.rainingConfetti(viewGroup,
                    (intArrayOf(Color.GREEN, Color.BLUE))).infinite()
                correctWordTextView.visibility = View.VISIBLE
                updateStreak("WIN")
                guessButton.setText("Reset")
                guessButton.setOnClickListener{
                    hideSoftKeyboard(keyboard)
                    reset()
                    if (confetti != null) {
                        confetti?.terminate()
                    }
                    guessButton.setOnClickListener{
                        wordle()
                    }
                }
            }

            // Used up all attempts
            else if (attemptCounter == 3) {
                Toast.makeText(this, "You've reached the maximum number of tries",
                                Toast.LENGTH_SHORT).show()
                correctWordTextView.visibility = View.VISIBLE
                guessButton.setText("Reset")
                updateStreak("LOSS")
                guessButton.setOnClickListener{
                    hideSoftKeyboard(keyboard)
                    reset()
                    guessButton.setOnClickListener{
                        wordle()
                    }
                }
            }
        }

        /**
         * Description: Handles popup menu
         * Parameters:
         *   view: View - View where popup will be connected to
         * */
        fun showPopup(view: View) {
            val popup = PopupMenu(this, view)
            // Inflate the menu from xml
            popup.inflate(R.menu.popup_filters)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                // Handle menu item selection
                when (item!!.itemId) {
                    R.id.defaultWords -> {
                        wordListID = 0
                    }
                    R.id.places -> {
                        wordListID = 1
                    }
                    R.id.animals -> {
                        wordListID = 2
                    }
                }
                // Reseting for new round of wordle
                hideSoftKeyboard(keyboard)
                reset()
                if (confetti != null) {
                    confetti?.terminate()
                }
                guessButton.setOnClickListener{
                    wordle()
                }
                true
            })
            popup.show()
        }

        // Beginning of game, get random word to be guessed
        correctWordTextView.setText(getRandomFourLetterWord(wordListID))

        filterButton.setOnClickListener{
            showPopup(filterButton)
        }

        // Begin Wordle when "Guess" button is clicked
        guessButton.setOnClickListener{
            wordle()
        }
    }
}
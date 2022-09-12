package com.example.wordle

import android.content.Context
import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    object FourLetterWordList {
        // List of most common 4 letter words from: https://7esl.com/4-letter-words/
        val fourLetterWords =
        "Area,Army,Baby,Back,Ball,Band,Bank,Base,Bill,Body,Book,Call,Card,Care,Case,Cash,City,Club,Cost,Date,Deal,Door,Duty,East,Edge,Face,Fact,Farm,Fear,File,Film,Fire,Firm,Fish,Food,Foot,Form,Fund,Game,Girl,Goal,Gold,Hair,Half,Hall,Hand,Head,Help,Hill,Home,Hope,Hour,Idea,Jack,John,Kind,King,Lack,Lady,Land,Life,Line,List,Look,Lord,Loss,Love,Mark,Mary,Mind,Miss,Move,Name,Need,News,Note,Page,Pain,Pair,Park,Part,Past,Path,Paul,Plan,Play,Post,Race,Rain,Rate,Rest,Rise,Risk,Road,Rock,Role,Room,Rule,Sale,Seat,Shop,Show,Side,Sign,Site,Size,Skin,Sort,Star,Step,Task,Team,Term,Test,Text,Time,Tour,Town,Tree,Turn,Type,Unit,User,View,Wall,Week,West,Wife,Will,Wind,Wine,Wood,Word,Work,Year,Bear,Beat,Blow,Burn,Call,Care,Cast,Come,Cook,Cope,Cost,Dare,Deal,Deny,Draw,Drop,Earn,Face,Fail,Fall,Fear,Feel,Fill,Find,Form,Gain,Give,Grow,Hang,Hate,Have,Head,Hear,Help,Hide,Hold,Hope,Hurt,Join,Jump,Keep,Kill,Know,Land,Last,Lead,Lend,Lift,Like,Link,Live,Look,Lose,Love,Make,Mark,Meet,Mind,Miss,Move,Must,Name,Need,Note,Open,Pass,Pick,Plan,Play,Pray,Pull,Push,Read,Rely,Rest,Ride,Ring,Rise,Risk,Roll,Rule,Save,Seek,Seem,Sell,Send,Shed,Show,Shut,Sign,Sing,Slip,Sort,Stay,Step,Stop,Suit,Take,Talk,Tell,Tend,Test,Turn,Vary,View,Vote,Wait,Wake,Walk,Want,Warn,Wash,Wear,Will,Wish,Work,Able,Back,Bare,Bass,Blue,Bold,Busy,Calm,Cold,Cool,Damp,Dark,Dead,Deaf,Dear,Deep,Dual,Dull,Dumb,Easy,Evil,Fair,Fast,Fine,Firm,Flat,Fond,Foul,Free,Full,Glad,Good,Grey,Grim,Half,Hard,Head,High,Holy,Huge,Just,Keen,Kind,Last,Late,Lazy,Like,Live,Lone,Long,Loud,Main,Male,Mass,Mean,Mere,Mild,Nazi,Near,Neat,Next,Nice,Okay,Only,Open,Oral,Pale,Past,Pink,Poor,Pure,Rare,Real,Rear,Rich,Rude,Safe,Same,Sick,Slim,Slow,Soft,Sole,Sore,Sure,Tall,Then,Thin,Tidy,Tiny,Tory,Ugly,Vain,Vast,Very,Vice,Warm,Wary,Weak,Wide,Wild,Wise,Zero,Ably,Afar,Anew,Away,Back,Dead,Deep,Down,Duly,Easy,Else,Even,Ever,Fair,Fast,Flat,Full,Good,Half,Hard,Here,High,Home,Idly,Just,Late,Like,Live,Long,Loud,Much,Near,Nice,Okay,Once,Only,Over,Part,Past,Real,Slow,Solo,Soon,Sure,That,Then,This,Thus,Very,When,Wide"

        // Returns a list of four letter words as a list
        fun getAllFourLetterWords(): List<String> {
            return fourLetterWords.split(",")
        }

        // Returns a random four letter word from the list in all caps
        fun getRandomFourLetterWord(): String {
            val allWords = getAllFourLetterWords()
            val randomNumber = (0..allWords.size).shuffled().last()
            return allWords[randomNumber].uppercase()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guessOne = findViewById<TextView>(R.id.guessOne)
        val guessOneCheck = findViewById<TextView>(R.id.guessOneCheck)
        val guessTwo = findViewById<TextView>(R.id.guessTwo)
        val guessTwoCheck = findViewById<TextView>(R.id.guessTwoCheck)
        val guessThree = findViewById<TextView>(R.id.guessThree)
        val guessThreeCheck = findViewById<TextView>(R.id.guessThreeCheck)
        val userWordOne = findViewById<TextView>(R.id.userWordOne)
        val userWordTwo = findViewById<TextView>(R.id.userWordTwo)
        val userWordThree = findViewById<TextView>(R.id.userWordThree)
        val correctOne = findViewById<TextView>(R.id.correctOne)
        val correctTwo = findViewById<TextView>(R.id.correctTwo)
        val correctThree = findViewById<TextView>(R.id.correctThree)
        val correctWord = findViewById<TextView>(R.id.correctWord)
        val keyboard = findViewById<EditText>(R.id.keyboard)
        val button = findViewById<Button>(R.id.button)
        var wordToGuess = correctWord.text
        var userGuess = keyboard.text
        var checkReturn = ""
        val viewGroup = findViewById<View>(android.R.id.content) as ViewGroup
        var counter = 0

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
            System.out.println(guess)
            System.out.println(wordToGuess)
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

        fun createSpannable(guess: String) : SpannableString {
            val spanGuess = SpannableString(guess)
            for (i in 0..3 ) {
                if (checkReturn[i].toString() == "O") {
                    spanGuess.setSpan(ForegroundColorSpan(-16711936), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else if (checkReturn[i].toString() == "X") {
                    spanGuess.setSpan(ForegroundColorSpan(-65536), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else if (checkReturn[i].toString() == "+") {
                    spanGuess.setSpan(ForegroundColorSpan(rgb(220,220,0)), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            return spanGuess
        }

        fun reset() {
            // reset TextViews to start another game
            correctWord.setText(FourLetterWordList.getRandomFourLetterWord())
            userWordOne.setText("")
            userWordTwo.setText("")
            userWordThree.setText("")
            correctOne.setText("")
            correctTwo.setText("")
            correctThree.setText("")
            button.setText("Guess")
            counter = 0
            guessOneCheck.visibility = View.INVISIBLE
            guessTwoCheck.visibility = View.INVISIBLE
            guessThreeCheck.visibility = View.INVISIBLE
            correctWord.visibility = View.INVISIBLE
        }


        fun wordle() {
            // handles wordle game
            hideSoftKeyboard(keyboard)
            userGuess = keyboard.getText()
            wordToGuess = correctWord.text
            checkReturn = checkGuess(userGuess.toString().uppercase())

            if (counter == 0) {
                userWordOne.setText(keyboard.text)
                correctOne.setText(createSpannable(checkReturn), TextView.BufferType.SPANNABLE)
                guessOneCheck.visibility = View.VISIBLE
            }
            else if (counter == 1) {
                userWordTwo.setText(keyboard.text)
                correctTwo.setText(createSpannable(checkReturn), TextView.BufferType.SPANNABLE)
                guessTwoCheck.visibility = View.VISIBLE
            }
            else if (counter == 2) {
                userWordThree.setText(keyboard.text)
                correctThree.setText(createSpannable(checkReturn), TextView.BufferType.SPANNABLE)
                guessThreeCheck.visibility = View.VISIBLE
            }

            counter++
            // Got the correct word
            if (checkReturn == "OOOO" && counter <= 3) {
                //CONGRATS
                Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
                //Store to terminate when button is pressed
                var confetti = CommonConfetti.rainingConfetti(viewGroup, (intArrayOf(Color.GREEN, Color.BLUE))).infinite()
                correctWord.visibility = View.VISIBLE
                button.text = "Reset"
                button.setOnClickListener{
                    hideSoftKeyboard(keyboard)
                    reset()
                    confetti.terminate()
                    button.setOnClickListener{
                        if(userGuess.toString().length != 4) {
                            Toast.makeText(this, "Please enter a four letter word", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        wordle()
                    }
                }
            }

            // Used up all attempts
            else if (counter == 3) {
                Toast.makeText(this, "You've reached the maximum number of tries", Toast.LENGTH_SHORT).show()
                //Store to terminate when button is pressed
                correctWord.visibility = View.VISIBLE
                button.text = "Reset"
                button.setOnClickListener{
                    hideSoftKeyboard(keyboard)
                    reset()
                    button.setOnClickListener{
                        if(userGuess.toString().length != 4) {
                            Toast.makeText(this, "Please enter a four letter word", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        wordle()
                    }
                }
            }
        }
        correctWord.setText(FourLetterWordList.getRandomFourLetterWord())
        button.setOnClickListener{
            if(userGuess.toString().length != 4) {
                Toast.makeText(this, "Please enter a four letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            wordle()
        }

    }
}
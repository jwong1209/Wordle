package com.example.wordle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

    /*
    fun handleWordle(View: EditText, View: Button, View) {

    }*/

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
        val keyboard = findViewById<TextInputEditText>(R.id.keyboard)
        val button = findViewById<Button>(R.id.button)
        var wordToGuess = correctWord.text
        var userGuess = keyboard.text
        var checkReturn = ""
        var counter = 1

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


        fun reset() {
            correctWord.setText(FourLetterWordList.getRandomFourLetterWord())
            userWordOne.setText("")
            userWordTwo.setText("")
            userWordThree.setText("")
            correctOne.setText("")
            correctTwo.setText("")
            correctThree.setText("")
            button.setText("Guess")
            counter = 1

            button.setOnClickListener{
                userGuess = keyboard.getText()
                if(userGuess.toString().length != 4) {
                    Toast.makeText(this, "Please enter a four letter word", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                wordToGuess = correctWord.text
                checkReturn = checkGuess(userGuess.toString().uppercase())

                if (counter == 1) {
                    userWordOne.setText(keyboard.text)
                    correctOne.setText(checkReturn)
                }
                else if (counter == 2) {
                    userWordTwo.setText(keyboard.text)
                    correctTwo.setText(checkReturn)
                }
                else if (counter == 3) {
                    userWordThree.setText(keyboard.text)
                    correctThree.setText(checkReturn)
                }
                counter++
                if (checkReturn == "OOOO" && counter <= 4) {
                    //CONGRATS
                    Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
                    button.text = "Reset"
                    button.setOnClickListener{
                        reset()
                    }
                }
                if (counter > 4) {
                    Toast.makeText(this, "You've exceeded the maximum number of tries", Toast.LENGTH_SHORT).show()
                    reset()
                }
            }

        }

        correctWord.setText(FourLetterWordList.getRandomFourLetterWord())

        button.setOnClickListener{
            userGuess = keyboard.getText()
            if(userGuess.toString().length != 4) {
                Toast.makeText(this, "Please enter a four letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            wordToGuess = correctWord.text
            checkReturn = checkGuess(userGuess.toString().uppercase())

            if (counter == 1) {
                userWordOne.setText(keyboard.text)
                correctOne.setText(checkReturn)
            }
            else if (counter == 2) {
                userWordTwo.setText(keyboard.text)
                correctTwo.setText(checkReturn)
            }
            else if (counter == 3) {
                userWordThree.setText(keyboard.text)
                correctThree.setText(checkReturn)
            }
            counter++
            if (checkReturn == "OOOO" && counter <= 4) {
                //CONGRATS
                Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
                button.text = "Reset"
                button.setOnClickListener{
                    reset()
                }
            }
            if (counter > 4) {
                Toast.makeText(this, "You've exceeded the maximum number of tries", Toast.LENGTH_SHORT).show()
                reset()
            }
        }

    }



}
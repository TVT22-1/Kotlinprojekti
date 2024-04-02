package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tictactoe.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var binding: ActivityGameBinding

    private var gameModel : GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GameData.fetchGameModel()

        // Assign click listeners to all buttons
        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)

        // Start game button listener
        binding.startGameBtn.setOnClickListener {
            startGame()
        }
        // Observe changes in game model from GameData singleton
        GameData.gameModel.observe(this){
            gameModel = it
            setUI()
        }
    }

    // Function to update UI elements based on game model
    fun setUI(){
        gameModel?.apply {
            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            // Set visibility of start game button based on game status
            binding.startGameBtn.visibility = View.VISIBLE

            // Set game status text based on game status
            binding.gameStatusText.text =
                when(gameStatus){
                    GameStatus.CREATED -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        "Game ID :"+ gameId
                    }
                    GameStatus.JOINED ->{
                        "Click on start game"
                    }
                    GameStatus.INPROGRESS ->{
                        binding.startGameBtn.visibility = View.INVISIBLE
                        when(GameData.myID){
                            currentPlayer -> "Your turn"
                            else ->  currentPlayer + " turn"
                        }

                    }
                    GameStatus.FINISHED ->{
                        if(winner.isNotEmpty()) {
                            when(GameData.myID){
                                winner -> "You won"
                                else ->   winner + " Won"
                            }

                        }
                        else "DRAW"
                    }
                }

        }
    }

    // Function to start the game
    fun startGame(){
        gameModel?.apply {
            updateGameData(
                GameModel(
                    gameId = gameId,
                    gameStatus = GameStatus.INPROGRESS
                )
            )
        }
    }
    // Function to update game data
    fun updateGameData(model : GameModel){
        GameData.saveGameModel(model)
    }

    // Function to check for a winner
    fun checkForWinner(){
        val winningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6),
        )

        gameModel?.apply {
            for ( i in winningPos){
                //012
                if(
                    filledPos[i[0]] == filledPos[i[1]] &&
                    filledPos[i[1]]== filledPos[i[2]] &&
                    filledPos[i[0]].isNotEmpty()
                ){
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]
                }
            }

            if( filledPos.none(){ it.isEmpty() }){
                gameStatus = GameStatus.FINISHED
            }


            updateGameData(this)

        }


    }
    // Function to handle button clicks
    override fun onClick(v: View?) {
        gameModel?.apply {
            if(gameStatus!= GameStatus.INPROGRESS){
                Toast.makeText(applicationContext,"Game not started",Toast.LENGTH_SHORT).show()
                return
            }

            // Check if it's the player's turn
            if(gameId!="-1" && currentPlayer!=GameData.myID ){
                Toast.makeText(applicationContext,"Not your turn",Toast.LENGTH_SHORT).show()
                return
            }
            // Get clicked position
            val clickedPos =(v?.tag  as String).toInt()
            // If clicked position is empty, fill it with current player's symbol
            if(filledPos[clickedPos].isEmpty()){
                filledPos[clickedPos] = currentPlayer
                // Switch to the next player
                currentPlayer = if(currentPlayer=="X") "O" else "X"
                // Check for a winner
                checkForWinner()
                // Update game data
                updateGameData(this)
            }

        }
    }
}
package com.example.tictactoe

import kotlin.random.Random

// Data class representing the game model
data class GameModel (
    var gameId : String = "-1", // Unique identifier for the game
    var filledPos : MutableList<String> = mutableListOf("","","","","","","","",""), // Represents the state of the board, each element represents a position on the board
    var winner : String ="",    // Stores the symbol of the winner ('X' or 'O') if there's a winner
    var gameStatus : GameStatus = GameStatus.CREATED, // Indicates the current status of the game
    var currentPlayer : String = (arrayOf("X","O"))[Random.nextInt(2)] // Randomly selects the starting player ('X' or 'O')
)

// Enum to represent different game statuses
enum class GameStatus{
    CREATED,    // Game has been created but not yet joined by another player
    JOINED,     // Game has been joined by another player and is ready to start
    INPROGRESS, // Game is in progress
    FINISHED    // Game has finished
}

package com.example.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Singleton object responsible for managing game data
object GameData {
    // LiveData object holding the current game model
    private var _gameModel : MutableLiveData<GameModel> = MutableLiveData()
    var gameModel : LiveData<GameModel> = _gameModel // Exposed LiveData for observing changes
    var myID = "" // Identifier for the player

    // Function to save the game model to Firebase Firestore
    fun saveGameModel(model : GameModel){
        _gameModel.postValue(model) // Update the local LiveData with the new model
        if(model.gameId != "-1"){   // Check if the game has a valid ID
            // Save the game model to Firestore
            Firebase.firestore.collection("games")
                .document(model.gameId)
                .set(model)
        }
    }

    // Function to fetch the game model from Firebase Firestore
    fun fetchGameModel(){
        // Access the current game model from the LiveData
        gameModel.value?.apply {
            // Check if the game has a valid ID
            if(gameId != "-1"){
                // Listen for changes to the game model in Firestore
                Firebase.firestore.collection("games")
                    .document(gameId)
                    .addSnapshotListener { value, error ->
                        // Convert the Firestore document to a GameModel object
                        val model = value?.toObject(GameModel::class.java)
                        // Update the local LiveData with the new model
                        _gameModel.postValue(model)
                    }
            }
        }
    }
}
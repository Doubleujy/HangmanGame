/**
 * Group Number:
 * Names: Shawn, Jing Yuan, Kyros, Justin
 * 
 * PURPOSE: This is the brain of the game. It contains all game rules, tracks game state, and validates player moves.
 */

import java.util.HashSet;
import java.util.Set;

public class GameLogic {
    //instance variables to track game logic
    private String targetWord;          // The word to guess
    private StringBuilder currentWordState; // Current display (e.g., "J _ V _")
    private Set<Character> guessedLetters; // Letters already guessed
    private int incorrectGuesses;       // Count of wrong guesses
    private final int MAX_INCORRECT_GUESSES = 6; // Constant: max allowed wrong guesses
    private boolean gameOver;           // Flag: is game finished?
    private boolean gameWon;            // Flag: did player win?
    
    //constructor - initializes a new game with given word
    public GameLogic(String word) {
        this.targetWord = word.toUpperCase();
        initializeGame();  // Set up initial game state
    }
    
    //initialize/reset game state
    private void initializeGame() {
        //create initial display: all underscores
        currentWordState = new StringBuilder();
        for (int i = 0; i < targetWord.length(); i++) {
            currentWordState.append("_");  //one underscore per letter
        }
        
        //initialize empty set for guessed letters
        guessedLetters = new HashSet<>();
        
        //reset counters and flags
        incorrectGuesses = 0;
        gameOver = false;
        gameWon = false;
    }
    
    //THE MOST IMPORTANT METHOD - Process player's guess
    public boolean makeGuess(char letter) {
        //normalize input (case-insensitive)
        letter = Character.toUpperCase(letter);
        
        //validate input - must be a letter
        if (!Character.isLetter(letter)) {
            return false; // Invalid character - doesn't count as guess
        }
        
        //check if letter was already guessed
        if (guessedLetters.contains(letter)) {
            return false; // Already guessed - doesn't count as guess
        }
        
        //add to guessed letters set
        guessedLetters.add(letter);
        
        //check if letter exists in target word
        if (targetWord.indexOf(letter) >= 0) {
            //letter is CORRECT - reveal all occurrences
            
            //loop through each character in target word
            for (int i = 0; i < targetWord.length(); i++) {
                //if this position matches guessed letter
                if (targetWord.charAt(i) == letter) {
                    //replace underscore with the letter
                    currentWordState.setCharAt(i, letter);
                }
            }
            
            //check if game is won
            //compare current state with target word
            if (currentWordState.toString().equals(targetWord)) {
                gameWon = true;    // Player has guessed all letters
                gameOver = true;   // Game ends when player wins
            }
            return true; //if valid and correct guess
            
        } else {
            //if letter is INCORRECT
            //+ wrong guess counter
            incorrectGuesses++;
            
            //check if game is lost
            if (incorrectGuesses >= MAX_INCORRECT_GUESSES) {
                gameOver = true; //game ends when max wrong guesses reached
            }
            return true; //valid but incorrect guess
        }
    }
    
    //get current word display (e.g., 5 letters "J _ _ _ _")
    public String getCurrentWordState() {
        return currentWordState.toString();
    }
    
    //get the target word (for end of game)
    public String getTargetWord() {
        return targetWord;
    }
    
    //get number of incorrect guesses
    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }
    
    //get maximum allowed incorrect guesses
    public int getMaxIncorrectGuesses() {
        return MAX_INCORRECT_GUESSES;
    }
    
    //get all guessed letters
    public Set<Character> getGuessedLetters() {
        return new HashSet<>(guessedLetters); // Return copy to protect original
    }
    
    //check if game is over
    public boolean isGameOver() {
        return gameOver;
    }
    
    //check if player won
    public boolean isGameWon() {
        return gameWon;
    }
    
    //calculate remaining guesses
    public int getRemainingGuesses() {
        return MAX_INCORRECT_GUESSES - incorrectGuesses;
    }
}
/**
 * Group Number: 1
 * Names: Shawn, Jing Yuan, Kyros, Justin
 * 
 * PURPOSE: Handles all user interaction - displays game state, gets user input, and shows results.
 */

import java.util.Scanner;
import java.util.Set;

public class GameUI {
    private Scanner scanner;
    public GameUI() {
        scanner = new Scanner(System.in);
    }
    
    //display welcome message
    public void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        WELCOME TO HANGMAN GAME        ");
        System.out.println("=======================================\n");
    }
    
    //display current game state
    public void displayGameState(GameLogic game) {
        //display hangman drawing based on wrong guesses
        System.out.println("\n" + getHangmanDrawing(game.getIncorrectGuesses()));
        
        //display current word with spaces between letters
        System.out.println("\nWord: " + formatWordDisplay(game.getCurrentWordState()));
        
        //display guess counter
        System.out.println("Incorrect Guesses: " + game.getIncorrectGuesses() + 
                          "/" + game.getMaxIncorrectGuesses());
        System.out.println("Remaining Guesses: " + game.getRemainingGuesses());
        
        //display guessed letters:
        Set<Character> guessedLetters = game.getGuessedLetters();
        if (!guessedLetters.isEmpty()) {
            System.out.println("Guessed Letters: " + formatGuessedLetters(guessedLetters));
        }
    }
    
    //formats word display
    private String formatWordDisplay(String word) {
        StringBuilder formatted = new StringBuilder();
        
        //add space between each character
        for (char c : word.toCharArray()) {
            //anyone know another way to do this? i took it from ai
            formatted.append(c).append(" ");
        }
        
        return formatted.toString().trim(); // Remove trailing space
    }
    
    //formats guessed letters
    private String formatGuessedLetters(Set<Character> letters) {
        StringBuilder sb = new StringBuilder();
        
        for (Character c : letters) {
            sb.append(c).append(" ");
        }
        
        return sb.toString().trim();
    }
    
    //get player's guesses
    public char getPlayerGuess() {
        //use infinite loop until valid input is received
        while (true) {
            try {
                System.out.print("\nEnter your guess (using a single letter): ");
                String input = scanner.nextLine().trim();
                
                //validate input length
                if (input.length() != 1) { //if not = 1
                    System.out.println("Error: Please enter exactly one letter.");
                    continue; //go back to start of loop
                }
                
                //get first character
                char guess = input.charAt(0);
                
                //validating it's a letter
                if (!Character.isLetter(guess)) {
                    System.out.println("Error: Only alphabetic characters are allowed.");
                    continue;
                }
                
                //return valid guess
                return guess;
                
            } catch (Exception a) {
                //handle any unexpected errors in input
                System.out.println("Error reading input. Please try again.");
                scanner.nextLine();
            }
        }
    }
    
    //display message for invalid/duplicate guess
    public void displayInvalidGuessMessage() {
        System.out.println("You've already guessed that letter / it's invalid. Try again.");
    }
    
    //display final game result
    public void displayGameResult(GameLogic game) {
        //show final hangman drawing
        System.out.println("\n" + getHangmanDrawing(game.getIncorrectGuesses()));
        
        System.out.println("\n=======================================");
        
        //display win/lose message
        if (game.isGameWon()) {
            System.out.println("CONGRATULATIONS! YOU WIN!");
        } else {
            System.out.println("GAME OVER! YOU LOSE!");
        }
        
        //reveal the word
        System.out.println("The word was: " + game.getTargetWord());
        System.out.println("=======================================");
    }
    
    //generate ASCII art hangman drawing
    private String getHangmanDrawing(int incorrectGuesses) {
        //array of hangman stages (0-6 incorrect guesses)
        String[] hangmanStages = {
            //stage 0: No wrong guesses
            "  +---+\n" +
            "  |   |\n" +
            "      |\n" +
            "      |\n" +
            "      |\n" +
            "      |\n" +
            "=========",
            
            //stage 1: Head
            "  +---+\n" +
            "  |   |\n" +
            "  O   |\n" +
            "      |\n" +
            "      |\n" +
            "      |\n" +
            "=========",
            
            //stage 2: Body
            "  +---+\n" +
            "  |   |\n" +
            "  O   |\n" +
            "  |   |\n" +
            "      |\n" +
            "      |\n" +
            "=========",
            
            //stage 3: Left arm
            "  +---+\n" +
            "  |   |\n" +
            "  O   |\n" +
            " /|   |\n" +
            "      |\n" +
            "      |\n" +
            "=========",
            
            //stage 4: Right arm
            "  +---+\n" +
            "  |   |\n" +
            "  O   |\n" +
            " /|\\  |\n" +
            "      |\n" +
            "      |\n" +
            "=========",
            
            //stage 5: Left leg
            "  +---+\n" +
            "  |   |\n" +
            "  O   |\n" +
            " /|\\  |\n" +
            " /    |\n" +
            "      |\n" +
            "=========",
            
            //stage 6: Right leg (hangman)
            "  +---+\n" +
            "  |   |\n" +
            "  O   |\n" +
            " /|\\  |\n" +
            " / \\  |\n" +
            "      |\n" +
            "========="
        };
        
        //return appropriate stage
        return hangmanStages[Math.min(incorrectGuesses, hangmanStages.length - 1)];
    }
    
    //close 
    public void close() {
        scanner.close();
    }

}

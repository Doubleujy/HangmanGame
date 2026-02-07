
/**
 * Group Number: 5
 * Names: Shawn, Jing Yuan, Kyros, Justin, Tyler
 *
 * PURPOSE: This is the pilot and controller of the game. It coordinates all other components and manages the game flow.
 */

public class HangmanBasic {
    //main method
    public static void main(String[] args) {
        try {
            //create WordLoader object
            WordLoader wordLoader = new WordLoader();
            //create GameUI object
            GameUI ui = new GameUI();
            //welcome message
            ui.displayWelcomeMessage();
            //get random word from WordLoader
            String targetWord = wordLoader.getRandomWord();
            //inform player about word length
            System.out.println("A word has been chosen. It has "+ targetWord.length() + " letters.");
            //create GameLogic object with the chosen word
            GameLogic game = new GameLogic(targetWord);
            //loop continues until game over
            while (!game.isGameOver()) {
                //display current game state
                ui.displayGameState(game);
                //get player's guess
                char guess = ui.getPlayerGuess();
                //processes the guess
                boolean validGuess = game.makeGuess(guess);
                //handles invalid/duplicate guess
                if (!validGuess) {
                    ui.displayInvalidGuessMessage();
                }
                //loop repeats, showing updated state after each guess
            }
            //display final result
            ui.displayGameResult(game);
            ui.close();
        } catch (Exception e) {
            System.err.println("Invalid input:" + e.getMessage()); //error message
            System.err.println("Please restart the game.");
        }
    }
}
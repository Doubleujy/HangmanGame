/**
 * Group Number: 5
 * Names: Shawn, Jing Yuan, Kyros, Justin, Tyler
 * 
 * PURPOSE: This is the brain of the game. It contains all game rules, tracks game state, and validity of player moves.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class HangmanAdditional {

    //maximum number of players allowed in the game
    private static final int MAX_PLAYERS = 5;
    
    //API key for validating words using API Ninjas Dictionary API
    private static final String API_KEY = "fHmRepD6A0Z9wtbSK80pCQK02r2lsxbGy5opzoDC";

    public static void main(String[] args) {
        // Initialize scanner for user input and UI for game display
        Scanner sc = new Scanner(System.in);
        HangmanAddGUI ui = new HangmanAddGUI();
        Random random = new Random();

        // Display welcome message to players
        ui.displayWelcomeMessage();

        boolean playAgain = true;

        // Main game loop - continues until players choose not to play again
        while (playAgain) {
            // Get the number of players for this round
            int numPlayers = promptNumPlayers(sc);

            // Collect player names and store them in a list
            List<String> players = new ArrayList<>();
            for (int i = 1; i <= numPlayers; i++) {
                System.out.print("Enter name for Player " + i + ": ");
                String name = sc.nextLine().trim();
                // If no name is entered, use a default name
                if (name.isEmpty()) name = "Player " + i;
                players.add(name);
            }

            // Initialize points tracking for each player (starts at 0)
            Map<String, Integer> playerPoints = new HashMap<>();
            for (String player : players) {
                playerPoints.put(player, 0);
            }

            // Randomly select one player to choose the secret word
            int chooserIndex = random.nextInt(numPlayers);
            String chooserName = players.get(chooserIndex);
            
            System.out.println("\n" + chooserName + " has been randomly selected to choose the word/phrase/sentence.");
            
            // Prompt the chosen player to enter a secret word/phrase
            String secret = promptSecret(sc, chooserName);

            // Clear the screen so other players can't see the secret word
            clearScreenFake();

            // Initialize the game logic with the secret word
            HangmanAddGL game = new HangmanAddGL(secret);

            // AUTOMATICALLY REVEAL ALL NON-LETTER CHARACTERS
            // This includes spaces, punctuation, hyphens, apostrophes, etc.
            // Players only need to guess actual letters
            for (int i = 0; i < secret.length(); i++) {
                char c = secret.charAt(i);
                // If the character is not a letter (space, apostrophe, hyphen, etc.)
                if (!Character.isLetter(c)) {
                    // Automatically "guess" it to reveal it on the board
                    game.makeGuess(c);
                }
            }

            // Display game information
            System.out.println("A word/phrase has been chosen. It has " + countLetters(secret) + " letter(s).");
            System.out.println("(Spaces/punctuation are shown automatically.)");

            // Start the guessing rotation with the player after the chooser
            int guesserIndex = (chooserIndex + 1) % numPlayers;

            // Main game loop - continues until the word is fully guessed or game is over
            while (!game.isGameOver()) {
                // Display current state of the hangman and guessed letters
                ui.displayGameState(game);

                // Get the current player's name
                String currentGuesser = players.get(guesserIndex);
                System.out.println("\n" + currentGuesser + ", it's your turn.");

                // KEEP PROMPTING THE SAME PLAYER UNTIL THEY MAKE A VALID GUESS
                // This prevents invalid guesses from advancing to the next player
                boolean validGuess = false;
                int pointsEarned = 0;
                
                while (!validGuess && !game.isGameOver()) {
                    // Get the player's letter guess
                    char guess = ui.getPlayerGuess();

                    // Count how many times this letter appears in the secret word
                    // This determines how many points the player will earn
                    pointsEarned = countLetterOccurrences(secret, guess);
                    
                    // Make the guess in the game
                    boolean valid = game.makeGuess(guess);

                    // If the guess was invalid (already guessed), display error and loop again
                    if (!valid) {
                        ui.displayInvalidGuessMessage();
                        System.out.println(currentGuesser + ", please try again with a different letter.");
                        // Loop continues - same player gets another chance
                    } else {
                        // Valid guess - exit the loop and proceed
                        validGuess = true;
                        
                        // Award points if the letter was in the word
                        if (pointsEarned > 0) {
                            playerPoints.put(currentGuesser, playerPoints.get(currentGuesser) + pointsEarned);
                            System.out.println(currentGuesser + " earned " + pointsEarned + " point(s)!");
                            System.out.println(currentGuesser + "'s total points: " + playerPoints.get(currentGuesser));
                        }
                    }
                }

                // Move to the next player in rotation (only after a valid guess)
                guesserIndex = (guesserIndex + 1) % numPlayers;
                
                // Skip the word chooser (they don't get to guess)
                if (guesserIndex == chooserIndex) {
                    guesserIndex = (guesserIndex + 1) % numPlayers;
                }
            }

            // Display the final result (win/loss)
            ui.displayGameResult(game);

            // Display final scores for all players (excluding the word chooser)
            System.out.println("\n=== FINAL SCORES ===");
            for (String player : players) {
                if (!player.equals(chooserName)) {
                    System.out.println(player + ": " + playerPoints.get(player) + " points");
                }
            }

            // Find the maximum score among all guessing players
            int maxPoints = -1;
            List<String> winners = new ArrayList<>();
            
            for (String player : players) {
                // Only consider players who were guessing (not the word chooser)
                if (!player.equals(chooserName)) {
                    int points = playerPoints.get(player);
                    
                    // If this player has more points than current max, they're the new leader
                    if (points > maxPoints) {
                        maxPoints = points;
                        winners.clear();
                        winners.add(player);
                    } 
                    // If this player ties with the current max, add them to winners list
                    else if (points == maxPoints) {
                        winners.add(player);
                    }
                }
            }

            // Announce the winner(s)
            if (maxPoints > 0) {
                if (winners.size() > 1) {
                    // Multiple winners - there's a tie
                    System.out.print("\n🏆 It's a tie! ");
                    
                    // Format the winner names nicely (e.g., "Alice, Bob and Charlie")
                    for (int i = 0; i < winners.size(); i++) {
                        System.out.print(winners.get(i));
                        if (i < winners.size() - 2) {
                            System.out.print(", ");
                        } else if (i == winners.size() - 2) {
                            System.out.print(" and ");
                        }
                    }
                    System.out.println(" tied with " + maxPoints + " points! 🏆");
                } else {
                    // Single winner
                    System.out.println("\n🏆 " + winners.get(0) + " wins with " + maxPoints + " points! 🏆");
                }
            } else {
                // No one scored any points
                System.out.println("\nNo points were scored this round!");
            }

            // Ask if players want to play another round
            playAgain = promptPlayAgain(sc);
            if (playAgain) {
                clearScreenFake();
            }
        }

        // Clean up resources and display goodbye message
        ui.close();
        sc.close();
        System.out.println("Thanks for playing!");
    }

    /**
     * Counts how many times a specific letter appears in the text
     * Used to determine how many points a player earns for a correct guess
     * 
     * @param text The secret word/phrase
     * @param letter The letter being guessed
     * @return The number of times the letter appears (case-insensitive)
     */
    private static int countLetterOccurrences(String text, char letter) {
        int count = 0;
        char lowerLetter = Character.toLowerCase(letter);
        
        // Check each character in the text
        for (int i = 0; i < text.length(); i++) {
            if (Character.toLowerCase(text.charAt(i)) == lowerLetter) {
                count++;
            }
        }
        return count;
    }

    /**
     * Validates whether the input is a valid English word or phrase
     * For phrases, validates each word individually using the API
     * 
     * @param input The word or phrase to validate
     * @return true if all words are valid, false otherwise
     */
    private static boolean isValidWord(String input) {
        try {
            // Clean the input: convert to lowercase and remove punctuation
            String cleanInput = input.trim().toLowerCase();
            cleanInput = cleanInput.replaceAll("[^a-z\\s]", "");
            
            // If the input contains spaces, it's a phrase - validate each word
            if (cleanInput.contains(" ")) {
                String[] words = cleanInput.split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        // If any word is invalid, the entire phrase is invalid
                        if (!checkWordWithAPI(word)) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                // Single word - validate it directly
                return checkWordWithAPI(cleanInput);
            }
        } catch (Exception e) {
            System.out.println("Error validating word: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a single word is valid using the API Ninjas Dictionary API
     * Makes an HTTP GET request to the API and parses the JSON response
     * 
     * @param word The word to validate
     * @return true if the word exists in the dictionary, false otherwise
     */
    private static boolean checkWordWithAPI(String word) {
        BufferedReader in = null;
        HttpURLConnection connection = null;
        
        try {
            // URL-encode the word to handle special characters
            String encodedWord = URLEncoder.encode(word, "UTF-8");
            
            // Construct the API endpoint URL
            String urlString = "https://api.api-ninjas.com/v1/dictionary?word=" + encodedWord;
            URL url = new URL(urlString);
            
            // Set up the HTTP connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Api-Key", API_KEY); // Authentication
            connection.setConnectTimeout(10000); // 10 second timeout
            connection.setReadTimeout(10000);
            
            // Get the response code
            int responseCode = connection.getResponseCode();
            
            // If the request was successful
            if (responseCode == 200) {
                // Read the JSON response
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                
                String jsonResponse = response.toString();
                
                // FIRST: Check if the API explicitly marked the word as invalid
                if (jsonResponse.contains("\"valid\": false") || 
                    jsonResponse.contains("\"valid\":false")) {
                    return false;
                }
                
                // SECOND: Check if the response is empty (word not found)
                if (jsonResponse.trim().equals("{}") || jsonResponse.trim().isEmpty()) {
                    return false;
                }
                
                // THIRD: Check if the word is valid and has a definition
                if ((jsonResponse.contains("\"valid\": true") || jsonResponse.contains("\"valid\":true"))) {
                    // Make sure the definition field is not empty
                    if (jsonResponse.contains("\"definition\": \"\"") || 
                        jsonResponse.contains("\"definition\":\"\"")) {
                        return false;
                    }
                    return true;
                }
                
                // If we can't determine validity from the response, assume invalid
                return false;
                
            } else {
                // Non-200 response code means the request failed
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            return false;
        } finally {
            // Clean up resources
            try {
                if (in != null) in.close();
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }

    /**
     * Prompts the user to enter the number of players
     * Validates that the number is between 2 and MAX_PLAYERS
     * 
     * @param sc Scanner for reading user input
     * @return The number of players (valid range)
     */
    private static int promptNumPlayers(Scanner sc) {
        while (true) {
            System.out.print("How many players? (2-" + MAX_PLAYERS + "): ");
            String input = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(input);
                // Check if the number is within valid range
                if (n >= 2 && n <= MAX_PLAYERS) return n;
                System.out.println("Please enter a number between 2 and " + MAX_PLAYERS + ".");
            } catch (NumberFormatException e) {
                // If input is not a number, ask again
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Prompts the word chooser to enter a secret word/phrase
     * Validates that:
     * - The input is not empty
     * - The input contains at least one letter
     * - All words in the input are valid English words (via API)
     * 
     * @param sc Scanner for reading user input
     * @param chooserName Name of the player choosing the word
     * @return The validated secret word/phrase
     */
    private static String promptSecret(Scanner sc, String chooserName) {
        while (true) {
            System.out.print(chooserName + ", enter the secret word/phrase/sentence: ");
            String secret = sc.nextLine();

            if (secret == null) secret = "";
            secret = secret.trim();

            // Check if the secret contains at least one letter
            boolean hasLetter = false;
            for (int i = 0; i < secret.length(); i++) {
                if (Character.isLetter(secret.charAt(i))) {
                    hasLetter = true;
                    break;
                }
            }

            // Reject empty input or input with no letters
            if (secret.isEmpty() || !hasLetter) {
                System.out.println("Secret must not be empty and must contain at least one letter.");
                continue;
            }

            // Validate the word(s) using the API
            System.out.println("\nValidating word(s)...");
            if (!isValidWord(secret)) {
                System.out.println("Invalid! Please enter a real English word or phrase.\n");
                continue;
            }

            // Word is valid - return it
            System.out.println("Word(s) validated successfully!\n");
            return secret;
        }
    }

    /**
     * Asks the user if they want to play another round
     * Accepts Y/y for yes, N/n for no
     * 
     * @param sc Scanner for reading user input
     * @return true if user wants to play again, false otherwise
     */
    private static boolean promptPlayAgain(Scanner sc) {
        while (true) {
            System.out.print("\nPlay again? (Y/N): ");
            String ans = sc.nextLine().trim().toUpperCase();
            if (ans.equals("Y")) return true;
            if (ans.equals("N")) return false;
            System.out.println("Please enter Y or N.");
        }
    }

    /**
     * Simulates clearing the screen by printing blank lines
     * Used to hide the secret word from other players after it's entered
     */
    private static void clearScreenFake() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    /**
     * Counts the number of letters in a string
     * Ignores spaces, punctuation, and other non-letter characters
     * 
     * @param s The string to count letters in
     * @return The number of letters
     */
    private static int countLetters(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) count++;
        }
        return count;
    }
}
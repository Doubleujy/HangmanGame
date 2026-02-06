
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HangmanAdditional {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameUI ui = new GameUI();

        ui.displayWelcomeMessage();

        boolean playAgain = true;

        while (playAgain) {
            int numPlayers = promptNumPlayers(sc);

            //get player names
            List<String> players = new ArrayList<>();
            for (int i = 1; i <= numPlayers; i++) {
                System.out.print("Enter name for Player " + i + ": ");
                String name = sc.nextLine().trim();
                if (name.isEmpty()) name = "Player " + i;
                players.add(name);
            }

            String chooserName = players.get(0);
            System.out.println("\n" + chooserName + " will choose the word/phrase/sentence.");
            String secret = promptSecret(sc, chooserName);

            //hide the word from other players
            clearScreenFake();

            //start game
            GameLogic game = new GameLogic(secret);

            System.out.println("A word/phrase has been chosen. It has " + countLetters(secret) + " letter(s).");
            System.out.println("(Spaces/punctuation are shown automatically.)");

            int guesserIndex = 1; //start guess from 2nd player

            while (!game.isGameOver()) {
                ui.displayGameState(game);

                String currentGuesser = players.get(guesserIndex);
                System.out.println("\n" + currentGuesser + ", it's your turn.");

                char guess = ui.getPlayerGuess();

                boolean valid = game.makeGuess(guess);

                if (!valid) {
                    //wrong guess
                    ui.displayInvalidGuessMessage();
                }

                //next player
                guesserIndex++;
                if (guesserIndex >= players.size()) {
                    guesserIndex = 1;
                }
            }

            ui.displayGameResult(game);

            playAgain = promptPlayAgain(sc);
            if (playAgain) {
                clearScreenFake();
            }
        }

        ui.close();
        sc.close();
        System.out.println("Thanks for playing!");
    }

    private static int promptNumPlayers(Scanner sc) {
        while (true) {
            System.out.print("How many human players? (minimum 2): ");
            String input = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(input);
                if (n >= 2) return n;
                System.out.println("Please enter a number 2 or higher.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String promptSecret(Scanner sc, String chooserName) {
        while (true) {
            System.out.print(chooserName + ", enter the secret word/phrase/sentence: ");
            String secret = sc.nextLine();

            //letter check
            if (secret == null) secret = "";
            secret = secret.trim();

            boolean hasLetter = false;
            for (int i = 0; i < secret.length(); i++) {
                if (Character.isLetter(secret.charAt(i))) {
                    hasLetter = true;
                    break;
                }
            }

            if (!secret.isEmpty() && hasLetter) {
                return secret;
            }

            System.out.println("Secret must not be empty and must contain at least one letter.");
        }
    }

    private static boolean promptPlayAgain(Scanner sc) {
        while (true) {
            System.out.print("\nPlay again? (Y/N): ");
            String ans = sc.nextLine().trim().toUpperCase();
            if (ans.equals("Y")) return true;
            if (ans.equals("N")) return false;
            System.out.println("Please enter Y or N.");
        }
    }

    private static void clearScreenFake() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    private static int countLetters(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) count++;
        }
        return count;
    }
}


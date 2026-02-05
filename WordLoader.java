/**
 * Group Number:
 * Names: Shawn, Jing Yuan, Kyros, Justin, Tyler
 * 
 * PURPOSE: This class manages the collection of words used in the Hangman game. It acts as a simple database for words.
 */

import java.util.ArrayList;
//the difference between a built-in array and an ArrayList, is that the size of an array cannot be modified (if you want to add or remove elements to/from an array, you have to create a new one). While elements can be added and removed from an ArrayList whenever you want.
import java.util.List;
import java.util.Random;

public class WordLoader {
    //instance variable to store words
    private List<String> wordList;
    //constructor
    public WordLoader() {
        wordList = new ArrayList<>();  // Initialize empty list
        initializeWords();              // Populate with default words
    }
    //private (because it's only used internally by this class) method to load initial words
    private void initializeWords() {
        //add words to the list
        //all words are stored in UPPERCASE for consistency
        //need to make it ignore caps *was taught in topic 11/12
        wordList.add("JAVA");
        wordList.add("PROGRAMMING");
        wordList.add("COMPUTER");
        wordList.add("SOFTWARE");
        wordList.add("DEVELOPMENT");
        wordList.add("NETBEANS");
        wordList.add("MAVEN");
        wordList.add("APPLICATION");
        wordList.add("HANGMAN");
        wordList.add("INTERFACE");
    }
    //public method to get a random word for the game
    public String getRandomWord() {
        //create random object to generate random index
        Random random = new Random();
        //generate random number between 0 and (list size - 1)
        int index = random.nextInt(wordList.size());
        //return
        return wordList.get(index);
    }

    }

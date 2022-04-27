import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.util.Scanner;
import javax.xml.transform.Templates;
import java.io.FileNotFoundException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
//simon feb 25 - march 25 - April 17-20 2022
public class GUI extends JFrame implements ActionListener {

    private static JPanel panel;
    private static JFrame frame;

    private static JLabel Title;
    private static JLabel stats;
    private static JTextField userText1;
    private static JLabel[] labels;

    public static Scanner s = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    static String[] possibleWords;
    static int tries;
    static char[] input;
    static long startTime;
    static char[] answer;
    static boolean done;
    static String answerChoosen;

	public static void main(String[] args) {

        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(220, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("GUI");
        frame.setLocationRelativeTo(null);
        frame.add(panel);

        panel.setLayout(null);
        Title = new JLabel("Wordle: ");
        Title.setBounds(10, 20, 80, 25);
        panel.add(Title);

        panel.setLayout(null);
        stats = new JLabel("Type a five letter word");
        stats.setBounds(10, 50, 180, 25);
        panel.add(stats);

        userText1 = new JTextField();
        userText1.addActionListener(new GUI());
        userText1.setBounds(40, 80 + (0 * 25), 80, 25);
        panel.add(userText1);

        JButton button = new JButton("Enter");
        button.setBounds(100, 20, 80, 25);
        button.addActionListener(new GUI());
        panel.add(button);

        //JLabel winScreen = new JLabel("Good Luck Have Fun Mate!");
        //winScreen.setBounds(10, 50, 350, 25);

        labels = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            labels[i] = new JLabel("<html><font size='5' color=blue> ----- </font> <font");
            labels[i].setBounds(44, 80 + (i * 25), 80, 25);
            panel.add(labels[i]);
        }

        frame.setVisible(true);

        StartWordle(); //gets the answer word, and does some other thing like starting the timer
    }

    public static void StartWordle() {
        //makes an array of the possible words (12947 lines long)
        possibleWords = new String[12947];
        try { //copied from https://replit.com/@skutschke/WordleWords#Main.java
            File myObj = new File("wordleWords.txt");
            Scanner myReader = new Scanner(myObj);
            int indexCounter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //add data to the array
                possibleWords[indexCounter] = data;
                indexCounter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        tries = 0;
        System.out.println("Wordle: Type A Five Letter Word");
        answerChoosen = ReturnRandomWord();
        answer = new char[5];
        for (int i = 0; i < 5; i++ ) answer[i] = answerChoosen.charAt(i);

        input = new char[5];
    }
    
    public static void EndWordle() {
        System.out.println("Wordle: The Answer Was: " + new String(answerChoosen));
        System.out.println("Wordle: You Found The Answer in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds and " + tries + " tries.");

        userText1.setEnabled(false);
        userText1.setVisible(false);

        if (!done) stats.setText("<html><font size='1' color=red> " + "The Answer Was: " + new String(answerChoosen) + ". You wasted \n " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds (:" + "</font> <font");
        else  stats.setText("<html><font size='1' color=green> " + "You Found The Answer in \n " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds and " + tries + " tries." + "</font> <font");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub // if the button is pressed
        EnterWord();
    }

    public static void EnterWord(){ //if its good, actually submit the word for checking
        if ( IsAValidWord(userText1.getText(), possibleWords) ) ButtonPressed();
        else System.out.println("Wordle: That is not a valid word");
    }

    public static void ButtonPressed(){
        userText1.setBounds(40, 80 + ((tries + 1) * 25), 80, 25);

        String userInput = userText1.getText();
        int[] colorOfLetters = PlayWordle(userInput);

        done = true;
        for (int i : colorOfLetters) {
            if (i != 2) done = false;
        }
        if (done || tries > 5) EndWordle();

        String[] numsToColors = new String[5];
        for (int i = 0; i < 5; i++) {
            if (colorOfLetters[i] == 0) numsToColors[i] = "black";
            else if (colorOfLetters[i] == 1) numsToColors[i] = "orange";
            else if (colorOfLetters[i] == 2) numsToColors[i] = "green";
        }

        System.out.println("Set colors to " + numsToColors[0] + " " + numsToColors[1] + " " + numsToColors[2] + " " + numsToColors[3] + " " + numsToColors[4] + " User Input was" + userInput + " answer was " + answerChoosen + " work on word is " + new String(answer));
        String finalString = (
        "<html><font size='5' color=" + numsToColors[0] + "> " + userInput.charAt(0) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[1] + "> " + userInput.charAt(1) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[2] + "> " + userInput.charAt(2) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[3] + "> " + userInput.charAt(3) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[4] + "> " + userInput.charAt(4) + "</font> <font            ");
        setNextLabel(finalString);

        userText1.setText(""); //set the text box to "" after all the logic is done
    }

    public static int[] PlayWordle(String InputWordleWord) {
        done = false;
        tries++;

        String R1 = InputWordleWord.toLowerCase();//String R1 = s.nextLine().toLowerCase();

        //check if it is 5 letters and is a possible word
        if (!IsAValidWord(R1, possibleWords)) {
            System.out.println("wasnt a good word");
        } else {
            for (int i = 0; i < 5; i++ ) { //puts the inputWord into a char[]
                input[i] = R1.charAt(i);
            }
        }
//just reset answer every time
        for (int i = 0; i < 5; i++ ) answer[i] = answerChoosen.charAt(i);
        return ReturnColorOfLeters(input, answer);
    }

    public static void setNextLabel(String string){
        labels[tries - 1].setText(string);
    }

    public static int[] ReturnColorOfLeters(char[] inputWord, char[] correctWord) {
        char[] answerTemp = correctWord;
        int[] colorForLetter = new int[5]; //0 is grey, yellow is 1, green is 2

        for (int i = 0; i < 5; i++) { //check for any correct position+letter (green)
            if (inputWord[i] == answerTemp[i]) {
                answerTemp[i] = '-';
                colorForLetter[i] = 2;
            }
        }

        for (int j = 0; j < 5; j++) { //check for any correct letter (yellow)
            for (int k = 0; k < 5; k++){
                if (inputWord[j] == answerTemp[k] && colorForLetter[j] != 2) {
                    //if that letter is not already green and matches some other letter
                    colorForLetter[j] = 1;
                    answerTemp[k] = '-';
                }
            }
        }

        for (int m = 0; m < 5; m++) {
            if (colorForLetter[m] == 0) System.out.print(inputWord[m]);
            if (colorForLetter[m] == 1) System.out.print(ANSI_YELLOW + inputWord[m] + ANSI_RESET);
            if (colorForLetter[m] == 2) System.out.print(ANSI_GREEN + inputWord[m] + ANSI_RESET);
        }

        System.out.println("");
        return colorForLetter;
    }

    public static boolean IsAValidWord(String input, String[] possibleWords) {
        if (input.length() < 5) {
            System.out.println("Wordle: The Word You Entered Was Not Long Enough");
            return false;
        }
        for (String string : possibleWords) {
            if (string.equals(input)) {
                return true;
            }
        }
        return false;
    }

    public static String ReturnRandomWord(){

        String[] answerList = new String[2315];
        try { //copied from https://replit.com/@skutschke/WordleWords#Main.java
            File myObj = new File("wordleAnswers.txt");
            Scanner myReader = new Scanner(myObj);
            int indexCounter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //add data to the array
                answerList[indexCounter] = data;
                indexCounter++;
            }
            myReader.close();   
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return answerList[(int)(Math.random() * (answerList.length - 1))]; //returns a random word from this large list
    }
}
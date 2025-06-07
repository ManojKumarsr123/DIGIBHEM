import java.util.Scanner;
import java.util.Random;

public class NumberGuessingGame {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        displayWelcomeMessage();
        
        boolean playAgain = true;
        while (playAgain) {
            playGame();
            playAgain = askToPlayAgain();
        }
        
        displayGoodbyeMessage();
        scanner.close();
    }

    private static void playGame() {
        int targetNumber = random.nextInt(MAX_RANGE - MIN_RANGE + 1) + MIN_RANGE;
        int attemptsLeft = MAX_ATTEMPTS;
        boolean hasWon = false;
        
        System.out.println("\nI've selected a number between " + MIN_RANGE + " and " + MAX_RANGE + ".");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess it. Good luck!\n");
        
        while (attemptsLeft > 0 && !hasWon) {
            System.out.print("Attempt #" + (MAX_ATTEMPTS - attemptsLeft + 1) + 
                           " (Remaining: " + attemptsLeft + ") → Enter your guess: ");
            
            int userGuess = getValidUserInput();
            attemptsLeft--;
            
            if (userGuess == targetNumber) {
                hasWon = true;
                displayWinMessage(MAX_ATTEMPTS - attemptsLeft);
            } else if (userGuess < targetNumber) {
                System.out.println("Too low! " + getTemperatureHint(userGuess, targetNumber));
            } else {
                System.out.println("Too high! " + getTemperatureHint(userGuess, targetNumber));
            }
        }
        
        if (!hasWon) {
            displayLoseMessage(targetNumber);
        }
    }

    private static int getValidUserInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input < MIN_RANGE || input > MAX_RANGE) {
                    System.out.print("Please enter a number between " + MIN_RANGE + 
                                   " and " + MAX_RANGE + ": ");
                } else {
                    return input;
                }
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    private static String getTemperatureHint(int guess, int target) {
        int difference = Math.abs(guess - target);
        if (difference > 30) return "Ice cold! ❄️";
        if (difference > 20) return "Cold! 🥶";
        if (difference > 10) return "Warm! 🌤";
        if (difference > 5) return "Hot! ♨️";
        return "Very hot! 🔥";
    }

    private static boolean askToPlayAgain() {
        System.out.print("\nWould you like to play again? (yes/no): ");
        String response = scanner.next().toLowerCase();
        while (!response.equals("yes") && !response.equals("no")) {
            System.out.print("Please enter 'yes' or 'no': ");
            response = scanner.next().toLowerCase();
        }
        return response.equals("yes");
    }

    private static void displayWelcomeMessage() {
        System.out.println("\n✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨");
        System.out.println("✨      NUMBER GUESSING GAME         ✨");
        System.out.println("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨");
        System.out.println("\nWelcome to the Number Guessing Challenge!");
        System.out.println("Try to guess the secret number between " + 
                          MIN_RANGE + " and " + MAX_RANGE + ".");
    }

    private static void displayWinMessage(int attemptsUsed) {
        System.out.println("\n🎉🎉🎉 CONGRATULATIONS! 🎉🎉🎉");
        System.out.println("You guessed the number correctly in " + attemptsUsed + " attempts!");
        
        if (attemptsUsed == 1) {
            System.out.println("AMAZING! You're a mind reader! 🔮");
        } else if (attemptsUsed <= 3) {
            System.out.println("Fantastic job! You're really good at this! 🌟");
        } else if (attemptsUsed <= 6) {
            System.out.println("Great work! You've got good intuition! 👍");
        } else {
            System.out.println("Good job! You got there in the end! 😊");
        }
    }

    private static void displayLoseMessage(int targetNumber) {
        System.out.println("\n☹️☹️☹️ GAME OVER ☹️☹️☹️");
        System.out.println("Sorry, you've used all your attempts.");
        System.out.println("The number I was thinking of was: " + targetNumber);
        System.out.println("Better luck next time!");
    }

    private static void displayGoodbyeMessage() {
        System.out.println("\nThank you for playing the Number Guessing Game!");
        System.out.println("Hope to see you again soon! 👋");
        System.out.println("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨");
    }
}
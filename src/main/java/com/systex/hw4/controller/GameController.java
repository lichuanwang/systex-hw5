package com.systex.hw4.controller;

import com.systex.hw4.model.GuessGame;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;

@Controller
@RequestMapping("/game")
public class GameController {

    @GetMapping("/index")
    public String playGame(HttpSession session, Model model) {
        // If no game is found in session, create a new GuessGame object
        if (session.getAttribute("guessGame") == null) {
            GuessGame guessGame = new GuessGame(10, 3);  // Initialize the game
            session.setAttribute("guessGame", guessGame);  // Store it in session
            model.addAttribute("message", "You have " + guessGame.getRemains() + " attempts");
        }

        // Display the play page with the form
        return "game/guess";  // "guess.jsp" will have the form for submitting guesses
    }

    @PostMapping("/play")
    public String playGame(
            @RequestParam(required = false) String guess,
            HttpSession session,
            Model model) {

        LinkedList<String> errorMsgs = new LinkedList<>();
        model.addAttribute("errors", errorMsgs); // request scope

        // Convert and Validate Data
        int guessNumber = parseGuess(guess, errorMsgs);

        if (!errorMsgs.isEmpty()) {
            return "game/guess";  // Forward to guess.jsp if there are errors
        }

        try {
            GuessGame guessGame = (GuessGame) session.getAttribute("guessGame");

            if (guessGame == null) {
                errorMsgs.add("Game not found. Please go back home and start a new game.");
                return "game/guess";  // Forward to guess.jsp if game is not found
            }

            boolean isCorrect = guessGame.guess(guessNumber);

            if (isCorrect) {
                session.removeAttribute("guessGame");  // Remove the game from session after win
                return "game/youWin";  // Forward to youWin.jsp if correct
            } else {
                int guessRemains = guessGame.getRemains();
                if (guessRemains > 0) {
                    model.addAttribute("message", "Incorrect guess. You have " + guessRemains + " attempts left.");
                    return "game/guess";  // Forward back to guess.jsp if guesses remain
                } else {
                    session.removeAttribute("guessGame");  // Remove the game from session after loss
                    model.addAttribute("message", "The correct number was: " + guessGame.getLuckyNumber());
                    return "game/youLose";  // Forward to youLose.jsp if out of guesses
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsgs.add(e.getMessage());
            return "game/guess";  // Forward back to guess.jsp on error
        }
    }

    /**
     * Parses the guess input and performs validation.
     * Handles type conversion exception and validates that the guess is within the correct range (1-10).
     */
    private int parseGuess(String guess, LinkedList<String> errorMsgs) {
        int guessNumber;
        if (guess != null && !guess.isEmpty()) {
            try {
                guessNumber = Integer.parseInt(guess);
                if (guessNumber < 1 || guessNumber > 10) {
                    errorMsgs.add("Please guess a number between 1 and 10.");
                    guessNumber = -1;
                }
            } catch (NumberFormatException e) {
                errorMsgs.add("Invalid input. Please enter a valid number.");
                guessNumber = -1;
            }
        } else {
            errorMsgs.add("Please guess a number between 1 and 10.");
            guessNumber = -1;
        }
        return guessNumber;
    }
}
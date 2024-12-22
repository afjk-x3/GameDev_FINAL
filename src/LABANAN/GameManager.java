package LABANAN;

import java.awt.Color;
import java.awt.Graphics;

import entities.Player;

public class GameManager {
    private int player1Wins = 0;
    private int player2Wins = 0;
    private int currentRound = 1;
    private final int maxRounds = 3;
    private final int winCondition = 2; // First to 2 wins ahead
    
    private Player player1;
    private Player player2;
    
    public GameManager(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
    }
    
    public void update() {
        if (currentRound > maxRounds) {
            endGame();  // End the game if 3 rounds are over
            return;
        }
        
        // Update the players' positions
        player1.update();
        player2.update();
        
        // Check if either player has died
        if (player1.getHealth() <= 0) {
            player2Wins++;
            resetRound();
        } else if (player2.getHealth() <= 0) {
            player1Wins++;
            resetRound();
        }

        // Check if any player has won
        if (player1Wins >= winCondition) {
            endGame();
        } else if (player2Wins >= winCondition) {
            endGame();
        }
    }

    private void resetRound() {
        // Reset players for the next round
        player1.resetPlayer();
        player2.resetPlayer();
        currentRound++;
    }

    
    private void endGame() {
        // Determine the winner
        if (player1Wins >= winCondition) {
            System.out.println("Player 1 wins the game!");
        } else if (player2Wins >= winCondition) {
            System.out.println("Player 2 wins the game!");
        }
        // Game over, reset everything if needed
        // Optionally restart the game or exit
    }
    
    // Render the round and score (optional)
    public void render(Graphics g) {
        // Display round and player scores
        g.setColor(Color.WHITE);
        g.drawString("Round: " + currentRound, 10, 20);
        g.drawString("Player 1 Wins: " + player1Wins, 10, 40);
        g.drawString("Player 2 Wins: " + player2Wins, 10, 60);
    }
    
    
}


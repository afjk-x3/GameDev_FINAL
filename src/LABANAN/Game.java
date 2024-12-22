package LABANAN;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import entities.Platform;
import entities.Player;
import utilz.LoadSave;

import javax.swing.*;

public class Game implements Runnable {
    private GameManager gameManager;
    private GameWindow GW;
    private GamePanel GP;
    private Player player1, player2;
    private Platform platform;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 180;
    private int round = 1;  // Current round
    private int player1Wins = 0;  // Wins for player 1
    private int player2Wins = 0;  // Wins for player 2
    private final int MAX_ROUNDS = 3;  // Total rounds per game
    
    public Game() {
        initClasses();
        GP = new GamePanel(this);
        GW = new GameWindow(GP);
        GP.requestFocus();
        // Listen for key presses to toggle hitbox debug mode
        GP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_H) {  // Press 'H' to toggle hitbox debug
                    player1.toggleDebugHitbox();
                    player2.toggleDebugHitbox();
                }
            }
        });

        startGameLoop();
    }
    private void initClasses() {
        platform = new Platform(700, 650, 500, 0, 250, 503, 288, 0, 1350, 503, 288, 0, 0, 5000, 10000, 10); 
        player1 = new Player(745, 200, LoadSave.PLAYER_ATLAS, false, platform); // Player 1 starts on the left, facing right
        player2 = new Player(1045, 200, LoadSave.PLAYER_ATLAS_2, true, platform); // Player 2 starts on the right, facing left
    }
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update() {
        player1.update();
        player2.update();
        // Check for collisions with attacks
        player1.checkAttackCollisions(player2);
        player2.checkAttackCollisions(player1);

        if (platform.isPlayerFalling(this)) {
            platform.respawnPlayer(this);
        }
        // Check if a round is over (one player wins)
        if (player1.getHealth() <= 0) {
            player2Wins++;
            resetRound();
        } else if (player2.getHealth() <= 0) {
            player1Wins++;
            resetRound();
        }
        // Check if game is over (one player reaches 2 wins)
        if (player1Wins == 2 || player2Wins == 2) {
            endGame();
        }
    }
    
    private void resetRound() {
        if (round < MAX_ROUNDS) {
            round++;  // Increment round number
            GP.endRound();
            player1.resetPlayer();  // Reset players for the next round
            player2.resetPlayer();
        }
    }
    private void endGame() {
        // Display winner and prompt for replay
        String winner = player1Wins == 2 ? "Player 1 Wins!" : "Player 2 Wins!";
        System.out.println(winner);

        // Show confirmation dialog to ask if players want to play again
        int result = JOptionPane.showConfirmDialog(null, "The game is over! " + winner + "\nDo you want to play again?", 
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Reset the game to start a new one
            resetGame();
        } else {
            // Exit the game
            System.exit(0);
        }
    }
    private void resetGame() {
        // Reset game state for a new round
        round = 1;
        player1Wins = 0;
        player2Wins = 0;
        player1.resetPlayer();
        player2.resetPlayer();
        // You could also reset other global state if needed
    }
    public void render(Graphics g) {
        player1.render(g);
        player2.render(g);
        platform.render(g);

        // Render the health bars above each player
        player1.renderHealthBar(g, 0);
        player2.renderHealthBar(g, 1);

        // Render hitboxes (for debugging)
        player1.renderHitboxes(g);
        player2.renderHitboxes(g);
        
//        player1.checkBlockCollisions(g);
//        player2.checkBlockCollisions(g);

        // Render the round and win counter
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Round: " + round + "/" + MAX_ROUNDS, 870, 80);
        g.drawString("Red: " + player1Wins, 155, 180);
        g.drawString(player2Wins + " :Blue", 1635, 180);
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();
        int frames = 0, updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0, deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            while (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                GP.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
    public void windowFocusLost() {
        player1.resetDirBooleans();
        player2.resetDirBooleans();
    }
    public Player getPlayer1() {return player1;}
    public Player getPlayer2() {return player2;}
}

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
    MainMenu mm = new MainMenu();
    
    private Player player1, player2;
    private Platform platform;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 180;
    private int round = 1;  // Current round
    private int player1Wins = 0;  // Wins for player 1
    private int player2Wins = 0;  // Wins for player 2
    private final int MAX_ROUNDS = 4;  // Total rounds per game

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
        mm.audioController.play(2);
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
       
        platform.checkForPlatformDamage(this); // Handles health reduction and respawning
        
        checkForRoundEnd(); // Check if a round ends after health depletion
    }
    
    private void checkForRoundEnd() {
        if (player1.getHealth() <= 0) {
            declareWinner(player2);
            
        } else if (player2.getHealth() <= 0) {
            declareWinner(player1);
        }

    }
    
    public void declareWinner(Player winner) {
        if (player1.getHealth() == 0) {
        	player2Wins++;
        	//GP.checkForGameOver();
            GP.showBlueWin = true;
            
            GP.isBlueWinImageVisible = true;  // Mark that the blueWin image should be visible
            GP.startBlueWinTimer();  // Start the timer for the blueWin image
            resetRound();
        } else if (player2.getHealth() == 0) {
            player1Wins++;
            GP.showRedWin = true;
            
            GP.isRedWinImageVisible = true;  // Mark that the blueWin image should be visible
            GP.startRedWinTimer();  // Start the timer for the blueWin image
            resetRound();
        }
      
    }
    
    public void resetRound() {
    	if (round < MAX_ROUNDS) {
            round++;  // Increment round number
            GP.endRound();
            player1.resetPlayer();  // Reset players for the next round
            player2.resetPlayer();
            GP.resetTimer();
            
            if (round == 1 || round == 2) {
            	mm.audioController.isPlaying();
            	
            }else if (round == 3) {
            	mm.audioController.pause();
            	mm.audioController.play(3);
            }else {
            	mm.audioController.close();
            }
        } else {
        	
            // Reset game state and scores when the game ends
            resetGame();
        }
    }

    public void resetGame() {
        // Reset game state for a new round
        round = 1;
        player1Wins = 0;
        player2Wins = 0;
        // You could also reset other global state if needed
    }
    public void render(Graphics g) {
        player1.render(g);
        player2.render(g);
        platform.render(g);

        // Render the health bars above each player
        player1.renderHealthBar(g, 0);
        player2.renderHealthBar(g, 1);


        
//        player1.checkBlockCollisions(g);
//        player2.checkBlockCollisions(g);

        // Render the round and win counter
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Round: " + round + "/" + 3, 870, 80);
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
                //System.out.println("FPS: " + frames + " | UPS: " + updates);
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
	public int getPlayer1Wins() {

		return player1Wins;
	}
	public int getPlayer2Wins() {

		return player2Wins;
	}


}

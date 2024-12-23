package LABANAN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import Inputs.KeyboardInputs;
import entities.Player;

public class GamePanel extends JPanel {

	private MainMenu mm;	
    private Game game;
    private Image bg, healthBar, obj, laban, blueWin, redWin, draw; // Images
    private Image pauseBG, overWinBlue, overWinRed, overDraw;
    public boolean showOverBlue = false, showOverRed = false, showOverDraw = false;
    private Image mainPlatform, leftPlatform, rightPlatform;
    private boolean isPaused = false; // Pause state
    private int exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight;
    private int playAgainButtonX, playAgainButtonY , playAgainButtonWidth, playAgainButtonHeight;
    private Timer blueWinTimer, redWinTimer; // Timer to control the 3-second delay for blueWin image
    public boolean isBlueWinImageVisible = false; // Flag to track if the image is visible
	public boolean isRedWinImageVisible = false;
	
    public boolean showLaban = true; // Flag for LABAN image display
    private boolean showObj = true;
	boolean showBlueWin = false, showRedWin = false;
    private Timer labanTimer; // Timer for LABAN display
    private Timer objTimer;
    private Timer gameTimer; // Main game timer
    private int secondsLeft = 60; // Game timer for 1 minute

    private boolean isGameOver = false; // Flag to check if the game is over
    private Player player1;
    private Player player2;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        loadBackgroundImage();
        loadPlatformImage();
        addKeyListener(new KeyboardInputs(this));

        objTimer = new Timer(2000, e -> startGameTimer()); // Show LABAN for 2 seconds
        objTimer.setRepeats(false); // Run once
        objTimer.start();

        // Initialize LABAN timer
        labanTimer = new Timer(2000, e -> startGameTimer()); // Show LABAN for 2 seconds
        labanTimer.setRepeats(false); // Run once
        labanTimer.start();

        // Initialize game timer
        gameTimer = new Timer(1000, e -> updateTimer());

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                	
                    togglePause();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if (isPaused) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    if (mouseX >= exitButtonX && mouseX <= exitButtonX + exitButtonWidth &&
                        mouseY >= exitButtonY && mouseY <= exitButtonY + exitButtonHeight) {
                       
//                    	mm.audioController.play(1);
                            mm = new MainMenu();
                   
                        
                        resetTimer();
                        game.resetGame();
                    }
                } else if (isGameOver) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    if (mouseX >= playAgainButtonX && mouseX <= playAgainButtonX + playAgainButtonWidth &&
                        mouseY >= playAgainButtonY && mouseY <= playAgainButtonY + playAgainButtonHeight) {
                        // Show LABAN image again on play again
                    	showLaban = true;  // Set flag to show LABAN image
                        labanTimer.start(); // Start the timer for LABAN image
                        game.resetRound();
                        
                        // Show the blue or red win image based on the last game's result
                        if (game.getPlayer1Wins() > game.getPlayer2Wins()) {
                            showRedWin = true; // Player 1 wins
                            isRedWinImageVisible = true;
                            showBlueWin = false; // Player 2 wins
                            isBlueWinImageVisible = false;
                            startRedWinTimer(); // Start blue win timer
                        } else if (game.getPlayer2Wins() > game.getPlayer1Wins()) {
                            showBlueWin = true; // Player 2 wins
                            isBlueWinImageVisible = true;
                            showRedWin = false; 
                            isRedWinImageVisible = false;
                            startBlueWinTimer(); // Start red win timer
                        }
                        // Reset the game state for a fresh start
                        resetTimer();
                        game.resetGame();
                        //resetGame();
                        startGameTimer(); // Start the game timer after LABAN is shown
                        isGameOver = false; // Reset the game over flag
                    } else if (mouseX >= exitButtonX && mouseX <= exitButtonX + exitButtonWidth &&
                               mouseY >= exitButtonY && mouseY <= exitButtonY + exitButtonHeight) {
                
                            new MainMenu();
                      
                        mm.audioController.stop();
                        resetTimer();
                        game.resetGame();
                        resetGame();
                        
                    }
                }
            }

        });
        setFocusable(true);
        requestFocusInWindow();

        // Initialize button dimensions
        exitButtonWidth = 200;
        exitButtonHeight = 50;
        playAgainButtonWidth = 200;
        playAgainButtonHeight = 50;
    }
    void startBlueWinTimer() {
        blueWinTimer = new Timer(1200, e -> { // 3000 milliseconds = 3 seconds
            showBlueWin = false; // Hide the blueWin image
            isBlueWinImageVisible = false;
            gameTimer.restart();
            gameTimer.start();   // Resume the game timer
            repaint();
        });
        blueWinTimer.setRepeats(false); // Ensure the timer runs only once
        blueWinTimer.start();  // Start the timer
    }
    void startRedWinTimer() {
        redWinTimer = new Timer(1200, e -> { // 3000 milliseconds = 3 seconds
            showRedWin = false; // Hide the blueWin image
            isRedWinImageVisible = false;
            gameTimer.restart();
            gameTimer.start();   // Resume the game timer
            repaint();
        });
        redWinTimer.setRepeats(false); // Ensure the timer runs only once
        redWinTimer.start();  // Start the timer
    }
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
        	gameTimer.stop(); // Stop timer when paused
        	mm.audioController.pause();
        } else {
        	gameTimer.start(); // Resume timer
        	mm.audioController.resume();
        }
        repaint();
    }
    //pauseBG, overWinBlue, overWinRed, overDraw;
    private void loadBackgroundImage() {
        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/BG NIGHT.png"));
            healthBar = ImageIO.read(getClass().getResourceAsStream("/HEALTH BAR.png"));
            obj = ImageIO.read(getClass().getResourceAsStream("/OBJECTIVE-sheet.png"));
            laban = ImageIO.read(getClass().getResourceAsStream("/LABAN.png"));
            blueWin = ImageIO.read(getClass().getResourceAsStream("/BLUE_WIN.png"));
            redWin = ImageIO.read(getClass().getResourceAsStream("/RED_WIN.png"));
            draw = ImageIO.read(getClass().getResourceAsStream("/DRAW.png"));
            pauseBG = ImageIO.read(getClass().getResourceAsStream("/PAUSE.png"));
            overWinBlue = ImageIO.read(getClass().getResourceAsStream("/GameOver, again, exit (BLUE).png"));
            overWinRed = ImageIO.read(getClass().getResourceAsStream("/GameOver, again, exit (RED).png"));
            overDraw = ImageIO.read(getClass().getResourceAsStream("/GameOver, again, exit (DRAW).png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading images");
        }
    }
    private void loadPlatformImage() {
        try {
            mainPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM.png"));
            leftPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM.png"));
            rightPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading platform images");
        }
    }
    private void setPanelSize() {
        Dimension size = new Dimension(1920, 1080);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
    private void startGameTimer() {
        showLaban = false; // Hide LABAN image
        labanTimer.stop(); // Stop LABAN timer
        gameTimer.start(); // Start game timer
        repaint();
    }
    private void resetGame() {
        // Reset player states
        player1.resetPlayer();  // Reset player 1
        player2.resetPlayer();  // Reset player 2

        // Reset other game-related variables
        secondsLeft = 60;  // Reset the timer to 60 seconds
        gameTimer.restart(); // Restart the game timer
        showObj = true; // Show the objective image again

        // Reset win states
        showBlueWin = false;
        showRedWin = false;
        showLaban = true;  // Show the LABAN image for the fresh start
        isGameOver = false; // Reset game over state
    }
    private void updateTimer() {
        if (secondsLeft > 0) {
            secondsLeft--;

            // Hide obj when the timer reaches 50 seconds
            if (secondsLeft == 50) {
                showObj = false;
                objTimer.stop(); // Ensure the objTimer stops
            }
        } else {
            gameTimer.stop();
            System.out.println("Time's up!");
            endRound(); // End-of-round logic
        }
        repaint();
    }
    public void resetTimer() {
        secondsLeft = 60;
        gameTimer.restart();
        repaint();
    }
    public void endRound() {
        System.out.println("Round finished!");
        resetTimer();  // Reset the timer at the end of each round
        checkForGameOver();
    }
    public void checkForGameOver() {
            
    	if (game.getPlayer1Wins() == 2 || game.getPlayer2Wins() ==2) {
    		isGameOver = true;
    		    // Display the winner and handle end-game behavior
    		    if (game.getPlayer2Wins() >= 2) {
    		    	System.out.println(game.getPlayer2Wins()+ "blue");
    		    	  showBlueWin = true;  // Player 2 wins, so show blue win image
    	                isBlueWinImageVisible = true;
    	                showOverBlue = true;
    	                showOverRed = false;
    		    } else {
    		    	System.out.println(game.getPlayer1Wins()+ "red");
                    showRedWin = true;  // Player 1 wins, so show red win image
                    isRedWinImageVisible = true;
                    showOverRed = true;
                    showOverBlue = false;
    		    }
    		}
        repaint(); // Trigger repaint to display the "Game Over" image and buttons
    }
    public boolean isGameReady() {
        return !showLaban; // Game is ready only when LABAN is not displayed
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(mainPlatform, 524, 345, 840, 500, null);
            g.drawImage(leftPlatform, 150, 206, 482, 500, null);
            g.drawImage(rightPlatform, 1250, 206, 482, 500, null);
            g.drawImage(healthBar, 46, 70, 1790, 500, null);
            if (showObj) {
                g.drawImage(obj, 93, -80, 1700, 1000, null);
            }
            // Draw LABAN if it's still displayed
            if (showLaban) {
                g.drawImage(laban, 0, 0, getWidth(), getHeight(), null);
            }
            if (showBlueWin && isBlueWinImageVisible) {
                g.drawImage(blueWin, 70, -30, getWidth() - 200, getHeight() - 200, null);
                gameTimer.stop(); // Pause the game timer while the blueWin image is visible
            } if(showRedWin && isRedWinImageVisible) {
            	g.drawImage(redWin, 70, -30, getWidth() - 200, getHeight() - 200, null);
            }
        }
        if (isPaused) {
            if (pauseBG != null) {
                g.drawImage(pauseBG, 0, 0, getWidth(), getHeight(), null); // Draw pause background
            }
            exitButtonX = getWidth() / 2  - exitButtonWidth / 2;
            exitButtonY = getHeight() / 2 + 190;
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(exitButtonX - 70 , exitButtonY, exitButtonWidth + 150 , exitButtonHeight);
        }
        else if (isGameOver) {
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent overlay
            g.fillRect(0, 0, getWidth(), getHeight());  // Fill the screen
            // Draw the appropriate "Game Over" image
            if (showOverBlue) {
                g.drawImage(overWinBlue, 0,0, getWidth(), getHeight(), null);  // Blue win image
            }else if (showOverRed) {
                g.drawImage(overWinRed, 0,0, getWidth(), getHeight(), null);  // Red win image
            }

            // Display the Play Again and Exit Game buttons
            playAgainButtonX = getWidth() / 2 - playAgainButtonWidth / 2;
            playAgainButtonY = getHeight() / 2 + 265;

            exitButtonX = getWidth() / 2 - exitButtonWidth / 2;
            exitButtonY = getHeight() / 2 + 380;
        } else {
            game.render(g);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            int seconds = secondsLeft % 60;
            String timeString = String.format("%02d", seconds);
            g.drawString(timeString, 915, 150);
        }
    }
    public Game getGame() {
        return game;
    }
}

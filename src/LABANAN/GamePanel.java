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
import javax.swing.JPanel;
import javax.swing.Timer;

import Inputs.KeyboardInputs;
import Inputs.MouseInputs;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;
    private Image bg, healthBar; // Declare the image variable for the background
    private Image mainPlatform, leftPlatform, rightPlatform;
    private boolean isPaused = false; // Pause state
    private int exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight;
    private JPanel timerPanel = new JPanel();
    
    private int secondsLeft = 60;  // Timer for 1 minute
    private Timer gameTimer;

    public GamePanel(Game game) {
        this.game = game;
        
        setPanelSize();
        loadBackgroundImage(); // Load the image here
        loadPlatformImage();
        addKeyListener(new KeyboardInputs(this));

        // Initialize the timer
        gameTimer = new Timer(1000, e -> updateTimer());
        gameTimer.start();

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
            public void mouseClicked(MouseEvent e) {
                if (isPaused) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    if (mouseX >= exitButtonX && mouseX <= exitButtonX + exitButtonWidth &&
                        mouseY >= exitButtonY && mouseY <= exitButtonY + exitButtonHeight) {
                        System.exit(0); // Exit the game
                    }
                }
            }
        });

        // Enable focus for keyboard inputs
        setFocusable(true);
        requestFocusInWindow();
        
        // Initialize button dimensions
        exitButtonWidth = 200;
        exitButtonHeight = 50;
    }

    // Method to toggle the pause state
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            System.out.println("Game Paused");
            gameTimer.stop();  // Stop the timer when paused
        } else {
            System.out.println("Game Resumed");
            gameTimer.start(); // Resume the timer
        }
        repaint();
    }

    // Method to load the background image
    private void loadBackgroundImage() {
        try {
            bg = ImageIO.read(getClass().getResourceAsStream("/BG NIGHT.png"));
            healthBar = ImageIO.read(getClass().getResourceAsStream("/HEALTH BAR.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading background image");
        }
    }

    private void loadPlatformImage() {
        try {
            mainPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM.png"));
            leftPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM.png"));
            rightPlatform = ImageIO.read(getClass().getResourceAsStream("/PLATFORM.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading background image");
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1920, 1080);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    // Method to update the timer
    private void updateTimer() {
        if (secondsLeft > 0) {
            secondsLeft--;
        } else {
            gameTimer.stop();
            System.out.println("Time's up!");
            endRound();  // Trigger end-of-round logic
        }
        repaint();  // Redraw the panel with updated timer
    }

    // Method to reset the timer
    public void resetTimer() {
        secondsLeft = 60;  // Reset to 1 minute
        gameTimer.restart();  // Restart the timer
        repaint();  // Update the display
    }

    // Method called when a round ends
    public void endRound() {
        System.out.println("Round finished!");
        // Add additional end-of-round logic here (e.g., score updates)
        resetTimer();  // Reset the timer for the next round
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(mainPlatform, 524, 345, 840, 500, null);
            g.drawImage(leftPlatform, 150, 206, 482, 500, null);
            g.drawImage(rightPlatform, 1250, 206, 482, 500, null);
            g.drawImage(healthBar, 46, 70, 1790, 500, null);
        }

        if (isPaused) {
            // Render the pause menu overlay
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Paused", getWidth() / 2 - 100, getHeight() / 2 - 100);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press ESC to Resume", getWidth() / 2 - 120, getHeight() / 2);

            // Render Exit button
            exitButtonX = getWidth() / 2 - exitButtonWidth / 2;
            exitButtonY = getHeight() / 2 + 50;
            g.setColor(Color.RED);
            g.fillRect(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Exit Game", exitButtonX + 50, exitButtonY + 30);
        } else {
            // Render the game elements
            game.render(g);

         // Render the timer
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            int seconds = secondsLeft % 60;
            String timeString = String.format("%02d", seconds);
            g.drawString(timeString, 915, 150);  // Draw the timer in the top-left corner
        }
    }

    public Game getGame() {
        return game;
    }
}

package LABANAN;
import javax.swing.*;

import Audio.AudioController;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private JFrame mainFrame;
    private Image menuBG, exitBG, playBG;
    AudioController audioController;
    public MainMenu() {
        try {
            audioController = new AudioController(
                "C:\\Users\\garaz\\Documents\\2nd Year\\1st sem\\152 - GameDev\\FINAL_PROJECT\\SFXX\\bgc.wav",
                "C:\\Users\\garaz\\Documents\\2nd Year\\1st sem\\152 - GameDev\\FINAL_PROJECT\\SFXX\\SFX\\level1.WAV",
                "C:\\Users\\garaz\\Documents\\2nd Year\\1st sem\\152 - GameDev\\FINAL_PROJECT\\SFXX\\SFX\\level2.WAV"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadMenuBackgroundImage();
        
        // Set up the main frame
        mainFrame = new JFrame("LABANAN");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainFrame.setUndecorated(true); 
        mainFrame.setLayout(new BorderLayout());

        // Add the main menu (this panel) to the frame
        mainFrame.add(this);
    
        // Add menu components
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(280, 50, -240, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Start Button
        JButton startButton = new JButton();
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);   // Remove focus outline
        startButton.setPreferredSize(new Dimension(200, 70));
        gbc.gridy = 1;
        add(startButton, gbc);

        // Exit Button
        JButton exitButton = new JButton();
        exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);   // Remove focus outline
        gbc.gridy = 2;
        exitButton.setPreferredSize(new Dimension(200, 50));
        add(exitButton, gbc);
        audioController.play(1);// Start the game

        // Add Action Listeners
        startButton.addActionListener(e -> {
            mainFrame.dispose(); // Close the main menu
            audioController.stop();
            new Game();
        });
        
        
        
        exitButton.addActionListener(e -> System.exit(0)); // Exit the application

        mainFrame.setVisible(true);
    }

    private void loadMenuBackgroundImage() {
        try {
            menuBG = ImageIO.read(getClass().getResourceAsStream("/menu.png"));
            playBG = ImageIO.read(getClass().getResourceAsStream("/MAIN_PLAY.png"));
            exitBG = ImageIO.read(getClass().getResourceAsStream("/MAIN_EXIT.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading background image");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuBG != null) {
            g.drawImage(menuBG, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(playBG, -40, -100, 2000, 1050, null);
            g.drawImage(exitBG, -40, -40, 2000, 1000, null);
        } else {
            System.out.println("Background image is missing!");
        }
    }
    

}
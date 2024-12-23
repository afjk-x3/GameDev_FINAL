package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Timer;

import LABANAN.Game;

public class Platform {
    private int x, y, width, height;
    private int leftX, leftY, leftWidth, leftHeight;
    private int rightX, rightY, rightWidth, rightHeight;
    private int fallX, fallY, fallWidth, fallHeight;
    private boolean player1Falling = false;
    private boolean player2Falling = false;
    private Timer player1Timer;
    private Timer player2Timer;
    
    // Platform damage
    private int platformDamage = 500;  // Set the damage dealt by the platform

    public Platform(int x, int y, int width, int height, int leftX, int leftY, int leftWidth, int leftHeight, 
                    int rightX, int rightY, int rightWidth, int rightHeight, 
                    int fallX, int fallY, int fallWidth, int fallHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.leftX = leftX;
        this.leftY = leftY;
        this.leftWidth = leftWidth;
        this.leftHeight = leftHeight;
        this.rightX = rightX;
        this.rightY = rightY;
        this.rightWidth = rightWidth;
        this.rightHeight = rightHeight;
        this.fallX = fallX;
        this.fallY = fallY;
        this.fallWidth = fallWidth;
        this.fallHeight = fallHeight;
    }

    public void render(Graphics g) {
        g.fillRect(x, y + 43, width, height);
        g.setColor(Color.BLUE);
        g.fillRect(leftX, leftY + 50, leftWidth, leftHeight);
        g.setColor(Color.RED);
        g.fillRect(rightX, rightY + 50, rightWidth, rightHeight);
        g.setColor(Color.YELLOW);
        g.fillRect(fallX, fallY + 10, fallWidth, fallHeight);  // Yellow platform for damage
    }

    public void checkForPlatformDamage(Game game) {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        // Check if players are touching the yellow platform and apply damage
        if (isPlayerOnYellowPlatform(player1)) {
            player1.takeDamage(platformDamage); // Deal damage to player1
        }
        
        if (isPlayerOnYellowPlatform(player2)) {
            player2.takeDamage(platformDamage); // Deal damage to player2
        }
    }

    private boolean isPlayerOnYellowPlatform(Player player) {
        return player.getHitbox().intersects(new Rectangle(fallX - 500, fallY + 10, fallWidth + 500, fallHeight));
    }
    
    // Getters for collision detection
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLeftX() { return leftX; }
    public int getLeftY() { return leftY; }
    public int getLeftWidth() { return leftWidth; }
    public int getLeftHeight() { return leftHeight; }
    public int getRightX() { return rightX; }
    public int getRightY() { return rightY; }
    public int getRightWidth() { return rightWidth; }
    public int getRightHeight() { return rightHeight; }
    public int getFallX() { return fallX; }
    public int getFallY() { return fallY; }
    public int getFallWidth() { return fallWidth; }
    public int getFallHeight() { return fallHeight; }
}

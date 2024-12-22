package entities;

import java.awt.Color;
import java.awt.Graphics;
import LABANAN.Game;

public class Platform {
    private int x, y, width, height;
    private int leftX, leftY, leftWidth, leftHeight;
    private int rightX, rightY, rightWidth, rightHeight;
    private int fallX, fallY, fallWidth, fallHeight;

    public Platform(int x, int y, int width, int height, int leftX, int leftY, int leftWidth, int leftHeight, int rightX, int rightY, int rightWidth, int rightHeight, int fallX, int fallY, int fallWidth, int fallHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height ;
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
        g.setColor(Color.red);
        g.fillRect(rightX, rightY + 50, rightWidth, rightHeight);
        g.setColor(Color.yellow);
        g.fillRect(fallX, fallY + 10, fallWidth, fallHeight);
    }
    //PLAYER1
    public boolean isPlayerFalling(Game game) {
    	Player player = game.getPlayer1();
    	Player player2 = game.getPlayer2();
    	
    	if (player.getY() > fallY + fallHeight) {
    		return true;
    	} else if(player2.getY() > fallY + fallHeight){
    		return true;
    	}else {
    	return false;
    	}
    }
    //RESPAWN p1
    public void respawnPlayer(Game game) {
    	Player player = game.getPlayer1();
    	Player player2 = game.getPlayer2();
    	
    	player.setX((x - 200) + width / 2);
    	player.setY(y - player.getHeight());
    	player2.setX((x + 100) + width / 2);
    	player2.setY(y - player2.getHeight());
    }
    // Getters for collision detection
    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width ;}
    public int getHeight() {return height ;}
    public int getLeftX() {return leftX;}
    public int getLeftY() {return leftY;}
    public int getLeftWidth() {return leftWidth ;}
    public int getLeftHeight() {return leftHeight ;}
    public int getRightX() {return rightX;}
    public int getRightY() {return rightY;}
    public int getRightWidth() {return rightWidth ;}
    public int getRightHeight() {return rightHeight ;}
    public int getFallX() {return fallX;}
    public int getFallY() {return fallY;}
    public int getFallWidth() {return fallWidth;}
    public int getFallHeight() {return fallHeight;}
}
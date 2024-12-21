package entities;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player extends Entity{
	
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15;
	private int playerAction = IDLE;
	private int playerDir = -1;
	private boolean moving = false, attacking = false, sungkit = false, launch = false, blocking = false;
	private boolean jumping = false, crouching = false, lookLeft = false, lookRight = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.07f;
	
	public Player(float x, float y) {
		super(x,y);
		loadAnimations();
	}
	
	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 150, 150, null);
	}
	
	private void loadAnimations() {
		InputStream RedSprite = getClass().getResourceAsStream("/RED_SPRITESHEET.png");
		InputStream BlueSprite = getClass().getResourceAsStream("/BLUE_SPRITESHEET.png");
		
		try {
			BufferedImage img = ImageIO.read(RedSprite);
			animations = new BufferedImage[20][12]; //[r][c]
			
			for(int j = 0; j < animations.length; j++) { //rows
				for(int i = 0; i < animations[j].length; i++) { //column
					animations[j][i] = img.getSubimage(i * 64, j * 64, 64, 64);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				RedSprite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void updateAnimationTick() {

		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmounts(playerAction)) {
				aniIndex = 0;
				attacking = false;
				sungkit = false;
				launch = false;
				jumping = false;
			}
			
		}
		
	}
	
	private void setAnimation() {
		int startAni = playerAction;
		
		if(moving) {
			playerAction = RUNNING;
		} else
			playerAction = IDLE;
		
		if(attacking) {
			playerAction = ATTACK_1;
		}
		
		if(sungkit) {
			playerAction = DOWN_ATTK;
		}
		
		if(launch) {
			playerAction = JUMP_ATTK;
		}
		
		if(blocking) {
			playerAction = DOWN_BLOCK;
		}
		
		if(jumping) {
			playerAction = JUMP;
		}
		
		if(startAni != playerAction) {
			resetAniTick();
		}
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

//	public void setDirection(int direction) {
//		this.playerDir = direction;
//		moving = true;
//	}
//	
//	public void setMoving(boolean moving) {
//		this.moving = moving;
//	}
	
	private void updatePos() {
		
		if(left && !right) {
			x -= playerSpeed;
			moving = true;
		} else if(right && !left){
			x += playerSpeed;
			moving = true;
		}else {
			moving = false;
		}
		
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	
	public void setAttack(boolean attacking) {
		this.attacking = attacking;
	}
	
	public void setCrouchAttack(boolean sungkit) {
		this.sungkit = sungkit;
	}
	
	public void setJumpAttack(boolean launch) {
		this.launch = launch;
	}
	public void setBlock(boolean blocking) {
		this.blocking = blocking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJump(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	

}

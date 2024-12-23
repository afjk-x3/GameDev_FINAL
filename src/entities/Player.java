package entities;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import utilz.LoadSave;

public class Player {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false, attacking = false, sungkit = false, launch = false, blocking = false;
    private boolean jumping = false, crouching = false;
    private boolean left, right;
    private boolean facingLeft = false; // Tracks the direction the player is facing
    private Rectangle sungkitHitbox; // Hitbox for down attack (sungkit)
    private Rectangle launchHitbox;  // Hitbox for jump attack (launch)
    private Rectangle blockHitbox;   // Hitbox for blocking
    
    private int width, height;
    private float x, y;
    private boolean debugHitbox = true;    
    private int health = 500;
    private int maxHealth;
    private int stamina = 100;
    private int attackDamage = 1;
    private int attackRange = 50; // Attack range in pixels
    private int swordDamage = 10;
    // Platform collision
    private Platform platform;
    private float gravity = 0.4f;  // Gravity force pulling the player down
    private float yVelocity = 9f;   // Vertical velocity
    private boolean isOnGround = false; // Whether the player is standing on the platform
    private float playerSpeed = 0;
    private float maxSpeed = 3.0f;
    private float acceleration = 0.2f;
    private float deceleration = 0.1f;
    private Rectangle hitbox;  // Hitbox for collision detection
    private Rectangle swordHitbox;  // Sword hitbox for attacking
    
    
    private float knockbackForce = 10.0f;  // Force applied during knockback
    private float knockbackDuration = 0.3f;  // Duration of the knockback effect in seconds
    private float knockbackTimer = 0;  // Timer to track knockback duration
    private boolean isKnockedBack = false;  // Flag to check if the player is currently knocked back
    private int knockbackDirection = 0;  // Direction of the knockback (1 = right, -1 = left)

    private boolean isFalling = false;
    private int healthReductionCounter = 0;
    
    // Spawn coordinates for respawn
    private float spawnX, spawnY;

    public Player(float x, float y, String spriteSheet, boolean facingLeft, Platform platform) {
        super();
        this.x = x;
        this.y = y;
        this.spawnX = x; // Set spawn location
        this.spawnY = y; // Set spawn location
        this.width = 50;  
        this.height = 100;
        this.facingLeft = facingLeft; // Initialize facing direction
        this.platform = platform; // Assign the platform for collision checks
        this.hitbox = new Rectangle((int) x, (int) y, width + 5, height);  // Initialize the player hitbox
        this.swordHitbox = new Rectangle();  // Sword hitbox will be updated dynamically during attacks
        this.sungkitHitbox = new Rectangle(); // Initialize sungkit hitbox
        this.launchHitbox = new Rectangle();  // Initialize launch hitbox
        this.blockHitbox = new Rectangle();  // Initialize block hitbox
        loadAnimations(spriteSheet);
    }

    public void update() {
        if (health <= 0) {
            die();  // If health reaches 0, restart the player
        } else {
            if (isKnockedBack) {
                applyKnockback();
            } else {
                updatePos();
            }
            updateSwordHitbox();  // Update sword hitbox
            updateSungkitHitbox();  // Update sungkit hitbox
            updateLaunchHitbox();   // Update launch hitbox
            updateBlockHitbox();    // Update block hitbox
            updateAnimationTick();
            setAnimation();
            hitbox.setLocation((int) x + 50, (int) y + 50);
        }
    }
    
    private void loadAnimations(String spriteSheet) {
        BufferedImage img = LoadSave.GetSpriteAtlas(spriteSheet);
        animations = new BufferedImage[20][12]; // Assuming a 20x12 sprite layout

        for (int j = 0; j < animations.length; j++) { // rows
            for (int i = 0; i < animations[j].length; i++) { // columns
                animations[j][i] = img.getSubimage(i * 64, j * 64, 64, 64); // Subimage extraction
            }
        }
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmounts(playerAction)) {
                aniIndex = 0;
                attacking = false;
                sungkit = false;
                launch = false;
                jumping = false;
            }
        }
    }
	
	 public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 150, 150, null);

	}
	 
	 private void setAnimation() {
		    int startAni = playerAction;

		    // Check for attack actions based on the facing direction
		    if (attacking) {
		        // Set attack animation depending on facing direction
		        playerAction = facingLeft ? ATTACK_1_LEFT : ATTACK_1;
		    } else if (sungkit) {
		        // Down attack based on facing direction
		        playerAction = facingLeft ? DOWN_ATTK_LEFT : DOWN_ATTK;
		    } else if (launch) {
		        // Jump attack based on facing direction
		    	playerAction = facingLeft ? JUMP_ATTK_LEFT : JUMP_ATTK;
		    } else if (blocking) {
		        // Blocking action based on facing direction
		        playerAction = facingLeft ? DOWN_BLOCK_LEFT : DOWN_BLOCK;
		    } else if (jumping) {
		        // Jump animation based on facing direction
		        playerAction = facingLeft ? JUMP_LEFT : JUMP;
		    } else if (moving) {
		        // Running animation based on facing direction
		        playerAction = facingLeft ? RUNNINGLEFT : RUNNING;
		    } else if (crouching) {
		        // Crouch animation based on facing direction
		        playerAction = facingLeft ? CROUCH_LEFT : CROUCH;
		    } else {
		        // Idle animation based on facing direction
		        playerAction = facingLeft ? IDLE_LEFT : IDLE;
		    }

		    if (startAni != playerAction) {
		        resetAniTick();
		    }
		}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}
	
	private void updatePos() {
	    // Horizontal movement (left/right)
	    if (left && !right) {
	        playerSpeed = Math.max(playerSpeed - acceleration, -maxSpeed);
	        facingLeft = true;
	    } else if (right && !left) {
	        playerSpeed = Math.min(playerSpeed + acceleration, maxSpeed);
	        facingLeft = false;
	    } else {
	        if (playerSpeed > 0) {
	            playerSpeed = Math.max(playerSpeed - deceleration, 0);
	        } else if (playerSpeed < 0) {
	            playerSpeed = Math.min(playerSpeed + deceleration, 0);
	        }
	    }

	    // Apply horizontal speed
	    x += playerSpeed;
	    moving = Math.abs(playerSpeed) > 0.1f;

	    // Launch Attack Logic (horizontal and vertical motion together)
	    if (launch) {
	        float launchSpeed = 5f;  // Horizontal movement speed during the launch

	        // Apply horizontal speed (based on direction)
	        if (facingLeft) {
	            x -= launchSpeed;  // Move to the left
	        } else {
	            x += launchSpeed;  // Move to the right
	        }

	        // Only apply a strong vertical force when the player is on the ground
	        if (!isOnGround) {
	            yVelocity = -1;  // Set a stronger upward velocity for the launch
	        }
	    }

	    // Gravity and movement when not in launch
	    if (!isOnGround) {
	        yVelocity += gravity;  // Apply gravity when in air
	    }

	    // Platform collision detection (adjust for vertical position)
	    float nextX = x;
	    float nextY = y + yVelocity; // Apply vertical movement

	    if (nextX + 64 > platform.getLeftX() && nextX < platform.getLeftX() + platform.getLeftWidth()) {
	        if (nextY + 64 > platform.getLeftY() && y + 64 <= platform.getLeftY()) {
	            nextY = platform.getLeftY() - 95;
	            yVelocity = 0;
	            isOnGround = true;
	        }
	    } else if (nextX + 64 > platform.getX() && nextX < platform.getX() + platform.getWidth()) {
	        if (nextY + 64 > platform.getY() && y + 64 <= platform.getY()) {
	            nextY = platform.getY() - 105;
	            yVelocity = 0;
	            isOnGround = true;
	        }
	    } else if (nextX + 64 > platform.getRightX() && nextX < platform.getRightX() + platform.getRightWidth()) {
	        if (nextY + 64 > platform.getRightY() && y + 64 <= platform.getRightY()) {
	            nextY = platform.getRightY() - 95;
	            yVelocity = 0;
	            isOnGround = true;
	        }
	    } else {
	        isOnGround = false; // Player is not on any platform
	    }

	    // Update position
	    x = nextX;
	    y = nextY;
	    hitbox.setLocation((int) x, (int) y);
	}

    // Update sword hitbox when attacking
	private void updateSwordHitbox() {
	    if (attacking) {
	        int swordWidth = 50;  // Width of the sword hitbox
	        int swordHeight = 10;  // Height of the sword hitbox

	        if (facingLeft) {
	            // Sword hitbox to the left of the player when facing left
	            swordHitbox.setBounds((int) x , (int) y + 80, swordWidth, swordHeight);  // Adjust position for left attack
	        } else {
	            // Sword hitbox to the right of the player when facing right
	            swordHitbox.setBounds((int) x + 100, (int) y + 80, swordWidth, swordHeight);  // Adjust position for right attack
	        }
	    } else {
	        // No sword hitbox when not attacking
	        swordHitbox.setBounds(0, 0, 0, 0);
	    }
	}
	private void updateBlockHitbox() {
	    if (blocking) {
	        int blockWidth = 50;  // Width of the block hitbox
	        int blockHeight = 30;  // Height of the block hitbox (can be adjusted for realism)

	        if (facingLeft) {
	            // Block hitbox in front of the player when facing left
	            blockHitbox.setBounds((int) x - 30, (int) y + 50, blockWidth, blockHeight);
	        } else {
	            // Block hitbox in front of the player when facing right
	            blockHitbox.setBounds((int) x + 50, (int) y + 50, blockWidth, blockHeight);
	        }
	    } else {
	        blockHitbox.setBounds(0, 0, 0, 0);  // No block hitbox when not blocking
	    }
	}

	private void updateSungkitHitbox() {
	    if (sungkit) {
	        int attackWidth = 70;  // Width of the sungkit hitbox
	        int attackHeight = 10;  // Height of the sungkit hitbox

	        if (facingLeft) {
	            // Sungkit hitbox for left-facing down attack
	            sungkitHitbox.setBounds((int) x - 30, (int) y + 110, attackWidth, attackHeight);  // Adjust for facing left
	        } else {
	            // Sungkit hitbox for right-facing down attack
	            sungkitHitbox.setBounds((int) x  + 110, (int) y + 110, attackWidth, attackHeight);  // Adjust for facing right
	        }
	    } else {
	        // No sungkit hitbox when not attacking
	        sungkitHitbox.setBounds(0, 0, 0, 0);
	    }
	}
	
	private void updateLaunchHitbox() {
	    if (launch) {
	        int attackWidth = 50;
	        int attackHeight = 15;

	        // Adjust for the launch movement
	        if (facingLeft) {
	            launchHitbox.setBounds((int) x, (int) y + 50, attackWidth, attackHeight);  // Left-facing
	        } else {
	            launchHitbox.setBounds((int) x + 100, (int) y + 50, attackWidth, attackHeight);  // Right-facing
	        }
	    } else {
	        launchHitbox.setBounds(0, 0, 0, 0);  // No hitbox when not attacking
	    }
	}

	public void checkAttackCollisions(Player otherPlayer) {
	    // Check if the other player is blocking and handle accordingly
	    if (swordHitbox.intersects(otherPlayer.getHitbox())) {
	        if (!otherPlayer.isBlocking()) {
	            otherPlayer.takeDamage(swordDamage, facingLeft ? -1 : 1);  // Apply sword damage if not blocking
	        } else {
	            // If the other player is blocking, apply knockback to the attacker
	            applyKnockbackToAttacker();
	        }
	    }

	    if (sungkitHitbox.intersects(otherPlayer.getHitbox())) {
	        if (!otherPlayer.isBlocking()) {
	            otherPlayer.takeDamage(swordDamage, facingLeft ? -1 : 1);  // Apply sungkit damage if not blocking
	        } else {
	            // If the other player is blocking, apply knockback to the attacker
	            applyKnockbackToAttacker();
	        }
	    }

	    if (launchHitbox.intersects(otherPlayer.getHitbox())) {
	        if (!otherPlayer.isBlocking()) {
	            otherPlayer.takeDamage(swordDamage, facingLeft ? -1 : 1);  // Apply launch damage if not blocking
	        } else {
	            // If the other player is blocking, apply knockback to the attacker
	            applyKnockbackToAttacker();
	        }
	    }
	}


	private void applyKnockbackToAttacker() {
	    // Apply knockback to the attacking player
	    // The knockback should be in the opposite direction of the player being attacked
	    float knockbackDirection = facingLeft ? 1 : -1;
	    this.x += knockbackForce * knockbackDirection;  // Move the player in the knockback direction
	    this.isKnockedBack = true;  // Set the knockback state
	    this.knockbackTimer = knockbackDuration;  // Start knockback timer for the attacker
	}

    public Rectangle getHitbox() {
        return hitbox;
    }
    // Getter for the sword hitbox
    public Rectangle getSwordHitbox() {
        return swordHitbox;
    }
    // Method to decrease health when the player is hit
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;  // Ensure health doesn't go below 0
    }
 // Method to render the player's health bar
    public void renderHealthBar(Graphics g, int playerIndex) {
        int barWidth = 195;
        int barHeight = 21;
        int healthBarY = 121;  // 550 is the base Y position, adjust for both players

        // Position for player 1 or player 2 at the bottom of the screen
        int healthBarX = playerIndex == 0 ? 155 : 1080;  // Adjust X for player 1 and player 2

        // Draw the background of the health bar (gray)
        g.setColor(Color.GRAY);
        g.fillRect(healthBarX, healthBarY, barWidth + 455, barHeight);

        // Draw the health (green)
        g.setColor(Color.GREEN);
        g.fillRect(healthBarX, healthBarY, (int) (barWidth * (health / 150.0)), barHeight);  // Scale health based on max value
    }
    
    public void renderHitboxes(Graphics g) {
        if (debugHitbox) {
            // Draw player hitbox (red for debugging)
            g.setColor(Color.RED);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

            // Draw sword hitbox (blue for debugging)
            g.setColor(Color.BLUE);
            g.drawRect(swordHitbox.x, swordHitbox.y, swordHitbox.width, swordHitbox.height);

            // Draw sungkit hitbox (green for debugging)
            g.setColor(Color.GREEN);
            g.drawRect(sungkitHitbox.x, sungkitHitbox.y, sungkitHitbox.width, sungkitHitbox.height);

            // Draw launch hitbox (purple for debugging)
            g.setColor(Color.MAGENTA);
            g.drawRect(launchHitbox.x, launchHitbox.y, launchHitbox.width, launchHitbox.height);

            // Draw block hitbox (yellow for debugging)
            g.setColor(Color.YELLOW);
            g.drawRect(blockHitbox.x, blockHitbox.y, blockHitbox.width, blockHitbox.height);
        }
    }

    // Set the debug flag to toggle hitbox visibility
    public void toggleDebugHitbox() {
        debugHitbox = !debugHitbox;
    }
	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	   // Method to restart player position and stats after death
    public void die() {
        resetPlayer();  // Reset the player's position and health
    }
    
    public void takeDamage(int damage, int attackerDirection) {
        // Only apply damage if the player is not blocking
        if (!blocking) {
            health -= damage;
            if (health < 0) health = 0;  // Ensure health doesn't go below 0

            // Apply knockback if the player is not already knocked back
            if (!isKnockedBack) {
                knockbackDirection = attackerDirection == 0 ? (facingLeft ? 1 : -1) : attackerDirection;  // Set direction based on attacker
                isKnockedBack = true;
                knockbackTimer = knockbackDuration;  // Start the knockback timer
            }
        }
    }

    private void applyKnockback() {
        // Apply knockback force
        x += knockbackForce * knockbackDirection;  // Move the player in the knockback direction
        
        // Decrease the knockback timer
        knockbackTimer -= 0.016f;  // Assuming 60 FPS, so 1 frame is approximately 0.016 seconds
        
        if (knockbackTimer <= 0) {
            isKnockedBack = false;  // End knockback effect
            knockbackTimer = 0;
        }
    }
    
//    public void reduceHealthGradually() {
//        // Reduce health by 100 every second (simulate with a counter)
//        healthReductionCounter++;
//        if (healthReductionCounter >= 1000) { // Assuming 60 ticks per second
//            health -= 300;
//            healthReductionCounter = 0;
//        }
//        if (health < 0) {
//            health = 0;
//        }
//    }
    
    public void respawn() {
        setX(spawnX); // Reset to initial spawn position
        setY(spawnY); // Reset to initial spawn position
        health = maxHealth; // Reset health
        isFalling = false;  // Reset falling state
        healthReductionCounter = 0; // Reset counter
    }

    
    // Reset the player's position, health, and other stats
    public void resetPlayer() {
        this.x = spawnX;  // Respawn at the saved spawnX
        this.y = spawnY + 345;  // Respawn at the saved spawnY
        this.health = 500;  // Reset health
        this.stamina = 100;  // Reset stamina
        this.attackDamage = 1;  // Reset attack damage
        this.playerSpeed = 0;  // Reset player speed
        this.isOnGround = true;  // Assume starting on ground
        this.yVelocity = 0;  // Reset vertical velocity
        this.hitbox.setLocation((int) x, (int) y);  // Reset hitbox location
    }
	public void setAttack(boolean attacking) {this.attacking = attacking;}
	public boolean isAttacking() {return attacking;}
	public void setSungkit(boolean sungkit) {this.sungkit = sungkit;}	
	public boolean isSungkit() {return sungkit;}
	public void setLaunch(boolean launch) {this.launch = launch;}
	public boolean isLaunch() {return launch;}
    public boolean isCrouch() {return crouching;}
    public void setCrouch(boolean crouching) {this.crouching = crouching;}
    public boolean isBlocking() {return blocking;}
    public void setBlock(boolean blocking) {this.blocking = blocking;}
	public boolean isLeft() {return left;}
	public void setLeft(boolean left) {this.left = left;}
	public boolean isJumping() {return jumping;}
	public void setJump(boolean jumping) {
		this.jumping = jumping;
		if (jumping && isOnGround) {
            yVelocity = -15;  // Apply an upward velocity to simulate jump
            isOnGround = false;
        }
	}
	public boolean isRight() {return right;}
	public void setRight(boolean right) {this.right = right;}
	//PLATFORM COLLSION
	public float getX() {return x;}
	public void setX(float x) {this.x = x;}
	public float getY() {return y;} 
	public void setY(float y) {this.y = y;}   
	public int getWidth() {return width;}   
	public void setWidth(int width) {this.width = width;}
	public int getHeight() {return height;}
	public void setHeight(int height) {this.height = height;}
	public int getHealth() {return health;}
	public void setHealth(int health) {this.health = health;}
    public int getMaxHealth() {return maxHealth;}
    public boolean isFalling() {return isFalling;}
    public void setFalling(boolean falling) {this.isFalling = falling; }
	
}
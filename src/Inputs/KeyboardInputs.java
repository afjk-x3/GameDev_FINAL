package Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import LABANAN.GamePanel;
import entities.Player;

public class KeyboardInputs implements KeyListener {
    private GamePanel GP;
    
    // Time when actions were last performed (in nanoseconds)
    private long lastAttackTime = 0;
    private long lastJumpTime = 0;
    private long lastBlockTime = 0;
    private long lastSungkitTime = 0;
    private long lastLaunchTime = 0;

    // Cooldown durations for each action (in nanoseconds)
    private long attackCooldown = 1000000000L;  // 1 second cooldown
    private long jumpCooldown = 500000000L;  // 0.5 second cooldown
    private long sungkitCooldown = 2000000000L; // 2 seconds cooldown
    private long launchCooldown = 2500000000L; // 2.5 seconds cooldown

    // Flags to track if an action is in progress
    private boolean isActionInProgress = false; 

    public KeyboardInputs(GamePanel GP) {
        this.GP = GP;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Player player1 = GP.getGame().getPlayer1();
        Player player2 = GP.getGame().getPlayer2();
        
        if(GP.showLaban || GP.isBlueWinImageVisible || GP.isRedWinImageVisible || isActionInProgress) {
            return; // Prevent other key presses if an action is in progress
        }

        long currentTime = System.nanoTime();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (currentTime - lastJumpTime >= jumpCooldown) {
                    player1.setJump(true);
                    lastJumpTime = currentTime;
                }
                break;
            case KeyEvent.VK_A:
                player1.setLeft(true);
                break;
            case KeyEvent.VK_S:
                player1.setCrouch(true);
                break;
            case KeyEvent.VK_D:
                player1.setRight(true);
                break;
            case KeyEvent.VK_C:
                if (currentTime - lastAttackTime >= attackCooldown) {
                    player1.setAttack(true);
                    lastAttackTime = currentTime;
                    setActionInProgress(true);  // Start the action
                }
                break;
            case KeyEvent.VK_V:
                if (currentTime - lastSungkitTime >= sungkitCooldown) {
                    player1.setSungkit(true);
                    lastSungkitTime = currentTime;
                    setActionInProgress(true);  // Start the action
                }
                break;
            case KeyEvent.VK_E:
                if (currentTime - lastLaunchTime >= launchCooldown) {
                    player1.setLaunch(true);
                    lastLaunchTime = currentTime;
                    setActionInProgress(true);  // Start the action
                }
                break;
            case KeyEvent.VK_Q:
                    player1.setBlock(true);
                break;

            // Player 2 Controls
            case KeyEvent.VK_UP:
                if (currentTime - lastJumpTime >= jumpCooldown) {
                    player2.setJump(true);
                    lastJumpTime = currentTime;
                }
                break;
            case KeyEvent.VK_LEFT:
                player2.setLeft(true);
                break;
            case KeyEvent.VK_DOWN:
                player2.setCrouch(true);
                break;
            case KeyEvent.VK_RIGHT:
                player2.setRight(true);
                break;
            case KeyEvent.VK_NUMPAD1:
                if (currentTime - lastAttackTime >= attackCooldown) {
                    player2.setAttack(true);
                    lastAttackTime = currentTime;
                    setActionInProgress(true);  // Start the action
                }
                break;
            case KeyEvent.VK_NUMPAD2:
                if (currentTime - lastSungkitTime >= sungkitCooldown) {
                    player2.setSungkit(true);
                    lastSungkitTime = currentTime;
                    setActionInProgress(true);  // Start the action
                }
                break;
            case KeyEvent.VK_NUMPAD3:
                if (currentTime - lastLaunchTime >= launchCooldown) {
                    player2.setLaunch(true);
                    lastLaunchTime = currentTime;
                    setActionInProgress(true);  // Start the action
                }
                break;
            case KeyEvent.VK_NUMPAD0:
             
                    player2.setBlock(true);
            
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Player player1 = GP.getGame().getPlayer1();
        Player player2 = GP.getGame().getPlayer2();
        
        if(GP.showLaban || GP.isBlueWinImageVisible || GP.isRedWinImageVisible) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player1.setLeft(false);
                break;
            case KeyEvent.VK_S:
                player1.setCrouch(false);
                break;
            case KeyEvent.VK_D:
                player1.setRight(false);
                break;
            case KeyEvent.VK_Q:
                player1.setBlock(false);
                break;
            // Player 2 Controls
            case KeyEvent.VK_LEFT:
                player2.setLeft(false);
                break;
            case KeyEvent.VK_DOWN:
                player2.setCrouch(false);
                break;
            case KeyEvent.VK_RIGHT:
                player2.setRight(false);
                break;
            case KeyEvent.VK_NUMPAD0:
                player2.setBlock(false);
                break;
        }
    }

    private void setActionInProgress(boolean inProgress) {
        this.isActionInProgress = inProgress;
        // You can reset the flag to false when the action is done (e.g., via a timer or after the action completes)
        // Example: Use a Timer to reset it after a delay
        if (inProgress) {
            // Reset after a specific delay
            new Thread(() -> {
                try {
                    Thread.sleep(1000);  // Set the delay for the action to complete
                    isActionInProgress = false;  // Reset flag after action completion
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }
}

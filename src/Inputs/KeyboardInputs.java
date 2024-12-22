package Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import LABANAN.GamePanel;
import entities.Player;

public class KeyboardInputs implements KeyListener {
    private GamePanel GP;
    private long lastAttackTime = 0;
    private long lastJumpTime = 0;
    private long lastBlockTime = 0;
    private long attackCooldown = 1000000000L;  // 1 second cooldown
    private long jumpCooldown = 500000000L;  // 0.5 second cooldown
    private long blockCooldown = 1500000000L;  // 1.5 second cooldown

    public KeyboardInputs(GamePanel GP) {this.GP = GP;}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        Player player1 = GP.getGame().getPlayer1();
        Player player2 = GP.getGame().getPlayer2();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player1.setJump(true);
            case KeyEvent.VK_A -> player1.setLeft(true);
            case KeyEvent.VK_S -> player1.setCrouch(true);
            case KeyEvent.VK_D -> player1.setRight(true);
            case KeyEvent.VK_C -> player1.setAttack(true);
            case KeyEvent.VK_V -> player1.setSungkit(true);
            case KeyEvent.VK_E -> player1.setLaunch(true);
            case KeyEvent.VK_Q -> player1.setBlock(true);

            // Player 2 Controls
            case KeyEvent.VK_UP -> player2.setJump(true);
            case KeyEvent.VK_LEFT -> player2.setLeft(true);
            case KeyEvent.VK_DOWN -> player2.setCrouch(true);
            case KeyEvent.VK_RIGHT -> player2.setRight(true);
            case KeyEvent.VK_NUMPAD1 -> player2.setAttack(true);
            case KeyEvent.VK_NUMPAD2 -> player2.setSungkit(true);
            case KeyEvent.VK_NUMPAD3 -> player2.setLaunch(true);
            case KeyEvent.VK_NUMPAD0 -> player2.setBlock(true);

            // New round trigger (for testing)
            //case KeyEvent.VK_R -> GP.getGame().startNewRound();  // Press 'R' to start a new round
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        Player player1 = GP.getGame().getPlayer1();
        Player player2 = GP.getGame().getPlayer2();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player1.setLeft(false);
            case KeyEvent.VK_S -> player1.setCrouch(false);
            case KeyEvent.VK_D -> player1.setRight(false);
            case KeyEvent.VK_Q -> player1.setBlock(false);
            // Player 2 Controls
            case KeyEvent.VK_LEFT -> player2.setLeft(false);
            case KeyEvent.VK_DOWN -> player2.setCrouch(false);
            case KeyEvent.VK_RIGHT -> player2.setRight(false);
            case KeyEvent.VK_NUMPAD0 -> player2.setBlock(false);
        }
    }
}

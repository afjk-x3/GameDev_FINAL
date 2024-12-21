package LABANAN;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import Inputs.KeyboardInputs;
import Inputs.MouseInputs;

public class GamePanel extends JPanel{
	
	private MouseInputs mouseInputs;
	private Game game;
	
	public GamePanel(Game game){
		this.game = game;
		
		mouseInputs = new MouseInputs(this);
		
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(1920, 1080);
		setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
	

}

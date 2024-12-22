package LABANAN;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow{
	
	private JFrame frame;
	
	public GameWindow(GamePanel GP) {
	    frame = new JFrame("LABANAN");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(GP);
	    frame.setResizable(true);
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setUndecorated(true); 
	    frame.pack();
	    
	    frame.addWindowFocusListener(new WindowFocusListener(){
	        @Override
	        public void windowGainedFocus(WindowEvent e) {
	            GP.getGame().windowFocusLost();
	        }
	        @Override
	        public void windowLostFocus(WindowEvent e) {
	            GP.getGame().windowFocusLost();
	        }
	    });
	    frame.setVisible(true);
	}
}
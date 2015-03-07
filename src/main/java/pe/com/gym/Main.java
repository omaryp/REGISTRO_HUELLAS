package pe.com.gym;

import javax.swing.UIManager;

import pe.com.gym.frm.RegistroHuellas;

/**
 * Enterprise Application Client main class.
 * 
 */
public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new RegistroHuellas();
	}
}

package survey;

import javax.swing.JOptionPane;

/**
 * 内部类时钟demo
 * 
 * @author sniper
 * 
 */
public class InnerClassTest {

	public static void main(String[] args) {
		TalkingClock clock = new TalkingClock(1000, true);
		clock.start();

		JOptionPane.showMessageDialog(null, "QUIT program?");
		System.exit(0);
	}
}

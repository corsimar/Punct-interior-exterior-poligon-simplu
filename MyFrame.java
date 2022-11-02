import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	
	public MyPanel panel;
	
	MyFrame() {
		panel = new MyPanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

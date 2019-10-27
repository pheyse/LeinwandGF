package de.bright_side.lgf.pc.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LPcMessagesWindow extends JFrame{
	private static final long serialVersionUID = 7686234190190299553L;
	private JTextArea mainTextArea = new JTextArea("");
	
	public LPcMessagesWindow() {
		setTitle("Ship Nav Messages");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(mainTextArea));
		mainTextArea.setEditable(false);
	}
	
	public void showWindow() {
		if (!isVisible()) {
			setVisible(true);
		}
	}
	
	public void addText(String text) {
		mainTextArea.setText(mainTextArea.getText() + text);
		mainTextArea.setCaretPosition(mainTextArea.getText().length());
	}
	
	public static void main(String[] args) {
		LPcMessagesWindow window = new LPcMessagesWindow();
		window.addText("Hello!\n");
		window.addText("Nice!\n");
		window.showWindow();
	}
}

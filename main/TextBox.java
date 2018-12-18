package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * @author Steven Lawrence
 */

/**
 * @author Steven Lawrence
 * A pop up text box that allows a user to write
 */
public class TextBox extends JFrame{
	private static final long serialVersionUID = 1L;
	JTextField jt = new JTextField();
	Font f = new Font("Helvetica", Font.BOLD, 32);
	public TextBox(Point p) {
		setSize(200,200);
		this.setLocation(p);
		jt = new JTextField();
		jt.setFont(f);
		Border jtBorder = BorderFactory.createLineBorder(Color.blue);
		jt.setBorder(jtBorder);
		this.add(jt);
		setAlwaysOnTop(true);
		pack();
		setVisible(true);
	}
	
	public String getText() {
		return jt.getText();
	}
}

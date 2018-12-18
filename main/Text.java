package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

/**
 * @author Steven Lawrence
 */
public class Text {
	Color c;
	Font f;
	String txt;
	Point p;
	
	/**
	 * @param text the characters and writing
	 * @param f the font
	 * @param c the color
	 * @param p the position
	 */
	public Text(String text, Font f, Color c, Point p) {
		txt = text;
		this.f = f;
		this.c = c;
		this.p = p;
	}
	
	public Color getDrawColor() {
		return c;
	}
	
	public String toString() {
		return String.format("%s,%s,%s,%d,%d,%d,%d,%d%n", "text", txt, f.getName(), f.getStyle(), f.getSize(), p.x, p.y, c.getRGB());
	}
}

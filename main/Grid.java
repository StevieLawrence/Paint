package main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Scale set to 2 gridlines per meter (100 pixels per meter) 400 pixels is 2
 * meters, T is 2.83 seconds
 * 
 * @author tompkinsj
 *
 */
public class Grid {

	private int w;
	private int h;
	private int x;
	private int y;

	/**
	 * Constructor for the grid class accepting
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public Grid(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}

	public void display(Graphics2D g2) {
		g2.setColor(new Color(0xf0f8ff)); // aliceblue
		g2.setFont(new Font("Serif", Font.PLAIN, 14));
		for (int x1 = x; x1 <= w; x1 += 10) {
			g2.drawLine(x1, 0, x1, h);
			if (x1 % 50 == 0 && x1 > 0) {
				g2.setColor(Color.black);
				g2.drawString(String.format("%3d", x1), x1, 15);
				g2.setColor(new Color(0xf0f8ff));
			}
		}
		for (int y1 = y; y1 <= h; y1 += 10) {
			g2.drawLine(0, y1, w, y1);
			if (y1 % 50 == 0) {
				g2.setColor(Color.black);
				g2.drawString(String.format("%3d", y1), 5, y1);
				g2.setColor(new Color(0xf0f8ff));
			}
		}

	}

}

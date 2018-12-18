package Shapes;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Line extends java.awt.geom.Line2D.Double {
	private static final long serialVersionUID = 1L;
	Color c;
	private Point direction;
	
	/**
	 * @param x1 one of the line's end point's x coordinate
	 * @param y1 one of the line's end point's y coordinate
	 * @param x2 the line's other end point's x coordinate
	 * @param y2 line's other end point's y coordinate
	 * @param c color of the line
	 */
	public Line(int x1, int y1, int x2, int y2, Color c) {
		super(x1, y1, x2, y2);
		this.c = c;
		Random rand = new Random();
		int xp = 0; int yp = 0;
		if (rand.nextBoolean()) {
			xp = 1;
		} else {
			xp = -1;
		}
		if (rand.nextBoolean()) {
			yp = 1;
		} else {
			yp = -1;
		}
		direction = new Point(xp, yp);
	}

	public Color getDrawColor() {
		return c;
	}
	
	public void moveTo(int x1, int y1, int x2, int y2) {
		this.setLine(x1, y1, x2, y2);
	}
	
	public void changeXDir() {
		direction.x = direction.x * -1;
	}
	
	public void changeYDir() {
		direction.y = direction.y * -1;
	}
	
	public Point getDir() {
		return direction;
	}
	
	public String toString() {
		return String.format("%d,%d,%d,%d,%d%n",(int) x1, (int) y1, (int) x2, (int) y2, c.getRGB());
	}
}
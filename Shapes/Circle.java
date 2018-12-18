package Shapes;

/**
 * @author Steven Lawrence
 */

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Circle extends java.awt.geom.Ellipse2D.Double {
	private static final long serialVersionUID = 1L;
	private Color c;
	public Boolean fill = false;
	private Point direction;
	
	/**
	 * A circle with color, and direction. This constructor makes a circle with no filling
	 * @param x x-coordinate of top left corner of the bounding rectangle
	 * @param y y-coordinate of to left corner of the bounding rectangle
	 * @param w width of bounding rectangle
	 * @param h height of bounding rectangle
	 * @param c Color of the rectangle
	 */
	public Circle(int x, int y, int w, int h, Color c) {
		super(x, y, w, h);
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
	
	/**
	 * A Circle with a filling
	 */
	public Circle(int x, int y, int w, int h, Color c, Boolean fill) {
		this(x, y, w, h, c);
		this.fill = fill;
	}

	public Color getDrawColor() {
		return c;
	}
	
	public void moveTo(int x, int y) {
		this.setFrame(x, y, super.getWidth(), super.getHeight());
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
	
	public void setDir(Point direction) {
		this.direction = direction;
	}
	
	public String toString() {
		return String.format("%s,%d,%d,%d,%d,%d%n", (fill ? "filledCircle":"circle"), (int) super.getX(), (int) super.getY(), (int) super.getWidth(), (int) super.getHeight(), (int) c.getRGB());
	}
	
}

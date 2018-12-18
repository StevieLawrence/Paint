package Shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Rectangle extends Rectangle2D.Double{
	private static final long serialVersionUID = 1L;
	private Color c;
	public Boolean fill = false;
	private Point direction;
	
	/**
	 * Rectangle with color and direction
	 * @param x x coordinate of the top left corner of the rectangle
	 * @param y y coordinate of the top left corner of the rectangle
	 * @param w width of the rectangle
	 * @param h height of the rectangle
	 * @param c color of the rectangle
	 */
	public Rectangle(int x, int y, int w, int h, Color c) {
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
	 * Constructor for a filled rectangle
	 */
	public Rectangle(int x1, int y1, int w, int h, Color c, Boolean fill) {
		this(x1, y1, w, h, c);
		this.fill = fill;
	}

	public Color getDrawColor() {
		return c;
	}
	
	/**
	 * moves the rectangle to a new location
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void moveTo(int x, int y) {
		this.setRect(x, y, super.getWidth(), super.getHeight());
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
		return String.format("%s,%d,%d,%d,%d,%d%n", (fill ? "filledRectangle":"rectangle"), (int) super.getX(), (int) super.getY(), (int) super.getWidth(), (int) super.getHeight(), (int) c.getRGB());
	}
}

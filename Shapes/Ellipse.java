package Shapes;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Ellipse extends java.awt.geom.Ellipse2D.Double {
	private static final long serialVersionUID = 1L;
	private Color c;
	public Boolean fill = false;
	private Point direction;
	
	/**
	 * An oval that is described by a bounding rectangle that has a direction, and color.
	 * @param x x coordinate of the bounding rectangle's top left corner
	 * @param y y coordinate of the bounding rectangle's top left corner
	 * @param w width of the bounding rectangle
	 * @param h height of the bounding rectangle
	 * @param c color of the ellipse
	 */
	public Ellipse(int x, int y, int w, int h, Color c) {
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
	 * Ellipse with a filling
	 */
	public Ellipse(int x1, int y1, int w, int h, Color c, Boolean fill) {
		this(x1, y1, w, h, c);
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
	
	public String toString() {
		return String.format("%s,%d,%d,%d,%d,%d%n", (fill ? "filledEllipse":"ellipse"), (int) super.getX(), (int) super.getY(), (int) super.getWidth(), (int) super.getHeight(), (int) c.getRGB());
	}
}
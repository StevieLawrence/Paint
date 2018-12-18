package Shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;


public class RoundRectangle extends RoundRectangle2D.Double{
	private static final long serialVersionUID = 1L;
	private Color c;
	public Boolean fill = false;
	private Point direction;
	
	/**
	 * Round rectangles have rounded edges described by an arc
	 * @param x x coordinate of the top left bounding rectangle
	 * @param y y coordinate of the top left bounding rectangle
	 * @param w width of bounding rectangle
	 * @param h height of bounding rectangle
	 * @param arcW width of the rounded edge arc
	 * @param arcH height of the rounded edge arc
	 * @param c color of the rounded rectangle
	 */
	public RoundRectangle(int x, int y, int w, int h, int arcW, int arcH, Color c) {
		super(x, y, w, h, arcW, arcH);
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
	 * Rounded rectangle with a filling 
	 */
	public RoundRectangle(int x1, int y1, int w, int h, int arcW, int arcH, Color c, Boolean fill) {
		this(x1, y1, w, h, arcW, arcH, c);
		this.fill = fill;
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

	public Color getDrawColor() {
		return c;
	}
	
	/**
	 * move the rounded rectangle to a new coordinate
	 */
	public void moveTo(int x, int y) {
		this.setRoundRect(x, y, super.getWidth(), super.getHeight(), super.getWidth(), super.getArcHeight());
	}
	
	public String toString() {
		return String.format("%s,%d,%d,%d,%d,%d,%d,%d%n", (fill ? "filledRoundRectangle":"roundRectangle"), (int) super.getX(), (int) super.getY(), (int) super.getWidth(), (int) super.getHeight(),(int) super.getArcWidth(), (int) super.getArcHeight(), (int) c.getRGB());
	}
}

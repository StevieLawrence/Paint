package Shapes;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.awt.Point;


public class Polygon2D extends Polygon {
	private static final long serialVersionUID = 1L;
	Color c;
	java.awt.Polygon p;
	
	/**
	 * A polygon is a plane shape with straight sides.
	 * @param polyPoints all of the points in the polygon
	 * @param c the color of the polygon
	 */
	public Polygon2D(ArrayList<Point> polyPoints, Color c) {
		super();
		this.c = c;
		this.xpoints = new int[polyPoints.size()];
		this.ypoints = new int[polyPoints.size()];
		for (Point p : polyPoints) {
			this.addPoint(p.x, p.y);
		}
	}
	
	public Color getDrawColor() {
		return c;
	}
	
	public String toString() {
		String s = "Polygon";
		for (int x : this.xpoints) {
			s += "," + Integer.toString(x);
		}
		s += ",|";
		for (int y : this.ypoints) {
			s += "," + Integer.toString(y);
		}
		s += ",|,";
		s += c.getRGB();
		return s;
	}
}

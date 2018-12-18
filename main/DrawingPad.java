package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;
import Shapes.Circle;
import Shapes.Ellipse;
import Shapes.Line;
import Shapes.Polygon2D;
import Shapes.Rectangle;
import Shapes.RoundRectangle;

/**
 * @author Steven Lawrence The drawing surface for the paint program that allows
 *         the user to hand colored shapes, texts, and images.
 */
public class DrawingPad extends Canvas {
	private ArrayList<Shape> shapes;
	private ArrayList<Shape> loadedShapes;
	private HashMap<BufferedImage, Point> images;
	private ArrayList<Text> text;
	private ArrayList<Text> loadedText;
	private Grid grid;
	private Boolean gridOn;
	private Histogram histogram;
	private int w, h;

	public DrawingPad(int w, int h) {
		setBackground(Color.white);
		setPreferredSize(new Dimension(w, h)); // set size
		grid = new Grid(0, 0, w, h);
		gridOn = false;
		this.w = w;
		this.h = h;
	}

	public DrawingPad(int w, int h, ArrayList<Shape> shapes) {
		this(w, h);
		this.shapes = shapes;
	}

	public DrawingPad(int w, int h, ArrayList<Shape> shapes, HashMap<BufferedImage, Point> images) {
		this(w, h, shapes);
		this.images = images;
	}

	/**
	 * Constructs the white drawing pad to draw on with
	 * 
	 * @param w
	 *            the width
	 * @param h
	 *            the height
	 * @param shapes
	 *            the shapes container holding all of the shapes on the pad
	 * @param images
	 *            the images container holding all of the shape on the pad
	 * @param text
	 *            the text container holding all of the text on the
	 */
	public DrawingPad(int w, int h, ArrayList<Shape> shapes, HashMap<BufferedImage, Point> images,
			ArrayList<Text> texts) {
		this(w, h, shapes, images);
		this.text = texts;
	}

	/**
	 * Adjusts the size of the drawingpad canvas (including the grid)
	 * 
	 * @param w
	 *            the new width
	 * @param h
	 *            the new height
	 */
	public void adjustSize(int w, int h) {
		this.w = w;
		this.h = h;
		setSize(new Dimension(w, h));
		grid = new Grid(0, 0, w, h);
	}

	/**
	 * Adds a shape or a text object to its appropriate list from the appropriate
	 * string data
	 * 
	 * @param data
	 *            an array of string data
	 * @param shapeList
	 *            an array list of shape objects
	 * @param strings
	 *            an array list of text objects
	 */
	private void addObject(String[] data, ArrayList<Shape> shapeList, ArrayList<Text> strings) {
		int x1 = -1, x2 = -1, y1 = -1, y2 = -1, w = -1, h = -1, arcW = -1, arcH = -1;
		Color color = null;
		if (data.length == 5) {
			x1 = Integer.parseInt(data[0].trim());
			y1 = Integer.parseInt(data[1].trim());
			x2 = Integer.parseInt(data[2].trim());
			y2 = Integer.parseInt(data[3].trim());
			color = new Color(Integer.parseInt(data[4].trim()));
			shapeList.add(new Line(x1, y1, x2, y2, color)); // add new Line object
		} else if (data[0].equals("circle")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			color = new Color(Integer.parseInt(data[5].trim()));
			shapeList.add(new Circle(x1, y1, w, h, color)); // add new Circle object
		} else if (data[0].equals("filledCircle")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			color = new Color(Integer.parseInt(data[5].trim()));
			shapeList.add(new Circle(x1, y1, w, h, color, true)); // add new Circle object w/ fill = true
		} else if (data[0].equals("rectangle")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			color = new Color(Integer.parseInt(data[5].trim()));
			shapeList.add(new Rectangle(x1, y1, w, h, color));
		} else if (data[0].equals("filledRectangle")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			color = new Color(Integer.parseInt(data[5].trim()));
			shapes.add(new Rectangle(x1, y1, w, h, color, true));
		} else if (data[0].equals("ellipse")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			color = new Color(Integer.parseInt(data[5].trim()));
			shapeList.add(new Ellipse(x1, y1, w, h, color)); // add new Ellipse object
		} else if (data[0].equals("filledEllipse")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			color = new Color(Integer.parseInt(data[5].trim()));
			shapeList.add(new Ellipse(x1, y1, w, h, color, true)); // add new Ellipse object w/ fill = true
		} else if (data[0].equals("roundRectangle")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			arcW = Integer.parseInt(data[5].trim());
			arcH = Integer.parseInt(data[6].trim());
			color = new Color(Integer.parseInt(data[7].trim()));
			shapeList.add(new RoundRectangle(x1, y1, w, h, arcW, arcH, color));
		} else if (data[0].equals("filledRoundRectangle")) {
			x1 = Integer.parseInt(data[1].trim());
			y1 = Integer.parseInt(data[2].trim());
			w = Integer.parseInt(data[3].trim());
			h = Integer.parseInt(data[4].trim());
			arcW = Integer.parseInt(data[5].trim());
			arcH = Integer.parseInt(data[6].trim());
			color = new Color(Integer.parseInt(data[7].trim()));
			shapeList.add(new RoundRectangle(x1, y1, w, h, arcW, arcH, color, true));
		} else if (data[0].equals("Polygon")) {
			ArrayList<Integer> xList = new ArrayList<Integer>();
			ArrayList<Integer> yList = new ArrayList<Integer>();
			ArrayList<Point> pList = new ArrayList<Point>();
			int i = 1;
			while (!data[i].equals("|")) {
				xList.add(Integer.parseInt(data[i]));
				i++;
			}
			i++;
			while (!data[i].equals("|")) {
				yList.add(Integer.parseInt(data[i]));
			}
			for (int k = 0; k < xList.size(); k++) {
				pList.add(new Point(xList.get(k), yList.get(k)));
			}
			color = new Color(Integer.parseInt(data[data.length - 1]));
			shapeList.add(new Polygon2D(pList, color));
		} else if (data[0].equals("text")) {
			Font f = new Font(data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]));
			Point pt = new Point(Integer.parseInt(data[5]), Integer.parseInt(data[6]));
			Color c = new Color(Integer.parseInt(data[7]));
			strings.add(new Text(data[1], f, c, pt));
		}
	}

	/**
	 * load a painting based on a filename
	 * 
	 * @param filename
	 *            a string representing the name of the file
	 * @return the name of the file or if the file couldn't be opened
	 */
	public String loadPainting(String filename) {
		File infile;
		Scanner scan = null;

		if (filename == null) {
			// Open File with JFileChooser in current directory
			JFileChooser jfc = new JFileChooser(".");
			jfc.showOpenDialog(null);
			infile = jfc.getSelectedFile();
		} else {
			infile = new File(filename);
		}
		try {
			scan = new Scanner(infile);
		} catch (FileNotFoundException | NullPointerException n) {
			scan = new Scanner("circle, 275, 500, 30, 30, -65536\r\n" + "filledCircle, 325, 500, 40, 40, -256\r\n"
					+ "circle, 375, 500, 47, 47, -16711936\r\n" + "filledCircle, 425, 500, 48, 48, -16776961\r\n"
					+ "circle, 475, 500, 47, 47, -256\r\n" + "filledCircle, 525, 500, 48, 48, -65536\r\n"
					+ "387, 264, 587, 263, -13421569");
			filename = "no file found - running short demo string";
		}
		if (shapes == null) {
			shapes = new ArrayList<Shape>();
		}

		// construct a shapes/Text arrayList, don't construct a new instance of shapes/Text to
		// keep the connection between the panel shapes/text lists
		loadedShapes = new ArrayList<Shape>();
		loadedText = new ArrayList<Text>();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(",");
			addObject(data, loadedShapes, loadedText);
		}
		// add all of the loaded elements to there respective holders
		text.addAll(loadedText);
		shapes.addAll(loadedShapes);
		scan.close();
		return filename;
	}

	public void loadPainting() {
		loadPainting(null);
	}

	public void toggleGrid() {
		gridOn = !gridOn;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (images != null) {
			for (BufferedImage bi : images.keySet()) {
				g2.drawImage(bi, images.get(bi).x, images.get(bi).y, null);
			}
		}

		for (Shape s : shapes) { // iterate over the ArrayList to set the color and draw the shapes
			if (s instanceof Line) {
				Line l = (Line) s;
				g2.setColor(l.getDrawColor());
				g2.draw(l);
			} else if (s instanceof Ellipse) {
				Ellipse e = (Ellipse) s;
				g2.setColor(e.getDrawColor());
				if (e.fill)
					g2.fill(e);
				else
					g2.draw(e);
			} else if (s instanceof Rectangle) {
				Rectangle r = (Rectangle) s;
				g2.setColor(r.getDrawColor());
				if (r.fill)
					g2.fill(r);
				else
					g2.draw(r);
			} else if (s instanceof Circle) {
				Circle c = (Circle) s;
				g2.setColor(c.getDrawColor());
				if (c.fill)
					g2.fill(c);
				else
					g2.draw(c);
			} else if (s instanceof RoundRectangle) {
				RoundRectangle rr = (RoundRectangle) s;
				g2.setColor(rr.getDrawColor());
				if (rr.fill)
					g2.fill(rr);
				else
					g2.draw(rr);
			} else if (s instanceof Polygon2D) {
				Polygon2D p2D = (Polygon2D) s;
				System.out.println(p2D);
				g2.setColor(p2D.getDrawColor());
				for (int i = 0; i < p2D.xpoints.length - 1; i++) {
					System.out.println(p2D.xpoints[i] + " " + p2D.ypoints[i] + " " + p2D.xpoints[i + 1] + " "
							+ p2D.ypoints[i + 1]);
					g2.drawLine(p2D.xpoints[i], p2D.ypoints[i], p2D.xpoints[i + 1], p2D.ypoints[i + 1]);
				}
				g2.drawLine(p2D.xpoints[0], p2D.ypoints[0], p2D.xpoints[p2D.xpoints.length - 1],
						p2D.ypoints[p2D.ypoints.length - 1]);
			}
		}

		for (Text t : text) {
			g2.setColor(t.getDrawColor());
			g2.setFont(t.f);
			g2.drawString(t.txt, t.p.x, t.p.y);
		}
		if (gridOn) {
			grid.display(g2);
		}

	}
	
	/**
	 * Repaints the canvas to make sure it is updated, and then draws it to a blank buffered image of the same size. 
	 * Does not draw the grid
	 * @return Return the drawing as a buffered image
	 */
	public BufferedImage getImg() {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		repaint();
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, w, h);

		if (images != null) {
			for (BufferedImage bi : images.keySet()) {
				g2.drawImage(bi, images.get(bi).x, images.get(bi).y, null);
			}
		}

		for (Shape s : shapes) { // iterate over the ArrayList to set the color and draw the shapes
			if (s instanceof Line) {
				Line l = (Line) s;
				g2.setColor(l.getDrawColor());
				g2.draw(l);
			} else if (s instanceof Ellipse) {
				Ellipse e = (Ellipse) s;
				g2.setColor(e.getDrawColor());
				if (e.fill)
					g2.fill(e);
				else
					g2.draw(e);
			} else if (s instanceof Rectangle) {
				Rectangle r = (Rectangle) s;
				g2.setColor(r.getDrawColor());
				if (r.fill)
					g2.fill(r);
				else
					g2.draw(r);
			} else if (s instanceof Circle) {
				Circle c = (Circle) s;
				g2.setColor(c.getDrawColor());
				if (c.fill)
					g2.fill(c);
				else
					g2.draw(c);
			} else if (s instanceof RoundRectangle) {
				RoundRectangle rr = (RoundRectangle) s;
				g2.setColor(rr.getDrawColor());
				if (rr.fill)
					g2.fill(rr);
				else
					g2.draw(rr);
			} else if (s instanceof Polygon2D) {
				Polygon2D p2D = (Polygon2D) s;
				System.out.println(p2D);
				g2.setColor(p2D.getDrawColor());
				for (int i = 0; i < p2D.xpoints.length - 1; i++) {
					System.out.println(p2D.xpoints[i] + " " + p2D.ypoints[i] + " " + p2D.xpoints[i + 1] + " "
							+ p2D.ypoints[i + 1]);
					g2.drawLine(p2D.xpoints[i], p2D.ypoints[i], p2D.xpoints[i + 1], p2D.ypoints[i + 1]);
				}
				g2.drawLine(p2D.xpoints[0], p2D.ypoints[0], p2D.xpoints[p2D.xpoints.length - 1],
						p2D.ypoints[p2D.ypoints.length - 1]);
			}
		}

		for (Text t : text) {
			g2.setColor(t.getDrawColor());
			g2.setFont(t.f);
			g2.drawString(t.txt, t.p.x, t.p.y);
		}

		g2.drawImage(image, 0, 0, null);
		return image;
	}
}

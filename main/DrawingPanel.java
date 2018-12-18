package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import Shapes.Circle;
import Shapes.Ellipse;
import Shapes.Line;
import Shapes.Rectangle;
import Shapes.RoundRectangle;
import Shapes.Polygon2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	DrawingPad drawingArea;
	JPanel drawPanel;
	private JTextField[] mouseStates;
	private String[] mouseText = { "Pressed", "Clicked", "Released", "Entered", "Exited", "Dragged", "X:", "Y:" };
	private Color drawColor = Color.black;
	private JButton clear, colorBtn, moveI, selBtn;
	private JLabel coords;
	private ArrayList<Shape> shapes;
	private HashMap<BufferedImage, Point> images;
	private ArrayList<Text> text;
	private String currentShape;
	private Point p1, textpt;
	private JPanel controlPanel, mousePanel;
	private int x1, x2, y1, y2;
	private ArrayList<Point> polyList;
	private boolean firstPoint;
	private Polygon2D lastP;
	private boolean mousePaneOn, moveImages, selectMode;
	JFrame drawFrame;
	private TextBox tx;
	int width, height;
	private Point biPoint;
	private BufferedImage pressedImg;
	private java.awt.Rectangle selection, prevSelect;
	private Point pt;
	private static final int NUMBER_OF_MOVES = 2000;
	private static final int SPEED = 8;


	/**
	 * Creates the drawing panel that houses the drawing pad
	 * 
	 * @param w
	 *            the width
	 * @param h
	 *            the height
	 */
	public DrawingPanel(int w, int h) {
		this.width = w;
		this.height = h;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(w, h));
		images = new HashMap<BufferedImage, Point>();
		shapes = new ArrayList<Shape>();
		text = new ArrayList<Text>();

		// set default variables
		currentShape = "Line";
		mousePaneOn = true;
		moveImages = false;
		selectMode = false;

		// add the drawing pad with listeners
		drawingArea = new DrawingPad(w, h, shapes, images, text);
		drawingArea.addMouseListener(new MyMouseListener());
		drawingArea.addMouseMotionListener(new MyMouseMotionListener());
		drawingArea.addKeyListener(new MyKeyListener());
		add(drawingArea);

		// Create the array of text fields.
		mousePanel = new JPanel();
		mouseStates = new JTextField[8];
		for (int i = 0; i < mouseStates.length; i++) {
			mouseStates[i] = new JTextField(mouseText[i], 10);
			mouseStates[i].setFont(new Font("TimesRoman", Font.BOLD, 14));
			mouseStates[i].setEditable(false);
			mousePanel.add(mouseStates[i]);
		}

		// internal panels and the shapes frame
		controlPanel = new JPanel();
		coords = new JLabel("(x, y)          ");
		controlPanel.add(coords);

		// set up shapes frame
		drawFrame = new JFrame();
		drawPanel = new JPanel();
		GridLayout gl = new GridLayout();
		gl.setRows(6);
		drawPanel.setLayout(gl);
		TitledBorder tBorder = new TitledBorder("SHAPES");
		tBorder.setTitleFont(new Font("Monospaced", Font.BOLD, 32));
		tBorder.setTitleJustification(TitledBorder.CENTER);
		drawPanel.setBorder(tBorder);

		// create and add all of the buttons
		clear = addButton("Clear", controlPanel);
		colorBtn = addButton("Color", controlPanel);
		moveI = addButton("Move Images", controlPanel);
		selBtn = addButton("Select Tool", controlPanel);
		addButton("Toggle Grid", controlPanel);
		addButton("Line", drawPanel);
		addButton("Straight Line", drawPanel);
		addButton("Circle", drawPanel);
		addButton("Filled Circle", drawPanel);
		addButton("Ellipse", drawPanel);
		addButton("Filled Ellipse", drawPanel);
		addButton("Rectangle", drawPanel);
		addButton("Filled Rectangle", drawPanel);
		addButton("Round Rectangle", drawPanel);
		addButton("Filled Round Rectangle", drawPanel);
		addButton("Polygon", drawPanel);
		addButton("Text", drawPanel);
		add("North", mousePanel);
		add("South", controlPanel);
		drawFrame.add(drawPanel);
		drawFrame.setPreferredSize(new Dimension(600, 600));
		drawFrame.pack();
		drawFrame.setVisible(false);
		drawFrame.setAlwaysOnTop(true);
	}

	/**
	 * Adds a button to a panel
	 * 
	 * @param name
	 *            name of the button
	 * @param pan
	 *            the panel the button is added to
	 * @return
	 */
	private JButton addButton(String name, JPanel pan) {
		JButton b = new JButton(name);
		b.addActionListener(new MyActionListener());
		pan.add(b);
		return b;
	}

	/**
	 * The clearTextFields method sets all of the text backgrounds to light gray.
	 */
	public void clearTextFields() {
		for (int i = 0; i < 6; i++)
			mouseStates[i].setBackground(Color.lightGray);
	}

	class MyMouseListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
			x1 = e.getX();
			y1 = e.getY();
			p1 = new Point(x1, y1);

			// if selection mode is active then if the mouse is in the bounds of the panel
			// get the points and create
			// add it for the starting point for the selection rectangle
			if (selectMode) {
				if (drawingArea.contains(e.getPoint())) {
					pt = e.getPoint();
					selection = new java.awt.Rectangle(pt);
				}
			}

			// if move images is selected then if the mouse is pressed within the bounds of
			// an image, change the cursor to the
			// hand, and get which image is being pressed on.
			else if (moveImages) {
				for (BufferedImage bi : images.keySet()) {
					biPoint = images.get(bi);

					Rectangle bounds = new Rectangle(biPoint.x, biPoint.y, bi.getWidth(), bi.getHeight(), null);
					if (bounds.contains(p1)) {
						pressedImg = bi;
						drawingArea.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}

				/*
				 * if current shape is a polygon then get the point and add it to the list of
				 * polygon points, Remove the last version of the polygon if this point was not
				 * the first point of the polygon. This allows the user to see the polygon as it
				 * is being drawn
				 */
			} else if (currentShape.equals("Polygon")) {
				Point nextP = new Point(e.getX(), e.getY());
				polyList.add(nextP);
				if (!firstPoint) {
					shapes.remove(lastP);
				}
				lastP = new Polygon2D(polyList, drawColor);
				shapes.add(lastP);
				firstPoint = false;

				// if it is text then set the location for the text box to the new point, and make the new point for the text
			} else if (currentShape.equals("Text")) {
				tx.setLocation(p1);
				textpt = new Point(p1);

			}
			clearTextFields();
			mouseStates[0].setBackground(Color.yellow);
		}

		public void mouseClicked(MouseEvent e) {

			clearTextFields();
			mouseStates[1].setBackground(Color.yellow);
		}

		public void mouseEntered(MouseEvent e) {
			clearTextFields();
			mouseStates[3].setBackground(Color.yellow);
		}

		public void mouseExited(MouseEvent e) {
			clearTextFields();
			mouseStates[4].setBackground(Color.yellow);
		}

		public void mouseReleased(MouseEvent e) {
			// switch back to the normal cursor from select mode
			if (!selectMode) {
				drawingArea.setCursor(Cursor.getDefaultCursor());
			}
			pressedImg = null; // for move images, here were no longer pressing an image
			x2 = e.getX();
			y2 = e.getY();
			p1 = null;
			
			// if we are in selection mode then create a selection window from the give area 
			if (selectMode) {
				if ((selection.getWidth() * selection.getHeight()) > 25) { // 25 is just for encase the user accidently clicked
					SelectionWindow selWin = new SelectionWindow(drawingArea, selection);
				}
				selection = null; // set the selection rectangle to null
				repaint();
			}
			// get the shape
			else if (currentShape.equals("Circle")) {
				int w = Math.max(x2 - x1, y2 - y1);
				shapes.add(new Circle(x1, y1, w, w, drawColor));
			} else if (currentShape.equals("Filled Circle")) {
				int w = Math.max(x2 - x1, y2 - y1);
				shapes.add(new Circle(x1, y1, w, w, drawColor, true));
			} else if (currentShape.equals("Rectangle")) {
				int w = x2 - x1;
				int h = y2 - y1;
				shapes.add(new Rectangle(x1, y1, w, h, drawColor));
			} else if (currentShape.equals("Filled Rectangle")) {
				int w = x2 - x1;
				int h = y2 - y1;
				shapes.add(new Rectangle(x1, y1, w, h, drawColor, true));
			} else if (currentShape.equals("Straight Line")) {
				shapes.add(new Line(x1, y1, x2, y2, drawColor));
			} else if (currentShape.equals("Ellipse")) {
				int w = x2 - x1;
				int h = y2 - y1;
				shapes.add(new Ellipse(x1, y1, w, h, drawColor));
			} else if (currentShape.equals("Filled Ellipse")) {
				int w = x2 - x1;
				int h = y2 - y1;
				shapes.add(new Ellipse(x1, y1, w, h, drawColor, true));
			} else if (currentShape.equals("Round Rectangle")) {
				int w = x2 - x1;
				int h = y2 - y1;
				int arcW = (int) (0.3 * w);
				int arcH = (int) (0.3 * h);
				shapes.add(new RoundRectangle(x1, y1, w, h, arcW, arcH, drawColor));
			} else if (currentShape.equals("Filled Round Rectangle")) {
				int w = x2 - x1;
				int h = y2 - y1;
				int arcW = (int) (0.3 * w);
				int arcH = (int) (0.3 * h);
				shapes.add(new RoundRectangle(x1, y1, w, h, arcW, arcH, drawColor, true));
			}
			drawingArea.repaint();
			clearTextFields();
			mouseStates[2].setBackground(Color.yellow);
		}
	}

	class MyMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			// if select mode then update the selection rectangle base on where the cursor is dragged to
			if (selectMode) {
				prevSelect = (java.awt.Rectangle) selection.clone();
				selection.setBounds((int) Math.min(pt.x, e.getX()), (int) Math.min(pt.y, e.getY()),
						(int) Math.abs(e.getX() - pt.x), (int) Math.abs(e.getY() - pt.y));
				repaint();
			}
			
			// if in move images mode and there is a pressed image then move that image based on where the cursor drags it
			else if (moveImages) {
				if (pressedImg != null) {
					int x = biPoint.x - e.getX();
					int y = biPoint.y - e.getY();
					images.put(pressedImg, new Point(biPoint.x - x, biPoint.y - y));
				}
			} else if (currentShape.equals("Line")) {
				x2 = e.getX();
				y2 = e.getY();
				shapes.add(new Line(x1, y1, x2, y2, drawColor));
				x1 = x2;
				y1 = y2;
				
			// drag the text box based on where the cursor is, don't let it go out of bounds of the panel though
			} else if (currentShape.equals("Text")) {
				if (drawingArea.contains(new Point(e.getX(), e.getY()))) {
					int x = p1.x - e.getX();
					int y = p1.y - e.getY();
					textpt.setLocation(textpt.x - x, textpt.y - y);
					tx.setLocation(textpt);
					p1 = e.getPoint();
				}
			} else if (p1 != null) {
				mouseStates[6].setText("Diameter: " + Math.max(e.getX() - x1, e.getY() - y1));
				mouseStates[7].setText("");
			}

			clearTextFields();
			mouseStates[5].setBackground(Color.yellow);
		}

		public void mouseMoved(MouseEvent e) {
			coords.setText("(" + e.getX() + ", " + e.getY() + ")");
			mouseStates[6].setText("X: " + e.getX());
			mouseStates[7].setText("Y: " + e.getY());
		}
	}

	class MyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String label = e.getActionCommand();

			if (label.equals("Save")) {
				save();
			}

			if (label.equals("Clear")) {
				clear();

			} else if (label.equals("Line") || label.equals("Circle") || label.equals("Filled Circle")
					|| label.equals("Rectangle") || label.equals("Filled Rectangle") || label.equals("Straight Line")
					|| label.equals("Ellipse") || label.equals("Filled Ellipse") || label.equals("Round Rectangle")
					|| label.equals("Filled Round Rectangle") || label.equals("Polygon") || label.equals("Text")) {
				currentShape = label;
				if (label.equals("Polygon")) {
					polyList = new ArrayList<Point>();
					firstPoint = true;
				}
				if (label.equals("Text")) {
					tx = new TextBox(new Point(0, 0)); // text box first pops up in the top left corner
					tx.jt.addKeyListener(new MyKeyListener()); // User presses enter to write the text to the canvas
				}

			} else if (label.equals("Toggle Grid")) {
				drawingArea.toggleGrid();
				drawingArea.repaint();
			} else if (label.equals("Move Images") && !selectMode) {
				moveImages = !moveImages;
				((JButton) e.getSource()).setBackground((moveImages ? Color.yellow : null)); // color indicates if user is in or out of move image mode
			} else if (label.equals("Select Tool")) {
				toggleSelectMode();
				switchSelCursor();
			}

			Object o = e.getSource();
			if (o == colorBtn) {
				selectDrawColor();
			}
			repaint();
		}
	}

	class MyKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (currentShape.equals("Polygon")) { // make new polygon
					polyList = new ArrayList<Point>();
					firstPoint = true;
				} else if (currentShape.equals("Text")) { // paint the text to the canvas
					text.add(new Text(tx.getText(), tx.f, drawColor, textpt));
					tx.dispatchEvent(new WindowEvent(tx, WindowEvent.WINDOW_CLOSING));
					drawingArea.repaint();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

	}

	public void save(String fileName) {
		/*
		 * This method creates and writes to a file containing the line data for the
		 *  drawing.
		 */

		/*
		 * Create a scanner object that asks the user for the filename upon clicking the
		 * save button .
		 */

		PrintWriter outFile = null;

		try {
			System.out.println("Entering" + " try statement");

			outFile = new PrintWriter(new FileWriter(fileName)); // write to a file with chosen filename

			for (Text txt : text) { // print the text
				outFile.print(txt);
			}
			// print the shapes to an out file.
			for (Shape shape : shapes) {
				outFile.print(shape);
			}

		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (outFile != null) {
				System.out.println("Closing PrintWriter");
				outFile.close();
			} else {
				System.out.println("PrintWriter not open");
			}
		}
	}

	/**
	 * overload method for save as functionality in viewer class
	 */
	public void save() {
		save("outFile");
	}
	
	/**
	 * Clears out the canvas of all shapes, text, and images
	 */
	public void clear() {
		drawColor = Color.black;
		colorBtn.setBackground(clear.getBackground());
		shapes.clear();
		text.clear();
		images.clear();
		currentShape = "Line";
		drawingArea.repaint();
	}
	
	/**
	 * Get a chosen color from the user
	 */
	public void selectDrawColor() {
		drawColor = JColorChooser.showDialog(null, " Color Pallette", Color.cyan);
		colorBtn.setBackground(drawColor);
	}

	/**
	 * Sets the draw panel to visible
	 */
	public void showDrawFrame() {
		drawFrame.setVisible(true);
	}

	/**
	 * Hides the draw Panel
	 */
	public void hideDrawFrame() {
		drawFrame.setVisible(false);
	}

	/**
	 * Sets the control panel to visible
	 */
	public void showControlPanel() {
		controlPanel.setVisible(true);
	}

	/**
	 * Hides the control Panel
	 */
	public void hideControlPanel() {
		controlPanel.setVisible(false);
	}
	
	/**
	 * Toggles the mouse panel on and off
	 */
	public void mousePanelToggle() {
		mousePaneOn = !mousePaneOn;
		if (mousePaneOn) {
			mousePanel.setVisible(true);
		} else {
			mousePanel.setVisible(false);
		}
	}

	/**
	 * set the draw shape
	 * 
	 * @param actionPerformed
	 *            the string representation that the action listener recorded the
	 *            user performing
	 */
	public void setShape(String actionPerformed) {
		currentShape = actionPerformed;
		if (currentShape.equals("Text")) {
			tx = new TextBox(new Point(0, 0));
			tx.jt.addKeyListener(new MyKeyListener());
		}
		if (currentShape.equals("Polygon")) {
			polyList = new ArrayList<Point>();
			firstPoint = true;
		}
	}
	
	/**
	 * Change the size of the draw panel
	 * @param w new width
	 * @param h new height
	 */
	public void adjustSize(int w, int h) {
		setSize(new Dimension(w, h));
		drawingArea.adjustSize(w, h);
		drawingArea.repaint();
		repaint();
	}
	
	/**
	 * Add an image to the canvas
	 * @param imgFile the image file containing the image
	 */
	public void addImg(File imgFile) {
		try {
			BufferedImage in = ImageIO.read(imgFile);
			images.put(in, new Point(0, 0));
			System.out.println("added to images");
			drawingArea.repaint();
		} catch (IOException e) {
			System.out.println("Couldn't add image from file to painting");
		}
	}
	
	/**
	 * Toggle on/off selection mode.  Selection mode allows the user to select a region of the canvas to get the paint images of.
	 */
	public void toggleSelectMode() {
		selectMode = !selectMode;
		if (selectMode && moveImages) {
			moveImages = false;
			moveI.setBackground(clear.getBackground());
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (selection != null) {
			Graphics2D g2d = (Graphics2D) drawingArea.getGraphics();
			g2d.setColor(Color.lightGray);
			g2d.fill(selection);
			double prevArea = prevSelect.getHeight() * prevSelect.getWidth();
			double area = selection.getWidth() * selection.getHeight();
			if (prevArea > area) {
				drawingArea.repaint();
			}
		}
	}
	
	public boolean getSelectMode() {
		return selectMode;
	}
	
	/**
	 * Switch to the selection cross hairs cursor
	 */
	public void switchSelCursor() {
		if (getSelectMode()) {
			drawingArea.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else {
			drawingArea.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	/**
	 * Gets a new x coordinate for a given shape x-coordinate
	 * @param x the x-coordinate of the shape
	 * @param xDir the x direction of the shape
	 * @return new x direction
	 */
	public int newX(int x, int xDir) {
		if (x < 0) {
			return 0;
		}
		else if (x > drawingArea.getWidth()) {
			return drawingArea.getWidth();
		} else {
			return x + (xDir * SPEED);
		}
	}
	
	/**
	 * Gets a new y coordinate for a given shape x-coordinate
	 * @param y the y-coordinate of the shape
	 * @param yDir the y direction of the shape
	 * @return new y direction
	 */
	public int newY(int y, int yDir) {
		if (y < 0) {
			return 0;
		} else if (y > drawingArea.getHeight()) {
			return drawingArea.getHeight();
		} else {
			return y + (yDir * SPEED);
		}
	}
	
	/**
	 * Moves the shapes drawn in the canvas using a Thread
	 */
	public void BoingBoing() {
		Thread t = new Thread() {
			public void run() {
				for (int i = 0; i < NUMBER_OF_MOVES; i++) {
					/* Use a copy of the shapes array list to avoid a java.util.ConcurrentModificationException
					 * (modifying the array list while iterating over it). 
					 */
					ArrayList<Shape> currentShapes = (ArrayList<Shape>) shapes.clone();
					
					// move all of those shapes
					for (Shape s : currentShapes) { 
						if (s instanceof Circle) {
							Circle cir = (Circle) s;
							int x = newX((int) cir.getX(), cir.getDir().x);
							if (x == 0 || x == drawingArea.getWidth()) {
								cir.changeXDir();
							}
							int y = newY((int) cir.getY(), cir.getDir().y);
							if (y == 0 || y == drawingArea.getHeight()) {
								cir.changeYDir();
							}
							cir.moveTo(x, y);
						} else if (s instanceof Ellipse) {
							Ellipse elp = (Ellipse) s;
							int x = newX((int) elp.getX(), elp.getDir().x);
							if (x == 0 || x == drawingArea.getWidth()) {
								elp.changeXDir();
							}
							int y = newY((int) elp.getY(), elp.getDir().y);
							if (y == 0 || y == drawingArea.getHeight()) {
								elp.changeYDir();
							}
							elp.moveTo(x, y);
						} else if (s instanceof Line) {
							Line lin = (Line) s;
							boolean xChanged = false; // prevents small lines from getting stuck in the walls
							boolean yChanged = false;
							int x1 = newX((int) lin.getX1(), lin.getDir().x);
							if (x1 == 0 || x1 == drawingArea.getWidth()) {
								lin.changeXDir();
								xChanged = true;
							}
							int y1 = newY((int) lin.getY1(), lin.getDir().y);
							if (y1 == 0 || y1 == drawingArea.getHeight()) {
								lin.changeYDir();
								yChanged = true;
							}
							int x2 = newX((int) lin.getX2(), lin.getDir().x);
							if ((x2 == 0 || x2 == drawingArea.getWidth()) && !xChanged) {
								lin.changeXDir();
							}
							int y2 = newY((int) lin.getY2(), lin.getDir().y);
							if ((y2 == 0 || y2 == drawingArea.getHeight()) && !yChanged) {
								lin.changeYDir();
							}
							lin.moveTo(x1, y1, x2, y2);
						} else if (s instanceof Rectangle) {
							Rectangle rec = (Rectangle) s;
							int x = newX((int) rec.getX(), rec.getDir().x);
							if (x == 0 || x == drawingArea.getWidth()) {
								rec.changeXDir();
							}
							int y = newY((int) rec.getY(), rec.getDir().y);
							if (y == 0 || y == drawingArea.getHeight()) {
								rec.changeYDir();
							}
							rec.moveTo(x, y);
						} else if (s instanceof RoundRectangle) {
							RoundRectangle ron = (RoundRectangle) s;
							int x = newX((int) ron.getX(), ron.getDir().x);
							if (x == 0 || x == drawingArea.getWidth()) {
								ron.changeXDir();
							}
							int y = newY((int) ron.getY(), ron.getDir().y);
							if (y == 0 || y == drawingArea.getHeight()) {
								ron.changeYDir();
							}
							ron.moveTo(x, y);
						}
					}
					drawingArea.repaint(); // repaint canvase after moving all of them
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						System.out.println("Error with thread");
					}
				}
			}
		};
		t.start(); // start the thread
	}
}

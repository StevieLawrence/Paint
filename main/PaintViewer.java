package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 * @author Steven Lawrence
 */
public class PaintViewer extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private DrawingPanel sketchpad;
	private String filename, currentDisplay;
	private int width, height;
	Container contentPane;
	private PaintingArea paintArea;
	private JButton mover;
	private static final int MOVER_W = 130, MOVER_H = 50, SHAPES_LVL = 200;
	private boolean moverPressed;
	private Point moverPoint, p1;
	private JFileChooser openFC, saveFC;
	private static final String LAST_FILE = "asldfqwiefewkflqjfwereiowerusdfassfjhaskfslafsdaslkjanvvbjahfjkalhfjfkfADSJDKSADKK";
	
	/**
	 * View the paint program
	 */
	public PaintViewer() {
		filename = LAST_FILE;
		currentDisplay = "Original";
		openFC = new JFileChooser();
		saveFC = new JFileChooser();
		openFC.setCurrentDirectory(new File("."));
		saveFC.setCurrentDirectory(new File("."));
		width = 650;
		height = 450;

		contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.gray);
		
		// Set the fonts
		Font f1 = new Font("TimesRoman", Font.BOLD, 24);
		UIManager.put("Menu.font", f1);
		UIManager.put("MenuBar.font", f1);
		UIManager.put("MenuItem.font", f1);
		Font f2 = new Font("Serif", Font.BOLD, 24);
		UIManager.put("Label.font", f2);
		UIManager.put("Button.font", f2);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenu imageMenu = new JMenu("Image");
		imageMenu.setMnemonic(KeyEvent.VK_I);
		JMenu drawMenu = new JMenu("Draw");
		drawMenu.setMnemonic(KeyEvent.VK_D);
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic(KeyEvent.VK_V);
		JMenu openMenu = new JMenu("Open");
		JMenu selectMenu = new JMenu("Select");

		addMenuItem("Selection Tool", selectMenu, KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK, null);
		addMenuItem("Theme", selectMenu, KeyEvent.VK_9, 0, null);
		addMenuItem("Boing Boing", selectMenu, KeyEvent.VK_0, 0, null);

		addMenuItem("Last Drawing", openMenu, KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Image", openMenu, KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("New Doodle Board", openMenu, KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK, null);
		fileMenu.add(openMenu);

		addMenuItem("Save", fileMenu, KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, null);
		addMenuItem("Save As", fileMenu, KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, null);
		addMenuItem("Exit", fileMenu, KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Save Image and Exit", fileMenu, -1, -1, null);

		addMenuItem("Back to Drawing", imageMenu, KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Histogram", imageMenu, KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Equalized Image", imageMenu, KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Sobel X", imageMenu, KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Sobel Y", imageMenu, KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Sobel", imageMenu, KeyEvent.VK_Z, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Grey Image", imageMenu, KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK, null);
		addMenuItem("Original", imageMenu, KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK, null);

		addMenuItem("Show Shapes Panel", viewMenu, KeyEvent.VK_F5, 0, null);
		addMenuItem("Hide Shapes Panel", viewMenu, KeyEvent.VK_F6, 0, null);
		addMenuItem("Show Control Panel", viewMenu, KeyEvent.VK_F7, 0, null);
		addMenuItem("Hide Control Panel", viewMenu, KeyEvent.VK_F8, 0, null);
		addMenuItem("Toggle Grid", viewMenu, KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK, null);
		addMenuItem("Toggle Mouse Panel", viewMenu, KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK, null);
		addMenuItem("Clear", viewMenu, KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK, null);

		addMenuItem("Text", drawMenu, KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, "images/text.png");
		addMenuItem("Straight Line", drawMenu, KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK, "images/sline.png");
		addMenuItem("Line", drawMenu, KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK, "images/Line.png");
		addMenuItem("Circle", drawMenu, KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, "images/circle.png");
		addMenuItem("Filled Circle", drawMenu, KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, "images/circleFilled.png");
		addMenuItem("Ellipse", drawMenu, KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK, "images/ellipse.png");
		addMenuItem("Filled Ellipse", drawMenu, KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK, "images/ellipseFilled.png");
		addMenuItem("Rectangle", drawMenu, KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, "images/rectangle.png");
		addMenuItem("Filled Rectangle", drawMenu, KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK,
				"images/rectangleFilled.png");
		addMenuItem("Round Rectangle", drawMenu, KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK, "images/roundRectangle.png");
		addMenuItem("Filled Round Rectangle", drawMenu, KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK,
				"images/roundRectangleFilled.png");
		addMenuItem("Polygon", drawMenu, KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK, "images/polygon.png");
		addMenuItem("Color", drawMenu, KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK, "images/colors.png");

		menuBar.add(fileMenu);
		menuBar.add(imageMenu);
		menuBar.add(drawMenu);
		menuBar.add(viewMenu);
		menuBar.add(selectMenu);
		setJMenuBar(menuBar);

		mover = new JButton("MoveMe");
		mover.setBounds(width, height, MOVER_W, MOVER_H);
		moverPoint = new Point(width, height);
		mover.addMouseMotionListener(new PaintMouseMotionListener());
		mover.addMouseListener(new PaintMouseListener());
		moverPressed = false;
		contentPane.add(mover);
		sketchpad = new DrawingPanel(width, height - 85);

		paintArea = new PaintingArea(sketchpad);
		paintArea.cards.setBounds(0, 0, width, height);
		paintArea.paintDrawingPane.addMouseMotionListener(new PaintMouseMotionListener());
		paintArea.paintDrawingPane.addMouseListener(new PaintMouseListener());

		contentPane.add(paintArea.cards);

		paintArea.cards.setLocation(0, 0);
		sketchpad = paintArea.paintDrawingPane;
		sketchpad.drawFrame.setLocation(width, SHAPES_LVL);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setLocationRelativeTo(menuBar);
		setVisible(true);
	}
	
	/**
	 * Adds a menu item 
	 * @param label the name of the menu item
	 * @param menu which menu it is being added to
	 * @param key int representing the accelerator
	 * @param mask the input mask
	 * @param iconPath image associated with the menu item
	 */
	private void addMenuItem(String label, JMenu menu, int key, int mask, String iconPath) {
		JMenuItem item;
		if (iconPath != null)
			item = new JMenuItem(label, new ImageIcon(iconPath));
		else
			item = new JMenuItem(label);
		if (key != -1)
			item.setAccelerator(KeyStroke.getKeyStroke(key, mask));
		item.addActionListener(this);
		menu.add(item);
	}

	private void exit() {
		System.exit(1); // exit the program
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		System.out.println(action);
		if (event.getSource() instanceof JMenuItem) {
			switch (action) {
			case "Theme":
				Color c = JColorChooser.showDialog(null, " Color Pallette", Color.cyan);
				contentPane.setBackground(c);
				break;
			case "Selection Tool":
				paintArea.display(0);
				paintArea.paintDrawingPane.toggleSelectMode();
				paintArea.paintDrawingPane.switchSelCursor();
				break;
			case "Save":
				if (filename.equals(LAST_FILE)) {
					boolean done = false;
					while (!done) {
						filename = JOptionPane.showInputDialog("Please input a valid filename first!:");
						if (filename.length() > 0) {
							done = true;
						}
					}
				}
				filename = filename + ".txt";
				sketchpad.save(filename);
				saveProcessedImage();
				break;
			case "Save As":
				int val = saveFC.showSaveDialog(this);
				if (val == JFileChooser.APPROVE_OPTION) {
					filename = saveFC.getSelectedFile().getName();
					filename = filename + ".txt";
					sketchpad.save(filename);
				}
				sketchpad.save(LAST_FILE);
				saveProcessedImage();
				break;
			case "Exit":
				exit();
				break;
			case "Save Image and Exit":
				sketchpad.save(filename);
				sketchpad.save(LAST_FILE);
				saveProcessedImage();
				exit();
				break;
			case "Boing Boing":
				// note: you have to wait for it to finish you cant stop it. Clicking on it again will just make it 
				// faster and longer
				sketchpad.BoingBoing();
				break;
			case "Color":
				sketchpad.selectDrawColor();
				break;
			case "Clear":
				sketchpad.clear();
				break;
			case "Toggle Grid":
				sketchpad.drawingArea.toggleGrid();
				sketchpad.drawingArea.repaint();
				repaint();
			case "Show Shapes Panel":
				sketchpad.showDrawFrame();
				this.requestFocus();
				break;
			case "Hide Shapes Panel":
				sketchpad.hideDrawFrame();
				break;
			case "Show Control Panel":
				sketchpad.showControlPanel();
				break;
			case "Hide Control Panel":
				sketchpad.hideControlPanel();
				break;
			case "Toggle Mouse Panel":
				sketchpad.mousePanelToggle();
				break;
			case "Last Drawing":
				sketchpad.drawingArea.loadPainting(filename);
				sketchpad.drawingArea.repaint();
				repaint();
				break;
			case "New Doodle Board":
				sketchpad.clear();
				break;
			case "Image":
				int value = openFC.showOpenDialog(this);
				if (value == JFileChooser.APPROVE_OPTION) {
					File choosenFile = openFC.getSelectedFile();
					String name = choosenFile.getName();
					String format = name.substring(name.lastIndexOf("."), name.length());
					if (format.equals(".PNG") || format.equals(".png")) {
						sketchpad.addImg(choosenFile);
					} else if (format.equals(".txt")) {
						filename = name;
						sketchpad.drawingArea.loadPainting(filename);
						sketchpad.drawingArea.repaint();
						repaint();
					} else {
						System.out.println(format + " is not a valid format");
					}
				}
				break;
			case "Back to Drawing":
				currentDisplay = "Original";
				paintArea.display(0);
				break;
			case "Histogram":
				currentDisplay = "Histogram";
				paintArea.display(3);
				break;
			case "Equalized Image":
				currentDisplay = "Equalized";
				paintArea.display(4);
				break;
			case "Sobel X":
				currentDisplay = "SobelX";
				paintArea.display(5);
				break;
			case "Sobel Y":
				currentDisplay = "SobelY";
				paintArea.display(6);
				break;
			case "Sobel":
				currentDisplay = "Sobel";
				paintArea.display(7);
				break;
			case "Grey Image":
				currentDisplay = "Grey";
				paintArea.display(2);
				break;
			case "Original":
				currentDisplay = "Original";
				paintArea.display(1);
				break;
			case "Line":
			case "Circle":
			case "Filled Circle":
			case "Straight Line":
			case "Rectangle":
			case "Filled Rectangle":
			case "Ellipse":
			case "Filled Ellipse":
			case "Round Rectangle":
			case "Filled Round Rectangle":
			case "Polygon":
			case "Text":
				sketchpad.setShape(action);
				break;
			default:
				System.out.printf("Action: %s not found", action);
			}
		}
	}

	class PaintMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			if (paintArea.isDrawingPanel()) {
				// allow for the move me button to position the paint area
				if (moverPressed) {
					int x = p1.x - e.getX();
					int y = p1.y - e.getY();
					moverPoint.setLocation(moverPoint.x - x, moverPoint.y - y);
					mover.setLocation(moverPoint.x, moverPoint.y);
					sketchpad.drawFrame.setLocation(moverPoint.x, SHAPES_LVL);
					p1 = e.getPoint();
				}
			}

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class PaintMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (paintArea.isDrawingPanel()) {
				// if the panel is displayed and the mouse is pressing the move button then turn
				// off the paint area and indicate that the move button is pressed
				if (mover.contains(e.getPoint())) {
					moverPressed = true;
					p1 = e.getPoint();
					paintArea.cards.setEnabled(false);
					paintArea.cards.setVisible(false);

				} else {
					moverPressed = false;
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (paintArea.isDrawingPanel()) {
				// set the size of the painting area
				paintArea.adjustSize(moverPoint.x, moverPoint.y);
				paintArea.addCards();
				paintArea.cards.setEnabled(true);
				paintArea.cards.setVisible(true);
			}
		}

	}

	/**
	 * Save the processed full canvas image
	 */
	private void saveProcessedImage() {
		try {
			BufferedImage img = paintArea.getCard(currentDisplay);
			File imageFile = new File("." + "\\images\\" + filename + currentDisplay + ".PNG");
			ImageIO.write(img, "PNG", imageFile);
		} catch (IOException e) {
			System.out.println("Couldn't save processed images");
		}
	}

	public static void main(String[] args) {
		new PaintViewer();
	}

}

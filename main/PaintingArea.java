package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Steven Lawrence
 *
 */
public class PaintingArea {
	DrawingPanel paintDrawingPane;
	DrawingPad paintDrawPad;
	PaintImages pImage;

	static final String DRAW_PANE = "Drawing Panel";
	static final String ORIGINAL = "Original";
	static final String GREY = "Grey";
	static final String HISTOGRAM = "Histogram";
	static final String EQUALIZER = "Equalized Image";
	static final String SOBEL_X = "Sobel x-gradient";
	static final String SOBEL_Y = "Sobel y-gradient";
	static final String SOBEL = "Sobel";

	JPanel cards;
	CardLayout cl;
	JLabel original, grey, hist, equal, sobX, sobY, sob;
	int width, height;
	
	/**
	 * The painting area that is being worked on
	 * @param artWork panel holding the drawing pad canvas
	 */
	public PaintingArea(DrawingPanel artWork) {
		paintDrawingPane = artWork;
		cl = new CardLayout();
		cards = new JPanel();
		cards.setLayout(cl);
		width = paintDrawingPane.width;
		height = paintDrawingPane.height;
		cards.setSize(new Dimension(width, height + 100));
		paintDrawPad = paintDrawingPane.drawingArea;
		addCards();
	}

	/**
	 * Adds the cards to the cardlayout panel
	 */
	public void addCards() {
		cards.removeAll();
		updateImages();
		cards.add(paintDrawingPane, DRAW_PANE);
		cards.add(original, ORIGINAL);
		cards.add(grey, GREY);
		cards.add(hist, HISTOGRAM);
		cards.add(equal, EQUALIZER);
		cards.add(sobX, SOBEL_X);
		cards.add(sobY, SOBEL_Y);
		cards.add(sob, SOBEL);
	}

	/**
	 * @param lab
	 *            the label holding the image
	 * @param pic
	 *            image icon
	 * @return label with the image as its icon
	 */
	private JLabel addLabel(JLabel lab, BufferedImage pic) {
		Icon ic = new ImageIcon(pic);
		lab = new JLabel(ic);
		return lab;
	}

	/**
	 * updates the paint images for any new drawings or size
	 */
	public void updateImages() {
		pImage = new PaintImages(paintDrawingPane.drawingArea);
		original = addLabel(original, pImage.getOriginal());
		grey = addLabel(grey, pImage.getLum());
		hist = addLabel(hist, pImage.getHistImage());
		equal = addLabel(equal, pImage.getEqualImage());
		sobX = addLabel(sobX, pImage.getSobelX());
		sobY = addLabel(sobY, pImage.getSobelY());
		sob = addLabel(sob, pImage.getSobel());
	}

	/**
	 * change the size of the painting area
	 * 
	 * @param w
	 *            the new width
	 * @param h
	 *            the new height
	 */
	public void adjustSize(int w, int h) {
		width = w;
		height = h;
		cards.setSize(new Dimension(w, h));
		paintDrawingPane.adjustSize(w, h);
		paintDrawingPane.repaint();
	}

	/**
	 * Displays the selected card
	 * 
	 * @param num
	 *            integer 0/default = draw panel, 1 = original, 2 = grey image, 3 =
	 *            histogram, 4 = equalized image, 5 = x sobel, 6 = y sobel, 7 =
	 *            sobel.
	 */
	public void display(int num) {
		addCards();
		switch (num) {
		case 0:
			cl.show(cards, DRAW_PANE);
			break;
		case 1:
			cl.show(cards, ORIGINAL);
			break;
		case 2:
			cl.show(cards, GREY);
			break;
		case 3:
			cl.show(cards, HISTOGRAM);
			break;
		case 4:
			cl.show(cards, EQUALIZER);
			break;
		case 5:
			cl.show(cards, SOBEL_X);
			break;
		case 6:
			cl.show(cards, SOBEL_Y);
			break;
		case 7:
			cl.show(cards, SOBEL);
			break;
		default:
			cl.show(cards, DRAW_PANE);
		}

	}

	/**
	 * @return boolean if the draw panel is the card showing
	 */
	public boolean isDrawingPanel() {
		return paintDrawingPane.isVisible();
	}

	/**
	 * Return the image of the card that is currently displayed in the paint program
	 * 
	 * @param name
	 *            the name of the displayed image
	 * @return current image being displayed
	 */
	public BufferedImage getCard(String name) {
		addCards();
		switch (name) {
		case "Histogram":
			return pImage.getHistImage();
		case "Equalized":
			return pImage.getEqualImage();
		case "SobelX":
			return pImage.getSobelX();
		case "SobelY":
			return pImage.getSobelY();
		case "Sobel":
			return pImage.getSobel();
		case "Grey":
			return pImage.getLum();
		case "Original":
		default:
			return pImage.getOriginal();
		}
	}

}

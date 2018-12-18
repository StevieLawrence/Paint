package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Steven Lawrence
 * A pop up frame that displays paint images for a selected area from a drawing pad canvas
 */
public class SelectionWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel selCards;
	private CardLayout selCL;
	private static final int NUMBER_OF_IMGS = 7;
	private static final String[] names = { "Original", "Grey", "Histogram", "Equalized Image", "Sobel x-gradient",
			"Sobel y-gradient", "Sobel" };
	private JComboBox selectScroll;
	private String displayedImg;
	BufferedImage[] paintImgs;
	
	/**
	 * Displays area selected canvas's painted images using card layout
	 * @param pad the drawing pad canvas
	 * @param selection rectangle that represents the selection area bounds
	 */
	public SelectionWindow(DrawingPad pad, Rectangle selection) {
		setLayout(new BorderLayout());
		BufferedImage Dor = pad.getImg();
		BufferedImage sel = Dor.getSubimage((int) selection.getX(), (int) selection.getY(), (int) selection.getWidth(),
				(int) selection.getHeight());
		PaintImages pImgs = new PaintImages(sel);
		selCL = new CardLayout();
		selCards = new JPanel();
		selCards.setLayout(selCL);
		BufferedImage[] paintImgs = { pImgs.getOriginal(), pImgs.getLum(), pImgs.getHistImage(), pImgs.getEqualImage(),
				pImgs.getSobelX(), pImgs.getSobelY(), pImgs.getSobel() };
		this.paintImgs = paintImgs;
		addSelCards(paintImgs);
		add(selCards);
		displayedImg = "Original";
		JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(new MyActionListener());
		selectScroll = new JComboBox(names);
		selectScroll.addActionListener(new MyActionListener());
		JPanel sPanel = new JPanel();
		sPanel.add(saveBtn);
		sPanel.add(selectScroll);
		add("South", sPanel);
		setLocation(selection.x, selection.y);
		pack();
		setVisible(true);
	}
	
	/**
	 * Make the cards for card layout
	 * @param paintImgs array of all of the paint images
	 */
	private void addSelCards(BufferedImage[] paintImgs) {
		for (int i = 0; i < NUMBER_OF_IMGS; i++) {
			selCards.add(addLabel(paintImgs[i]), names[i]);
		}
	}
	
	/**
	 * Adds an image to a label
	 * @param im the image to be used as the icon
	 * @return the label with the image
	 */
	private JLabel addLabel(BufferedImage im) {
		Icon ic = new ImageIcon(im);
		JLabel lab = new JLabel(ic);
		return lab;
	}

	class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			String action;
			String ac = ae.getActionCommand();
			action = ac;
			if (ae.getSource() instanceof JComboBox) {
				JComboBox cb = (JComboBox) ae.getSource();
				action = (String) cb.getSelectedItem();
			}
			if (action.equals("Save")) {
				JFileChooser sFC = new JFileChooser();
				sFC.setCurrentDirectory(new File("."));
				int val = sFC.showSaveDialog(null);
				if (val == JFileChooser.APPROVE_OPTION) {
					String filename = sFC.getSelectedFile().getName();
					try {
						BufferedImage img = getSelImg();
						File imageFile = new File("." + "\\images\\" + filename + displayedImg + ".PNG");
						ImageIO.write(img, "PNG", imageFile);
					} catch (IOException e) {
						System.out.println("Couldn't save selected processed images");
					}
				}
			}

			else if (action.equals("Original") || action.equals("Grey") || action.equals("Histogram")
					|| action.equals("Equalized Image") || action.equals("Sobel x-gradient")
					|| action.equals("Sobel y-gradient") || action.equals("Sobel")) {
				displayedImg = action;
				selCL.show(selCards, action);
			}
		}

	}
	
	/**
	 * @return the currently displayed paint image
	 */
	private BufferedImage getSelImg() {
		switch (displayedImg) {
		case "Original":
			return paintImgs[0];
		case "Grey":
			return paintImgs[1];
		case "Histogram":
			return paintImgs[2];
		case "Equalized Image":
			return paintImgs[3];
		case "Sobel x-gradient":
			return paintImgs[4];
		case "Sobel y-gradient":
			return paintImgs[5];
		case "Sobel":
		default:
			return paintImgs[6];

		}
	}

}
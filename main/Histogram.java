package main;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

 

/**
 * @author Steven Lawrence A histogram is a diagram consisting of bins whose
 *         height is proportional to the frequency of a variable (For assignment
 *         6 that variable is pixel intensity)
 **/
public class Histogram {
	private int[] hist;
	private int[][] colorHist;
	HashMap<Integer, Integer> equalizerMap;
	private BufferedImage img;
	private BufferedImage histImg, eqImg;
	private int w, h, totalPixels;
	int peak;
	boolean isEqualized;
	boolean colored;
	int peakRed, peakGreen, peakBlue;

	/**
	 * Construct a grey scaled histogram from an image, by looping through all of
	 * the pixels and incrementing the corresponding bin representing that pixel
	 * intensity.
	 * 
	 * @param img
	 *            the image to construct a histogram from
	 */
	public Histogram(BufferedImage img) {
		this(img, false);
	}

	/**
	 * Make a grey scaled histogram and maybe a colored histogram from the given
	 * image
	 * 
	 * @param img
	 *            the image to make the histogram from
	 * @param colored
	 *            boolean representing if the histogram should be colored or grey
	 *            scaled
	 */
	public Histogram(BufferedImage img, boolean colored) {
		this.colored = colored;
		this.img = img;
		w = img.getWidth();
		h = img.getHeight();
		histImg = new BufferedImage(w, h, img.getType());
		eqImg = new BufferedImage(w, h, img.getType());
		totalPixels = w * h; // area
		hist = new int[256];
		if (colored) {
			colorHist = new int[3][256];
		}
		// make the histogram(s)
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				Color c = new Color(img.getRGB(col, row));
				// get grey scale intensity
				int greyIntensity = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
				if (colored) {
					colorHist[0][c.getRed()]++;
					colorHist[1][c.getGreen()]++;
					colorHist[2][c.getBlue()]++;
				}
				hist[greyIntensity] += 1;
			}
		}
		// find the maximum value in the grey scaled histogram
		peak = 1;
		for (int r : hist) {
			if (r > peak) {
				peak = r;
			}
		}
		// find the maximum values for the red, green, and blue channels in the colored
		// histogram
		if (colored) {
			peakRed = 1;
			peakGreen = 1;
			peakBlue = 1;
			for (int i = 0; i < colorHist[0].length; i++) {
				if (peakRed < colorHist[0][i]) {
					peakRed = colorHist[0][i];
				}
				if (peakGreen < colorHist[1][i]) {
					peakGreen = colorHist[1][i];
				}
				if (peakBlue < colorHist[2][i]) {
					peakBlue = colorHist[2][i];
				}
			}
		}
		isEqualized = false; // the histogram is initialized to not being equalized
	}

	/**
	 * Histogram equalization adjusts image intensities to enhance contrast. Given a
	 * range of pixel intensities to L-1 (in this case L = 256). The histogram is
	 * equalized by finding the cumulative probability, where the probability of one
	 * intensity is the number of pixels with that intensity over the total number
	 * of pixels, and multiplying it by L-1. A look up table can then be created to
	 * be used in creating the equalized image.
	 */
	public void equalize() {
		isEqualized = true; // set the histogram to equalized
		//colored = false; // switch from the colored histogram to using the grey scaled histogram
		equalizerMap = new HashMap<Integer, Integer>(); // map the look up table
		int[] equalHist = new int[256];
		double probSum = 0;
		int initBin = 0;
		int newBinVal;
		for (int freq : hist) {
			double prob = (double) freq / totalPixels; // probability
			probSum += prob;
			double eqIntensity = probSum * 255;
			newBinVal = (int) eqIntensity;
			equalizerMap.put(initBin, newBinVal);
			equalHist[newBinVal] += 1;
			initBin += 1;
		}
		hist = equalHist;

		// find the peak in the equalized histogram
		peak = 1;
		for (int s : hist) {
			if (s > peak)
				peak = s;
		}

		// make the equalized image using the mapping
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				int rgb = img.getRGB(col, row);
				int b = rgb & 0xff;
				int g = rgb >> 8 & 0xff;
				int r = rgb >> 16 & 0xff;
				int oldGrey = (int) (r * 0.299 + g * 0.587 + b * 0.114);
				int newVal = equalizerMap.get(oldGrey);
				Color newGrayScale = new Color(newVal, newVal, newVal);
				eqImg.setRGB(col, row, newGrayScale.getRGB());
			}
		}
	}

	/**
	 * @return the equalized image
	 */
	public BufferedImage getEqulizedImg() {
		if (!isEqualized)
			equalize();
		return eqImg;
	}

	/**
	 * draws the colored, or grey histogram
	 * 
	 * @param g
	 *            the tool to draw with
	 */
	public void draw() {
		int yRange, xRange;
		Graphics2D g2 = (Graphics2D) histImg.getGraphics();
		// make everything white
		g2.setColor(Color.white);
		g2.fillRect(0, 0, w, h);

		// Get the x and y-axis in relation to width and height
		xRange = (int) (w * 0.9);
		yRange = (int) (h * 0.9);

		// draw the axis
		int xCord = w - xRange;
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(5)); // set line thickness
		g2.drawLine(xCord, yRange, w, yRange); // x-axis
		g2.drawLine(xCord, yRange, xCord, 0); // y-axis

		// Make a histogram in terms of width and height
		if (!colored) {
			// if its not a colored image then draw the grey scaled histogram
			int[] newHist = new int[256];
			int index = 0;
			for (int freq : hist) {
				/*
				 * normalize the image basedon the peak and then multiply it by the y-axis range
				 * to re-scale the histogram to fit on the screen
				 */
				double norm = (double) freq / peak;
				int newFreq = (int) (yRange * norm);
				newHist[index] = newFreq;
				index++;
			}

			/*
			 * draw the scaled histogram. Re-scale the histogram in relation to the x-axis.
			 * Evenly spaced objects of a known total width can go into a larger known width
			 * by using the formula (outerWidth - widthOfObjects) / (widthOfObjects + 1).
			 */
			// get the space length
			double spaceLength = (double) (xRange - newHist.length) / (newHist.length + 1);

			int k = 0, y1Cord, y2Cord, x2Cord;
			double x = xCord; // since spacing is a double, x needs to be a double
			for (int i = 0; i < newHist.length - 1; i++) {
				k = i + 1;
				// top left is (0, 0) so need to flip the y values
				y1Cord = yRange - newHist[i];
				y2Cord = yRange - newHist[k];
				xCord = (int) x;
				x2Cord = (int) (x + spaceLength + 1);
				g2.drawLine(xCord, y1Cord, x2Cord, y2Cord);
				x += spaceLength + 1;
			}

		} else {
			// draw the colored histogram, which has three separate lines for the red,
			// green, and blue channels
			int[][] newHist = new int[3][256];
			for (int i = 0; i < newHist[0].length; i++) {
				
				// normalize each color channel the same way the grey histogram was
				double normRed = (double) colorHist[0][i] / peakRed;
				double normGreen = (double) colorHist[1][i] / peakGreen;
				double normBlue = (double) colorHist[2][i] / peakBlue;
				
				// re-scale each channel based on the y-axis
				int redFreq = (int) (yRange * normRed);
				int greenFreq = (int) (yRange * normGreen);
				int blueFreq = (int) (yRange * normBlue);
				newHist[0][i] = redFreq;
				newHist[1][i] = greenFreq;
				newHist[2][i] = blueFreq;
			}

			/*
			 * Again the x-axis can be re-scaled by using the handy carpenter formula
			 * (outerWidth - widthOfObjects) / (widthOfObjects + 1)
			 */
			double spaceLength = (double) (xRange - newHist[0].length) / (newHist[0].length + 1);

			int k = 0, y1Red, y2Red, y1Green, y2Green, y1Blue, y2Blue, x2Cord;
			double x = xCord; // since spacing is a double, x needs to be a double
			for (int i = 0; i < newHist[0].length - 1; i++) {
				k = i + 1;
				// top left is (0, 0) so need to flip the y values for all three channels
				y1Red = yRange - newHist[0][i];
				y2Red = yRange - newHist[0][k];
				y1Green = yRange - newHist[1][i];
				y2Green = yRange - newHist[1][k];
				y1Blue = yRange - newHist[2][i];
				y2Blue = yRange - newHist[2][k];

				// get the x coordinate
				xCord = (int) x;
				x2Cord = (int) (x + spaceLength + 1);

				// draw the individual channels
				g2.setColor(Color.red);
				g2.drawLine(xCord, y1Red, x2Cord, y2Red);
				g2.setColor(Color.green);
				g2.drawLine(xCord, y1Green, x2Cord, y2Green);
				g2.setColor(Color.blue);
				g2.drawLine(xCord, y1Blue, x2Cord, y2Blue);
				x += spaceLength + 1;
			}
		}
	}

	public BufferedImage getImage() {
		draw();
		return histImg;
	}

}
package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * @author Steven Lawrence
 * 
 *         Given the painting on a drawingpad, makes images for the painting, in
 *         normal, greyscale, histogram equalized, the histogram graph, x
 *         gradient sobel, y grading sobel, and sobel.
 */
public class PaintImages {
	BufferedImage image;
	BufferedImage greyImage;
	Histogram histogram;
	BufferedImage equalImage;
	BufferedImage sobelX;
	BufferedImage sobelY;
	BufferedImage sobel;

	public static final float[] SOBELX3x3 = { 1.f, 0.f, -1.f, 2.f, 0.f, -2.f, 1.f, 0.f, -1.f };

	public static final float[] SOBELY3x3 = { 1.f, 2.f, 1.f, 0.f, 0.f, 0.f, -1.f, -2.f, -1.f };

	public PaintImages(DrawingPad canvas) {
		this(canvas.getImg());
	}

	public PaintImages(BufferedImage pic) {
		image = pic;

		// determine if it should be a 3 channel histogram on single channel histogram
		if (IsMonotoned(image)) {
			histogram = new Histogram(image);
		} else {
			histogram = new Histogram(image, true);
		}
		greyImage = luminance(image);
		setSobel();
	}

	public BufferedImage getOriginal() {
		return image;
	}

	public BufferedImage getLum() {
		return greyImage;
	}

	public BufferedImage getHistImage() {
		return histogram.getImage();
	}

	public BufferedImage getEqualImage() {
		return histogram.getEqulizedImg();
	}

	public BufferedImage getSobelX() {
		return sobelX;
	}

	public BufferedImage getSobelY() {
		return sobelY;
	}

	public BufferedImage getSobel() {
		return sobel;
	}

	/**
	 * Determines if the image is monotoned by check the rgb values at each pixel
	 * 
	 * @param img
	 *            the image being checked
	 * @return boolean
	 */
	private boolean IsMonotoned(BufferedImage img) {
		boolean isMono = true;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color rgb = new Color(image.getRGB(i, j));
				int r = rgb.getRed();
				int g = rgb.getGreen();
				int b = rgb.getBlue();
				if (r != g && g != b) {
					isMono = false;
					break;
				}
			}
		}
		return isMono;
	}

	/**
	 * converts image to greyscale
	 * 
	 * @param im
	 *            image being converted
	 * @return the grey buffered image
	 */
	private BufferedImage luminance(BufferedImage im) {
		BufferedImage greyIm = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		for (int x = 0; x < im.getWidth(); x++) {
			for (int y = 0; y < im.getHeight(); y++) {
				int rgb = im.getRGB(x, y); // AAAAAAAA|RRRRRRRR|GGGGGGGG|BBBBBBBBB
				int b = rgb & 0xff; // BBBBBBBBB
				int g = rgb >> 8 & 0xff; // GGGGGGGG
				int r = rgb >> 16 & 0xff; // RRRRRRRR
				int lum = (int) (r * .299 + g * .587 + b * .114);
				int L = 0xff << 24;
				L += lum << 16;
				L += lum << 8;
				L += lum;
				greyIm.setRGB(x, y, L);
			}
		}
		return greyIm;
	}

	/**
	 * Gets the x and y gradient sobel images using buffered image operation and the
	 * appropiate kernels. Then uses those images to calculate the combined sobel
	 * image using gradientEstimate = (x^2 + y^2)^1/2 (hypotenous calc)
	 */
	private void setSobel() {
		sobelX = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		sobelY = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		sobel = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		BufferedImageOp op1 = new ConvolveOp(new Kernel(3, 3, SOBELX3x3), ConvolveOp.EDGE_NO_OP, null);
		BufferedImageOp op2 = new ConvolveOp(new Kernel(3, 3, SOBELY3x3), ConvolveOp.EDGE_NO_OP, null);

		op1.filter(image, sobelX);
		op2.filter(image, sobelY);

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color Gx = new Color(sobelX.getRGB(x, y));
				Color Gy = new Color(sobelY.getRGB(x, y));
				int newR = (int) Math.hypot(Gx.getRed(), Gy.getRed());
				if (newR > 255)
					newR = 255;
				int newG = (int) Math.hypot(Gx.getGreen(), Gy.getGreen());
				if (newG > 255)
					newG = 255;
				int newB = (int) Math.hypot(Gx.getBlue(), Gy.getBlue());
				if (newB > 255)
					newB = 255;
				Color G = new Color(newR, newG, newB);
				sobel.setRGB(x, y, G.getRGB());
			}
		}
	}
}

package com.javaspring.reposity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class GraphicImage {
	// add logp
	public void addImageWatermark(File watermarkImageFile, File sourceImageFile, File destImageFile) {
		try {
			BufferedImage sourceImage = ImageIO.read(sourceImageFile);
			BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

			// initializes necessary graphic properties
			Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
			AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
			g2d.setComposite(alphaChannel);

			// calculates the coordinate where the image is painted
			int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
			int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;

			// paints the image watermark
			g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);

			ImageIO.write(sourceImage, "png", destImageFile);
			g2d.dispose();

			System.out.println("The image watermark is added to the image.");

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public void addTextWatermark(String text, File sourceImageFile, File destImageFile) {
		try {
			BufferedImage sourceImage = ImageIO.read(sourceImageFile);
			Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

			// initializes necessary graphic properties
			AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
			g2d.setComposite(alphaChannel);
			g2d.setColor(Color.RED);
			g2d.setFont(new Font("Arial", Font.BOLD, 50)); // chưa set size chữ
			FontMetrics fontMetrics = g2d.getFontMetrics();
			Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

			// calculates the coordinate where the String is painted
			int centerX = sourceImage.getWidth() - (int) rect.getWidth();
			int centerY = sourceImage.getHeight() - (int) rect.getHeight();

			// paints the textual watermark
			g2d.drawString(text, centerX, centerY);

			ImageIO.write(sourceImage, "png", destImageFile);
			g2d.dispose();

			System.out.println("The tex watermark is added to the image.");

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	
}

package views.storageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IsRatedImagePanel extends JPanel {
	private BufferedImage image;
	private static final String rated_icon = "src/main/resources/rated_icon.png";
	private static final String unrated_icon = "src/main/resources/unrated_icon.png";
	private boolean isRated;

	public IsRatedImagePanel() {
		setIsRated(false);
	}

	public void setIsRated(boolean isRated) {
		this.isRated = isRated;
		setIcon();
	}
	public boolean isRated() {
		return isRated;
	}
	public void setIcon() {
		try {
			if(isRated)
				image = ImageIO.read(new File(rated_icon));
			else
				image = ImageIO.read(new File(unrated_icon));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}
}

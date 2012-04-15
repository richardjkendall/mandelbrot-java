import java.awt.Image;

public interface ImagePanel {
	public int getImageWidth();
	public int getImageHeight();
	public Image getImage();
	public Image renderImage(final int x, final int y);
}

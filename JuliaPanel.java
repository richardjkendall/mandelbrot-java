/**********************************************
JuliaPanel.java
v0.1 (very young)

Richard James Kendall
richard@richardjameskendall.com

Draws a detailed and coloured Julia set on the
screen.
**********************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class JuliaPanel extends JComponent implements ImagePanel {
    int width = 800;
    int height = 600;
    
    int mx_it = 200;
    
    double mn_real = -2.0;
    double mx_real = 2.0;
    double mn_im = -1.5;
    double mx_im = 0;

    double real_factor = 0;
    double im_factor = 0;

    double c_real = 0;
    double c_im = 0;

    Image jset;

    int mouse_x = 0;
    int mouse_y = 0;

    int c_mouse_x = 0;
    int c_mouse_y = 0;

    // colour of julia set
    int r1 = 164;
    int g1 = 200;
    int b1 = 239;

    // colour of background
    int r2 = 10;
    int g2 = 36;
    int b2 = 102;

    public JuliaPanel(final int pwidth, final int pheight, final int mit, final double creal, final double cim) {
		width = pwidth;
		height = pheight;
		mx_it = mit;
		c_real = creal;
		c_im = cim;
		calculate();
    }

    private void calculate() {
		mx_im = mn_im + (mx_real - mn_real) * height / width;
		real_factor = (mx_real - mn_real) / (width - 1);
		im_factor = (mx_im - mn_im) / (height - 1);
    }

    private Color getcol(int it) {
		double p = new Integer(it).doubleValue() / new Integer(mx_it).doubleValue();
		double r = ((r1 - r2) * p) + r2;
		double g = ((g1 - g2) * p) + g2;
		double b = ((b1 - b2) * p) + b2;
		//System.out.println(it + " : " + r + ", " + g + ", " + b);
		Color c = new Color(new Double(r).intValue(), new Double(g).intValue(), new Double(b).intValue());
		return c;
    }

    private String printcol(int it) {
		double p = new Integer(it).doubleValue() / new Integer(mx_it).doubleValue();
		double r = ((r1 - r2) * p) + r2;
		double g = ((g1 - g2) * p) + g2;
		double b = ((b1 - b2) * p) + b2;
		return it + "/" + mx_it + " = " + p + "% : " + r + ", " + g + ", " + b;
    }
    
    private Image drawjset() {
		int[] t = new int[mx_it+1];
		Image im = createImage(width, height);
		Graphics g = im.getGraphics();
		for(int y = 0;y < height;y++) {
			for(int x = 0;x < width;x++) {
				double z_real = mn_real + x * real_factor;
				double z_im = mx_im - y * im_factor;
				boolean inside = true;
				int i = 0;
				int c = 0;
				for(i = 0; i < mx_it;i++) {
					c++;
					double z_reala = z_real * z_real;
					double z_ima = z_im * z_im;
					if(z_reala + z_ima > 4) {
						inside = false;
						break;
					}
					z_im = 2 * z_real * z_im + c_im;
					z_real = z_reala - z_ima + c_real;
				}
				t[i]++;
				if(i == mx_it) {
					g.setColor(new Color(r1, g1, b1));
					g.drawLine(x,y,x,y);
				} else {
					g.setColor(getcol(c));
					//g.setColor(new Color(r2,g2,b2));
					g.drawLine(x,y,x,y);
				}
			}
		}
		/*for(int j = 0;j <= mx_it;j++) {
		System.out.println(j + ":\t" + t[j] + "\t" + printcol(j));
	    }*/
		return im;
    }
	
	private Image drawjset(final int twidth, final int theight) {
		double n_mx_im = mn_im + (mx_real - mn_real) * theight / twidth;
		double n_real_factor = (mx_real - mn_real) / (twidth - 1);
		double n_im_factor = (n_mx_im - mn_im) / (theight - 1);
		int[] t = new int[mx_it+1];
		Image im = createImage(twidth, theight);
		Graphics g = im.getGraphics();
		for(int y = 0;y < theight;y++) {
			for(int x = 0;x < twidth;x++) {
				double z_real = mn_real + x * n_real_factor;
				double z_im = n_mx_im - y * n_im_factor;
				boolean inside = true;
				int i = 0;
				int c = 0;
				for(i = 0; i < mx_it;i++) {
					c++;
					double z_reala = z_real * z_real;
					double z_ima = z_im * z_im;
					if(z_reala + z_ima > 4) {
						inside = false;
						break;
					}
					z_im = 2 * z_real * z_im + c_im;
					z_real = z_reala - z_ima + c_real;
				}
				t[i]++;
				if(i == mx_it) {
					g.setColor(new Color(r1, g1, b1));
					g.drawLine(x,y,x,y);
				} else {
					g.setColor(getcol(c));
					//g.setColor(new Color(r2,g2,b2));
					g.drawLine(x,y,x,y);
				}
			}
		}
		return im;
		//return null;
	}

    public void paintComponent(Graphics g) {
		if(jset == null) {
			jset = drawjset();
		}
		jset.flush();
		g.drawImage(jset,0,0,this);
    }

    public Dimension getPrefferedSize() {
		return new Dimension(width, height);
    }
	
	public int getImageWidth() {
		return width;
    }
	
    public int getImageHeight() {
		return height;
    }
	
    public Image getImage() {
		return jset;
    }
	
    public Image renderImage(final int x, final int y) {
		mx_it = 100;
		Image jset = drawjset(x, y);
		mx_it = 100;
		return jset;
    }
	
}

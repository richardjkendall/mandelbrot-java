/**********************************************
MandelbrotPanel.java
v0.1 (very young)

Richard James Kendall
richard@richardjameskendall.com

Draws a Mandelbrot set on the screen, shows a
Julia set for the mouse x,y and opens a
detailed Julia set window on clicking.
**********************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MandelbrotPanel extends JComponent implements MouseMotionListener, MouseListener, ImagePanel {
    int width = 800;
    int height = 600;
    
    int mx_it = 400;
	int hq_mx_it = 400;
    
    double mn_real = -2.0;
    double mx_real = 1.0;
    double mn_im = -1.0;
    double mx_im = 0;

    double real_factor = 0;
    double im_factor = 0;
	
	int show_j_preview = 0;

    // colour of mandelbrot set
    int r1 = 0;
    int g1 = 0;
    int b1 = 0;

    // colour of background
    int r2 = 255;
    int g2 = 255;
    int b2 = 255;

    Image mset;

    int mouse_x = 0;
    int mouse_y = 0;

    int c_mouse_x = 0;
    int c_mouse_y = 0;

    int s_mouse_x = 0;
    int s_mouse_y = 0;
	
	int d_mouse_x = 0;
	int d_mouse_y = 0;
	
	int d_offset_x = 0;
	int d_offset_y = 0;
	
	boolean dragging = false;

    String desc = new String("");

    boolean zoom_mode = false;
	
	boolean show_export = true;

    public MandelbrotPanel(final int pwidth, final int pheight, final int mit, final int preview, final boolean s_export) {
		width = pwidth;
		height = pheight;
		mx_it = mit;
		show_j_preview = preview;
		show_export = s_export;
		calculate();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
    }

    private void calculate() {
		mx_im = mn_im + (mx_real - mn_real) * height / width;
		real_factor = (mx_real - mn_real) / (width - 1);
		im_factor = (mx_im - mn_im) / (height - 1);
    }

    private void drawjset(int x, int y) {
		double c_real = mn_real + (real_factor * x);
		double c_im = mn_im + (im_factor * y);
		JuliaWindow panel = new JuliaWindow(c_real, c_im, show_export);
    }

    private void updatedesc(int x, int y) {
		double c_real = mn_real + (real_factor * x);
		double c_im = mn_im + (im_factor * y);
		desc = "c = " + c_real + " + " + c_im;
    }

    private void zoom() {
		double c_real_l = mn_real + (real_factor * c_mouse_x);
		double c_im_l = mn_im + (im_factor * c_mouse_y);
		double c_real_r = mn_real + (real_factor * mouse_x);
		double c_im_r = mn_im + (im_factor * mouse_y);
		mn_real = c_real_l;
		mx_real = c_real_r;
		mn_im = c_im_l;
		calculate();
		mset = drawmset();
		c_mouse_x = 0;
		c_mouse_y = 0;
		repaint();
    }

    private Color getcol(int it) {
		double p = new Integer(it).doubleValue() / new Integer(mx_it).doubleValue();
		double r = ((r1 - r2) * p) + r2;
		double g = ((g1 - g2) * p) + g2;
		double b = ((b1 - b2) * p) + b2;
		Color c = new Color(new Double(r).intValue(), new Double(g).intValue(), new Double(b).intValue());
		return c;
    }

    private Image drawjset(int jwidth, int jheight, int jqual, double c_real, double c_im, Color col) {
		double mn_real = -2.0;
		double mx_real = 2.0;
		double mn_im = -1.5;
		double mx_im = mn_im + (mx_real - mn_real) * jheight / jwidth;
		double real_factor = (mx_real - mn_real) / (jwidth - 1);;
		double im_factor = (mx_im - mn_im) / (jheight - 1);

		Image im = createImage(jwidth, jheight);
		Graphics g = im.getGraphics();
		for(int y = 0;y < jheight;y++) {
			for(int x = 0;x < jwidth;x++) {
				double z_real = mn_real + x * real_factor;
				double z_im = mx_im - y * im_factor;
				boolean inside = true;
				int i = 0;
				int c = 0;
				for(i = 0; i < jqual;i++) {
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
				if(i == jqual) {
					g.setColor(col);
					g.drawLine(x,y,x,y);
				} else {
					g.setColor(new Color(255,255,255));
					g.drawLine(x,y,x,y);
				}
			}
		}
		g.setColor(new Color(0,0,0));
		g.drawRect(0, 0, jwidth-1, jheight-1);
		return im;
	}

    private Image drawmset() {
		Image im = createImage(width, height);
		Graphics g = im.getGraphics();
		for(int y = 0;y <= height;y++) {
			double c_im = mx_im - y * im_factor;
			for(int x = 0;x < width;x++) {
				double c_real = mn_real + x * real_factor;
				double z_real = c_real;
				double z_im = c_im;
				int i = 0;
				for(i = 0; i < mx_it;i++) {
					double z_reala = z_real * z_real;
					double z_ima = z_im * z_im;
					if(z_reala + z_ima > 4) {
						break;
					}
					z_im = 2 * z_real * z_im + c_im;
					z_real = z_reala - z_ima + c_real;
				}
				if(i == mx_it) {
					g.setColor(new Color(r1,g1,b1));
					g.drawLine(x,height-y,x,height-y);
				} else {
					g.setColor(getcol(i));
					g.drawLine(x,height-y,x,height-y);
				}
			}
		}
		return im;
    }

    private Image drawmset(final int twidth, final int theight) {
		// need to recalculate the real and im factors
		double n_mx_im = mn_im + (mx_real - mn_real) * theight / twidth;
		double n_real_factor = (mx_real - mn_real) / (twidth - 1);
		double n_im_factor = (n_mx_im - mn_im) / (theight - 1);
		// then draw the set as before
		Image im = createImage(twidth,theight);
		Graphics g = im.getGraphics();
		for(int y = 0;y < theight;y++) {
			double c_im = mx_im - y * n_im_factor;
			for(int x = 0;x < twidth;x++) {
				double c_real = mn_real + x * n_real_factor;
				double z_real = c_real;
				double z_im = c_im;
				int i = 0;
				for(i = 0; i < mx_it;i++) {
					double z_reala = z_real * z_real;
					double z_ima = z_im * z_im;
					if(z_reala + z_ima > 4) {
						break;
					}
					z_im = 2 * z_real * z_im + c_im;
					z_real = z_reala - z_ima + c_real;
				}
				if(i == mx_it) {
					g.setColor(new Color(r1,g1,b1));
					g.drawLine(x,theight-y,x,theight-y);
				} else {
					g.setColor(getcol(i));
					g.drawLine(x,theight-y,x,theight-y);
				}
			}
		}
		return im;
    }

    public void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D)gg;
		if(mset == null) {
			//System.out.println("drawing mset");
			mset = drawmset();
		}
		mset.flush();
		if(dragging) {
			g.drawImage(mset,d_offset_x,d_offset_y,this);
		} else {
			g.drawImage(mset,0,0,this);
		}
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		if(!zoom_mode) {
			if(!(mouse_x <= 0 || mouse_y <= 0)) {
				if(!(mouse_x > width || mouse_y > height)) {
					//g.setColor(new Color(255,0,0));
					//g.drawLine(mouse_x-10,mouse_y,mouse_x+10,mouse_y);
					//g.drawLine(mouse_x,mouse_y-10,mouse_x,mouse_y+10);
					double c_real = mn_real + (real_factor * mouse_x);
					double c_im = mn_im + (im_factor * mouse_y);
					Image jset = drawjset(150, 112, 15, c_real, c_im, new Color(91,91,91));
					g.setColor(new Color(91,91,91));
					if(show_j_preview == 1) {
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f));
						g.drawImage(jset,10,25,this);
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
					}
					g.setColor(new Color(18,64,171));
					g.drawString("c = " + c_real + " + " + c_im, 10, 15);
		
				}
			}
			if(!(c_mouse_x == 0 && c_mouse_y == 0)) {
				//g.setColor(new Color(0,0,255));
				//g.drawLine(c_mouse_x-20,c_mouse_y,c_mouse_x+20,c_mouse_y);
				//g.drawLine(c_mouse_x,c_mouse_y-20,c_mouse_x,c_mouse_y+20);
			}
		} else {
			if(!(c_mouse_x == 0 && c_mouse_y == 0)) {
				g.setColor(new Color(18,64,171));
				g.drawRect(c_mouse_x, c_mouse_y, mouse_x - c_mouse_x, mouse_y - c_mouse_y);
			}
		}
		//g.setColor(new Color(91,91,91));
		g.setColor(new Color(18,64,171));
		g.drawString("Quadratic plane bounds: " + mn_real + ", " + mx_real + " : " + mn_im + ", " + mx_im, 10, height - 30);
		g.drawString("By Richard Kendall, Dec 2011", 10, height - 15);
		if(!(c_mouse_x == 0 && c_mouse_y == 0)) {
			//g.drawString("Selected point: " + c_mouse_x + ", " + c_mouse_y, 10, height - 25);
		}
	}

    public Dimension getPrefferedSize() {
		return new Dimension(width, height);
    }

    public void setzoommode(final boolean mode) {
		c_mouse_x = 0;
		c_mouse_y = 0;
		zoom_mode = mode;
    }

    public void setcorners(double x1, double x2, double y1) {
		mn_real = x1;
		mx_real = x2;
		mn_im = y1;
		calculate();
		mset = null;
		repaint();
    }
	
	public void toggleJSetPreview() {
		show_j_preview = show_j_preview == 0 ? 1 : 0;
		repaint();
	}

    // ====== CODE TO SATISFY INTERFACES ==========
    // mouse motion stuff

    public void mouseDragged(MouseEvent e) {
		// drag the mandelbrot set around the screen
		// do do this we need to alter the 
		if(!dragging) {
			dragging = true;
			d_mouse_x = e.getX();
			d_mouse_y = e.getY();
		} else {
			// find the offset from the first position
			d_offset_x = -(d_mouse_x - e.getX());
			d_offset_y = -(d_mouse_y - e.getY());
			repaint();
		}
    }

    public void mouseMoved(MouseEvent e) {
		mouse_x = e.getX();
		mouse_y = e.getY();
		this.repaint();
    }

    // mouse click stuff

    public void mouseClicked(MouseEvent e) {
		if(!zoom_mode) {
			c_mouse_x = e.getX();
			c_mouse_y = e.getY();
			this.repaint();
			drawjset(e.getX(),e.getY());
		} else {
			if (c_mouse_x == 0 && c_mouse_y == 0) {
				c_mouse_x = e.getX();
				c_mouse_y = e.getY();
				this.repaint();
			} else {
				zoom();
				zoom_mode = false;
			}
		}
    }

    public void mouseEntered(MouseEvent e) {
		// don't need
    }

    public void mouseExited(MouseEvent e) {
		// don't need
    }

    public void mousePressed(MouseEvent e) {
		// nothing
    }

    public void mouseReleased(MouseEvent e) {
		if(dragging) {
			dragging = false;
			mn_real -= (real_factor * d_offset_x);
			mx_real -= (real_factor * d_offset_x);
			mn_im -= (im_factor * d_offset_y);
			mx_im -= (im_factor * d_offset_y);
			d_offset_y = 0;
			d_offset_x = 0;
			d_mouse_x = 0;
			d_mouse_y = 0;
			mset = drawmset();
			repaint();
		}
    }

    // code for the ImagePanel interface (used for exporting images)
    public int getImageWidth() {
		return width;
    }

    public int getImageHeight() {
		return height;
    }

    public Image getImage() {
		return mset;
    }

    public Image renderImage(final int x, final int y) {
		Image tImg;
		mx_it = 800;
		tImg = drawmset(x,y);
		mx_it = 800;
		return tImg;
    }
}

/**********************************************
Fractal.java
v0.1 (very young)

Richard James Kendall
richard@richardjameskendall.com

Wraps up the Mandelbrot drawing panel
**********************************************/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FractalApplet extends JApplet implements ActionListener {
    //JFrame window;
    MandelbrotPanel mandelbrot;

    public void init() {
		//window = new JFrame("Mandelbrot Set");
		Container iContent = this.getContentPane();
		JButton bzoom = new JButton("Zoom In...");
		bzoom.setActionCommand("zoom");
		JButton reset = new JButton("Reset Image");
		reset.setActionCommand("reset");
		JButton export = new JButton("Export Image...");
		export.setActionCommand("export");
		JCheckBox preview = new JCheckBox("Show Julia set preview");
		preview.setActionCommand("preview");
		bzoom.addActionListener((ActionListener)this);
		reset.addActionListener((ActionListener)this);
		//export.addActionListener((ActionListener)this);
		preview.addActionListener((ActionListener)this);
		mandelbrot = new MandelbrotPanel(800, 600, 400, 0, false);
		mandelbrot.setcorners(-2.0,1.0,-1.1);
		Container c = new Container();
		c.setLayout(new FlowLayout());
		c.add(bzoom);
		c.add(reset);
		//c.add(export);
		c.add(preview);
		iContent.add(mandelbrot, BorderLayout.CENTER);
		iContent.add(c, BorderLayout.SOUTH);
		//window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		//window.setSize(800,660);
		//Dimension tScreenDim = Toolkit.getDefaultToolkit().getScreenSize();
		//window.setLocation((int)((tScreenDim.getWidth() - window.getWidth()) / 2),(int)((tScreenDim.getHeight() - window.getHeight()) / 2));
		//window.setResizable(false);
		//window.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("zoom")) {
			mandelbrot.setzoommode(true);
		}
		if(e.getActionCommand().equals("reset")) {
			mandelbrot.setcorners(-2.0,1.0,-1.1);
		}
		if(e.getActionCommand().equals("export")) {
			ExportImageWindow panel = new ExportImageWindow((ImagePanel)mandelbrot);
		}
		if(e.getActionCommand().equals("preview")) {
			mandelbrot.toggleJSetPreview();
		}
    }
}

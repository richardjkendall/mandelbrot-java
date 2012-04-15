import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class ExportImageWindow implements ActionListener {
	ImagePanel iImagePanel;
	JFrame iWindow;
	JTextField iWidthField;
	JTextField iHeightField;

	public ExportImageWindow(ImagePanel target) {
		iImagePanel = target;
		iWindow = new JFrame("Export Image...");
		iWidthField = new JTextField("" + iImagePanel.getImageWidth());
		iHeightField = new JTextField("" + iImagePanel.getImageHeight());
		JButton tExport = new JButton("Export...");
		tExport.setActionCommand("export");
		tExport.addActionListener(this);
		Container tWindowContent = iWindow.getContentPane();
		Container tControls = new Container();
		tControls.setLayout(new FlowLayout());
		tControls.add(new JLabel("Width: "));
		tControls.add(iWidthField);
		tControls.add(new JLabel(" Height: "));
		tControls.add(iHeightField);
		tWindowContent.add(tControls,BorderLayout.NORTH);
		tWindowContent.add(tExport,BorderLayout.SOUTH);
		iWindow.setSize(200,100);
		iWindow.setResizable(false);
		Dimension tScreenDim = Toolkit.getDefaultToolkit().getScreenSize();
		iWindow.setLocation((int)((tScreenDim.getWidth() - iWindow.getWidth()) / 2),(int)((tScreenDim.getHeight() - iWindow.getHeight()) / 2));
		iWindow.setVisible(true);
	}	

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("export")) {
			//System.out.println("export triggered...");
			try {
				int tWidth = Integer.parseInt(iWidthField.getText());
				int tHeight = Integer.parseInt(iHeightField.getText());
				Image tImage = iImagePanel.renderImage(tWidth,tHeight);
				File tFile = new File("exports/" + System.currentTimeMillis() + ".png");
				ImageIO.write((BufferedImage)tImage, "png", tFile);
				JOptionPane.showMessageDialog(iWindow,"Image exported to:\n" + tFile.getAbsolutePath(), "Export Complete", JOptionPane.INFORMATION_MESSAGE);
				iWindow.dispose();
			} catch (NumberFormatException f) {
				JOptionPane.showMessageDialog(iWindow,"Width and Height must be expressed as postive Integers (whole numbers).", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception f) {
				System.out.println("Unknown Exception");
				System.out.println(f);
			}
		}
	}
}

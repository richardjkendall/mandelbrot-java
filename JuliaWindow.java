import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JuliaWindow implements ActionListener {
	JuliaPanel iPanel;
	
    public JuliaWindow(final double c_real, final double c_im, final boolean show_export) {
		JFrame iWindow = new JFrame("c = " + c_real + " + " + c_im);
		Container iContent = iWindow.getContentPane();
		Container iButtons = new Container();
		iButtons.setLayout(new FlowLayout());
		JButton iExportButton = new JButton("Export Image...");
		iExportButton.addActionListener(this);
		iExportButton.setActionCommand("export");
		iButtons.add(iExportButton);
		iPanel = new JuliaPanel(800, 600, 75, c_real, c_im);
		iContent.add(iPanel, BorderLayout.CENTER);
		if(show_export) {
			iContent.add(iButtons, BorderLayout.SOUTH);
		}
		if(show_export) {
			iWindow.setSize(800,660);
		} else {
			iWindow.setSize(800,600);
		}
		iWindow.setVisible(true);
		iWindow.setResizable(false);
    }
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("export")) {
			ExportImageWindow panel = new ExportImageWindow((ImagePanel)iPanel);
		}
	}
}

import java.awt.*;
import javax.swing.*;
/*
 * this class is used for settingn up
 * all the other classes into one
 * big JPanel that serves as the
 * final interface
 */
public class GUI 
{
	public GUI()
	{
		GUIwindow window = new GUIwindow("Digital Doilies");
		window.init();
	}
}

class GUIwindow extends JFrame
{
	public GUIwindow(String name)
	{
		super(name);
	}
	
	void init()
	{
		this.getContentPane();
		this.setLayout(new BorderLayout());

		Display dwindow = new Display();
		this.add(dwindow, BorderLayout.CENTER);
		Gallery gwindow = new Gallery();
		this.add(gwindow, BorderLayout.SOUTH);
		Control cwindow = new Control(dwindow,gwindow);
		this.add(cwindow, BorderLayout.EAST);
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE) ;
	    this.setSize(900,700);
	    this.setResizable(false);
	    this.setVisible(true);
	}
}



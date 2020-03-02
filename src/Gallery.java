import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/*
 * this class stores all the paintings we save, up to
 * a number of 12
 */
public class Gallery extends JPanel
{
	public List<JToggleButton> imageList = new ArrayList<>();//we store all the saved images as icons on toggle buttons
	public final int maxium_images = 12;
	
	public Gallery()
	{
		super();
		this.init();
	}
	
	public void init()
	{
		setLayout(new GridBagLayout());	
	}
}

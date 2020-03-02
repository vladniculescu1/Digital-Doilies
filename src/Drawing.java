import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/*
 * this class stores all the information for drawing lines
 */
public class Drawing
{
	public Color currentColour = Color.white;//this is the variable that stores the current colour. Default is white in contrast with the background
	public Color lastColour;//we need this variable to change back to after using the eraserS
	public int currentThickness = 4;
	public int noSec = 12;
	public boolean currentReflection = false;
	public List<List<Point>> pointList = new ArrayList<>();//we store all the points array into a list of lists
	public List<Point> points;//the points we draw from the mouse press up until the mouse release are stored in a list
	/*
	 * the following 3 lists store the colour,thickness
	 * and whether or not the point is reflected 
	 */
	public List<Color> colours = new ArrayList<>();
	public List<Integer> thickness = new ArrayList<>();
	public List<Boolean> reflections = new ArrayList<>();
	
	public List<List<Point>> deleteList = new ArrayList<>();//the list in which we put all the "undone" lines/points
	/*
	 * the information for each undone line/point needs to be recorder
	 * in order for the redo button to work
	 */
	public List<Color> deleteColours = new ArrayList<>();
	public List<Integer> deleteThickness = new ArrayList<>();
	public List<Boolean> deleteReflections = new ArrayList<>();
	
	/*
	 * the following methods are getter and setters
	 * for different variables
	 */
	public void setCurrentColour(Color colour)
	{
		currentColour = colour;
	}
	
	public int getThickness()
	{
		return currentThickness;
	}

	public void setThickness(int newThickness)
	{
		currentThickness = newThickness;
	}
	
	public void setNoSec(int newNo)
	{
		noSec = newNo;
	}
	
	public int getNoSec()
	{
		return noSec;
	}
}

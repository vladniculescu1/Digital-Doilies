import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicArrowButton;
/*
 * this is the class in which all the buttons and their
 * functionalities are stored
 */
public class Control extends JPanel
{
	/*
	 * we need references for both a display and gallery objects
	 * this ensures that any change we make in this class also
	 * affect the other classes
	 */
	Display d;
	Gallery g;
	public Control(Display display, Gallery gallery)
	{
		d = display;
		g = gallery;
		this.init();
	}
	
	void init()
	{
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		/*
		 * we declare all the buttons
		 */
		JButton undo = new JButton("Undo");
		JButton redo = new JButton("Redo");
		JButton clear = new JButton("Clear");
		JButton colourchange = new JButton("Colour");
		JRadioButton toggle = new JRadioButton("Hide Lines");
	    JRadioButton reflect = new JRadioButton("Toggle Reflection");
	    JRadioButton eraser = new JRadioButton("Eraser");
	    JButton save = new JButton("Save");
	    JButton delete = new JButton("Delete");
	    JLabel sectorsText = new JLabel("Number of Sectors");
	    JLabel thicknessText = new JLabel("Pen Size");
	    
		
	    /*
	     * for the number of sectors and thickness of the pen,
	     * we add up and down arrows that increase or decrease
	     * the respective values
	     */
		JPanel noSectors = new JPanel();
		BasicArrowButton upS = new BasicArrowButton(BasicArrowButton.NORTH);
		BasicArrowButton downS = new BasicArrowButton(BasicArrowButton.SOUTH);
		JTextField sectors = new JTextField(Integer.toString(d.draw.getNoSec()), 5);
		noSectors.setLayout(new FlowLayout());
		noSectors.add(upS);
		noSectors.add(downS);
		noSectors.add(sectors);
		
		
		JPanel thickness = new JPanel();
		BasicArrowButton upT = new BasicArrowButton(BasicArrowButton.NORTH);
		BasicArrowButton downT = new BasicArrowButton(BasicArrowButton.SOUTH);
		JTextField thick = new JTextField(Integer.toString(d.draw.getThickness()), 5);
		thickness.setLayout(new FlowLayout());
		thickness.add(upT);
		thickness.add(downT);
		thickness.add(thick);
	    
		/*
		 * we add listeners for all the buttons
		 */
		
		colourchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//whenever the button is pressed, we open a new windows that allows the user to select a colour
				Color colour = JColorChooser.showDialog(d,"Choose a colour",Color.blue);
				d.draw.setCurrentColour(colour);//the colour selected becomes the current colour
			}
			
		}
		);
		
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(!d.draw.pointList.isEmpty())//we don't undo if there is nothing to undo
				{
					/*
					 * whenever we undo the last drawing, we move the points and their
					 * respective information to 'delete lists'
					 * this ensures that the drawing is not displayed but are not lost
					 */
					d.draw.deleteList.add(d.draw.pointList.get(d.draw.pointList.size()-1));
					d.draw.pointList.remove(d.draw.pointList.size()-1);
					d.draw.deleteColours.add(d.draw.colours.get(d.draw.colours.size()-1));
					d.draw.colours.remove(d.draw.colours.size()-1);
					d.draw.deleteThickness.add(d.draw.thickness.get(d.draw.thickness.size()-1));
					d.draw.thickness.remove(d.draw.thickness.size()-1);
					d.draw.deleteReflections.add(d.draw.reflections.get(d.draw.reflections.size()-1));
					d.draw.reflections.remove(d.draw.reflections.size()-1);
				}
				else
				{
					//if there is nothing to undo, we display an error message
					JOptionPane.showMessageDialog(d,"There is nothing to undo!","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);
		
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				/*
				 * this button has the opposite function of undo
				 * we move the drawing and information from the 'delete lists'
				 * to the actual lists, thus displaying the drawings
				 */
				if(!d.draw.deleteList.isEmpty())
				{
					d.draw.pointList.add(d.draw.deleteList.get(d.draw.deleteList.size()-1));
					d.draw.deleteList.remove(d.draw.deleteList.size()-1);
					d.draw.colours.add(d.draw.deleteColours.get(d.draw.deleteColours.size()-1));
					d.draw.deleteColours.remove(d.draw.deleteColours.size()-1);
					d.draw.thickness.add(d.draw.deleteThickness.get(d.draw.deleteThickness.size()-1));
					d.draw.deleteThickness.remove(d.draw.deleteThickness.size()-1);
					d.draw.reflections.add(d.draw.deleteReflections.get(d.draw.deleteReflections.size()-1));
					d.draw.deleteReflections.remove(d.draw.deleteReflections.size()-1);
				}
				else
				{
					JOptionPane.showMessageDialog(d,"There is nothing to redo!","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				/*
				 * this button empties all our lists. By emptying the drawing lists we clear the screen
				 * and by emptying the delete lists we ensure that when new points are drawn, there are no
				 * information retrieval anomalies.
				 * Since deleting all the information is permanent, we want to make sure that the user didn't
				 * click the button by mistake and lost all their drawings. We enforce this with a warning 
				 * panel popping up whenever the user presses the button.
				 */
				int n = JOptionPane.showConfirmDialog(
					    d,
					    "Clearing the panel is irreversible!\n" + "Are you sure you want to continue?",
					    "Warning",
					    JOptionPane.YES_NO_OPTION);
				
				if(n == JOptionPane.YES_OPTION)	
				{
					d.draw.pointList.clear();
					d.draw.deleteList.clear();
					d.draw.colours.clear();
					d.draw.deleteColours.clear();
					d.draw.thickness.clear();
					d.draw.deleteThickness.clear();
					d.draw.reflections.clear();
					d.draw.deleteReflections.clear();
				}
			}
		}
		);
		
		eraser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				/*
				 * the eraser button changes the colour of the pen to the background colour,
				 * giving the illusion of deleting lines
				 */
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					d.draw.lastColour = d.draw.colours.get(d.draw.colours.size()-1);//we need to record the last colour used in order the change back to it
			    	d.draw.setCurrentColour(d.getBackground());
				}
			    else
			    	d.draw.setCurrentColour(d.draw.lastColour);
		}
		});
		
		toggle.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				    d.showLines = !d.showLines;//whenever the button is pressed, we decide if we need to draw or not the sector lines
			}
			});
		
		reflect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				    d.draw.currentReflection = !d.draw.currentReflection;//whenever the button is pressed, we decide if the drawing will we reflected or not
			}
			});
		
		upT.addActionListener(new ActionListener() {//this button increases the thickness by 1
			public void actionPerformed(ActionEvent e)
			{
				d.draw.setThickness(d.draw.getThickness() + 1);
				thick.setText(Integer.toString(d.draw.getThickness()));
			}
		}
		);
		
		downT.addActionListener(new ActionListener() {//this button decreases the thickness by 1
			public void actionPerformed(ActionEvent e)
			{
				if(d.draw.getThickness() != 1)
				{
					d.draw.setThickness(d.draw.getThickness() - 1);
					thick.setText(Integer.toString(d.draw.getThickness()));
				}
				else
				{
					JOptionPane.showMessageDialog(d,
						    "Thickness can't be less than 1.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);
		
		thick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
				   Integer.parseInt(thick.getText());//we check if the input is a number
				} 
				catch (NumberFormatException exception)
				{
					int temp = d.draw.getThickness();
					JOptionPane.showMessageDialog(d,
						    "Invalid input!",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					thick.setText(Integer.toString(temp));
				}
				if(Integer.parseInt(thick.getText()) < 1)//we make sure that thickness isn't lower than 1
				{
					JOptionPane.showMessageDialog(d,
						    "Thickness can't be less than 1.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					d.draw.setThickness(1);
					thick.setText(Integer.toString(d.draw.getThickness()));
				}
				else
				{
					d.draw.setThickness(Integer.parseInt(thick.getText()));
				}
			}
		}
		);
		
		upS.addActionListener(new ActionListener() {//this button increases the number of sectors by 1
			public void actionPerformed(ActionEvent e)
			{
				d.draw.setNoSec(d.draw.getNoSec() + 1);
				sectors.setText(Integer.toString(d.draw.getNoSec()));
			}
		}
		);
		
		downS.addActionListener(new ActionListener() {//this button decreases the number of sectors by 1
			public void actionPerformed(ActionEvent e)
			{
				if(d.draw.getNoSec() != 1)
				{
					d.draw.setNoSec(d.draw.getNoSec() - 1);
					sectors.setText(Integer.toString(d.draw.getNoSec()));
				}
				else
				{
					JOptionPane.showMessageDialog(d,
						    "There can't be less than 1 sector.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		);
		
		sectors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
				   Integer.parseInt(sectors.getText());//wwe check if the input is a number
				} 
				catch (NumberFormatException exception)
				{
					int temp = d.draw.getNoSec();
					JOptionPane.showMessageDialog(d,
						    "Invalid input!",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					sectors.setText(Integer.toString(temp));
				}
				if(Integer.parseInt(sectors.getText()) < 1)//we make sure that the number of sectors isn't lower than 1
				{
					JOptionPane.showMessageDialog(d,
						    "There can't be less than 1 sector.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					d.draw.setNoSec(1);
					sectors.setText(Integer.toString(d.draw.getNoSec()));
				}
				else
				{
					d.draw.setNoSec(Integer.parseInt(sectors.getText()));
				}
			}
		}
		);
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//this button saves the current drawing
				if(g.imageList.size() < g.maxium_images)//we only save the drawing if we haven't reached the maximum number of drawings
				{
				/*
				 * we store the current drawing as an icon for a toggle button
				 * we then display the new created button in the gallery panel
				 * and store it in an array
				 */
				Image scaledImage;
				BufferedImage selectedImage;
				JToggleButton newImage = new JToggleButton();
				
				selectedImage = d.screenshot();
				Graphics2D g2D = selectedImage.createGraphics();
				
				//we set the spacing between the button icon and the margins
				Border line;
				Border margin;
				Border compound;
				line = new LineBorder(Color.black);
				margin = new EmptyBorder(1,1,1,1);
				compound = new CompoundBorder(line, margin);
				newImage.setBorder(compound);
				
				scaledImage = selectedImage.getScaledInstance(145,112,Image.SCALE_SMOOTH);//we set the size of the buttons
				
				/*
				 * when the button is selected, we draw a red box
				 * over its margins to make it clear to the user
				 */
				g2D.drawImage(scaledImage,0,0,null);
				g2D.setColor(Color.red);
				g2D.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2D.drawLine(1, 1, 144, 1);
				g2D.drawLine(144, 1, 144,111);
				g2D.drawLine(144, 111, 1,111);
				g2D.drawLine(1, 111, 1,1);
				newImage.setSelectedIcon(new ImageIcon(selectedImage));
				newImage.setIcon(new ImageIcon(scaledImage));
				
				GridBagConstraints c = new GridBagConstraints();
				if(g.imageList.size() < g.maxium_images/2)//the first 6 images are displayed on the first row
				{
					c.gridy = 0;
				}
				else//the last 6 images are displayed on the second row
				{
					c.gridy = 1;
				}
				
				
				g.imageList.add(newImage);
				g.add(newImage,c);
				
				repaint();
				revalidate();
				}
				else//if we have reached the image limit, we display an error message
				{
					JOptionPane.showMessageDialog(d,
						    "You can't save more than 12 images!",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		}
		);
		
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				/*
				 * this button iterates over the toggle buttons that 
				 * represent the saved images. All the selected buttons
				 * are then deleted
				 */
				JToggleButton currentImage;
				Iterator<JToggleButton> it = g.imageList.iterator();
				
				while(it.hasNext())
				{
					currentImage = it.next();
					if(currentImage.isSelected())
					{
						g.remove(currentImage);
						it.remove();
					}
				}
				repaint();
				revalidate();
			}
		}
		);
		
		//we center all the buttons of the panel
	    undo.setAlignmentX(Component.CENTER_ALIGNMENT);
		redo.setAlignmentX(Component.CENTER_ALIGNMENT);
		clear.setAlignmentX(Component.CENTER_ALIGNMENT);
		colourchange.setAlignmentX(Component.CENTER_ALIGNMENT);
		toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
		reflect.setAlignmentX(Component.CENTER_ALIGNMENT);
		eraser.setAlignmentX(Component.CENTER_ALIGNMENT);
		save.setAlignmentX(Component.CENTER_ALIGNMENT);
		noSectors.setAlignmentX(Component.CENTER_ALIGNMENT);
		thickness.setAlignmentX(Component.CENTER_ALIGNMENT);
		delete.setAlignmentX(Component.CENTER_ALIGNMENT);
		sectorsText.setAlignmentX(Component.CENTER_ALIGNMENT);
		thicknessText.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//we add vertical glue between the buttons to space them properly in the panel
		add(Box.createVerticalGlue());
	    add(undo);
	    add(Box.createVerticalGlue());
	    add(redo);
	    add(Box.createVerticalGlue());
		add(colourchange);
		add(Box.createVerticalGlue());
		add(clear);
	    add(Box.createVerticalGlue());
		add(toggle);
		add(Box.createVerticalGlue());
		add(reflect);
		add(Box.createVerticalGlue());
		add(eraser);
		add(Box.createVerticalGlue());
		add(sectorsText);
		add(noSectors);
		add(Box.createVerticalGlue());
		add(thicknessText);
		add(thickness);
		add(Box.createVerticalGlue());
		add(save);
		add(Box.createVerticalGlue());
		add(delete);
		add(Box.createVerticalGlue());
	}
}

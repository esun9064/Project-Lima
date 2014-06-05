package project.pad;

import java.awt.*;
import javax.swing.JPanel;

/**
 * Interface for sending messages
 * @author Team Lima
 */
public class InputPad extends Panel
{
	public TextField inputWords=new TextField("",20);
	public Choice userChoice=new Choice();
	
	/**
	 * Initialize new input pad.
	 */
	public InputPad()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		for(int i=0;i<50;i++)
		{
			userChoice.addItem(i+"."+"No Users");
		}
		userChoice.setSize(60,24);
		add(userChoice);
		add(inputWords);
	}
}
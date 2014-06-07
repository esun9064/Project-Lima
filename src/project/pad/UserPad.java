package project.pad;
import java.awt.*;
import javax.swing.JPanel;

/**
 * Interface for displaying currently online users.
 * @author Team Lima
 */
public class UserPad extends Panel
{
	public List userList=new List(10);
	
	/**
	 * Initialize new input pad.
	 */
	public UserPad()
	{
		setLayout(new BorderLayout());
		
		for(int i=0;i<50;i++)
		{
			userList.add(i+"."+"No Users");
		}
		add(userList,BorderLayout.CENTER);
		
	}
	
}
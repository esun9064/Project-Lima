package project.pad;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * Class represents the chat panel used in the client.
 * @author Team Lima
 */
public class ChatPad extends Panel
{
	public TextArea chatLineArea=new TextArea("",18,30);
	
	/**
	 * Constructs a new ChatPad, sets the layout and adds a TextArea with scroll bar.
	 */
	public ChatPad()
	{
		setLayout(new BorderLayout());
		
		add(new JScrollPane(chatLineArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}
	
}
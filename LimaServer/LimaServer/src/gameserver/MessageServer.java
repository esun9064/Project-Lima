/*
 * Created on 2014-5-20
 */

package gameserver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;

/**
 *
 * @author Jack
 */
public class MessageServer extends Panel{
    TextArea messageBoard=new TextArea("",22,50,TextArea.SCROLLBARS_VERTICAL_ONLY);
    Label statusLabel=new Label("Number of Connected:",Label.LEFT);
    Panel boardPanel=new Panel();
    Panel statusPanel=new Panel();

    /** 
     * Constructor of MessageServer. 
    */
    public MessageServer(){
        setSize(350,300);
        setBackground(Color.pink);
        setLayout(new BorderLayout());
        boardPanel.setLayout(new FlowLayout());
        boardPanel.setSize(210,210);
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setSize(210,50);
        boardPanel.add(messageBoard);
        statusPanel.add(statusLabel,BorderLayout.WEST);
        add(boardPanel,BorderLayout.CENTER);
        add(statusPanel,BorderLayout.NORTH);
    }
}

 

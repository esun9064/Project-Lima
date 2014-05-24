/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project.lima;

import project.thread.*;
import project.pad.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.*;
import project.card.*;
import project.deck.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import project.card.Card.ability;


public class ProjectLima extends Frame implements ActionListener,KeyListener
{
 public UserPad userpad=new UserPad();
 public ChatPad chatpad=new ChatPad();
 public ControlPad controlpad=new ControlPad();
 public GamePad gamepad=new GamePad(controlpad);
 public InputPad inputpad=new InputPad();


 public Socket chatSocket;
 public DataInputStream in;
 public DataOutputStream out;
 public String gameClientName=null;
 public String host=null;
 public int port=4331;

 boolean isOnChat=false;  //�����죿
 public boolean isOnGame=false; //�����壿
 boolean isGameConnected=false; //����Ŀͻ������ӣ�
 public boolean isServer=false; //��������������
 public boolean isClient=false; //���������Ŀͻ���

 Panel southPanel=new Panel();
 Panel northPanel=new Panel();
 //Panel centerPanel=new Panel();
 Panel westPanel=new Panel();
 Panel eastPanel=new Panel();

 	//overall menubar
	public static JMenuBar gameMenu = new JMenuBar();
	public static JMenu file = new JMenu("File");
	public static JMenuItem quit = new JMenuItem("Quit");
        
 ProjectLima()
 {
  super("Java ...Game Client");
  setLayout(new BorderLayout());
  host=controlpad.inputIP.getText();

  westPanel.setLayout(new BorderLayout());
  westPanel.add(userpad,BorderLayout.NORTH);
  westPanel.add(chatpad,BorderLayout.CENTER);
  westPanel.add(inputpad,BorderLayout.SOUTH);
  westPanel.setBackground(Color.pink);

  inputpad.inputWords.addKeyListener(this);
  gamepad.host=controlpad.inputIP.getText();

  //centerPanel.add(gamepad,BorderLayout.CENTER);
  //centerPanel.add(inputpad,BorderLayout.SOUTH);
  //centerPanel.setBackground(Color.pink);

  controlpad.connectButton.addActionListener(this);
  controlpad.creatGameButton.addActionListener(this);
  controlpad.joinGameButton.addActionListener(this);
  controlpad.cancelGameButton.addActionListener(this);
  controlpad.exitGameButton.addActionListener(this);

  controlpad.creatGameButton.setEnabled(false);
  controlpad.joinGameButton.setEnabled(false);
  controlpad.cancelGameButton.setEnabled(false);

  southPanel.add(controlpad,BorderLayout.CENTER);
  //southPanel.setBackground(Color.pink);


  addWindowListener(new WindowAdapter()
  {
   public void windowClosing(WindowEvent e)
   {
     if(isOnChat)
     {
      try
      {
       chatSocket.close();
      }
      catch(Exception ed)
      {
      }
     }
     if(isOnGame || isGameConnected)
     {
      try
      {
       gamepad.gameSocket.close();
      }
      catch(Exception ee)
      {
      }
     }
    System.exit(0);
   }
   public void windowActivated(WindowEvent ea)
   {

   }
  });

       add(westPanel,BorderLayout.WEST);
      add(gamepad,BorderLayout.CENTER);
      add(southPanel,BorderLayout.SOUTH);

  pack();
  setSize(1366,720);
  setVisible(true);
  setResizable(false);
  //add main menu items
				file.add(quit);
				gameMenu.add(file);
                                
  validate();
 }

 

 public boolean connectServer(String serverIP,int serverPort) throws Exception
 {
  try
  {
   chatSocket=new Socket(serverIP,serverPort);
   in=new DataInputStream(chatSocket.getInputStream());
   out=new DataOutputStream(chatSocket.getOutputStream());

   ClientThread clientthread=new ClientThread(this);
   clientthread.start();
   isOnChat=true;
   return true;
  }
  catch(IOException ex)
  {
   chatpad.chatLineArea.setText("chessClient:connectServer: Connect failed, please restart the client \n");
  }
  return false;
 }


  public void actionPerformed(ActionEvent e)
  {
   if(e.getSource()==controlpad.connectButton)
   {
        host=gamepad.host=controlpad.inputIP.getText();
        try
        {
         if(connectServer(host,port))
         {
          chatpad.chatLineArea.setText("");
          controlpad.connectButton.setEnabled(false);
          controlpad.creatGameButton.setEnabled(true);
          controlpad.joinGameButton.setEnabled(true);
          controlpad.statusText.setText("Connected, please create or join a game");
         }


        }
        catch(Exception ei)
        {
         chatpad.chatLineArea.setText("controlpad.connectButton:Connect failed, please restart the client \n");
        }
    }
   else if(e.getSource()==controlpad.exitGameButton)
    {
        if(isOnChat)
        {
         try
         {
          chatSocket.close();
         }
         catch(Exception ed)
         {
         }
        }
        if(isOnGame || isGameConnected)
        {
         try
         {
          gamepad.gameSocket.close();
         }
         catch(Exception ee)
         {
         }
        }
        System.exit(0);

    }else if(e.getSource()==controlpad.joinGameButton)  {
        String selectedUser=userpad.userList.getSelectedItem();
        if(selectedUser==null || selectedUser.startsWith("[inchess]") ||
          selectedUser.equals(gameClientName))
        {
         controlpad.statusText.setText("Please select a user...");
        }
        else
        {
         try
         {
          if(!isGameConnected)
          {
           if(gamepad.connectServer(gamepad.host,gamepad.port))
           {
            isGameConnected=true;
            isOnGame=true;
            isClient=true;
            controlpad.creatGameButton.setEnabled(false);
            controlpad.joinGameButton.setEnabled(false);
            controlpad.cancelGameButton.setEnabled(true);
            gamepad.gamethread.sendMessage("/joingame "+userpad.userList.getSelectedItem()+" "+gameClientName);
           }
          }
          else
          {
           isOnGame=true;
           isClient=true;
           controlpad.creatGameButton.setEnabled(false);
           controlpad.joinGameButton.setEnabled(false);
           controlpad.cancelGameButton.setEnabled(true);
           gamepad.gamethread.sendMessage("/joingame "+userpad.userList.getSelectedItem()+" "+gameClientName);
          }


         }
         catch(Exception ee)
         {
          isGameConnected=false;
          isOnGame=false;
          isClient=false;
          controlpad.creatGameButton.setEnabled(true);
          controlpad.joinGameButton.setEnabled(true);
          controlpad.cancelGameButton.setEnabled(false);
          chatpad.chatLineArea.setText("gamepad.connectServer Connect failed \n"+ee);
         }

        }
    }else if(e.getSource()==controlpad.creatGameButton) {
        try
        {
         if(!isGameConnected)
         {
          if(gamepad.connectServer(gamepad.host,gamepad.port))
          {
           isGameConnected=true;
           isOnGame=true;
           isServer=true;
           controlpad.creatGameButton.setEnabled(false);
           controlpad.joinGameButton.setEnabled(false);
           controlpad.cancelGameButton.setEnabled(true);
           gamepad.gamethread.sendMessage("/creatgame "+"[inchess]"+gameClientName);
          }
         }
         else
         {
          isOnGame=true;
          isServer=true;
          controlpad.creatGameButton.setEnabled(false);
          controlpad.joinGameButton.setEnabled(false);
          controlpad.cancelGameButton.setEnabled(true);
          gamepad.gamethread.sendMessage("/creatgame "+"[inchess]"+gameClientName);
         }
        }
        catch(Exception ec)
        {
         isGameConnected=false;
         isOnGame=false;
         isServer=false;
         controlpad.creatGameButton.setEnabled(true);
         controlpad.joinGameButton.setEnabled(true);
         controlpad.cancelGameButton.setEnabled(false);
         ec.printStackTrace();
         chatpad.chatLineArea.setText("gamepad.connectServer Connect failed \n"+ec);
        }

    }else if(e.getSource()==controlpad.cancelGameButton){
            if(isOnGame)
            {
          gamepad.gamethread.sendMessage("/giveup "+gameClientName);
          //cardpad.chessVictory(-1*gamepad.chessColor);
          controlpad.creatGameButton.setEnabled(true);
          controlpad.joinGameButton.setEnabled(true);
          controlpad.cancelGameButton.setEnabled(false);
          controlpad.statusText.setText("Please create or join a game");
         }
         if(!isOnGame)
         {
          controlpad.creatGameButton.setEnabled(true);
          controlpad.joinGameButton.setEnabled(true);
          controlpad.cancelGameButton.setEnabled(false);
          controlpad.statusText.setText("Please create or join a game");
         }
         isClient=isServer=false;
    }

   }

 

   public void keyPressed(KeyEvent e)
   {
    TextField inputWords=(TextField)e.getSource();


    if(e.getKeyCode()==KeyEvent.VK_ENTER)
    {
     if(inputpad.userChoice.getSelectedItem().equals("All"))
     {
      try
      {
       out.writeUTF(inputWords.getText());
       inputWords.setText("");
      }
      catch(Exception ea)
      {
       chatpad.chatLineArea.setText("chessClient:KeyPressed Connect failed, please retry \n");
       userpad.userList.removeAll();
       inputpad.userChoice.removeAll();
       inputWords.setText("");
       controlpad.connectButton.setEnabled(true);
      }
     }
     else
     {
      try
      {
       out.writeUTF("/"+inputpad.userChoice.getSelectedItem()+" "+inputWords.getText());
       inputWords.setText("");
      }
      catch(Exception ea)
      {
       chatpad.chatLineArea.setText("chessClient:KeyPressed Connect failed, please retry  \n");
       userpad.userList.removeAll();
       inputpad.userChoice.removeAll();
       inputWords.setText("");
       controlpad.connectButton.setEnabled(true);
      }
     }
      }

   }

   public void keyTyped(KeyEvent e)
   {
   }
   public void keyReleased(KeyEvent e)
   {
   }

 

   public static void main(String args[])
   {
    ProjectLima chessClient=new ProjectLima();
   }
}


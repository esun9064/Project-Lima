/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.pad;

import java.awt.*;
import javax.swing.JLabel;

public class ControlPad extends Panel
{
 public Label IPlabel=new Label("IP",Label.LEFT);
 public TextField inputIP=new TextField("localhost",10);
 public Button connectButton=new Button("Connect Server");
 public Button creatGameButton=new Button("Create Game");
 public Button joinGameButton=new Button("Join Game");
 public Button cancelGameButton=new Button("Abandon Game");
 public Button exitGameButton=new Button("Quit");

 public JLabel statusText=new JLabel("Please connect to server first.");
 
 public ControlPad()
 {
  setLayout(new FlowLayout(FlowLayout.LEFT));
        //setBackground(Color.pink);

  add(IPlabel,BorderLayout.EAST);
  add(inputIP);
  add(connectButton);
  add(creatGameButton);
  add(joinGameButton);
  add(cancelGameButton);
  add(exitGameButton);
  add(statusText, BorderLayout.WEST);
 }

}
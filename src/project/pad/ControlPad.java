/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.pad;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ControlPad extends Panel
{
 public Label IPlabel = new Label("IP",Label.LEFT);
 public TextField inputIP = new TextField("localhost",10);
 public JButton connectButton = new JButton("Connect Server");
 public JButton creatGameButton = new JButton("Create Game");
 public JButton joinGameButton = new JButton("Join Game");
 public JButton cancelGameButton = new JButton("Abandon Game");
 public JButton readyButton = new JButton("Ready to Play");
 public JButton endTurnButton = new JButton("End Turn");
 public JButton exitGameButton = new JButton("Quit");

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
  add(readyButton);
  readyButton.setEnabled(false);
  endTurnButton.setEnabled(false);
  add(endTurnButton);
  add(exitGameButton);
  add(statusText, BorderLayout.WEST);
 }

}
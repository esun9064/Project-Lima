/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.pad;

import java.awt.*;
import javax.swing.JPanel;

public class InputPad extends JPanel
{
 public TextField inputWords=new TextField("",40);
 public Choice userChoice=new Choice();

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


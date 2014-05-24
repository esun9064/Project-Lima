/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.pad;
import java.awt.*;
import javax.swing.JPanel;

public class UserPad extends Panel
{
 public List userList=new List(10);

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
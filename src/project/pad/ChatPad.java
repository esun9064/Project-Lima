/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.pad;

import java.awt.*;
import javax.swing.JPanel;


public class ChatPad extends Panel
{
 public TextArea chatLineArea=new TextArea("",18,30,TextArea.SCROLLBARS_VERTICAL_ONLY);

 public ChatPad()
 {
  setLayout(new BorderLayout());

  add(chatLineArea,BorderLayout.CENTER);
 }

}

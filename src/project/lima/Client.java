/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.lima;

import java.awt.BorderLayout;   
import java.awt.Button;   
import java.awt.TextArea;   
import java.awt.TextField;   
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;   
import java.io.BufferedReader;   
import java.io.IOException;   
import java.io.InputStreamReader;   
import java.io.PrintStream;   
import java.net.Socket;   
import java.net.UnknownHostException;   
import javax.swing.JFrame;   
  
@SuppressWarnings("serial")   
public class Client extends JFrame {   
    TextArea textArea = new TextArea();// 创建一个文本域   
    TextField textField = new TextField();// 创建一个文本框   
  
    Button button_send = new Button("发送");   
    Button button_clear = new Button("清屏");   
  
    static Socket clientLink;   
    BufferedReader br;   
    PrintStream ps;   
    String msg;   
    StringBuffer sb = new StringBuffer();   
  
    static {   
        try {   
            clientLink = new Socket("127.0.0.1",8888);   
        } catch (UnknownHostException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }   
  
    public Client() {   
        super("客户端");   
    }   
  
    public void runClient() {   
        this.setLocation(400, 450);   
        this.setSize(400, 400);   
        this.add(textArea, BorderLayout.NORTH);// 把文本添加到窗体中   
        this.add(textField, BorderLayout.SOUTH);// 把文本框添加到窗体中   
        this.add(button_send, BorderLayout.EAST);// 把按钮添加到窗体中   
        this.add(button_clear, BorderLayout.WEST);   
        this.setVisible(true);// 窗体是否可见   
        this.pack();// 自动调整布局   
        textField.setText("请输入信息");   
  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
  
        try {   
  
            br = new BufferedReader(new InputStreamReader(clientLink   
                    .getInputStream()));   
            ps = new PrintStream(clientLink.getOutputStream());   
  
            // 发送按钮事件   
            button_send.addActionListener(new ActionListener() {   
                @Override  
                public void actionPerformed(ActionEvent e) {   
                    msg = textField.getText();   
                    System.out.println(msg);   
                    // 这里不能用缓冲流,向服务器发送信息   
                    ps.println(msg);   
                    ps.flush();   
                }   
  
            });   
  
            // 回车时,文本域事件   
            textField.addActionListener(new ActionListener() {   
                @Override  
                public void actionPerformed(ActionEvent e) {   
                    msg = textField.getText();   
                    // 这里不能用缓冲流写   
                    ps.println(msg);   
                    ps.flush();   
  
                }   
  
            });   
  
            // 清屏按钮事件   
            button_clear.addActionListener(new ActionListener() {   
                @Override  
                public void actionPerformed(ActionEvent e) {   
                    textArea.setText("");   
                    sb.delete(0, sb.length());   
                }   
            });   
  
            // 接受服务器信息   
            while (true) {   
                msg = br.readLine();   
                sb.append(msg + "\n");   
                textArea.setText(sb.toString());   
                textField.setText("");   
            }   
  
        } catch (UnknownHostException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                if (null != clientLink) {   
                    clientLink.close();   
  
                }   
                if (null != br) {   
                    br.close();   
                }   
                if (null != ps) {   
                    ps.close();   
                }   
  
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
  
    }   
  
    public static void main(String[] args) {   
        new Client().runClient();   
    }   
  
}   
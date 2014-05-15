/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.lib;

import java.io.BufferedReader;   
import java.io.IOException;   
import java.io.InputStreamReader;   
import java.io.PrintStream;   
import java.net.Socket;   
import java.util.List;   
  
public class ServerThread implements Runnable {   
    List<Socket> clientLinkList;   
    Socket clientLink;   
    BufferedReader br;   
    PrintStream ps;   
    String msg;   
    String ip;   
    public ServerThread(List<Socket> clientLinkList, Socket clientLink) {   
        this.clientLinkList = clientLinkList;   
        this.clientLink = clientLink;   
    }   
  
    @Override  
    public void run() {   
  
        try {   
            br = new BufferedReader(new InputStreamReader(clientLink   
                    .getInputStream()));               
            //获得当前用户的IP   
            ip=clientLink.getInetAddress().getHostAddress();   
               
            //把当前用户发来的信息发送给所有用户.   
            while (null != (msg=br.readLine())) {          
                for (int i = 0; i < clientLinkList.size(); i++) {   
                    ps = new PrintStream(clientLinkList.get(i)   
                            .getOutputStream());   
                    ps.println("IP为:"+ip+"的朋友说:"+msg+"\n");   
                       
                    ps.flush();   
                }   
            }   
               
               
           
        } catch (IOException e) {   
            e.printStackTrace();   
        }finally{   
            try {   
                if(null!=clientLink){   
                    clientLink.close();   
                       
                }   
                if(null!=br){   
                    br.close();   
                }   
                if(null!=ps){   
                    ps.close();   
                }   
                   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
  
    }   
  
}  
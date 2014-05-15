/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project.lib;

import java.io.IOException;   
import java.net.ServerSocket;   
import java.net.Socket;   
import java.util.ArrayList;   
import java.util.List;   
  
/**  
 * @author GYH  
 *   
 */  
public class Server {   
  
    static ServerSocket server;   
    Socket clientLink;   
    List<Socket> clientLinkList = new ArrayList<Socket>();   
    int count;   
  
    static {   
        try {   
            // open server   
            server = new ServerSocket(8888);   
            System.out.println("Server opened!");   
  
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }   
  
    public void runServer() {   
        try {   
            while (true) {   
                clientLink = server.accept();   
                System.out.println("number of connected:" + (++count));   
                clientLinkList.add(clientLink);   
                ServerThread st = new ServerThread(clientLinkList, clientLink);   
                new Thread(st).start();   
            }   
  
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
  
    }    
  
}  
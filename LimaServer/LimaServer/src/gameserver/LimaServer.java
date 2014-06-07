/*
 * Created on 2014-05-20
 */

package gameserver;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 *
 * @author Jack
 */
public class LimaServer extends Frame implements ActionListener{
    Button messageClearButton=new Button("Clear");
    Button serverStatusButton=new Button("Serve Status");
    Button serverOffButton=new Button("Close Server");
    Panel buttonPanel=new Panel();

    MessageServer server=new MessageServer();
    ServerSocket serverSocket;
    Hashtable clientDataHash=new Hashtable(50);
    Hashtable clientNameHash=new Hashtable(50);
    Hashtable gamePeerHash=new Hashtable(50);

    /** 
     * Constructor for LimaServer. 
     * <br>
     * Initiate user interface
     */ 
    public LimaServer(){
        super("Lima Game Server");
        setBackground(Color.pink);


        buttonPanel.setLayout(new FlowLayout());
        messageClearButton.setSize(60,25);
        buttonPanel.add(messageClearButton);
        messageClearButton.addActionListener(this);
        serverStatusButton.setSize(75,25);
        buttonPanel.add(serverStatusButton);
        serverStatusButton.addActionListener(this);
        serverOffButton.setSize(75,25);
        buttonPanel.add(serverOffButton);
        serverOffButton.addActionListener(this);

        add(server,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
             System.exit(0);
            }
        });

        pack();
        setVisible(true);
        setSize(400,450);
        setResizable(false);
        validate();
        
        try
        {
            makeMessageServer(4331,server);
        }
        catch(Exception e)
        {
            System.out.println("e");
        }
    }

    /** 
     * wait connections from clients. 
     * @param port, 4331;
     * @param server, instance of user interface--MessageServer;<br>
     */ 
    public void makeMessageServer(int port, MessageServer server) throws IOException
    {
        Socket clientSocket;
        long clientAccessNumber=1;
        this.server=server;

        try
        {
            serverSocket=new ServerSocket(port);
            server.messageBoard.setText("Server Started at:"+serverSocket.getInetAddress().getLocalHost()+":"+serverSocket.getLocalPort()+"\n");

            while(true)
            {
                clientSocket=serverSocket.accept();
                server.messageBoard.append("User Connected:"+clientSocket+"\n");

                DataOutputStream outData=new DataOutputStream(clientSocket.getOutputStream());

                clientDataHash.put(clientSocket,outData);
                clientNameHash.put(clientSocket,("NewPlayer"+clientAccessNumber++));

                ServerThread thread=new ServerThread(clientSocket, clientDataHash, clientNameHash, gamePeerHash, server);

                thread.start();
            }
        }catch(IOException ex){
            System.out.println("Server is running. \n");
        }
    }

    /** 
     * capture button click events. 
     * @param e, action event;
     */ 
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==messageClearButton){
            server.messageBoard.setText("");
        }
        if(e.getSource()==serverStatusButton){
            try{
                server.messageBoard.append("From Server:"+serverSocket.getInetAddress().getLocalHost()+":"+serverSocket.getLocalPort()+"\n");
            }
            catch(Exception ee)
            {
                System.out.println("serverSocket.getInetAddress().getLocalHost() error \n");
            }
        }
        if(e.getSource()==serverOffButton){
            System.exit(0);
        }
    }

    /** 
     * main function. 
     */ 
    public static void main(String args[]){
        LimaServer limaServer=new LimaServer();
    }
}

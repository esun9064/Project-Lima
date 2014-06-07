/*
 * Created on 2014-05-20
 */

package gameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *
 * @author Jack
 */
public class ServerThread extends Thread{
    Socket clientSocket;
    Hashtable clientDataHash;
    Hashtable clientNameHash;
    Hashtable gamePeerHash;
    MessageServer server;

    boolean isClientClosed=false;

    /** 
     * Constructor of ServerThread. 
     * @param clientSocket, socket for a client.;
     * @param clientDataHash, Hashtable for client socket(key)--outputstream(value);
     * @param clientNameHash, Hashtable for client socket(key)--client name(value);
     * @param clientNameHash, Hashtable for client socket(key)--client name(value);
     * @param gamePeerHash, Hashtable for peers. clientname(key)--peername(value);
     * @param server, instance of user interface;
    */
    ServerThread(Socket clientSocket, Hashtable clientDataHash, Hashtable clientNameHash,
                    Hashtable gamePeerHash, MessageServer server){
        this.clientSocket=clientSocket;
        this.clientDataHash=clientDataHash;
        this.clientNameHash=clientNameHash;
        this.gamePeerHash=gamePeerHash;
        this.server=server;
    }

    /** 
     * Transfer message according to message type. 
     * @param message, received message.;
    */
    public void messageTransfer(String message){
        String clientName,peerName;

        System.out.println("message:"+message);
        
        if(message.startsWith("/")){

            if(message.startsWith("/changename ")){
                clientName=message.substring(12);
                if( clientName.length()<=0             || clientName.length()>20                   ||
                 clientName.startsWith("/")         || clientNameHash.containsValue(clientName) ||
                    clientName.startsWith("changename")|| clientName.startsWith("list")            ||
                    clientName.startsWith("[inchess]") || clientName.startsWith("creatgame")       ||
                    clientName.startsWith("joingame")  || clientName.startsWith("yourname")        ||
                    clientName.startsWith("userlist")  || clientName.startsWith("chess")           ||
                    clientName.startsWith("OK")        || clientName.startsWith("reject")          ||
                    clientName.startsWith("peer")      || clientName.startsWith("peername")        ||
                    clientName.startsWith("giveup")    || clientName.startsWith("youwin")          ||
                    clientName.startsWith("All"))
                {
                    message="Invalid Command";
                    Feedback(message);
                }else{
                    if(clientNameHash.containsValue(("[inchess]"+(String)clientNameHash.get(clientSocket)))){
                        synchronized(clientNameHash)
                        {
                            clientNameHash.put((Socket)getHashKey(clientNameHash,("[inchess]"+clientNameHash.get(clientSocket))),
                                 ("[inchess]"+clientName));
                            gamePeerTalk(("[inchess]"+clientName),("/yourname "+("[inchess]"+clientName)));
                        }
                    }else if(gamePeerHash.containsKey(clientNameHash.get(clientSocket))){
                        //change the name of game client
                        synchronized(clientNameHash)
                        {
                            clientNameHash.put((Socket)getHashKey(clientNameHash,("[inchess]"+clientNameHash.get(clientSocket))),
                                 ("[inchess]"+clientName));
                        }

                        synchronized(gamePeerHash)
                        {
                            //gamePeerHash: add new name
                            gamePeerHash.put(clientName,gamePeerHash.get(clientNameHash.get(clientSocket)));
                            gamePeerHash.remove(clientNameHash.get(clientSocket));
                        }
                        //send new name to clinet
                        gamePeerTalk(("[inchess]"+clientName),("/yourname "+("[inchess]"+clientName)));
                        //send to peer
                        gamePeerTalk((String)gamePeerHash.get(clientName),("/peer "+"[inchess]"+clientName));

                    }else if(gamePeerHash.containsValue(clientNameHash.get(clientSocket))){
                        synchronized(clientNameHash)
                        {
                            clientNameHash.put((Socket)getHashKey(clientNameHash,("[inchess]"+clientNameHash.get(clientSocket))),
                                       ("[inchess]"+clientName));
                        }
                        synchronized(gamePeerHash)
                        {
                            gamePeerHash.put((String)getHashKey(gamePeerHash,clientNameHash.get(clientSocket)),clientName);
                            gamePeerTalk(("[inchess]"+clientName),("/yourname "+("[inchess]"+clientName)));
                        }
                        gamePeerTalk((String)getHashKey(gamePeerHash,clientName),("/peer "+"[inchess]"+clientName));
                    }

                    message=clientNameHash.get(clientSocket)+"Name changed to:"+clientName;
                    synchronized(clientNameHash)
                    {
                        clientNameHash.put(clientSocket,clientName);
                    }
                    publicTalk(message);
                    Feedback("/yourname "+(String)clientNameHash.get(clientSocket));
                    publicTalk(getUserList());

                }
                }else if(message.equals("/list")){
                    Feedback(getUserList());
                }else if(message.startsWith("/creatgame [inchess]")){
                    String chessServerName=message.substring(20);
                    synchronized(clientNameHash)
                    {
                        clientNameHash.put(clientSocket,message.substring(11));
                    }
                    synchronized(gamePeerHash)
                    {
                        gamePeerHash.put(chessServerName,"wait");
                    }
                    Feedback("/yourname "+clientNameHash.get(clientSocket));
                    gamePeerTalk(chessServerName,"/OK");
                    publicTalk(getUserList());
                }else if(message.startsWith("/joingame ")){
                    StringTokenizer userToken=new StringTokenizer(message," ");
                    String getUserToken,serverName,selfName;
                    String[] chessNameOpt={"0","0"};
                    int getOptNum=0;

                    while(userToken.hasMoreTokens())
                    {
                        getUserToken=(String)userToken.nextToken(" ");
                        if(getOptNum>=1 && getOptNum<=2)
                        {
                         chessNameOpt[getOptNum-1]=getUserToken;
                        }
                        getOptNum++;
                    }
                    serverName=chessNameOpt[0];
                    selfName=chessNameOpt[1];

                    if(gamePeerHash.containsKey(serverName) && gamePeerHash.get(serverName).equals("wait")){
                        synchronized(clientNameHash)
                        {
                         clientNameHash.put(clientSocket,("[inchess]"+selfName));
                        }
                        synchronized(gamePeerHash)
                        {
                         gamePeerHash.put(serverName,selfName);
                        }
                        publicTalk(getUserList());
                        gamePeerTalk(selfName,("/peer "+"[inchess]"+serverName));
                        gamePeerTalk(serverName,("/peer "+"[inchess]"+selfName));
                    }else{
                        gamePeerTalk(selfName,"/reject");
                        try
                        {
                            clientClose();
                        }
                        catch(Exception ez){}
                       }
                }else if(message.startsWith("/[inchess]")){
                    int firstLocation=0,lastLocation;
                    lastLocation=message.indexOf(" ",0);

                    peerName=message.substring((firstLocation+1),lastLocation);
                    message=message.substring((lastLocation+1));
                    if(gamePeerTalk(peerName,message))
                    {
                        Feedback("/error");
                    }
                }else if(message.startsWith("/giveup ")){
                    String gameClientName=message.substring(8);
                    if(gamePeerHash.containsKey(gameClientName) && !((String)gamePeerHash.get(gameClientName)).equals("wait"))
                    {
                        gamePeerTalk((String)gamePeerHash.get(gameClientName),"/youwin");
                        synchronized(gamePeerHash)
                        {
                         gamePeerHash.remove(gameClientName);
                        }
                    }
                    if(gamePeerHash.containsValue(gameClientName))
                    {
                        gamePeerTalk((String)getHashKey(gamePeerHash,gameClientName),"/youwin");
                        synchronized(gamePeerHash)
                        {
                            gamePeerHash.remove((String)getHashKey(gamePeerHash,gameClientName));
                        }
                    }
                }else{

                    int firstLocation=0,lastLocation;
                    lastLocation=message.indexOf(" ",0);
                    if(lastLocation==-1){
                       Feedback("Invalid Command");
                       return;
                    }else{
                        peerName=message.substring((firstLocation+1),lastLocation);
                        message=message.substring((lastLocation+1));
                        message=(String)clientNameHash.get(clientSocket)+">"+message;
                        if(peerTalk(peerName,message))
                        {
                            Feedback("No User found:"+peerName+"\n");
                        }
                    }
               }
        } else{
            message=clientNameHash.get(clientSocket)+">"+message;
            server.messageBoard.append(message+"\n");
            publicTalk(message);
            server.messageBoard.setCaretPosition(server.messageBoard.getText().length());
        }
    }
    
    /** 
     * Transfer public chat messages. 
     * @param publicTalkMessage, message needs to be sent.;
    */
    public void publicTalk(String publicTalkMessage)
    {
        synchronized(clientDataHash)
        {
            for(Enumeration enu=clientDataHash.elements();enu.hasMoreElements();){
                DataOutputStream outData=(DataOutputStream)enu.nextElement();
                try
                {
                    outData.writeUTF(publicTalkMessage);
                }
                catch(IOException es)
                {
                    es.printStackTrace();
                }
            }
        }
    }


    /** 
     * Transfer private chat messages. 
     * @param peerTalk, peer name.;
     * @param publicTalkMessage, message needs to be sent.;
    */
    public boolean peerTalk(String peerTalk, String talkMessage)
    {
        for(Enumeration enu=clientDataHash.keys(); enu.hasMoreElements();)
        {
            Socket userClient=(Socket)enu.nextElement();

            if(peerTalk.equals((String)clientNameHash.get(userClient)) && !peerTalk.equals((String)clientNameHash.get(clientSocket)))
            {
                synchronized(clientDataHash)
                {
                    DataOutputStream peerOutData=(DataOutputStream)clientDataHash.get(userClient);
                    try
                    {
                     peerOutData.writeUTF(talkMessage);
                    }
                    catch(IOException es)
                    {
                     es.printStackTrace();
                    }
                }
                Feedback(talkMessage);
                return(false);
            }else if(peerTalk.equals((String)clientNameHash.get(clientSocket))){
                Feedback(talkMessage);
                return(false);
            }
        }
        return(true);
    }
    
    /** 
     * Transfer private chat messages. 
     * @param gamePeerTalk, peer name.;
     * @param gameTalkMessage, message needs to be sent.;
    */
    public boolean gamePeerTalk(String gamePeerTalk, String gameTalkMessage)
    {

        for(Enumeration enu=clientDataHash.keys();enu.hasMoreElements();)
        {
            Socket userClient=(Socket)enu.nextElement();

            if(gamePeerTalk.equals((String)clientNameHash.get(userClient)) && !gamePeerTalk.equals((String)clientNameHash.get(clientSocket)))
            {
                synchronized(clientDataHash)
                {
                    DataOutputStream peerOutData=(DataOutputStream)clientDataHash.get(userClient);
                    try
                    {
                        peerOutData.writeUTF(gameTalkMessage);
                    }
                    catch(IOException es)
                    {
                        es.printStackTrace();
                    }
                }
                return(false);
            }
        }
        return true;
    }

    /** 
     * Send back messages to sender. 
     * @param feedbackString, message needs to be sent.;
    */
    public void Feedback(String feedbackString){
        synchronized(clientDataHash)
        {
            DataOutputStream outData=(DataOutputStream)clientDataHash.get(clientSocket);
            try
            {
                outData.writeUTF(feedbackString);
            }
            catch(Exception eb)
            {
                eb.printStackTrace();
            }
        }
    }

    /** 
     * Return all connected users. 
    */
     public String getUserList(){
       String userList="/userlist";

       for(Enumeration enu=clientNameHash.elements();enu.hasMoreElements();)
       {
            userList=userList+" "+(String)enu.nextElement();
       }
       return(userList);
    }

    public Object getHashKey(Hashtable targetHash,Object hashValue){
       Object hashKey;
       for(Enumeration enu=targetHash.keys();enu.hasMoreElements();)
       {
            hashKey=(Object)enu.nextElement();
            if(hashValue.equals((Object)targetHash.get(hashKey)))
            return(hashKey);
        }
        return(null);
    }
    
    /** 
     * Send help messages to a newly connected user. 
    */
    public void firstCome()
    {
        publicTalk(getUserList());
        Feedback("/yourname "+(String)clientNameHash.get(clientSocket));
        Feedback("Java .....Game Client");
        Feedback("/changename <Your Name>  --Change Name");
        Feedback("/list --Update Connected List");
        Feedback("/<User Name> <Your words>  --Private Chat");
        
    }
    
    /** 
     * Clear hash table when a client disconnected. 
    */
    public void clientClose()
    {
        server.messageBoard.append("User Disconnected:"+clientSocket+"\n");
        
        synchronized(gamePeerHash)
        {
            if(gamePeerHash.containsKey(clientNameHash.get(clientSocket)))
            {
                gamePeerHash.remove((String)clientNameHash.get(clientSocket));
            }
            if(gamePeerHash.containsValue(clientNameHash.get(clientSocket)))
            {
                gamePeerHash.put((String)getHashKey(gamePeerHash,(String)clientNameHash.get(clientSocket)),"tobeclosed");
            }
        }
        synchronized(clientDataHash)
        {
            clientDataHash.remove(clientSocket);
        }
        synchronized(clientNameHash)
        {
            clientNameHash.remove(clientSocket);
        }
        publicTalk(getUserList());
        server.statusLabel.setText("Number of Connected:"+clientDataHash.size());
        
        try
        {
            clientSocket.close();
        }
        catch(IOException ex)
        {
        }
        isClientClosed=true;
    }

    /** 
     * The "run" method for the thread. 
    */
    public void run()
    {
        DataInputStream inData;
        synchronized(clientDataHash)
        {
            server.statusLabel.setText("Number of Connected:" + clientDataHash.size());
        }
        try{
            inData=new DataInputStream(clientSocket.getInputStream());
            firstCome();
            
            while(true)
            {
                String message=inData.readUTF();
                messageTransfer(message);
            }
        }
        catch(IOException esx){}
        finally
        {
            if(!isClientClosed)
            {
               clientClose();
            }
        }
    }
}

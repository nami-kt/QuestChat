package questchat.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import questchat.msg.*;

/**
 *
 * @author katja
 * 
 * Class for chat-room functional realization for player
 */
public class ChatClient {
    
    private boolean stopFlag = false;   // flags stopping the message flow
    private Tools       tools;          // class for socket interactions
    private Converter   converter;      // converts message to transport format and back
    private int id = -1;                // player's ID
    private String srvHost;             // server address
    private int    srvPort;             // server port

    public ChatClient() {
        try {
            tools       = Tools.getInstance();
            converter   = new Converter();
            System.out.println("start client");

            Thread rcvrThread = new Thread(receiver);
            rcvrThread.setName("client:receiver");
            rcvrThread.start();
            
        } catch (SocketException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSrvHost(String srvHost) {
        this.srvHost = srvHost;
        tools.setSrvHost(srvHost);
    }

    public void setSrvPort(int srvPort) {
        this.srvPort = srvPort;
        tools.setSrvPort(srvPort);
    }
    
    /*
    sets server and port in format address:port
    if port is not specified, uses 7777
    */
    public void setServer(String str){ // address:port
        if(str==null || str.length()==0) return;
        String[] parts = str.split(":");
        if(parts.length>0) setSrvHost(parts[0]);
        try{
            int port = (parts.length>1) ? Integer.parseInt(parts[1]) : 7777;
            setSrvPort(port);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
    }
    
    private static final int ST_ZERO=0, ST_REGISTRED=1;
    private int state = 0;
    
    private long timeout = 1000;

    // reads messages from UDP-socket and calls handlers
    Runnable receiver = new Runnable() {
        @Override
        public void run() {
            System.out.println("Start client receiver");
            while (!stopFlag) {
                try {
                    DatagramPacket pck = tools.recieve(timeout);
                    if (pck != null) //message
                    { // process message
                        byte[] buf = Tools.getData(pck);
                        //System.out.println("rcvd: "+(new String(buf)));

                        Message msg = Message.of(buf);
                        onMessage(msg);
                        
                        
                    } else {
                        //System.out.println("timeout");
                        onTimeout();
                    }                 
                } catch (SocketException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    };
    
    MessageListener listener = null;

    // registration of call-back methods for processing incoming messages
    public void setListener(MessageListener listener) {
        this.listener = listener;
    }
    
    // processing received message, unpacking from transport format
    // 
    private void onMessage(Message msg){
        System.out.println("rcvd: "+msg);
        
        //clear waiting
        if(waitingResponse && waitingType==msg.getType()) setWaiting(0, ' ');
        
        String body = msg.getBody();
        
        if(msg.getType()==Message.M_LOGIN_RS) {
            // processing response to connection request
            LoginRs rs = converter.loginRs(body);
            id = rs.getId();
            if(listener!=null) listener.onLoginRs(rs);
        } else if(msg.getType()=='T') {
            // processing message with information about chat-room
            TopicInfo ti = converter.topicInfo(body);
            if(listener!=null) listener.onTopicInfo(ti);
        } else if(msg.getType()=='Q') {
            // processing message with information about the task
            TaskInfo ti = converter.taskInfo(body);
            if(listener!=null) listener.onTaskInfo(ti);
        } else if(msg.getType()=='m') {
            // processing message for chat
            ChatMessageRs ti = converter.chatMessageRs(body);
            if(listener!=null) listener.onChatMessage(ti);
        }
    }
    
    // processing timeout (no new messages in given time interval)
    private void onTimeout(){
        //System.out.println("timeout");
        if(waitingResponse){
            // if an answer to a request is expected, calls listener.onTimeout()
            if(waitingTimeout<System.currentTimeMillis()){
                setWaiting(0, ' ');
                if(listener!=null) listener.onTimeout();
            }
        }
    }
    
    boolean waitingResponse = false;
    long    waitingTimeout  = 0;
    char    waitingType     = 0;
    
    // waiting for a server response of a given type turned on
    // if there is no response in given time interval, listener.onTimeout() is called
    // to - time interval in milliseconds
    // type - symbol of message type
    private void setWaiting(long to, char type){
        waitingResponse = (to!=0);
        waitingTimeout  = System.currentTimeMillis() + to;
        waitingType     = type;
        //System.out.println("setWaiting "+waitingResponse+" "+waitingTimeout+" "+waitingType);
    }
    
    // player login send to server
    public void login(String name) throws IOException{
        LoginRq loginRq = new LoginRq();
        loginRq.setLogin(name);
        Message msg = converter.toMessage(loginRq);
        System.out.println(msg);
        tools.sendToSrv(msg.toArray());
        setWaiting(1000, 'l');
    }
    
    // enter the chat-room
    public void enter(String name) throws IOException{
        EnterRq enterRq = new EnterRq();
        enterRq.setTopic(name);
        Message msg = converter.toMessage(enterRq);
        System.out.println(msg);
        tools.sendToSrv(msg.toArray());
        //setWaiting(1000, 'l');
    }
    
    // create a new chat-room
    public void create(String name, int rounds, int time) throws IOException{
        CreateTopicRq rq = new CreateTopicRq();
        rq.setTopic(name);
        rq.setRounds(rounds);
        rq.setTime(time);
        Message msg = converter.toMessage(rq);
        System.out.println(msg);
        tools.sendToSrv(msg.toArray());
        //setWaiting(1000, 'l');
    }
    
    // send task solution
    public void solution(String sol) throws IOException{
        SolutionMsg sm = new SolutionMsg(sol);
        Message msg = converter.toMessage(sm);
        System.out.println(msg);
        tools.sendToSrv(msg.toArray());
    }

    // send a message to chat
    public void guess(String str) throws IOException{
        ChatMessageRq m = new ChatMessageRq(str);
        Message msg = converter.toMessage(m);
        System.out.println(msg);
        tools.sendToSrv(msg.toArray());
    }

    private void stop(){
        stopFlag=true;
    }
    
    public void close(){
        stop();
        tools.close();
    }
    
    
}

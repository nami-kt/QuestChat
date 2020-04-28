package questchat.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import questchat.Context;
import questchat.data.TopicData;
import questchat.data.UserData;
import questchat.msg.*;

/**
 *
 * @author katja
 * 
 * Class implementing chat functional of chat for server
 */
public class ChatServer {

    private boolean stopFlag = false;   // flags stopping the message flow
    private Tools tools = null;         // class for interaction with with UDP-socket
    private Converter converter;        // converts message to transport format and back

    // class for storing context information
    private static Context context = Context.getInstance();

    public ChatServer() {
        try {
            tools       = Tools.getServerInstance(context.getPort());
            converter   = new Converter();
            
            Thread rcvrThread = new Thread(receiver);
            rcvrThread.setName("server:receiver");
            rcvrThread.start();
            
        } catch (SocketException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private long timeout = 1000;

    // calculating the wait time for the next message or event
    private long getNextTimeout(){
        if (hmScheduler.size() == 0) {
            return timeout;
        }
        long first = hmScheduler.firstKey() - System.currentTimeMillis();
        if (first <= 0) {
            return 10;
        }
        return first;
    }
    
    // participant counter used to generate identifiers for participants
    private int userCounter = 0;
    // registry of users
    private HashMap<Integer, UserData>  hmUsers  = new HashMap<>();
    // registry of chat-rooms
    private HashMap<String , TopicData> hmTopics = new HashMap<>();
    // timetable of task updates in chat-rooms
    private TreeMap<Long   , String>    hmScheduler = new TreeMap<>();
    
    
    // flow of loading messages
    Runnable receiver = new Runnable() {
        @Override
        public void run() {
            System.out.println("Start server receiver");
            while (!stopFlag) {
                try {
                    long tout = getNextTimeout();
                    System.out.println("tout="+tout);
                    DatagramPacket pck = tools.recieve(tout);
                    if (pck != null) //message
                    { // process message
                        byte[] buf = Tools.getData(pck);
                        System.out.println("rcvd: "+(new String(buf)));
                        
                        Message msg = Message.of(buf);
                        msg.setHost( pck.getAddress().getHostAddress() );
                        msg.setPort(pck.getPort());

                        onMessage(msg);
                        
                    } else {
                        // message is not received in given time interval
                        System.out.println("timeout");
                        onTimeout();
                    }
                    // timetable check
                    while(hmScheduler.size()>0){
                        long currTime = System.currentTimeMillis();
                        long dueTime  = hmScheduler.firstKey();
                        if(dueTime>currTime) break;
                        String name = hmScheduler.get(dueTime);
                        onSchedule(name);
                        hmScheduler.remove(dueTime);
                    }
                } catch (SocketException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    };
    
    // timetable handler
    // name - chat-room name
    private void onSchedule(String name){
        System.out.println("schedule: " + name);
        TopicData td = hmTopics.get(name);
        if (td == null) {
            return;
        }
        sendTopicInfo(name, null);
        sendTask(name);
        if (td.getRounds() >= td.getCurRound()) {
            schedule(name, td.getTimeSec());
        }
    }
    
    // find user by token
    private int findUser(String token) {
        for (UserData user : hmUsers.values()) {
            if (user.getToken().equals(token)) {
                return user.getId();
            }
        }
        return -1;
    }
    
    // sending a message to user
    private boolean sendTo(UserData user, Message msg){
        try {
            tools.send(msg.toArray(), user.getHost(), user.getPort());
            System.out.println("send to "+user+" : "+msg);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    // sending a message to user by his identification
    private boolean sendTo(int userId, Message msg){
        if (hmUsers.containsKey(userId)) {
            return sendTo(hmUsers.get(userId), msg);
        }
        return false;
    }
    
    // sending a message to all players in the chat-room
    private boolean sendToTopic(String topic, Message msg){
        TopicData data = hmTopics.get(topic);
        if (data == null) {
            return false;
        }
        boolean bRes = true;
        for (Integer id : data.getUsers()) {
            bRes = bRes && sendTo(id, msg);
        }
        return bRes;
    }
    
    // sending information about chat room
    private boolean sendTopicInfo(String topic, String newcomer){
        System.out.println("sendTopicInfo " + topic);
        TopicData data = hmTopics.get(topic);
        if (data == null) {
            return false;
        }
        TopicInfo info = new TopicInfo();
        info.setName(topic);
        info.setNewcomer(newcomer);
        ArrayList<Integer> users = data.getUsers();
        ArrayList<UserInfo> list = new ArrayList<>();
        for (Integer id : users) {
            UserData ud = hmUsers.get(id);
            if (ud == null) {
                continue;
            }
            UserInfo ui = new UserInfo();
            ui.setId(id);
            ui.setName(ud.getName());
            ui.setScore(ud.getScore());
            list.add(ui);
        }
        Collections.sort(list);
        info.setUsers(list.toArray(new UserInfo[list.size()]));
        info.setRound(data.getCurRound());

        Message msg = converter.toMessage(info);
        return sendToTopic(topic, msg);
    }
    
    // receiving a string array from collection
    private String[] toArr(Collection<String> src){
        if (src == null || src.size() == 0) {
            return new String[0];
        }
        String[] lst = src.toArray(new String[src.size()]);
        Arrays.sort(lst);
        return lst;
    }
    
    // add an event (topic with interval tout seconds) to chat-room to the timetable
    private void schedule(String topic, long tout){
        long time = System.currentTimeMillis()+tout*1000;
        hmScheduler.put(time, topic);
    }

    // generate and send all players the task
    private boolean sendTask(String topic){
        TopicData td = hmTopics.get(topic);
        if (td == null) {
            return false;
        }
        Task task = gen.getGenerator();
        Solution sol = task.generate();
        System.out.println(sol);
        td.setSolution(sol);

        TaskInfo ti = new TaskInfo();
        if (td.getRounds() <= td.getCurRound()) {
            // раунды закончились
            ti.setTask("All done!");
            ti.setTimeout(0);
        } else {
            // новый раунд
            td.setCurRound(td.getCurRound() + 1);
            ti.setTask(sol.getEquation());
            ti.setTimeout(td.getTimeSec());
        }
        long curTime = System.currentTimeMillis();
        td.setStartRound(curTime);
        return sendToTopic(topic, converter.toMessage(ti));
    }

    // send the current task to player
    private boolean sendTask(TopicData td, UserData ud) {
        if (td == null) {
            return false;
        }
        Solution sol = td.getSolution();
        if (sol == null) {
            return false;
        }

        TaskInfo ti = new TaskInfo();
        td.setCurRound(td.getCurRound() + 1);
        ti.setTask(sol.getEquation());
        ti.setTimeout(td.getTimeSec());

        return sendTo(ud, converter.toMessage(ti));
    }
    
    // received message handler
    private void onMessage(Message msg){
        String body = msg.getBody();
        String host = msg.getHost();
        int port = msg.getPort();
        String token = host + ":" + port;
        int userId = findUser(token);
        UserData ud = hmUsers.get(userId);
        System.out.println("user: " + ud);

        if (msg.getType() == 'L') {
            // login new user
            LoginRq loginRq = converter.loginRq(body);

            userId = userCounter;
            userCounter++;
            //String name = loginRq.getLogin();
            UserData userData = new UserData(userId, loginRq.getLogin(), host, port);
            hmUsers.put(userId, userData);
            System.out.println("new user: " + userData);
            LoginRs loginRs = new LoginRs();
            loginRs.setState(0);
            loginRs.setTopics(toArr(hmTopics.keySet()));

            Message rs = converter.toMessage(loginRs);
            sendTo(userData, rs);
        } else if (msg.getType() == 'E') {
            // enter to topic
            EnterRq enterRq = converter.enterRq(body);
            //UserData ud = hmUsers.get(userId);
            String topic = enterRq.getTopic();
            if (ud != null && hmTopics.containsKey(topic)) {
                // remove from prev topic
                if (ud.getTopic() != null) {
                    TopicData pt = hmTopics.get(ud.getTopic());
                    pt.getUsers().remove(userId);
                    sendTopicInfo(ud.getTopic(), null);
                }
                TopicData td = hmTopics.get(topic);
                td.getUsers().add(userId);
                ud.setScore(0);
                ud.setTopic(topic);
                sendTopicInfo(topic, ud.getName());
                sendTask(td, ud);
            }
        } else if (msg.getType() == 'C') {
            // create topic
            if (ud == null) {
                return;
            }
            CreateTopicRq rq = converter.createTopicRq(body);
            String topic = rq.getTopic();
            if (hmTopics.containsKey(topic)) {
                topic = topic + "@" + ud.getName();
            }
            if (hmTopics.containsKey(topic)) {
                topic = topic + ":" + ud.getId();
            }
            TopicData td = new TopicData(topic);
            td.setRounds(rq.getRounds());
            td.setTimeSec(rq.getTime());
            td.getUsers().add(userId);
            hmTopics.put(topic, td);
            ud.setScore(0);
            ud.setTopic(topic);
            sendTopicInfo(topic, ud.getName());
            sendTask(topic);
            schedule(topic, td.getTimeSec());
        } else if (msg.getType() == 'S') {
            // solution
            SolutionMsg sm = converter.solution(body);
            TopicData td = hmTopics.get(ud.getTopic());
            if (td != null) {
                onSolution(sm, ud, td);
            }

            // TODO: use solution here
        } else if (msg.getType() == 'M') {
            // сообщение для чат-комнаты
            ChatMessageRq cm = converter.chatMessageRq(body);
            String topic = ud.getTopic();
            ChatMessageRs rs = new ChatMessageRs(ud.getName(), cm.getMessage());
            Message m = converter.toMessage(rs);
            sendToTopic(topic, m);
        }
    }
    
    private void onTimeout(){
        
    }

    // converting a string of numbers separated by a comma into an array of integers
    private int[] toIntArr(String str) throws Exception {
        if (str == null || str.length() == 0) {
            throw new Exception("Empty string");
        }
        String[] lst = str.split(",");
        int[] res = new int[lst.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = Integer.parseInt(lst[i].trim());
        }
        return res;
    }

    // player decision processing
    private void onSolution(SolutionMsg sm, UserData ud, TopicData td) {
        try {
            long curTime = System.currentTimeMillis();
            int[] res = toIntArr(sm.getSolution());
            Solution sol = td.getSolution();
            boolean bRes = sol.check(res);
            if (ud.getRound() != td.getCurRound()) {
                // reset player statistics for the last round
                ud.setRound(td.getCurRound());
                ud.setErrcnt(0);
            }

            if (bRes) {
                // correct solution
                ChatMessageRs rs = new ChatMessageRs(null, ""+ud.getName()+" solved the equation!");
                sendToTopic(td.getName(), converter.toMessage(rs));
                // calculate score
                long dif = curTime-td.getStartRound();
                int maxScore = 500;
                for(int i=ud.getErrcnt(); i>0; i--){
                    maxScore/=2;
                }
                System.out.println("maxScore="+maxScore+", errors="+ud.getErrcnt());
                int rest = (int)((double)(td.getTimeSec()-dif/1000)*maxScore/td.getTimeSec());
                if(rest<0) rest=0;
                System.out.println("rest="+rest);
                ud.setScore(ud.getScore()+rest);
                System.out.println("score="+ud.getScore());
            } else {
                // wrong solution
                ChatMessageRs rs = new ChatMessageRs(null, "Incorrect solution");
                sendTo(ud, converter.toMessage(rs));
                ud.setErrcnt(ud.getErrcnt()+1);
            }

        } catch (Exception e) {
            // exception - the answer format is incorrect, inform the player
            ChatMessageRs rs = new ChatMessageRs(null, "Incorrect format");
            Message m = converter.toMessage(rs);
            sendTo(ud, m);
        }
    }

    public static void main(String[] args) {
        ChatServer srv = new ChatServer();

    }

}

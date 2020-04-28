package questchat.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import questchat.msg.ChatMessageRs;
import questchat.msg.LoginRs;
import questchat.msg.TaskInfo;
import questchat.msg.TopicInfo;
import questchat.net.ChatClient;
import questchat.net.MessageListener;
import javax.swing.text.StyledDocument;

/**
 *
 * @author katja
 * 
 * Implements chat user interface
 */
public class Client extends JPanel implements MessageListener{

    SelectTopicPane selectTopicPane = new SelectTopicPane();
    ChatClient chat;
    UsersPane usersPane = new UsersPane();
    CardLayout cardLayout = new CardLayout();
    JPanel     panelSide  = new JPanel(cardLayout);
    
    JTextPane taChat = new JTextPane();
    JPanel     panelChat = new JPanel(new BorderLayout(5, 5));
    QueryPane  queryPane = new QueryPane();
    
    JLabel lGuess = new JLabel("Tell your friends something:");
    JTextField tfGuess = new JTextField();
    JButton btnGuess = new JButton("Send");

    SimpleAttributeSet normal, bold, serverMessage;
    
    LoginPane loginPane = new LoginPane();

    
    // Chat panel activation/deactivation
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tfGuess.setEditable(enabled);
        btnGuess.setEnabled(enabled);
    }

    
    
    // creating UI
    public Client() {
    
        setLayout(new BorderLayout(5,5));
        panelSide.add(selectTopicPane,"selectTopicPane");
        panelSide.add(usersPane,"usersPane");
        cardLayout.show(panelSide, "selectTopicPane");
        
        add(panelSide, BorderLayout.WEST);
        add(panelChat, BorderLayout.CENTER);
        
        panelChat.add(new JScrollPane(taChat), BorderLayout.CENTER);
        panelChat.add(queryPane, BorderLayout.NORTH);
        panelChat.add(Box.createVerticalStrut(10), BorderLayout.EAST);
        queryPane.setEnabled(false);
        
        taChat.setEditable(false);
        
        Box box = new Box(BoxLayout.Y_AXIS);
        panelChat.add(box, BorderLayout.SOUTH);
        box.add(lGuess);
        box.add(Box.createVerticalStrut(5));

        Box line = new Box(BoxLayout.X_AXIS);
        box.add(line);
        line.add(tfGuess);
        line.add(Box.createHorizontalStrut(5));
        line.add(btnGuess);
        box.add(Box.createVerticalStrut(5));
        setEnabled(false);
        
        add(Box.createVerticalStrut(10), BorderLayout.EAST);
        
        init();
    }
    
    // initializing and setting up UI
    private void init(){
        
        normal = new SimpleAttributeSet();
        
        bold = new SimpleAttributeSet(normal);
        StyleConstants.setBold(bold, true);
        
        serverMessage = new SimpleAttributeSet(normal);
        StyleConstants.setItalic(serverMessage, true);
        
        loginPane.setServer("localhost:7777"); // default address value
        loginPane.setLogin(System.getProperty("user.name")); // default login
        
        // chat room login handler
        selectTopicPane.addOnEnterListener((e)->{
            String topic = selectTopicPane.getSelectedTopicName();
            try {
                chat.enter(topic);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // creating new chat room handler
        selectTopicPane.addOnCreateListener((e)->{
            String topic = selectTopicPane.getCreateTopicName();
            try {
                chat.create(topic, selectTopicPane.getCreateTopicRounds(), selectTopicPane.getCreateTopicTime());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // solution sending handler
        queryPane.addOnSentListener((e)->{
            String sol = queryPane.getAnswer();
            try {
                chat.solution(sol);
                queryPane.clearAnswer();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // chat message entry handler
        ActionListener alGuess = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String str = tfGuess.getText().trim();
            if(str.length()==0) return;
            try {
                chat.guess(str);
                tfGuess.setText("");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        };
        
        btnGuess.addActionListener(alGuess);
        tfGuess.addActionListener(alGuess);

    }
    
    // launch of work
    public void work(){
        login();
    }

    // current state of interaction with the server
    int state = 0; // 1-wait login, 2-select topic, 3- in chat
    
    // connecting to the server
    // displaying a dialog box to enter server address and login
    // if Cancel is chosen - app shuts down
    // if no confirmation from server was received during the timeout
    // dialog is shown again
    private void login(){
        boolean res = loginPane.showDialog();
        if(!res) System.exit(0);
        
        String login = loginPane.getLogin();
        String server= loginPane.getServer();
        
        System.out.println("Login as '"+login+"' to '"+server+"'");

        if(chat==null) chat = new ChatClient();
        chat.setServer(server);
        chat.setListener(this);
        try {
            state = 1;
            chat.login(login);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
    // method is called when login confirmation from server was received
    @Override
    public void onLoginRs(LoginRs msg) {
        System.out.println("onLoginRs: "+msg);
        selectTopicPane.setTopics(msg.getTopics());
        state=2;
    }

    // method is called if the request is waiting for a response and there is no response within specified time
    @Override
    public void onTimeout() {
        System.out.println("Timeout");
        if(state==1){ // wait for loginRs
            login();
        }
    }

    
    // method is called when a message with information about chat-room is received
    @Override
    public void onTopicInfo(TopicInfo msg) {
        System.out.println("onTopicInfo: "+msg);
        if(state==2) {
            cardLayout.show(panelSide, "usersPane");
            queryPane.setEnabled(true);
            setEnabled(true);

            state=3;
        }
        usersPane.setUserInfo(msg.getUsers());
        String newcomer = msg.getNewcomer();
        if(newcomer!=null && newcomer.length()>0) chatNotification(newcomer+" in  our room!");
    }

    // method is called when task is received
    @Override
    public void onTaskInfo(TaskInfo msg) {
        System.out.println("onTaskInfo: "+msg);
        queryPane.setQuery(msg.getTask());
        if(msg.getTimeout()>0) chatNotification("New equation");
    }

    // method is called when new message for chat is received
    @Override
    public void onChatMessage(ChatMessageRs msg) {
        System.out.println("onChatMessage: "+msg);
        if(msg.getAuthor()==null) { // server message
            chatNotification(msg.getMessage());
        } else {
            //String str = msg.getAuthor()+":\n"+msg.getMessage()+"\n\n";
            StyledDocument doc = taChat.getStyledDocument();
            
            try {
                doc.insertString(doc.getLength(), msg.getAuthor()+": ", bold);
                doc.insertString(doc.getLength(), msg.getMessage()+"\n", normal);
                taChat.setCaretPosition(doc.getLength());
            } catch (BadLocationException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    // method is called when message from server is received
    // displayed in the chat panel using an individual style
    public void chatNotification(String msg){
        if(msg!=null && msg.length()>0 ) {
            StyledDocument doc = taChat.getStyledDocument();
            try {
                doc.insertString(doc.getLength(), msg, serverMessage);
                doc.insertString(doc.getLength(), "\n", normal);
                taChat.setCaretPosition(doc.getLength());
            } catch (BadLocationException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    public static void main(String[] args) {
        
        Client pane = new Client();
        
        JFrame frame = new JFrame("QuestChat");

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 550);
        frame.pack();
        frame.setVisible(true);
        
        pane.work();
    }

}

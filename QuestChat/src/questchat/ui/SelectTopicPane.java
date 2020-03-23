package questchat.ui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 *
 * @author katja
 * 
 * Select chat-room section
 */
public class SelectTopicPane extends JPanel {

    int row = 1;
    int rowHeight = 20;
        
        DefaultListModel<String> dlmTopics = new DefaultListModel<>();
        JLabel lTopics = new JLabel("Select room");
        JList  lstTopics = new JList(dlmTopics);
        JButton btnEnter = new JButton("Enter");
        
        JLabel lCreate   = new JLabel("Create new room");
        JLabel lName     = new JLabel("Name");
        JLabel lRounds   = new JLabel("Rounds");
        JLabel lTime     = new JLabel("Time (sec)");
        
        JTextField tfName= new JTextField();
        JFormattedTextField tfRounds = new JFormattedTextField(new Integer(5));
        JFormattedTextField tfTime   = new JFormattedTextField(new Integer(30));
        JButton btnCreate = new JButton("Create");

        // create UI
        public SelectTopicPane() {
        setLayout(null);
        
        
        lTopics.setBounds(20, row * rowHeight, 200, 20);  add(lTopics);
        row++;
        lstTopics.setBounds(20, row * rowHeight, 240, rowHeight*12);  add(lstTopics);
        lstTopics.setBorder(BorderFactory.createEtchedBorder());
        row+=12;
        btnEnter.setBounds(80, row * rowHeight+5, 100, 20);  add(btnEnter);
        btnEnter.setEnabled(false);
        row++;
        row++;
        JSeparator s1 = new JSeparator();
        s1.setBounds(20, row * rowHeight-rowHeight/2, 240, 10);  add(s1);
        lCreate.setBounds(20, row * rowHeight-2, 240, 20);  add(lCreate);
        row++;
        lName.setBounds(20, row * rowHeight, 100, 20);  add(lName);
        tfName.setBounds(120, row * rowHeight, 120, 20);  add(tfName);
        row++;
        lRounds.setBounds(20, row * rowHeight, 100, 20);  add(lRounds);
        tfRounds.setBounds(120, row * rowHeight, 120, 20);  add(tfRounds);
        row++;
        lTime.setBounds(20, row * rowHeight, 100, 20);  add(lTime);
        tfTime.setBounds(120, row * rowHeight, 120, 20);  add(tfTime);
        row++;
        btnCreate.setBounds(80, row * rowHeight+5, 100, 20);  add(btnCreate);
        row++;
        row++;

        setPreferredSize(new Dimension(280, row*rowHeight));
        
        init();
    }
    
    // initialization
    private void init(){
        
    }
    
    // add listener to selecting an existing chat-room
    public void addOnEnterListener(ActionListener l){
        btnEnter.addActionListener(l);
    }
  
    // add listener to creating a new chat-room
    public void addOnCreateListener(ActionListener l){
        btnCreate.addActionListener(l);
        tfName.addActionListener(l);
        tfRounds.addActionListener(l);
        tfTime.addActionListener(l);
    }
    
    // returns selected chat-room name
    public String getSelectedTopicName(){
        return (String)lstTopics.getSelectedValue();
    }

    // returns new chat-room name
    public String getCreateTopicName(){
        return (String)tfName.getText().trim();
    }

    // returns number of rounds in new chat-room
    public int getCreateTopicRounds(){
        return (Integer)tfRounds.getValue();
    }

    // returns time for solving task in new chat-room (in seconds)
    public int getCreateTopicTime(){
        return (Integer)tfTime.getValue();
    }
    
    // sets a list of existing chat-rooms
    public void setTopics(String[] list){
        if(list==null) return;
        dlmTopics.removeAllElements();
        for(String name: list){
            dlmTopics.addElement(name);
        }
        if(list.length>0){
            lstTopics.setSelectedIndex(0);
            btnEnter.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        
        SelectTopicPane pane = new SelectTopicPane();

        JFrame frame = new JFrame(pane.getClass().getName());

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 550);
        frame.pack();
        frame.setVisible(true);
        
    }
    
}

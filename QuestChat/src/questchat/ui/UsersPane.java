package questchat.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import questchat.msg.UserInfo;

/**
 *
 * @author katja
 * 
 * Chat-room players list section
 */
public class UsersPane extends JPanel {

    JLabel lRound = new JLabel("Round");

    DefaultListModel<String> dlmUsers = new DefaultListModel<>();
    JList lstUsers = new JList(dlmUsers);
    
    // creating UI
    public UsersPane() {
        setLayout(new BorderLayout());
        lRound.setMinimumSize(new Dimension(240, 10));
        lRound.setPreferredSize(new Dimension(240, 20));
        add(lRound, BorderLayout.NORTH);
        
        lstUsers.setBorder(BorderFactory.createEtchedBorder());
        lstUsers.setMinimumSize(new Dimension(240, 50));
        add(lstUsers, BorderLayout.CENTER);
        Font oldFont = lstUsers.getFont();
        Font font = new Font(Font.MONOSPACED, oldFont.getStyle(), oldFont.getSize());
        lstUsers.setFont(font);
        
        add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        
        init();
    }
    
    // initialization UI
    private void init(){
        
    }
    
    // list update
    public void setUserInfo(UserInfo[] users){
        dlmUsers.removeAllElements();
        for(UserInfo info: users){
            String str = String.format("%-20s %5d", info.getName(),info.getScore());
            dlmUsers.addElement(str);
        }
    }
    
    // components check
    public static void main(String[] args) {
        
        UsersPane pane = new UsersPane();
        
        JFrame frame = new JFrame(pane.getClass().getName());

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 550);
        //frame.pack();
        frame.setVisible(true);
        
    }
    
}

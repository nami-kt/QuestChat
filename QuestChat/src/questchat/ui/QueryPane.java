package questchat.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author katja
 * 
 * Task for chat-room section
 */
public class QueryPane extends JPanel {

    JLabel lTask = new JLabel("Equation");
    JTextArea taTask = new JTextArea(3, 50);
    JLabel lAnswer = new JLabel("Enter the solutions separated by comma:");
    JTextField tfAnswer = new JTextField();
    JButton btnSend = new JButton("Send");

    // secton activation/deactivation
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        taTask.setEditable(enabled);
        tfAnswer.setEditable(enabled);
        btnSend.setEnabled(enabled);
    }
    
    
    // creating UI
    public QueryPane() {
        setLayout(new BorderLayout(5, 5));
        
        add(lTask, BorderLayout.NORTH);
        
        Box box = new Box(BoxLayout.Y_AXIS);
        add(box, BorderLayout.CENTER);
        box.add(taTask);    
            taTask.setBorder(BorderFactory.createEtchedBorder());
            taTask.setEditable(false);
        box.add(Box.createVerticalStrut(5));
        box.add(lAnswer);
        box.add(Box.createVerticalStrut(5));

        Box line = new Box(BoxLayout.X_AXIS);
        box.add(line);
        line.add(tfAnswer);
        line.add(Box.createHorizontalStrut(5));
        line.add(btnSend);
        box.add(Box.createVerticalStrut(5));
        
        //add(Box.createVerticalStrut(5), BorderLayout.WEST);
        add(Box.createVerticalStrut(10), BorderLayout.EAST);
        
        init();
    }
    
    // initialization
    private void init(){
        
    }
    
    // task text setup
    public void setQuery(String str){
        taTask.setText(str);
    }
    
    // returns answer in entry box
    public String getAnswer(){
        return tfAnswer.getText().trim();
    }
    
    // clears up entry box
    public void clearAnswer(){
        tfAnswer.setText("");
    }

    // sets up listener for answer entry
    public void addOnSentListener(ActionListener l){
        btnSend.addActionListener(l);
        tfAnswer.addActionListener(l);
    }
    
    public static void main(String[] args) {
        
        QueryPane pane = new QueryPane();
        
        JFrame frame = new JFrame(pane.getClass().getName());

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 550);
        frame.pack();
        frame.setVisible(true);        
    }
}

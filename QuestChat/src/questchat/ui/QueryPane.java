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

class EquationPane extends JComponent {

    private String text;

    public EquationPane() {
        Dimension d = new Dimension(200, 100);
        setMinimumSize(d);
        setPreferredSize(d);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        Font font = getFont();
        int size = font.getSize();

        Font upFn = font.deriveFont((float)(font.getSize()-4));


        int xpos = 10;
        int s_w = 10;
        int ypos = (height + size) / 2;

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.blue);

        boolean upMode = false;
        if (text != null) {
            for (char c : text.toCharArray()) {
                if (upMode && !Character.isDigit(c)) {
                    upMode = false;
                }
                if (c == '^') {
                    upMode = true;
                    continue;
                }
                char[] data = {c};
                g2d.setFont(font);
                if (upMode) {
                    g2d.setFont(upFn);
                    g2d.drawChars(data, 0, 1, xpos, ypos - size / 2);
                } else {
                    g2d.drawChars(data, 0, 1, xpos, ypos);
                }
                xpos += s_w;
            }
        }
    }

}


public class QueryPane extends JPanel {

    JLabel lTask = new JLabel("Equation");
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
        box.add(Box.createVerticalStrut(5));
        box.add(lAnswer);
        box.add(Box.createVerticalStrut(5));

        Box line = new Box(BoxLayout.X_AXIS);
        box.add(line);
        line.add(tfAnswer);
        line.add(Box.createHorizontalStrut(5));
        line.add(btnSend);
        box.add(Box.createVerticalStrut(5));

        add(Box.createVerticalStrut(10), BorderLayout.EAST);
        
        init();
    }
    
    // initialization
    private void init(){
        Font f = taTask.getFont().deriveFont(14f).deriveFont(Font.BOLD);
        taTask.setFont(f);
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
        pane.setQuery(" 5x^2 + 3x + 7 = 0");
        
        JFrame frame = new JFrame(pane.getClass().getName());

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 550);
        frame.pack();
        frame.setVisible(true);        
    }
}

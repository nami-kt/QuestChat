package questchat.ui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author katja
 * 
 * Connecting to server section
 */
public class LoginPane extends JPanel {

    JLabel lServer = new JLabel("Server");
    JLabel lLogin = new JLabel("Login");

    JTextField tfServer = new JTextField();
    JTextField tfLogin = new JTextField();

    int row = 1;
    int rowHeight = 20;

    // creating UI
    public LoginPane() {
        setLayout(null);

        lServer.setBounds(20, row * rowHeight, 50, 20);
        add(lServer);
        tfServer.setBounds(80, row * rowHeight, 120, 20);
        add(tfServer);
        row++;
        lLogin.setBounds(20, row * rowHeight, 50, 20);
        add(lLogin);
        tfLogin.setBounds(80, row * rowHeight, 120, 20);
        add(tfLogin);
        row++;
        row++;

        Dimension d = new Dimension(220, row * rowHeight);
        setSize(d);
        setPreferredSize(d);
        init();
    }

    // initialization
    private void init() {

    }

    String server;
    String login;

    // returns server address entered by user
    public String getServer() {
        return server;
    }

    // sets up server address
    public void setServer(String server) {
        this.server = server;
        tfServer.setText(server);
    }

    // returns user login
    public String getLogin() {
        return login;
    }

    // user login setup
    public void setLogin(String login) {
        this.login = login;
        tfLogin.setText(login);
    }

    // returns dialog box for server adress and login entry
    public boolean showDialog() {
        boolean bRes = true;
        int res = 0;
        do {
            res = JOptionPane.showConfirmDialog(null, this, "Login to server", JOptionPane.OK_CANCEL_OPTION);
            bRes = (res == JOptionPane.OK_OPTION);
            if (bRes) {
                login = tfLogin.getText().trim();
                server = tfServer.getText().trim();
            }
        } while (bRes && (login.length() == 0 || server.length() == 0));
        return bRes;
    }

    public static void main(String[] args) {
        System.out.println("pane");

        LoginPane pane = new LoginPane();

        boolean bRes = pane.showDialog();
        System.out.println("bRes=" + bRes);

        JFrame frame = new JFrame(pane.getClass().getName());

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 150);
        frame.pack();
        frame.setVisible(true);

    }
}

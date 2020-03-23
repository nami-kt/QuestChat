package questchat;

import java.net.SocketException;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author katja
 *
 * Class for storing general app data
 * implemented as Singleton
 */

public class Context {
    private static Context instance;

    private int		port = 7777;            // server port
    private String	address = "localhost";  // server address

    private Context(){
    }

    public static Context getInstance(){
        if(instance==null) instance = new Context();
        return instance;
    }

    // set port
    public void setPort(int port) {
        this.port = port;
    }

    // set address
    public void setAddress(String address) {
        this.address = address;
    }

    // get port
    public int getPort() {
        return port;
    }

    // get address
    public String getAddress() {
        return address;
    }



}


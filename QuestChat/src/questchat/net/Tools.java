package questchat.net;


import java.io.IOException;
import java.net.*;
import questchat.Context;

/**
 *
 * @author katja
 *
 * Class for interaction with UDP-socket
 * implemented as Singleton
 */

public class Tools {

    private static Tools instance;

    private DatagramSocket socket;
    //private static Context context = Context.getInstance();
    
    private String srvHost;
    private int    srvPort;

    private Tools() throws SocketException {
        socket = new DatagramSocket();
    }

    private Tools(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public static Tools getServerInstance(int port) throws SocketException {
        if(instance==null) instance = new Tools(port);
        return instance;
    }

    public static Tools getInstance() throws SocketException {
        if(instance==null) instance = new Tools();
        return instance;
    }

    public void setSrvHost(String srvHost) {
        this.srvHost = srvHost;
    }

    public void setSrvPort(int srvPort) {
        this.srvPort = srvPort;
    }

    // sending DatagramPacket
    public void send(DatagramPacket pck) throws IOException {
        socket.send(pck);
    }

    // sending an array to the given address and port
    public void send(byte[] buf, String addr, int port) throws IOException {
        //System.out.println("Sent to: "+addr+":"+port);
        InetAddress ia = InetAddress.getByName(addr);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, ia, port);
        send(packet);
    }

    // sending message to server
    public void sendToSrv(byte[] buf) throws IOException {
        send(buf, srvHost, srvPort);
    }
    
    // receiving a packet with given timeout
    private byte[] rcvBuf = new byte[10*1024];
    public DatagramPacket recieve(long timeout) throws IOException {
        if(timeout<0) timeout=10;
        socket.setSoTimeout((int)timeout);
        try{
            DatagramPacket pck = new DatagramPacket(rcvBuf, rcvBuf.length);
            socket.receive(pck);
            return pck;
        }catch(SocketTimeoutException te){
            return null;
        }
    }

    // Data extraction from DatagramPacket
    public static byte[] getData(DatagramPacket pck){
        int len = pck.getLength();
        byte[] res = new byte[len];
        System.arraycopy(pck.getData(),0,res,0,len);
        return res;
    }

    public int getPort(){
        return socket.getLocalPort();
    }
    
    public void close(){
        if(socket!=null) socket.close();
    }

    public static void main(String[] args) throws IOException {
        Tools tools = new Tools();
    }

}

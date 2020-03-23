package questchat.msg;

/**
 *
 * @author katja
 * Player-server message data interchange format
 */
public class Message {
    
    public static final char M_LOGIN_RQ='L', M_LOGIN_RS='l', M_RESPONSE='r';
    
    char type;      // symbol for marking message type
    String body;    // body of message in JSON format
    String host;    // sender address
    int port;       // sender port

    public Message() {
    }

    public Message(char type, String body) {
        this.type = type;
        this.body = body;
    }

    public Message(byte[] buf) {
        if(buf.length>0){
            String str = new String(buf);
            type = str.charAt(0);
            body = str.substring(1);
        }
    }
    
    public static Message of(byte[] buf){
        return new Message(buf);
    }
    
    public byte[] toArray(){
        return (""+type+body).getBytes();
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
    
    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", body=" + body + '}';
    }
}

package questchat.msg;

/**
 *
 * @author katja
 * 
 * Chat message from server for players in the room
 */
public class ChatMessageRs {
    String author;      //message author's login. If logon==null - message from server
    String message;     //message text
    long   timestamp;   //message timestamp

    public ChatMessageRs() {
    }

    public ChatMessageRs(String author, String message) {
        this.author = author;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessageRs{" + "author=" + author + ", message=" + message + ", timestamp=" + timestamp + '}';
    }
    
}

package questchat.data;

/**
 *
 * @author katja
 * 
 * Class for storing data about the payer
 */
public class UserData {
    int id;         // player's ID set by server
    String token;   // unique token of the player - based on player's IP address and port
    String name;    // player's login
    String host;    // player's address
    int    port;    // players's port
    String topic;   // name of the room player joined
    int    score;   // player's score
    int round;
    int errcnt;

    // Constructors

    public UserData(int id) {
        this.id = id;
    }

    public UserData(int id, String name, String host, int port) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.token = host + ":" + port;
    }

    // Getters and setters for class fields
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        token=host+":"+port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        token=host+":"+port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getToken() {
        return token;
    }

    public int getRound() {
        return this.round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getErrcnt() {
        return this.errcnt;
    }

    public void setErrcnt(int errcnt) {
        this.errcnt = errcnt;
    }

    public String toString() {
        return "UserData{id=" + this.id + ", token=" + this.token + ", name=" + this.name + ", host=" + this.host + ", port=" + this.port + ", topic=" + this.topic + ", score=" + this.score + '}';
    }
}

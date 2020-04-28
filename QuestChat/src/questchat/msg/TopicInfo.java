package questchat.msg;

import java.util.Arrays;

/**
 *
 * @author katja
 * 
 * Message with information about chat-room from server for player
 */
public class TopicInfo {
    String name;        // chat-room name
    String newcomer;    // message with login of a new player in chat-room
    UserInfo[] users;   // list of players in chat-room

    public TopicInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewcomer() {
        return newcomer;
    }

    public void setNewcomer(String newcomer) {
        this.newcomer = newcomer;
    }

    public UserInfo[] getUsers() {
        return users;
    }

    public void setUsers(UserInfo[] users) {
        this.users = users;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "TopicInfo{" + "name=" + name + ", newcomer=" + newcomer + ", users=" + Arrays.toString(users) + '}';
    }
}

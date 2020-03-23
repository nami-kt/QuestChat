package questchat.msg;

/**
 *
 * @author katja
 * 
 * Infromation about player in chat-room
 */
public class UserInfo implements Comparable<UserInfo> {
    int     id;     // player'sID
    String  name;   // player's login
    int     score;  // player's score

    public UserInfo() {
    }

    public UserInfo(int id) {
        this.id = id;
    }

    public UserInfo(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(UserInfo o) {
        if(score>o.score) return -1;
        if(score>o.score) return  1;
        return 0;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "id=" + id + ", name=" + name + ", score=" + score + '}';
    }
    
    
    
}

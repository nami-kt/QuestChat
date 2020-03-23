package questchat.msg;

/**
 *
 * @author katja
 * 
 * Basic response to request type
 */
public class Response {
    int state;   // 0 - ok, 1 ... - error code
    String desc; // state description

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
}

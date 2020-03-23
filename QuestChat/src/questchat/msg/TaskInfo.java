package questchat.msg;

/**
 *
 * @author katja
 * 
 * Message from server for player with equation
 */
public class TaskInfo {
    String  task;       // task text
    long    timeout;    // time limit for a single task

    public TaskInfo() {
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "TaskInfo{" + "task=" + task + ", timeout=" + timeout + '}';
    }
    
    
}

package Core;

import java.util.List;

public interface Messenger {
    public void sendMessage(String destination, String message);
    public List<Message> recvMessage();
    public String getUserName();

}

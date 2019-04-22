import java.util.List;

public interface Messanger {
    public boolean sendMessage(Message message);
    public List<Message> recvMessage();
}

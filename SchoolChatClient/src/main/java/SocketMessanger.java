import java.io.*;
import java.net.Socket;
import java.util.List;

public class SocketMessanger implements Messanger {
    String userName;
    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    boolean isAutorize=false;

    public SocketMessanger(String userName,Socket socket) throws IOException {
        this.userName=userName;
        this.socket = socket;
        objectInputStream=new ObjectInputStream(socket.getInputStream());
        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());

    }

    public boolean sendMessage(String message){
        Message msg=new Message(userName,message);
        return sendMessage(msg);
    }

    @Override
    public boolean sendMessage(Message message) {
        return false;
    }

    @Override
    public List<Message> recvMessage() {
        return null;
    }

}

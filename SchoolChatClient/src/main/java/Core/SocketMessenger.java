package Core;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SocketMessenger implements Messenger {
    String userName;
    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    boolean isAutorize = false;


    public SocketMessenger(String userName, Socket socket) throws IOException {
        this.userName = userName;
        this.socket = socket;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

    }

    public String getUserName(){return userName;}

    public void authorize(){
        System.err.println("Посылаем сообщение авторизации");
        Message msg=new Message(userName,"system",userName, Message.messageType.Authorization);
        sendMessage(msg);
    }

    @Override
    public void sendMessage(String destination, String message) {
        Message msg = new Message(userName, destination, message, Message.messageType.SendMessage);
        sendMessage(msg);

    }

    private void sendMessage(Message message) {
        Message recvMsg = null;
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> recvMessage() {
        Message cmdMessage=new Message(userName, "system", "Запрос на получение всех сообщений", Message.messageType.GetMessage);
        LinkedList<Message> list=null;
        try {
           sendMessage(cmdMessage);
           list= (LinkedList<Message>) objectInputStream.readObject();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

}

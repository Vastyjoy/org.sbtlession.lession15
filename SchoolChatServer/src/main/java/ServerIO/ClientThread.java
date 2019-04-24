package ServerIO;

import Core.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ClientThread extends Thread {
    private String userName;
    private Socket socket;
    private MsgBase msgBase;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private static final String systemUser="system";

    public ClientThread(Socket socket, MsgBase msgBase) throws IOException {
        this.socket = socket;
        this.msgBase = msgBase;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    private boolean authorizeAndRegisterInBase() throws IOException, ClassNotFoundException {
        System.err.println("Получаем сообщение регистрации");
        Message msg = (Message) objectInputStream.readObject();
        switch (msg.getMessageType()) {
            case Authorization:
                String tempUserName = msg.getMessage();

                if (!msgBase.registerNewUser(tempUserName)){
                    /*
                    objectOutputStream.writeObject(new Message(systemUser, userName,"Ошибка подключения, такой пользователь уже авторизирован", Message.messageType.UnSuccessRequest));
                    */
                    System.err.println("Попытка подключения уже авторизованного пользователя:"+tempUserName);
                    doFinally();
                    return false;
                }

                this.userName=tempUserName;

                Message sysMsg = new Message(systemUser,
                        "all",
                        "Подключился новый пользователь :" + userName,
                        Message.messageType.SendMessage);

                msgBase.multiPutMessage(sysMsg);

               /* objectOutputStream.writeObject(new Message(systemUser, userName, "Успешное подключение",Message.messageType.SuccessRequest));*
               */

                break;
            default:
                objectOutputStream.writeObject(new Message(systemUser, userName,
                        "Ошибка подключения, неверный первичный запрос." +
                                "Ожидался messageType:Authorization " + "Получен:" + msg.getMessageType(),
                        Message.messageType.UnSuccessRequest));
                doFinally();
                return false;
        }
        return true;
    }

    private void doFinally() {
        try {
            msgBase.unregisterUser(userName);
            objectOutputStream.close();
            objectInputStream.close();
            socket.close(); // задать вопрос, если просто закрыть сокет то каналы по идет тоже закроются сами.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.err.println("Авторизация нового подключения от " + socket);
            boolean isSuccess = authorizeAndRegisterInBase();
            if (!isSuccess) interrupt();

            while (!isInterrupted()) {
                Message currentMsg = (Message) objectInputStream.readObject();
                switch (currentMsg.getMessageType()) {
                    case GetMessage:
                        /*objectOutputStream.writeObject(new Message(systemUser,userName,"Успешный запрос", Message.messageType.SuccessRequest));*/

                        LinkedList<Message> linkedList = msgBase.getAllMessage(currentMsg.getSourceName());
                        objectOutputStream.writeObject(linkedList);
                        break;
                    case SendMessage:
                        msgBase.putMessage(currentMsg.getDestinationName(), currentMsg);
                        msgBase.putMessage(currentMsg.getSourceName(),currentMsg);
                       /* objectOutputStream.writeObject(new Message(systemUser
                                ,userName
                                ,"Сообщение отправлено"
                                , Message.messageType.SuccessRequest));*/
                        break;
                }
            }
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            System.err.println("Обрыв соединения с пользователем" +userName);
            msgBase.multiPutMessage(new Message(systemUser,"all","Обрыв соединения с пользователем:"+userName+" ", Message.messageType.SendMessage));
        }finally {
            doFinally();
        }
    }
}

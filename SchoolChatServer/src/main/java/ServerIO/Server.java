package ServerIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server  {
    private ServerSocket serverSocket;
    private MsgBase msgBase;
    private List<ClientThread> list;
    public Server(int port) throws IOException {
        serverSocket=new ServerSocket(port);
        msgBase=new MsgBase();
        list= new ArrayList<>();
    }

    public void run() {
        try {
            while (true) {
                Socket socket=serverSocket.accept();
                System.err.println("Новое подключение"+socket);
                ClientThread clientThread=new ClientThread(socket,msgBase);
                list.add(clientThread);
                clientThread.start();
                System.err.println("Старт");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            Server server=new Server(12345);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

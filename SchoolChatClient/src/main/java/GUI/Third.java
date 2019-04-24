package GUI;

import Core.SocketMessenger;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Third{
    public static void main(String[] args){
        Socket socket= null;
        try {
            socket = new Socket("localhost",12345);
            SocketMessenger messenger=new SocketMessenger("Irina",socket);
            messenger.authorize();
            MainFrame mainFrame=new MainFrame(messenger);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

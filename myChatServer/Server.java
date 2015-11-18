package lecture12.mySocketChat.myChatServer;

import lecture12.mySocketChat.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends Thread {
    private int port;
    private List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());

    Server(int port){
        this.port = port;
    }

    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (!isInterrupted()) {
                Socket socket = serverSocket.accept();
                new ClientThread(socket, messages).start();
            }

            serverSocket.close();
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }
}

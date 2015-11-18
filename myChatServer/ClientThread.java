package lecture12.mySocketChat.myChatServer;

import lecture12.mySocketChat.Message;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {
    private Socket socket;
    private List<Message> messageList;
    private int messageCount = 0;

    public ClientThread(Socket socket, List<Message> messages) {
        this.socket = socket;
        this.messageList = messages;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            while (!isInterrupted()) {
                if (messageCount < messageList.size()) {
                    sendMessages(out);
                }

                if (in.available() > 0) {
                    System.out.println(in.available());
                    getMessages(in);
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private void getMessages(InputStream in) {
        try (DataInputStream data = new DataInputStream(in)) {

            int messageSize = data.readInt();
            byte[] packet = new byte[messageSize];
            data.read(packet);

            ByteArrayInputStream bis = new ByteArrayInputStream(packet);
            ObjectInputStream ois = new ObjectInputStream(bis);

            Message newMessage = (Message) ois.readObject();
            messageList.add(newMessage);

            ois.close();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private void sendMessages(OutputStream out) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            for (int i = messageCount; i < messageList.size(); i++) {
                oos.writeObject(messageList.get(i));
                oos.flush();

                byte[] packet = bos.toByteArray();

                out.write(packet.length);
                out.write(packet);
                out.flush();
            }

            messageCount = messageList.size();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}

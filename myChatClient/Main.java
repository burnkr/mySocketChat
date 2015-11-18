package lecture12.mySocketChat.myChatClient;

import lecture12.mySocketChat.Message;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        String text = "";

        try {
            Socket socket = new Socket("127.0.0.1", 1313);
            OutputStream out = socket.getOutputStream();
            final InputStream in = socket.getInputStream();

            Thread messageListener = new Thread() {
                @Override
                public void run() {
                    while (!isInterrupted()) {
                        try {
                            if (in.available() > 0)
                                receiveMessage(in);
                        } catch (IOException ioe) {
                            System.out.println(ioe.getMessage());
                        }
                    }
                }
            };
            messageListener.setDaemon(true);
            messageListener.start();

            while (!(text = scanner.nextLine()).isEmpty()) {
                Message message = new Message(login, text);
                //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream wrapper = new ObjectOutputStream(out);

                wrapper.writeObject(message);
                wrapper.flush();

                //byte[] packet = bos.toByteArray();

                //DataOutputStream dos = new DataOutputStream(out);
                //dos.writeInt(packet.length);
                //dos.write(packet);
                //dos.flush();
            }
        } catch (UnknownHostException uhe) {
            System.out.println(uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public static void receiveMessage(InputStream in) {
        try (DataInputStream ois = new DataInputStream(in)) {
            //int messageSize = ois.readInt();
            //byte[] packet = new byte[messageSize];
            //ois.read(packet);

            //ByteArrayInputStream bis = new ByteArrayInputStream(packet);
            ObjectInputStream wrapper = new ObjectInputStream(in);

            Message message = (Message) wrapper.readObject();
            System.out.println("[" + message.getFrom() + "] " + message.getText());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
    }
}

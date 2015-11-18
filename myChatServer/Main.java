package lecture12.mySocketChat.myChatServer;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(1313);
        server.setDaemon(true);

        server.start();

        try {
            server.join();
        } catch (InterruptedException ie){
            System.out.println(ie.getMessage());
        }

    }
}

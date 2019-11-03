import java.io.*;
import java.net.*;

class GuessingServer {
    public static void main(String argv[]) {

        
        //Initializes the port that will be used to listen to new connections
        ServerSocket listeningSocket;
        int listeningPort = 2639;
        try {
            listeningSocket = new ServerSocket(listeningPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Listening for connexions on port " + listeningPort + " ...");

        //Infinite loop used to listen for new connections and assigning them a socket and thread
        Socket gameSocket;
        GuessingGame gameWorker;
        while (true) {

            try {
                gameSocket = listeningSocket.accept();
                gameWorker = new GuessingGame(gameSocket);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            gameWorker.start();
        }
    }
}
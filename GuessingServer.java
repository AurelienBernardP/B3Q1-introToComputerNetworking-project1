import java.io.*;
import java.net.*;
import java.util.*;

class GuessingServer {
    public static void main(String argv[]) {

        ServerSocket listeningSocket;
        int listeningPort = 2012;
        try {
            listeningSocket = new ServerSocket(listeningPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Listening for connetions on port " + listeningPort+ " ...");
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
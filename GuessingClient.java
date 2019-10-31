import java.io.*;
import java.net.*;

class GuessingClient {
    public static void main(String argv[]) {

        /*
        check if the number of arguments is correct,
        send a message to the user and close program if incorrectly used
        */
        if (argv.length != 3) {
            System.out.println("Wrong number of arguments given," + argv.length + "were given but 3 are expected.");
            System.out.println("correct use: java GuessingClient IPServer IPTester PortTester");
            return;
        }

        /*
        Initialize the user socket using the socket given as the third argument of the program
        Disables Nagle's algorithm and get the output stream of the initialized socket
        */
        Socket clientSocket = null;
        OutputStream outStream;
        try {
            clientSocket = new Socket(InetAddress.getByName(argv[1]), Integer.parseInt(argv[2]));
            clientSocket.setTcpNoDelay(true);
            outStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
            return;
        }

        /*
        write on the network output stream the necessary data for the tester server
        to establish connection with the game server  
        */
        try {
            outStream.write(new String(argv[0] + " " + 2639 + "\r\n").getBytes());
            outStream.flush();
        } catch (IOException e) {
            System.out.println("ERROR : Data streams could not be sent to tester server");
            try {
                clientSocket.close();
            } catch (IOException s) {
                s.printStackTrace();
            }
            return;
        }

        /*
        Write on the network output stream the necessary data for the tester server
        to establish connection with the game server  
        */
        BufferedReader socketReader;
        String message;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message = socketReader.readLine();
        } catch (IllegalArgumentException | IOException e) {
            System.out.println("ERROR : readBuffer could not be initialized");
            try {
                clientSocket.close();
            } catch (IOException s) {
                s.printStackTrace();
            }
            return;
        }

        
        //Read the message from the tester server confirming the message is well received
        if (!message.equals("OK")) {
            System.out.println("ERROR : connection to tester could not be established");
        }else{
            System.out.println("Connection to tester was established");
        }
        //close socket
        try {
            clientSocket.close();
        } catch (IOException s) {
            s.printStackTrace();
        }
        
        return;
    }
}
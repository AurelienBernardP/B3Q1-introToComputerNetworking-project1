import java.io.*;
import java.net.*;

class GuessingClient {
    public static void main(String argv[]) {
        if (argv.length != 3) {
            System.out.println("Wrong number of arguments given," + argv.length + "were given but 3 are expected.");
            return;
        }

        Socket clientSocket = null;
        OutputStream outStream;

        try {
            clientSocket = new Socket("localhost", Integer.parseInt(argv[2]));
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

        try {
            outStream.write(new String(argv[1] + " " + 2012 + "\r\n").getBytes());
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

        if (!message.equals("OK")) {
            System.out.println("ERROR : connection to tester could not be established");
        }else{
            System.out.println("Connection to tester was established");
        }

        try {
            clientSocket.close();
        } catch (IOException s) {
            s.printStackTrace();
        }
        
        return;
    }
}
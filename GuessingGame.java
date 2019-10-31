import java.io.*;
import java.net.*;
import java.util.Random;

class GuessingGame extends Thread {

    private Socket gameSocket; // server socket assigned for this thread
    private int randomNo; // random number to be found by the client to win the game
    static final int TIMEOUTTIME = 1000 * 15; //server timeout time, set to 15 seconds


    GuessingGame(Socket givenSocket) {
        gameSocket = givenSocket;
        randomNo = new Random(System.currentTimeMillis()).nextInt(21);
    }

    @Override
    public void run() {

        /*
        Dissables nagel's algorithm and
        gets the output and input streams of the assigned socket
        */
        OutputStream gameOutStream;
        InputStream gameInStream;
        try {
            gameOutStream = gameSocket.getOutputStream();
            gameInStream = gameSocket.getInputStream();
            gameSocket.setSoTimeout(TIMEOUTTIME);
            gameSocket.setTcpNoDelay(true);
        } catch (IOException e) {
            System.out.println("ERROR : Data streams could not be set");
            e.printStackTrace();
            this.closeGameSocket();
            return;
        }

        
        //Initialize buffered reader from the previously obtained input stream
        BufferedReader socketReader;
        try {
            socketReader = new BufferedReader(new InputStreamReader(gameInStream));
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR : readBuffer could not be initialized");
            e.printStackTrace();
            this.closeGameSocket();
            return;
        }

        /*
        Start 
        */
        String message;
        String output;
        while (true) {
            try {

                message = socketReader.readLine();
                if (message == null) {
                    this.closeGameSocket();
                    return;
                }

                output = handleInput(message);
                if (output == null) {
                    this.closeGameSocket();
                    return;
                }
                System.out.println(message);
                System.out.println(output);
                gameOutStream.write(output.getBytes());
                gameOutStream.flush();

                if (output.equals("CORRECT\r\n")) {
                    this.closeGameSocket();
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
                this.closeGameSocket();
                return;
            }
        }
    }

    String handleInput(String message) {
        if (message == null) {
            return null;
        }

        String[] words = message.split(" ");
        if (words.length <= 0) {
            return "WRONG\r\n";
        }

        switch (words[0]) {
            case "TRY":
                if (words.length != 2) {
                    return "WRONG\r\n";
                }
                Integer number = Integer.parseInt(words[1]);
                if (number < 0 || number > 20) {
                    return "WRONG\r\n";
                } else {
                    if (number < randomNo) {
                        return "HIGHER\r\n";
                    }
                    if (number > randomNo) {
                        return "LOWER\r\n";
                    }
                    if (number == randomNo) {
                        return "CORRECT\r\n";
                    }
                }
                break;

            case "CHEAT":
                return new String(randomNo + "\r\n");
            default:
                return "WRONG\r\n";

        }
        return "WRONG\r\n";
    }

    private void closeGameSocket() {
        try {
            this.gameSocket.close();
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

}
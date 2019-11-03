import java.io.*;
import java.net.*;
import java.util.Random;

class GuessingGame extends Thread {

    private Socket gameSocket; // server socket assigned for this thread
    private int randomNo; // random number to be found by the client to win the game
    static final int TIMEOUTTIME = 1000 * 30; //server timeout time, set to 30 seconds


    GuessingGame(Socket givenSocket) {
        gameSocket = givenSocket;
        randomNo = new Random(System.currentTimeMillis()).nextInt(21);
    }

    @Override
    public void run() {

        /*
        Disables Nagle's algorithm and
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

        
        //The guessing game loop starts
        String message;
        String output;
        while (true) {
            try {
                /*
                  reads the message from the client,
                  if the connection was closed, null is received and socket is closed
                */
                 message = socketReader.readLine();
                if (message == null) {
                    this.closeGameSocket();
                    return;
                }

                //handles the guess message and outputs a response
                output = handleInput(message);
                if (output == null) {
                    this.closeGameSocket();
                    return;
                }
                gameOutStream.write(output.getBytes());
                gameOutStream.flush();
                
                //if the output message signalled that the client wins, close socket
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

    /*
    Given an input string it handles the input and returns the appropriate return string.
    null is returned if the input message is null.
    */    
    private String handleInput(String message) {
        //ensure input is not null
        if (message == null) {
            return null;
        }
        //split the message into words
        String[] words = message.split(" ");
        if (words.length <= 0) {
            return "WRONG\r\n";
        }

        //handle the first word of the message
        switch (words[0]) {
            case "TRY":
                //if the key word is "try" but without a number, return wrong
                if (words.length != 2) {
                    return "WRONG\r\n";
                }
                //parse the number to an integer and check its validity
                Integer number = Integer.parseInt(words[1]);
                if (number < 0 || number > 20) {
                    return "WRONG\r\n";
                } else {
                    //if valid give a hint or send answer validation
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

    //Closes the game socket and handles the possible error
    private void closeGameSocket() {
        try {
            this.gameSocket.close();
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

}
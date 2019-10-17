import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.jdi.connect.ListeningConnector;

class GuessingServer{
    public static void main(String argv[]){

        ServerSocket listeningSocket;
        try{
        listeningSocket = new ServerSocket(2012);
        }catch(IOException e){
            e.printStackTrace();
            return;
        }
        System.out.println("Listening");
        Socket gameSocket;GuessingGame gameWorker;
        while(true){
            
            try{
                gameSocket = listeningSocket.accept();
                gameWorker = new GuessingGame(gameSocket);
            }catch(IOException e){
                e.printStackTrace();
                continue;
            }
            gameWorker.start();
        }
    }
}



class GuessingGame extends Thread{

    Socket gameSocket;
    int randomNo;

    GuessingGame(Socket givenSocket) throws IOException {
        gameSocket = givenSocket;
        gameSocket.setTcpNoDelay(true);
        randomNo = new Random(System.currentTimeMillis()).nextInt(21);
    }

    @Override
    public void run() {
        super.run();

        OutputStream gameOutStream;
        InputStream gameInStream;
        try{
            gameOutStream = gameSocket.getOutputStream();
            gameInStream = gameSocket.getInputStream();
        }catch(IOException e){
            System.out.println("ERROR : Data streams could not be set");
            this.closeGameSocket();
            return;
        }

        BufferedReader socketReader;
        try{
            socketReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
        }catch(IllegalArgumentException | IOException e){
            System.out.println("ERROR : readBuffer could not be initialized");
            this.closeGameSocket();
            return;
        }

        String message;
        String output;
        while(true){
            try{
                message = socketReader.readLine();
            }catch(IOException e){
                System.out.println("ERROR : network message could not be read");
                this.closeGameSocket();
                return;
            }
            
            try{
                
                output = handleInput(message);
                synchronized(this){
                    System.out.println(output);
                   /* if(output == null){
                        continue;
                    }*/
                    gameOutStream.write(output.getBytes());
                    gameOutStream.flush();
                }
                if(output.equals("CORRECT\r\n")){
                    this.closeGameSocket();
                    return;
                }
            
            }catch(IOException e){
                System.out.println("ERROR : Data streams could not be sent to tester server");
                e.printStackTrace();
                this.closeGameSocket();
                return;
            }
        
        }

    }

    String handleInput(String message){
        if(message == null){
            return null;
        }
        System.out.println(message);
        String[] words = message.split(" ");
        if(words.length <= 0){
            return "WRONG\r\n";
        }

        switch(words[0]){
            case "TRY":
                if(words.length != 2){
                    return "WRONG\r\n";
                }
                Integer number = Integer.parseInt(words[1]);
                if(number< 0 || number > 20){
                    return "WRONG\r\n";
                }else{
                    if(number < randomNo){
                        return "HIGHER\r\n";
                    }
                    if(number > randomNo){
                        return "LOWER\r\n";
                    }
                    if(number == randomNo){
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

    void closeGameSocket(){
        try{
            this.gameSocket.close();
        }catch(IOException s){
            s.printStackTrace();
        }
    }

}
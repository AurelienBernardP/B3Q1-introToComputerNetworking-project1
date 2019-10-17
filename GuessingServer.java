import java.io.*;
import java.net.*;

import com.sun.jdi.connect.ListeningConnector;

class GuessingServer{
    public static void main(){
        try{
        ServerSocket listeningSocket = new ServerSocket(2019);
        }catch(){

        }

        while(true){
            Socket gameSocket = listeningSocket.accept();
            OutputStream gameOutStream = gameSocket.getOutputStream();
            InputStream gameInStream = gameSocket.getInputStream();
            
        }
    }
}



class Game extends Thread{

    Socket s;

}
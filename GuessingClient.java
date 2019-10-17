import java.io.*;
import java.net.*;

class GuessingClient {
    public static void main(String argv[]){
        
        if(argv.length != 3){
            System.out.println("bad number of arguments given");
            return;
        }
        Socket clientSocket;
        try{
            clientSocket = new Socket("localhost", Integer.parseInt(argv[2]));
        }catch( Exception e){
            System.out.println("ERROR : socket could not be initialized");
            return;
        }

        try{
            clientSocket.setTcpNoDelay(true);
        }catch(SocketException e){
            System.out.println("ERROR : socket could not be set to no delay mode");
            clientSocket.close();
            return;
        }

        OutputStream outStream;
        InputStream inStream;
        try{
            outStream = clientSocket.getOutputStream();
            inStream = clientSocket.getInputStream();
        } catch(IOException e){
            System.out.println("ERROR : Data streams could not be initialized");
            clientSocket.close();
            return;
        }

        try{
            outStream.write(new String(argv[1] + argv[2] + "\n\r").getBytes());
            outStream.flush();
        }catch(IOException e){
            System.out.println("ERROR : Data streams could not be sent to tester server");
            clientSocket.close();
            return;
        }

        BufferedReader socketReader;
        try{
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        }
        catch(IllegalArgumentException e){
            System.out.println("ERROR : readBuffer could not be initialized");
            clientSocket.close();
            return;
        }
        String message;
        try{
            message = socketReader.readLine();
        }
        catch(IOException IOe){
            System.out.println("ERROR : network message could not be read");
            clientSocket.close();
            return;
        }

        if(! message.equals("OK\n\r")){
            System.out.println("ERROR : connection to tester could not be established");
        }
        
        System.out.println("Connection to tester was established");
        clientSocket.close();
    }

}
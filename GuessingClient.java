import java.io.*;
import java.net.*;

class GuessingClient {
    public static void main(String argv[]){
        
        if(argv.length != 3){
            System.out.println("bad number of arguments given");
        }

        try{
            Socket clientSocket = new Socket(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]));
        }catch( Exception e){
            System.out.println("ERROR : socket could not be initialized");
            clientSocket.close();
            return;
        }

        try{
            socket.setTcpNoDelay(true);
        }catch(SocketException e){
            System.out.println("ERROR : socket could not be set to no delay mode");
            clientSocket.close();
            return;
        }

        try{
            OutputStream outStream = clientSocket.getOutputStream();
            InputStream inStream = clientSocket.getInputStream();
        } catch(IOException e){
            System.out.println("ERROR : Data streams could not be initialized");
            clientSocket.close();
            return;
        }

        try{
            outStream.write(new string(argv[1] + argv[2] + "\n\r").getBytes());
            outStream.flush();
        }catch(IOException e){
            System.out.println("ERROR : Data streams could not be sent to tester server");
            clientSocket.close();
            return;
        }

        try{
            BufferedReader socketReader = BufferedReader(inStream);

        }
        catch(IllegalArgumentException e){
            System.out.println("ERROR : readBuffer could not be initialized");
            clientSocket.close();
            return;
        }

        try{
        String message = socketReader.readLine();
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
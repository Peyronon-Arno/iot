import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = 7019;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("La socket serveur est créé");

        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Connexion réussie à la socket " + socket.toString());

            Reader readerSocket = new InputStreamReader(socket.getInputStream());
            BufferedReader messageRecu = new BufferedReader(readerSocket);

            PrintStream messageAEnvoyer = new PrintStream(socket.getOutputStream());

            while(!socket.isClosed()){
                String code = messageRecu.readLine(); //Integer.parseInt(messageRecu.readLine());
                System.out.println(code);
                /*
                if(code == 1){
                    messageAEnvoyer.println(new StringBuffer(code).reverse());
                }else if(code == 2){
                    messageAEnvoyer.println(new StringBuffer(code).reverse());
                }else if(code == 3){
                    socket.close();
                }else{
                    messageAEnvoyer.println(new StringBuffer("Veuillez saisir un nombre !").reverse());
                }*/
            }
        }
    }
}
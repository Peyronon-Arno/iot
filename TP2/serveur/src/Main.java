import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        int port = 7019;
        System.out.println("La socket serveur est créé");

        try (
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split(":");
                String action = parts[0];
                String mot = parts[1];
                switch (action) {
                    case "1" -> outputLine = Integer.toString(valeurDuMot(mot));
                    case "2" -> outputLine = Integer.toString(nombreDeLettres(mot));
                    default -> {
                        return;
                    }
                }
                out.println(outputLine);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écoute sur le port " + port);
            System.out.println(e.getMessage());
        }
    }

    public static int valeurDuMot(String mot) {
        return mot.length();
    }

    public static int nombreDeLettres(String mot) {
        int valeur = 0;
        for (int i = 0; i < mot.length(); i++) {
            char c = Character.toLowerCase(mot.charAt(i));
            valeur += (int) c - 96;
        }
        return valeur;
    }
}

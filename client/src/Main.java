import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    private static int port;
    public static void main (String [] args) {
        String serverHostname = "localhost";
        int portNumber = 7019;

        try (
            Socket socket = new Socket(serverHostname, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            String userInput;
            displayMenu();

            while ((userInput = stdIn.readLine()) != null) {
                if (userInput.equals("1") || userInput.equals("2")) {
                    System.out.print("Entrez un mot : ");
                    String mot = stdIn.readLine();

                    if (userInput.equals("1")) {
                        out.println("1:" + mot);
                    } else if (userInput.equals("2")) {
                        out.println("2:" + mot);
                    }

                    String response = in.readLine();
                    System.out.println("Réponse du serveur : " + response);
                } else if (userInput.equals("3")) {
                    System.out.println("Client terminé.");
                    return;
                } else {
                    System.out.println("Option non valide.");
                }
                displayMenu();
            }
        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu : " + serverHostname);
        } catch (IOException e) {
            System.err.println("Erreur d'E/S pour la connexion au serveur " + serverHostname);
        }
    }

    public static void displayMenu() {
        System.out.println("1 - Nombres de lettres \n ");
        System.out.println("2 - Valeur des lettres \n ");
        System.out.println("3 - Quitter \n ");
    }

}



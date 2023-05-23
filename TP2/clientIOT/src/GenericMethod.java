import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class GenericMethod {

  public void getContentOfPageWithSocket(Socket socket, String request, String serverHostname) throws IOException {
    request = request + " HTTP/1.1\r\n" +
        "Host: " + serverHostname + "\r\n" +
        "Connection: close\r\n\r\n";;
    socket.getOutputStream().write(request.getBytes());
    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String line;
    StringBuilder response = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      response.append(line).append("\n");
    }
    socket.close();
    extractContentOfResult(response);
  }

  public void getContentOfPage(URL url) throws IOException {
    Scanner scNet = new Scanner(url.openStream());
    StringBuilder sb = new StringBuilder();
    while(scNet.hasNext()) {
      sb.append(scNet.next());
    }
    extractContentOfResult(sb);
  }

  public void extractContentOfResult(StringBuilder response) {
    String result = response.toString();
    result= result.replaceAll("<[^>]*>", "\n");
    System.out.println(result);
  }
  public void wrongChoice() {
    System.out.println("Choix effectué non valide");
  }


  public  void displayMenu() {
    System.out.println("Menu : \n");
    System.out.println("1 - Aller sur toto.html");
    System.out.println("2 - Case 2");
  }

  public String getNom() {
    Scanner sc2 = new Scanner(System.in);
    System.out.println("Entrez le nom");
    return sc2.nextLine();
  }

  public String getAge() {
    Scanner sc3 = new Scanner(System.in);
    System.out.println("Entrez l'age");
    return sc3.nextLine();
  }
}

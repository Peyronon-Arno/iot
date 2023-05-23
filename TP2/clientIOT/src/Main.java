import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
      // useHttUrlConnection();
      useSocket();

<<<<<<< HEAD
        Scanner sc = new Scanner(System.in);
        displayMenu();
        String value = sc.nextLine();
        while(value != null) {
            if(value.equals("1")) {
              URL url = new URL("http://localhost/iot/TP2/php/index.php");
              getContentOfPage(url);
              break;
            }
            else if(value.equals("2")) {
              String nom = getNom();
              String age = getAge();
              URL url = new URL("http://localhost/iot/TP2/php/index.php?nom="+nom+"&age="+age);
              getContentOfPage(url);
            } else {
              System.out.println("Choix effectué non valide");
            }
            value = sc.nextLine();
        }
=======
>>>>>>> d2f5a6c289745b003f5d60e54ea9f40b1ee88ac6
    }

  protected static void useSocket() throws IOException {
    Scanner sc = new Scanner(System.in);
    displayMenu();
    String value = sc.nextLine();
    String serverHostname = "localhost";
    String path = "/iot/TP2/php/index.php";
    int port = 80;
    String request = "GET " + path;

    while(value != null) {
      if(value.equals("1")) {
        Socket socket = new Socket(serverHostname, port);
        getContentOfPageWithSocket(socket, request, serverHostname);
      } else if (value.equals("2")) {
        String nom = getNom();
        String age = getAge();
        request = request + "?nom=" + nom + "&age=" + age;
        Socket socket = new Socket(serverHostname, port);
        getContentOfPageWithSocket(socket, request, serverHostname);
      } else {
        wrongChoice();
      }
      displayMenu();
      value = sc.nextLine();
    }
  }

  protected static void getContentOfPageWithSocket(Socket socket, String request, String serverHostname) throws IOException {
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

  protected static void extractContentOfResult(StringBuilder response) {
    String result = response.toString();
    result= result.replaceAll("<[^>]*>", "\n");
    System.out.println(result);
  }

  protected static void useHttÛrlConnection() throws IOException {
    Scanner sc = new Scanner(System.in);
    displayMenu();
    String value = sc.nextLine();
    while(value != null) {
      if(value.equals("1")) {
        URL url = new URL("http://localhost/iot/TP2/php/index.php");
        getContentOfPage(url);
      }
      else if(value.equals("2")) {
        String nom = getNom();
        String age = getAge();
        URL url = new URL("http://localhost/iot/TP2/php/index.php?nom="+nom+"&age="+age);
        getContentOfPage(url);
      } else {
        wrongChoice();
      }
      displayMenu();
      value = sc.nextLine();
    }
  }

  protected static void wrongChoice() {
    System.out.println("Choix effectué non valide");
  }

  protected static void getContentOfPage(URL url) throws IOException {
    Scanner scNet = new Scanner(url.openStream());
    StringBuilder sb = new StringBuilder();
    while(scNet.hasNext()) {
      sb.append(scNet.next());
    }
    extractContentOfResult(sb);
  }

  protected static void displayMenu() {
    System.out.println("Menu : \n");
    System.out.println("1 - Aller sur toto.html");
    System.out.println("2 - Case 2");
  }

  protected static String getNom() {
    Scanner sc2 = new Scanner(System.in);
    System.out.println("Entrez le nom");
    return sc2.nextLine();
  }

  protected static  String getAge() {
    Scanner sc3 = new Scanner(System.in);
    System.out.println("Entrez l'age");
    return sc3.nextLine();
  }
}
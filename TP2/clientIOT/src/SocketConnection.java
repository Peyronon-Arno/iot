import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SocketConnection {

  private static final GenericMethod genericMethod=  new GenericMethod();
  public void useSocket() throws IOException {
    Scanner sc = new Scanner(System.in);
    genericMethod.displayMenu();
    String value = sc.nextLine();
    String serverHostname = "localhost";
    String path = "/iot/TP2/php/index.php";
    int port = 80;
    String request = "GET " + path;

    while(value != null) {
      if(value.equals("1")) {
        Socket socket = new Socket(serverHostname, port);
        genericMethod.getContentOfPageWithSocket(socket, request, serverHostname);
      } else if (value.equals("2")) {
        String nom = genericMethod.getNom();
        String age = genericMethod.getAge();
        request = request + "?nom=" + nom + "&age=" + age;
        Socket socket = new Socket(serverHostname, port);
        genericMethod.getContentOfPageWithSocket(socket, request, serverHostname);
      } else {
        genericMethod.wrongChoice();
      }
      genericMethod.displayMenu();
      value = sc.nextLine();
    }
  }
}

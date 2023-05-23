import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class UrlConnection {

  private static final GenericMethod genericMethod = new GenericMethod();
  protected void useHttUrlConnection() throws IOException {
    Scanner sc = new Scanner(System.in);
    genericMethod.displayMenu();
    String value = sc.nextLine();
    while(value != null) {
      if(value.equals("1")) {
        URL url = new URL("http://localhost/iot/TP2/php/index.php");
        genericMethod.getContentOfPage(url);
      }
      else if(value.equals("2")) {
        String nom = genericMethod.getNom();
        String age = genericMethod.getAge();
        URL url = new URL("http://localhost/iot/TP2/php/index.php?nom="+nom+"&age="+age);
        genericMethod.getContentOfPage(url);
      } else {
        genericMethod.wrongChoice();
      }
      genericMethod.displayMenu();
      value = sc.nextLine();
    }
  }
}

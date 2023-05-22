import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
      int port = 80;
      System.out.println("La socket serveur est créé");

        Scanner sc = new Scanner(System.in);
        displayMenu();
        String value = sc.nextLine();
        while(value != null) {
            if(value.equals("1")) {
              URL url = new URL("http://www.something.com/");
              Scanner scNet = new Scanner(url.openStream());
              StringBuffer sb = new StringBuffer();
              while(scNet.hasNext()) {
                sb.append(scNet.next());
              }
              String result = sb.toString();
              System.out.println(result);
              result = result.replaceAll("<[^>]*>", "");
              System.out.println("Contents of the web page: "+result);
              break;
            }
            else if(value.equals("2")) {
              String nom = getNom();
              String age = getAge();

            } else {
              System.out.println("Choix effectué non valide");
            }
            value = sc.nextLine();
        }
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
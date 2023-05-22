import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
      // useHttÛrlConnection();

    }

  private static void useHttÛrlConnection() throws IOException {
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
          System.out.println("Choix effectué non valide");
        }
        displayMenu();
        value = sc.nextLine();
    }
  }

  protected static void getContentOfPage(URL url) throws IOException {
    Scanner scNet = new Scanner(url.openStream());
    StringBuilder sb = new StringBuilder();
    while(scNet.hasNext()) {
      sb.append(scNet.next());
    }
    String result = sb.toString();
    Pattern pattern = Pattern.compile("<[^>]*>");
    Matcher matcher = pattern.matcher(result);
    result = matcher.replaceAll("");
    result = result.trim().replaceAll("\\s+", " \n");
    System.out.println(result);
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
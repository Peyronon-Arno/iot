<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>TP2 IoT</title>
  </head>
  <body>
    <h1>Bonjour</h1>

    <div>
      <?php 
        session_start();
        if(isset($_SESSION) && isset($_SESSION["nom"]) && isset($_SESSION["age"])){
          echo("<p>Vous êtez connecté en tant que : " . $_SESSION["nom"] . ".</p>");
          echo("<p>Vous avez " . $_SESSION["age"] . " ans.</p>");
        } else{
          echo("Vous n'êtez pas connecté");
        }
      ?>
    </div>

    <a href="./form.html">Go to form</a>
  </body>
</html>

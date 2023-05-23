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
        if(isset($_POST["nom"]) && isset($_POST["age"])){
          echo("<p>Vous êtez connecté en tant que : " . $_POST["nom"] . ".</p>");
          echo("<p>Vous avez " . $_POST["age"] . " ans.</p>");
        } else{
          echo("<p>Vous n'êtez pas connecté.</p>");
        }
      ?>
    </div>

    <a href="./form.html">Go to form</a>
  </body>
</html>

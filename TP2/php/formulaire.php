<?php

    session_start();

    $_SESSION["nom"] = $_GET["nom"];
    $_SESSION["age"] = $_GET["age"];

    header("Location: ./index.php");
    exit();
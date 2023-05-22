<?php

    session_start();

    $_SESSION["nom"] = $_GET["nom"];
    $_SESSION["age"] = $_GET["age"];

    /* $host = "172.20.10.3";
    $port = 7050;
    $message = "test";

    $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
    socket_connect($socket,$host,$port);
    socket_write($socket,$message);
    $retour = socket_read($socket,2048);
    echo $retour;
    socket_close($socket); */

    header("Location: ./index.php");
    exit();
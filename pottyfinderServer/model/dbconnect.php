<?php
function getConnection(){  
    $servername = "localhost"; //servername
    $username = "root"; //username
    $password = "1234"; //password of db
    $dbname = "pottyfinder"; //name of the DB

    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname); 
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    
    return $conn;
}


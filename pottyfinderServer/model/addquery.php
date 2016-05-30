<?php

//All Required Files for the PHP file to function
    require 'dbconnect.php'; //Database Connection
    require_once 'getquery.php'; //getters to the Database
    require_once 'updateQuery.php'; //updates values in Database

    /** Follows this format
        
        1. Get Connection
            1.5 Process any parameters that need processing
        2. Prepare the SQL Statements
        3. Bind to parameters if there are any
        4. Execute the query
        5. Return the success of the query

    **/    

    function addBenefit($benelist){

        $conn = getConnection();

        $stmt = $conn->prepare("INSERT INTO `pottyfinder`.`benefitslist` (`btypeID`, `PottyTable_pottyid`) VALUES (?,?)");

        $stmt->bind_param("si", $benelist['bname'], $benelist['bid']);
        
        $ret = $stmt->execute();
        $raddid = $conn->insert_id;
        $conn->close();  

        return $raddid;
    }

    //Adds the in the database
    function addRestroom($restroom){

        $conn = getConnection();

        $stmt = $conn->prepare("INSERT INTO `pottyfinder`.`pottytable` (`ptName`, `longitude`, `latitude`, `clean`, `access`, `active`, `rtType_idrtType`, `userid`) VALUES(?,?,?,'1',?,'Y',?,?)");

        $stmt->bind_param("sddsss", $restroom['rname'], $restroom['rlong'], $restroom['rlat'], $restroom['raccess'], $restroom['rtype'], $restroom['ruserid']);
        
        $ret = $stmt->execute();
        $raddid = $conn->insert_id;
        $conn->close();  

        if($raddid != 0){

            $bens = $restroom['benefits'];
            $bene = Array();
            $bene['bid'] = $raddid; 
            for($i = 0; $i < count($bens); $i++){
                $bene['bname'] = $bens[$i];

                addBenefit($bene);
            }

        }
        else{
            return 0;
        }
        return $raddid;
    }



    //Adds a favorite restroom in the list
    function addFavoriteList($fv_rs){

        $conn = getConnection();

        $stmt = $conn->prepare("INSERT INTO `pottyfinder`.`favoriteslist` (`userid`, `PottyTable_pottyid`) VALUES (?,?)");

        $stmt->bind_param("si", $fv_rs['rid'], $fv_rs['rtable']);

        $ret = $stmt->execute();
        $raddid = $conn->insert_id;
        $conn->close();  

        return $raddid;

        
    }

    //Adds the Review for the Restroom
    function addReview($review){

        $conn = getConnection();

        $stmt = $conn->prepare(" INSERT INTO `pottyfinder`.`reviewtable` (`comment`, `PottyTable_pottyid`, `userid`, `rating`) VALUES (?,?,?,?)");

        $stmt->bind_param("siss", $review['rcomment'], $review['rtable'], $review['rid'], $review['rate']);

        $ret = $stmt->execute();
        $raddid = $conn->insert_id;
        $conn->close();  

        return $raddid;

       
    }

    if (isset($_POST['type']) && !empty($_POST['type'])) {
        $action = $_POST['type'];
        switch($action) {
            case 'arestroom': 
                
                $jsss = json_decode($_POST['restroom'], true);
                echo addRestroom($jsss);

                break;
            case 'afave': 


                break;
            case 'areview': 

                $jd = json_decode($_POST['review'], true);

                echo addReview($jd);

                break;
        }
    }  
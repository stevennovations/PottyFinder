<?php
    
    //All Required Files for the PHP file to function
    require_once 'dbconnect.php'; //Database Connection

    /** Follows this format
        
        1. Get Connection
            1.5 Process any parameters that need processing
        2. Prepare the SQL Statements
        3. Bind to parameters if there are any
        4. Execute the query
        5. Return the Array of objects that you have queried

    **/

    function getAllRestrooms(){

        $inx = 0;
        $conn = getConnection();

        $stmt = $conn->prepare("SELECT * FROM pottytable");

        $stmt->execute();

        $result = $stmt->get_result();

        if($result->num_rows > 0){
            while($row = $result->fetch_assoc()){


                $benefits = getBenefits($row['pottyid']);

                $resarr[$inx] = array(

                    "pid" => $row['pottyid'],
                    "ptn" => $row['ptName'],
                    "ptlo" => $row['longitude'],
                    "ptla" => $row['latitude'],
                    "ptc" => $row['clean'],
                    "pta" => $row['access'],
                    "ptac" => $row['active'],
                    "ptrt" => $row['rtType_idrtType'],
                    "belist" => $benefits

                );
                $inx++;
            }
        }
        $conn->close();  

        echo json_encode($resarr);
    }

    function getRestroom($id){
        $inx = 0;
        $conn = getConnection();

        $benefits = getBenefits($id);
        $comments = getComments($id);

        $stmt = $conn->prepare("SELECT * FROM pottytable where pottyid = ?");
        $stmt->bind_param("i", $id);

        $stmt->execute();

        $result = $stmt->get_result();

        if($result->num_rows > 0){

            while($row = $result->fetch_assoc()){
                $resarr[$inx] = array(

                    "pid" => $row['pottyid'],
                    "ptn" => $row['ptName'],
                    "ptlo" => $row['longitude'],
                    "ptla" => $row['latitude'],
                    "ptc" => $row['clean'],
                    "pta" => $row['access'],
                    "ptac" => $row['active'],
                    "ptrt" => $row['rtType_idrtType'],
                    "belist" => $benefits,
                    "comnts" => $comments
                    
                );
                $inx++;
            }
        }
        $conn->close();  

        echo json_encode($resarr);

    }

    function getBenefits($rid){

        $inx = 0;
        $benefits = array();
        $conn = getConnection();

        $blist = $conn->prepare("SELECT btypeID FROM benefitslist where PottyTable_pottyid = ?");
        $blist->bind_param("i", $rid);

        $blist->execute();

        $resList = $blist->get_result();

        if($resList->num_rows > 0){

            while($row2 = $resList->fetch_assoc()){
                $benefits[$inx] = $row2['btypeID'];
                $inx++;
            }
        }
        else{
            $benefits = Array();
        }
        $conn->close();  

        return $benefits;

    }

    function getComments($tid){
        $inx = 0;
        $conn = getConnection();

        $stmt = $conn->prepare("SELECT comment, PottyTable_pottyid, userid, rating FROM reviewtable where PottyTable_pottyid = ?");
        $stmt->bind_param("i", $tid);

        $stmt->execute();

        $result = $stmt->get_result();

        if($result->num_rows > 0){

            while($row = $result->fetch_assoc()){
                $resarr[$inx] = array(

                    "comment" => $row['comment'],
                    "ptid" => $row['PottyTable_pottyid'],
                    "userid" => $row['userid'],
                    "rts" => $row['rating']
                );

                $inx++;
            }
        }
        else {
            $resarr = Array();
        }
        $conn->close();  

        return $resarr;

    }

    function getAddedBy($uid){
        $inx = 0;
        $conn = getConnection();

        //echo $uid;

        $stmt = $conn->prepare("SELECT * FROM pottytable where userid = ?");
        $stmt->bind_param("d", $uid);

        //echo $uid;

        $stmt->execute();

        $result = $stmt->get_result();

        if($result->num_rows > 0){

            while($row = $result->fetch_assoc()){
                $resarr[$inx] = array(

                    "pid" => $row['pottyid'],
                    "ptn" => $row['ptName'],
                    "ptlo" => $row['longitude'],
                    "ptla" => $row['latitude'],
                    "ptc" => $row['clean'],
                    "pta" => $row['access'],
                    "ptac" => $row['active'],
                    "ptrt" => $row['rtType_idrtType']                    
                );
                $inx++;
            }
        }
        else{
            $resarr = Array();
        }
        $conn->close();  

        echo json_encode($resarr);
    }

    function getAllPoints($id){

        /*

            Point System is
            1. Pag nag add ng cr 2 points
            2. Review ng cr 1 point
            3. Kada review ng cr na inadd mo 1 point

        */

        $inx = 0;
        $conn = getConnection();

        //echo $uid;

        $stmt = $conn->prepare("SELECT count(*) FROM pottytable where userid = ?");
        $stmt->bind_param("d", $uid);

        //echo $uid;

        $stmt->execute();

        $result = $stmt->get_result();

        if($result->num_rows > 0){

            while($row = $result->fetch_assoc()){
                $resarr[$inx] = array(

                    "cnt" => $row['count(*)']                 
                );
                $inx++;
            }
        }
        else{
            $resarr = Array();
        }
        $conn->close();  

        echo json_encode($resarr);
    }


    if (isset($_POST['type']) && !empty($_POST['type'])) {
        $action = $_POST['type'];
        switch($action) {
            case 'getRestroom': 

                getRestroom($_POST['rid']);
                

                break;
            case 'gallrestroom':

                getAllRestrooms();

                break;
            case 'gfave': 

                getFavorites($_POST['uid']);

                break;
            case 'greview': 

                getComments($_POST['toiletID']);

                break;
            case 'getAdded':

                getAddedBy($_POST['uidd']);

                break;
        }
    }
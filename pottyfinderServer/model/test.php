<?php

	//INSERT INTO `pottyfinder`.`pottytable` (`ptName`, `pt_desc`, `longitude`, `latitude`, `clean`, `access`, `active`, `rtType_idrtType`, `userid`) VALUES ('

	require 'addquery.php';
	require_once 'getquery.php';

	// $r1['rname'] = 'One Archers Restroom';
	// $r1['rdesc'] = 'This is the one archers restrom its really nice';
	// $r1['rlong'] = 120.992933;
	// $r1['rlat'] = 14.566839;
	// $r1['rclean'] = 3;
	// $r1['raccess'] = 'public';
	// $r1['rtype'] = 'Male&Female';
	// $r1['ruserid'] = '1823534311';

	// addRestroom($r1);

	//getAllRestrooms();

	//getRestroom(1018);
	//getAddedBy(116628554671590);
	getAllPoints(116628554671590);
	//getComments(1001);
	//getBenefits(1001);
	// $jscon = getAllRestrooms();

	// $jsss = json_decode($jscon, true);

	// //echo '<br /><br />';
	// $bene = Array();
	// foreach($jsss as $item){
	// 	$hello = $item['belist'];
		
	// 	$bene['bid'] = 1; 
	// 	for($i = 0; $i < count($hello); $i++){
	// 		$bene['bname'] = $hello[$i];
	// 		echo $bene['bname'];
	// 		echo $bene['bid'] . '<br />';
	// 	}

	// 	echo $bene['bname'];
	// }
	


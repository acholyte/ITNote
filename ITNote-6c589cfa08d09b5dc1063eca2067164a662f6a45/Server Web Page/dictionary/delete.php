<?php
	// 데이터베이스의 레코드를 삭제하는 페이지
	include "dbConnect.php";
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	$num = $_POST["num"]; // POST 변수로 번호 받아옴

	$db = new DBConnect;
	$db->connect();
	
	$query = "delete from dictionary where num = $num;"; // DB에서 레코드를 삭제하기 위한 쿼리문

	mysql_query($query); // 쿼리문 실행

	$db->disconnect();
?>

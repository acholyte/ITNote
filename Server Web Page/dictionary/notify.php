<?php
	// DB 서버의 최근에 변경된 날짜를 알려주는 페이지
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	include 'dbConnect.php';

	$db = new DBConnect;
	$db->connect();

	$sql = "select MAX(regdate) from dictionary limit 1"; // 데이터베이스에서 가장 최근에 변경된 레코드의 변경일자를 읽어옴

	$result = mysql_query($sql); // 쿼리 실행 결과를 담음

	$obj = mysql_result($result, 0); // result에서 변경일자를 읽어옴
	
	echo $obj;

	$db->disconnect();
?>

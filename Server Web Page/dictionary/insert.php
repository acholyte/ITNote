<?php
	// 데이터베이스의 레코드를 삽입하는 페이지
	include "dbConnect.php";
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	$subject = $_POST["subject"]; // POST 변수로 제목 받아옴
	$content = $_POST["content"]; // POST 변수로 내용 받아옴
	$ip = $_SERVER["REMOTE_ADDR"]; // 클라이언트의 IP

	$db = new DBConnect;
	$conn = $db->connect();
	
	$query = "insert into dictionary (subject, content, ip) values ('$subject', '$content', '$ip');"; // DB에 삽입하기 위한 쿼리문

	mysql_query($query); // 쿼리문 실행

	$db->disconnect();
?>

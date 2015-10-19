<?php
	// 데이터베이스에 레코드를 수정하는 페이지
	include "dbConnect.php";
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	$subject = $_POST["subject"]; // POST 변수로 제목 변수 받아옴
	$content = $_POST["content"]; // POST 변수로 내용 변수 받아옴
	$num = $_POST["num"]; // POST 변수로 번호 변수 받아옴
	$ip = $_SERVER["REMOTE_ADDR"]; // 클라이언트 IP 받아옴

	$db = new DBConnect;
	$db->connect();
	 
	$query = "update dictionary set subject = '$subject', content = '$content', ip = '$ip', regdate = now() where num = $num;"; // DB에서 레코드를 수정하기 위한 쿼리문

	mysql_query($query); // 쿼리문 실행

	$db->disconnect();
?>

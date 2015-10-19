<?php
	// 데이터베이스 연결 페이지
	include 'dbInfo.php';
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	// 데이터베이스 연결 관리 페이지
	class DBConnect {
		// 연결 시작 함수
		function connect() {
			// 데이터베이스에 연결
			$connect = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die('Could not connect to Server '.mysql_errno());
			
			// 데이터베이스 선택
			$select_db = mysql_select_db(DB_NAME);

			// 연결 객체 반환
			return $connect;
		}

		// 연결 종료 함수
		function disconnect() {
			// 연결 종료
			mysql_close();
		}
	}
?>

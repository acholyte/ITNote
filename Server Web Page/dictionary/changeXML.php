<?php
	// XML 갱신 페이지
	error_reporting(E_ALL);
	ini_set("display_errors", 1);
	
	include 'dbConnect.php';

	// 데이터베이스 연결 객체 생성
	$db = new DBConnect;
	$db->connect();

	$sql = "select * from dictionary";

	$result = mysql_query($sql);
	
	// XML 문서 생성
	// .= 연산자를 이용해 문자열 연결
	$xmlDoc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	$xmlDoc .= "<dictionary>";
	
	// DB의 컬럼명을 이용해 레코드의 값을 가져옴
	while ($obj = mysql_fetch_object($result)) {
		$dicNum = $obj->num;
		$dicSubject = $obj->subject;
		$dicContent = $obj->content;
		$dicRegdate = $obj->regdate;
		$dicIp = $obj->ip;

		$dicContent = str_replace("\n", "&#xA;", $dicContent); // 라인 피드 문자 처리
		$dicContent = str_replace("\r", "&#xD;", $dicContent); // 캐리지 리턴 문자 처리
		
		$xmlDoc .= "<item>";
		$xmlDoc .= "<num>$dicNum</num>";
		$xmlDoc .= "<subject>$dicSubject</subject>";
		$xmlDoc .= "<content>$dicContent</content>";
		$xmlDoc .= "<regdate>$dicRegdate</regdate>";
		$xmlDoc .= "<ip>$dicIp</ip>";
		$xmlDoc .= "</item>";
	}

	$xmlDoc .= "</dictionary>";

	// XML 문서로 저장
	$dir = "/var/www/html/dictionary";
	$filename = $dir."/dicXML.xml";

	file_put_contents($filename, $xmlDoc); // 기존 문서가 있으면 이를 지우고 새로 생성
	$db->disconnect();
?>

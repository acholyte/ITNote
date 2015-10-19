<?php
	// XML ���� ������
	error_reporting(E_ALL);
	ini_set("display_errors", 1);
	
	include 'dbConnect.php';

	// �����ͺ��̽� ���� ��ü ����
	$db = new DBConnect;
	$db->connect();

	$sql = "select * from dictionary";

	$result = mysql_query($sql);
	
	// XML ���� ����
	// .= �����ڸ� �̿��� ���ڿ� ����
	$xmlDoc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	$xmlDoc .= "<dictionary>";
	
	// DB�� �÷����� �̿��� ���ڵ��� ���� ������
	while ($obj = mysql_fetch_object($result)) {
		$dicNum = $obj->num;
		$dicSubject = $obj->subject;
		$dicContent = $obj->content;
		$dicRegdate = $obj->regdate;
		$dicIp = $obj->ip;

		$dicContent = str_replace("\n", "&#xA;", $dicContent); // ���� �ǵ� ���� ó��
		$dicContent = str_replace("\r", "&#xD;", $dicContent); // ĳ���� ���� ���� ó��
		
		$xmlDoc .= "<item>";
		$xmlDoc .= "<num>$dicNum</num>";
		$xmlDoc .= "<subject>$dicSubject</subject>";
		$xmlDoc .= "<content>$dicContent</content>";
		$xmlDoc .= "<regdate>$dicRegdate</regdate>";
		$xmlDoc .= "<ip>$dicIp</ip>";
		$xmlDoc .= "</item>";
	}

	$xmlDoc .= "</dictionary>";

	// XML ������ ����
	$dir = "/var/www/html/dictionary";
	$filename = $dir."/dicXML.xml";

	file_put_contents($filename, $xmlDoc); // ���� ������ ������ �̸� ����� ���� ����
	$db->disconnect();
?>

<?php
	// �����ͺ��̽��� ���ڵ带 �����ϴ� ������
	include "dbConnect.php";
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	$subject = $_POST["subject"]; // POST ������ ���� �޾ƿ�
	$content = $_POST["content"]; // POST ������ ���� �޾ƿ�
	$ip = $_SERVER["REMOTE_ADDR"]; // Ŭ���̾�Ʈ�� IP

	$db = new DBConnect;
	$conn = $db->connect();
	
	$query = "insert into dictionary (subject, content, ip) values ('$subject', '$content', '$ip');"; // DB�� �����ϱ� ���� ������

	mysql_query($query); // ������ ����

	$db->disconnect();
?>

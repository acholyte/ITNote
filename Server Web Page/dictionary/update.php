<?php
	// �����ͺ��̽��� ���ڵ带 �����ϴ� ������
	include "dbConnect.php";
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	$subject = $_POST["subject"]; // POST ������ ���� ���� �޾ƿ�
	$content = $_POST["content"]; // POST ������ ���� ���� �޾ƿ�
	$num = $_POST["num"]; // POST ������ ��ȣ ���� �޾ƿ�
	$ip = $_SERVER["REMOTE_ADDR"]; // Ŭ���̾�Ʈ IP �޾ƿ�

	$db = new DBConnect;
	$db->connect();
	 
	$query = "update dictionary set subject = '$subject', content = '$content', ip = '$ip', regdate = now() where num = $num;"; // DB���� ���ڵ带 �����ϱ� ���� ������

	mysql_query($query); // ������ ����

	$db->disconnect();
?>

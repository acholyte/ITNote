<?php
	// �����ͺ��̽��� ���ڵ带 �����ϴ� ������
	include "dbConnect.php";
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	$num = $_POST["num"]; // POST ������ ��ȣ �޾ƿ�

	$db = new DBConnect;
	$db->connect();
	
	$query = "delete from dictionary where num = $num;"; // DB���� ���ڵ带 �����ϱ� ���� ������

	mysql_query($query); // ������ ����

	$db->disconnect();
?>

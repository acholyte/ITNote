<?php
	// DB ������ �ֱٿ� ����� ��¥�� �˷��ִ� ������
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	include 'dbConnect.php';

	$db = new DBConnect;
	$db->connect();

	$sql = "select MAX(regdate) from dictionary limit 1"; // �����ͺ��̽����� ���� �ֱٿ� ����� ���ڵ��� �������ڸ� �о��

	$result = mysql_query($sql); // ���� ���� ����� ����

	$obj = mysql_result($result, 0); // result���� �������ڸ� �о��
	
	echo $obj;

	$db->disconnect();
?>

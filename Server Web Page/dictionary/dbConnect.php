<?php
	// �����ͺ��̽� ���� ������
	include 'dbInfo.php';
	
	error_reporting(E_ALL);
	ini_set("display_errors", 1);

	// �����ͺ��̽� ���� ���� ������
	class DBConnect {
		// ���� ���� �Լ�
		function connect() {
			// �����ͺ��̽��� ����
			$connect = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die('Could not connect to Server '.mysql_errno());
			
			// �����ͺ��̽� ����
			$select_db = mysql_select_db(DB_NAME);

			// ���� ��ü ��ȯ
			return $connect;
		}

		// ���� ���� �Լ�
		function disconnect() {
			// ���� ����
			mysql_close();
		}
	}
?>

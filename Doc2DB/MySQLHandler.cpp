/*
 * DB�� �����ϴ� Ŭ����
 */
#include "MySQLHandler.h"

MySQLHandler::MySQLHandler() {
	try {	
		driver = mysql::get_mysql_driver_instance(); // MySQL ����̹� �ν��Ͻ� �޾ƿ�
		conn = driver->connect("tcp://127.0.0.1:3306", "test", "abc123"); // ����̹��� �̿��� MySQL�� ����
	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	}	
}

MySQLHandler::~MySQLHandler() {
	try {
		if (stmt != NULL) delete stmt; // ����� Statement �޸� ����
		if (pstmt != NULL) delete pstmt; // ����� Prestatement �޸� ����
		delete conn; // ���� ����
	} catch (SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	}	
}

// �����ͺ��̽��� ����ϱ� ���� �Լ�
void MySQLHandler::useDB() {
	try {
		stmt = conn->createStatement(); // Connection ��ü�� ���� Statement ��ü�� ����
		stmt->execute("use dictionary"); // DB�� ����ϱ� ���� ������ ����
	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	} 	
}

// ������ ���� �Լ�
void MySQLHandler::insertDB(SQLString subject, SQLString content, SQLString ip) {

	try {
		pstmt = conn->prepareStatement("insert into dictionary(subject, content, ip) values (?, ?, ?)"); // Connection ��ü�� ���� Prestatement ��ü�� ����

		pstmt->setString(1, subject); // ����
		pstmt->setString(2, content); // ����
		pstmt->setString(3, ip); // IP

		pstmt->execute(); // ������ ����
	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	} 	
}

void MySQLHandler::selectDB() {
	try {
		rs = stmt->executeQuery("select * from dictionary"); // Statement ��ü�� �������� ������ ResultSet ��ü�� ����
		
		while (rs->next()) {
			// cout << rs->getString("num") << endl;
			cout << rs->getString("subject") << endl; // ���� ���
			cout << rs->getString("content") << endl; // ���� ���
		}	

	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	} 
}

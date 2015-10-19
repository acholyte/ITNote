/*
 * DB에 연결하는 클래스
 */
#include "MySQLHandler.h"

MySQLHandler::MySQLHandler() {
	try {	
		driver = mysql::get_mysql_driver_instance(); // MySQL 드라이버 인스턴스 받아옴
		conn = driver->connect("tcp://127.0.0.1:3306", "test", "abc123"); // 드라이버를 이용해 MySQL에 연결
	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	}	
}

MySQLHandler::~MySQLHandler() {
	try {
		if (stmt != NULL) delete stmt; // 사용한 Statement 메모리 해제
		if (pstmt != NULL) delete pstmt; // 사용한 Prestatement 메모리 해제
		delete conn; // 연결 해제
	} catch (SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	}	
}

// 데이터베이스를 사용하기 위한 함수
void MySQLHandler::useDB() {
	try {
		stmt = conn->createStatement(); // Connection 객체를 통해 Statement 객체를 만듦
		stmt->execute("use dictionary"); // DB를 사용하기 위한 쿼리문 실행
	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	} 	
}

// 데이터 삽입 함수
void MySQLHandler::insertDB(SQLString subject, SQLString content, SQLString ip) {

	try {
		pstmt = conn->prepareStatement("insert into dictionary(subject, content, ip) values (?, ?, ?)"); // Connection 객체를 통해 Prestatement 객체를 만듦

		pstmt->setString(1, subject); // 제목
		pstmt->setString(2, content); // 내용
		pstmt->setString(3, ip); // IP

		pstmt->execute(); // 쿼리문 실행
	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	} 	
}

void MySQLHandler::selectDB() {
	try {
		rs = stmt->executeQuery("select * from dictionary"); // Statement 객체의 쿼리문을 실행해 ResultSet 객체를 만듦
		
		while (rs->next()) {
			// cout << rs->getString("num") << endl;
			cout << rs->getString("subject") << endl; // 제목 출력
			cout << rs->getString("content") << endl; // 내용 출력
		}	

	} catch(SQLException &e) {
		std::cout << "Error: " << e.what() << std::endl;
	} 
}

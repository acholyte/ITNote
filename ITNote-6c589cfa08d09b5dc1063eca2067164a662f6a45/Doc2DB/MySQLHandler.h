#include <iostream>

#include "Connector/include/mysql_driver.h"

#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/statement.h>
#include <cppconn/prepared_statement.h>
#include <cppconn/resultset.h>

using namespace sql;
using std::cout;
using std::endl;

class MySQLHandler {
	public:
		MySQLHandler();
		~MySQLHandler();

		void useDB();
		void insertDB(SQLString subject, SQLString content, SQLString ip);
		void selectDB();
	private:
		mysql::MySQL_Driver *driver;
		Connection *conn;
		Statement *stmt;
		PreparedStatement *pstmt;
		ResultSet *rs;
};

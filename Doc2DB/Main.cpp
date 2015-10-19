/*
 * Main 함수
 */
#include "DocReader.h"
#include "MySQLHandler.h"

int main() {
	fstream fs; // 파일 객체
	string subject, content;
	int num = 0;
	DocReader *reader; // 문서를 읽기 위한 객체
	MySQLHandler *handler; // DB에 연결하기 위한 객체

	fs.open("Document.txt"); // 문서 열기

	if (fs.fail()) {
		cout << "File Open Error" << endl;
		return -1;
	}

	reader = new DocReader(&fs); // 파일 객체를 생성자에 넣고 초기화
	handler = new MySQLHandler();
	handler->useDB();

	while (!fs.eof()) {
		reader->parse(&fs, &num, &subject, &content); // 문서에서 번호, 제목, 내용을 가져옴

		// cout << "num: " << num << endl;

		if (num == 0) break; // 문서에서 번호가 0인 경우-> 더 이상 읽을 내용이 없는 경우

		handler->insertDB(subject, content, "192.168.0.1"); // DB에 번호, 제목, 내용을 삽입

		num = 0; // 다시 초기화
		subject = "";
		content = "";
	}

	cout << "End" << endl;

	handler->selectDB(); // 삽입 후 완료된 결과를 보여줌

	delete handler;
	delete reader;

	fs.close();

	return 0;
}

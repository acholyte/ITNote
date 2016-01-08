/*
 * Main �Լ�
 */
#include "DocReader.h"
#include "MySQLHandler.h"

int main() {
	fstream fs; // ���� ��ü
	string subject, content;
	int num = 0;
	DocReader *reader; // ������ �б� ���� ��ü
	MySQLHandler *handler; // DB�� �����ϱ� ���� ��ü

	fs.open("Document.txt"); // ���� ����

	if (fs.fail()) {
		cout << "File Open Error" << endl;
		return -1;
	}

	reader = new DocReader(&fs); // ���� ��ü�� �����ڿ� �ְ� �ʱ�ȭ
	handler = new MySQLHandler();
	handler->useDB();

	while (!fs.eof()) {
		reader->parse(&fs, &num, &subject, &content); // �������� ��ȣ, ����, ������ ������

		// cout << "num: " << num << endl;

		if (num == 0) break; // �������� ��ȣ�� 0�� ���-> �� �̻� ���� ������ ���� ���

		handler->insertDB(subject, content, "192.168.0.1"); // DB�� ��ȣ, ����, ������ ����

		num = 0; // �ٽ� �ʱ�ȭ
		subject = "";
		content = "";
	}

	cout << "End" << endl;

	handler->selectDB(); // ���� �� �Ϸ�� ����� ������

	delete handler;
	delete reader;

	fs.close();

	return 0;
}

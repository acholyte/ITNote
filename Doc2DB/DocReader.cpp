/*
 * ������ �б� ���� Ŭ����
*/
#include "DocReader.h"

DocReader::DocReader(fstream *fs)
:line("")
, pos(0)
{
	getline(*fs, line); // �ν��Ͻ� ���� �� ���� �� ���� �а� ����
}

DocReader::~DocReader()
{
}

// ������ �а� ��ȣ, ����, �������� �и��ϴ� �Լ�
void DocReader::parse(fstream *fs, int *num, string *subject, string *content)
{
	pos = line.find('.'); // ��ħǥ�� ��ġ Ȯ��
	*num = atoi(line.substr(0, pos).c_str()); // ��ħǥ������ �о��.->����
	pos = line.find('\t'); // �� ������ ��ġ Ȯ��
	*subject = line.substr(pos + 1, line.length()); // �� ���ں��� �� ������ �о��.->����

	getline(*fs, line);

	// char *c = const_cast<char*> (line.substr(0, 1).c_str());
	// int n = static_cast<int> (*c);

	do {
		char *c = const_cast<char*> (line.substr(0, 1).c_str());
		int n = static_cast<int> (*c); // ù ���ڸ� �о��
		if (n > 48 && n < 58) break; // ù ���ڰ� ������ ��쿡�� ����

		pos = line.find('\t'); // �� ������ ��ġ Ȯ��
		*content += line.substr(pos + 1, line.length()); // �� ���ں��� �� ������ �о��->����
		getline(*fs, line);
	} while (line.compare("") != 0); // �ٿ� �ƹ� ���ڵ� ���� ������ �ݺ�

}

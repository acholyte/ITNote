#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <algorithm>
#include <cctype>

using namespace std;

class DocReader  {
	public:
		DocReader(fstream *fs);
		~DocReader();

		void parse(fstream *fs, int *num, string *subject, string *content);

	private:
		string line; // Ŀ�� ���� ����Ű�� string ��� ����
		int pos; // Ŀ�� ��ġ�� ����Ű�� int ��� ����
};

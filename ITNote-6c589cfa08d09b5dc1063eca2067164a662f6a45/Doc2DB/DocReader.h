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
		string line; // 커서 줄을 가리키는 string 멤버 변수
		int pos; // 커서 위치를 가리키는 int 멤버 변수
};

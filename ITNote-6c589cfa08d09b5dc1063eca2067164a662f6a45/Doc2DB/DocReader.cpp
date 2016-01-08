/*
 * 문서를 읽기 위한 클래스
*/
#include "DocReader.h"

DocReader::DocReader(fstream *fs)
:line("")
, pos(0)
{
	getline(*fs, line); // 인스턴스 생성 시 문서 한 줄을 읽고 시작
}

DocReader::~DocReader()
{
}

// 문서를 읽고 번호, 제목, 내용으로 분리하는 함수
void DocReader::parse(fstream *fs, int *num, string *subject, string *content)
{
	pos = line.find('.'); // 마침표의 위치 확인
	*num = atoi(line.substr(0, pos).c_str()); // 마침표까지를 읽어옴.->숫자
	pos = line.find('\t'); // 탭 문자의 위치 확인
	*subject = line.substr(pos + 1, line.length()); // 탭 문자부터 줄 끝까지 읽어옴.->제목

	getline(*fs, line);

	// char *c = const_cast<char*> (line.substr(0, 1).c_str());
	// int n = static_cast<int> (*c);

	do {
		char *c = const_cast<char*> (line.substr(0, 1).c_str());
		int n = static_cast<int> (*c); // 첫 글자를 읽어옴
		if (n > 48 && n < 58) break; // 첫 글자가 숫자인 경우에는 나감

		pos = line.find('\t'); // 탭 문자의 위치 확인
		*content += line.substr(pos + 1, line.length()); // 태 문자부터 줄 끝까지 읽어옴->내용
		getline(*fs, line);
	} while (line.compare("") != 0); // 줄에 아무 글자도 없을 때까지 반복

}

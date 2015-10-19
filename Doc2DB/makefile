OBJS = DocReader.o MySQLHandler.o Main.o
SRCS = $(OBJS:.o = .cpp)

Doc2DB: $(OBJS)
	g++ -I./Connector/include/cppcone -o $@ $^ -L./Connector/lib -lmysqlcppconn
	echo Doc2DB: all done
	rm *.o
.cpp.o:
	g++ -c $<

clean:
	rm $(OBJS) $(TARGET)
	echo Doc2DB: objs deleted

dep: gccmakedep $(SRCS)

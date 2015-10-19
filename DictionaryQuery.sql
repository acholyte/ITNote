create table dictionary(
num int(5) primary key auto_increment,
subject varchar(100) not null, 
content varchar(3000) not null, 
regdate datetime not null default current_timestamp, 
ip varchar(16) not null,
unique (subject)
);

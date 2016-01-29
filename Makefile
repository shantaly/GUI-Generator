#
#Name: Ahmed El Shantaly
#ID: 0836885
#Date: March 13, 2015
#Assignment 3
#
CC = gcc
CFLAGS  = -Wall -ansi
INCD = /usr/lib/jvm/java-1.6.0-openjdk/include -I/usr/lib/jvm/java-1.6.0-openjdk/include/linux

all: Dialogc

yadc.o: list.c list.h lex.yy.c y.tab.h y.tab.c
	gcc list.c lex.yy.c y.tab.c -Iinclude -ll -o yadc 

lex.yy.c: myscanner.l y.tab.c
	lex myscanner.l

y.tab.c: myscanner.y
	yacc -d -y myscanner.y

y.tab.h: myscanner.y
	yacc -d -y myscanner.y

JNILIB.class: JNILIB.java
	javac JNILIB.java

Dialogc.class: Dialogc.java
	javac Dialogc.java

ParsePM.class: ParsePM.java
	javac ParsePM.java

JavaMake.class: JavaMake.java
	javac JavaMake.java

JNILIB.h: JNILIB.class
	javah JNILIB

JNILIB.o: JNILIB.h
	$(CC) $(CFLAGS) -I$(INCD) -fPIC -c JNILIB.c

ParameterManager.o: ParameterManager.c ParameterManager.h
	$(CC) $(CFLAGS) -I$(INCD) -fPIC -c ParameterManager.c

libJNIpm.so: JNILIB.o JNILIB.h ParameterManager.o
	$(CC) -shared -Wl,-soname,libJNIpm.so -I$(INCD) -o libJNIpm.so JNILIB.o ParameterManager.o

Dialogc: libJNIpm.so Dialogc.class ParsePM.class JavaMake.class yadc.o
	export LD_LIBRARY_PATH=. && java Dialogc

clean:
	rm -f *.class *.o *.so lex.yy.c y.tab.c y.tab.h yadc



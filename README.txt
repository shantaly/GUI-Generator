Author: Ahmed El Shantaly
Date: March 13, 2015

Dialogc.java   
	: This runs the Main  GUI and has all the options to run and compile with different options.

JNILIB.c
	: Connects Java with the parameter manager library

JNILIB.java
	: Calls JNILIB.c

list.c
	: Manages the lists used in yacc

list.h 
	: Function declerations for list.c

myscanner.l
	: lex definitions used to parse.

myscanner.y
	: Grammer to parse and generate all the files required and compiles all the java files generated.

ParameterManager.c
	: Parameter manager that manages the parsed variables and values

ParameterManager.h 
	: Header file for the parameter manager structs and functions

ParsePM.java        
	: Calls the parameter manager library to parse file opened in GUI

JavaMake.java 
	: Generates file based on paramerter manager parsing
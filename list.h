/*
Name: Ahmed El Shantaly
ID: 0836885
Date: March 13, 2015
Assignment 3
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
/*Struct that defines a list and holds nams and values.*/
typedef struct list
{
	char * node;
	char * value;
	struct list * next;
}theList;

/*Function Declaration*/
theList * createNode(char * node);
theList * addToBack(theList *  head, theList * toBeAdded);
char ** getListVal(theList * head);
char ** getListValT(theList * head);
int compList(theList * head, char * node);
void  addValue(theList * head, int pos, char * value);
char * makeTitle(theList * head);

/*Global Variables*/
int pass;
int pos;

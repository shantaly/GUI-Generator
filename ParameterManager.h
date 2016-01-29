/*
Name: Ahmed El Shantaly
ID: 0836885
Date: March 13, 2015
Assignment 3
*/
#include <stdio.h>
#include <stdlib.h>

typedef enum param_t 
{
	INT_TYPE, 
	REAL_TYPE, 
	BOOLEAN_TYPE, 
	STRING_TYPE, 
	LIST_TYPE
}param_t;

typedef enum Boolean 
{
	false = 0,
	true = 1 
}Boolean; 

typedef struct ParameterList {
	char* item;
	int index;
	int size;
	struct ParameterList *next;
}ParameterList;

union param_value
{
    int           int_val;
    double        real_val;
    Boolean       bool_val;   /* see additional types section below */
    char          *str_val;
    ParameterList *list_val;  /* see additional types section below */
}param_value;

typedef struct Parameter {
	char* name;
	union param_value value;
	param_t type;
	int required;
	int assigned;
	struct Parameter *next;
}Parameter;

typedef struct ParameterManager {
    int size;
    Parameter **table;
}ParameterManager;


ParameterManager * PM_create(int size);

int PM_destroy(ParameterManager *p);

int PM_parseFrom(ParameterManager *p, FILE *fp, char comment);

int hashVal(char *pname, int size);

int PM_manage(ParameterManager *p, char *pname, param_t ptype, int required);

int PM_hasValue(ParameterManager *p, char *pname);

union param_value PM_getValue(ParameterManager *p, char *pname);

char * PL_next(ParameterList *l);


/*
Name: Ahmed El Shantaly
ID: 0836885
Date: March 13, 2015
Assignment 3
*/
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include "ParameterManager.h"

/*Creates hash table with size passed and initializes placeholders
for the table. Returns a new parameter manager object*/
ParameterManager * PM_create(int size){
	ParameterManager* p;
	int i;

	if (size < 1)
	{
		return NULL;
	}
	p = malloc(sizeof(ParameterManager));
	if (p == NULL)
	{
		return NULL;
	}

	p->table = malloc(sizeof(Parameter*) * size);
	if (p->table == NULL)
	{
		return NULL;
	}

	for (i = 0; i < size; i++)
	{
		p->table[i] = malloc(sizeof(Parameter));
		p->table[i]->name = malloc(sizeof(char)*50);

		if (p->table[i]->name == NULL || p->table[i] == NULL)
		{
			return NULL;
		}
	}

	for (i = 0; i < size; i++)
	{
		strcpy(p->table[i]->name, "empty");
		p->table[i]->value.int_val = 0;
		p->table[i]->type = STRING_TYPE;
		p->table[i]->required = 0;
		p->table[i]->assigned = 0;
		p->table[i]->next = NULL;

	}
	p->size = size;

	return p;
		
}

/*Destroys a parameter manager object. All memory associated with 
parameter manager p is freed; returns 1 on success, 0 otherwise*/
int PM_destroy(ParameterManager *p){
	int i;
    Parameter *list, *temp;
    ParameterList *temp1, *list1;

    if(p == NULL){
    	return 0;
    }
			
    for(i = 0; i < p->size; i++){
        list = p->table[i];

        while(list!=NULL) {
            temp = list;
            list = list->next;
            if (temp->assigned == 1)
            {
            	if (temp->type == STRING_TYPE)
				{
					free(temp->value.str_val);
				}
				else if (temp->type == LIST_TYPE)
				{
					list1 = temp->value.list_val;
					while(list1 != NULL ){
						temp1 = list1;
						list1 = list1->next;
						free(temp1->item);
						free(temp1);
					}
				}
            }
            free(temp->name);
            free(temp);
        }
    }

    /* Free the table itself */
    free(p->table);
    free(p);
    return 1;
}

/*Parses an input stream for desired parameter names and their values. 
All parameters managed are assigned values from fp. Returns 1 on success, 
0 on Parse Error or memory allocation failure.*/

int PM_parseFrom(ParameterManager *p, FILE *fp, char comment){
	char character;
	char cc[2];
	char name[100];
	char value[200];
	char *token = NULL;
	int state = 0;
	int error = 1;
	int index, check, i;
	Boolean bType;
	param_t type;
	Parameter *temp = NULL;
	ParameterList *temp1 = NULL;

	memset(name,'\0',100);
   	memset(value,'\0',200);
  	cc[1] = '\0';

	while((character = fgetc(fp)) != EOF){
		if (state == 0){
			if (!isspace(character)){
				if(character == comment){
					while(character != '\n' && character != EOF){
						character = fgetc(fp);
					}
				}
				else{
					if (isalpha(character))
					{
						cc[0] = character;
						strcat(name, cc); 
						state = 1;
					}
					else{
						fprintf(stderr, "Error\n");
						error = 0;
					}
				}
			}
		}
		else if (state == 1){
			if (isspace(character)){
				state = 2;

			}
			else if (character == comment){
				while(character != '\n' && character != EOF){
					character = fgetc(fp);
				}
				state = 2;
			}
			else{
				if (character == '='){
					state = 3;
				}
				else{
					cc[0] = character;
					strcat(name, cc); 
				}
			}
			
		}
		else if (state == 2){
			if (!isspace(character)){
				if(character == comment && character != EOF){
					while(character != '\n'){
						character = fgetc(fp);
					}
				}
				else if (character == '='){
					state = 3;
				}else{
					fprintf(stderr, "Error\n");
					error = 0;
				}
			}
		}
		else if(state == 3){
			if (!isspace(character)){
				if (character == comment && character != EOF){
					while(character != '\n'){
						character = fgetc(fp);
					}
				}
				else if (character == '=' || character == ';')
				{
					fprintf(stderr, "Error\n");
					error = 0;
				}
				else{
					cc[0] = character;
					strcat(value, cc);
					state = 4;
				}
			}
		}
		else if (state == 4){
			if (!isspace(character)){
				if (character == comment && character != EOF){
					while(character != '\n'){
						character = fgetc(fp);
					}
				}
				else{
					if (strcmp(value, "{") == 0){
						cc[0] = character;
						strcat(value, cc);
						while(character != '}'){
							character = fgetc(fp);
							if (character == comment){
								while(character != '\n'){
									character = fgetc(fp);
								}
							}
							else if (character != '\n'){
								cc[0] = character;
								strcat(value, cc);
							}
						}
						type = LIST_TYPE;
						state = 5;
					}
					else if (value[0] ==  '\"'){
						if (isspace(value[1]))
						{
							if (value[0] == '\"'){
    							memmove(value, value+1, strlen(value));
							}
							
						}else{
							value[0] = character;
							character = fgetc(fp);
						}

						while(character != '\"'){
							cc[0] = character;
							strcat(value, cc);
							character = fgetc(fp);
						}
						type = STRING_TYPE;
						state = 5;
					}
					else if (strcmp(value, "f") == 0 || strcmp(value, "t") == 0){
						if (isalpha(character)){
							cc[0] = character;
							strcat(value, cc);
						}
						else{
							fprintf(stderr, "Errors\n");
							error = 0;
							break;
						}
						while(character != 'e'){
							character = fgetc(fp);
							if (isalpha(character)){
								cc[0] = character;
								strcat(value, cc);
							}
							else{
								fprintf(stderr, "Errors\n");
								error = 0;
								break;
							}
						}
						type = BOOLEAN_TYPE;
						state = 5;
					}
					else if (isdigit(value[0]) || strcmp(value,"-") == 0){
						type = INT_TYPE;
						while(!isspace(character) && character != ';'){
							if (isdigit(character) || character == '.'){
								if (character == '.'){
									type = REAL_TYPE;
								}
								cc[0] = character;
								strcat(value, cc);
							}
							else{
								fprintf(stderr, "Errorr\n");
								error = 0;
								break;
							}
							character = fgetc(fp);
						}
						
						state = 6;

					}
				}
				if (character == ';'){
					state = 6;
				}
			}else{
				if (value[0] == '\"')
				{
					cc[0] = character;
					strcat(value, cc);
				}
			}

		}
		else if (state == 5){
			if (!isspace(character)){
				if (character == comment && character != EOF){
					while(character != '\n'){
						character = fgetc(fp);
					}
				}
				else if (character != ';')
				{
					fprintf(stderr, "Parse Error! Expecting ;\n");
					error = 0;
				}
				else {
					state = 7;
				}
			}
		}
		
		if (state == 6){
			if (!isspace(character)){
				if (character == comment && character != EOF){
					while(character != '\n'){
						character = fgetc(fp);
					}
				}
				else if (character != ';')
				{
					fprintf(stderr, "Parse Error! Expecting ;\n");
					error = 0;
				}
				else{
					state = 7;
				}
			}
		}

		if (state == 7){

			index = hashVal(name, p->size);

			temp = p->table[index];

			while(temp != NULL){
				if (strcmp(temp->name, name) == 0)
				{
					if (type == temp->type || (type == INT_TYPE && temp->type == REAL_TYPE))
					{
						if (type == INT_TYPE)
						{
							temp->value.int_val = atoi(value);
						}
						else if (type == REAL_TYPE)
						{
							temp->value.real_val = atof(value);
						}
						else if (type == BOOLEAN_TYPE)
						{
							if (strcmp(value, "true") == 0)
							{
								bType = true;
							}
							else if (strcmp(value, "false") == 0)
							{
								bType = false;
							}
							temp->value.bool_val = bType;
						}
						else if (type == STRING_TYPE)
						{
							temp->value.str_val = malloc(sizeof(char)*50);
							strcpy(temp->value.str_val, value);
						}
						else if (type == LIST_TYPE)
						{
							memmove (value, value+1, strlen (value));
							value[strlen(value)-1] = '\0';

							if (value[0] != '\0')
							{
								temp->value.list_val = malloc(sizeof(ParameterList));
								temp1 = temp->value.list_val;
								temp1->item = malloc(sizeof(char)*50);
								temp1->next = NULL;

								token = strtok(value, "\"");
								strcpy(temp1->item, token);
								temp->value.list_val->size = 1;
								temp->value.list_val->index = 1;

								i = 0;
								while( token != NULL )
								{
									token = strtok(NULL, "\"");
									i++;
									if (token != NULL)
									{
										if (!(i % 2))
										{
											temp1->next = malloc(sizeof(ParameterList));
											temp1 = temp1->next;
											temp1->item = malloc(sizeof(char)*50);
											strcpy(temp1->item, token);
											temp->value.list_val->size++;
										}
									}
									
								}
							}
							else{
								temp->value.list_val = malloc(sizeof(ParameterList));
								temp->value.list_val->size = 0;
							}
							
						}
						temp->assigned = 1;

					}else{
						fprintf(stderr, "Parse Error! Invalid type\n");
						error = 0;
					}
				}
				temp = temp->next;
			}
   			memset(name,'\0',100);
   			memset(value,'\0',200);
			state = 0;
		}
	}

	for (i = 0; i < p->size; i++)
	{
		temp = p->table[i];

		if (strcmp(temp->name, "empty") != 0)
		{
			if (temp->required == 1)
			{
				check = PM_hasValue(p, temp->name);
				if (check == 0)
				{
					fprintf(stderr, "Parse Error! Required value not found\n");
					error = 0;
				}
			}
		}
		while(temp->next != NULL){
			temp = temp->next;
			if (strcmp(temp->name, "empty") != 0)
			{
				if (temp->required == 1)
				{
					check = PM_hasValue(p, temp->name);
					if (check == 0)
					{
						fprintf(stderr, "Parse Error! Required value not found\n");
						error = 0;
					}
				}
			}
		}
	}
	return error;
}

/*Gets hash value for a parameter name*/
int hashVal(char *pname, int size){
	int hashValue = 0;
	int i = 0;

	for (i = 0; pname[i] != '\0'; ++i)
	{
		hashValue = hashValue + (int)pname[i];
	}
	hashValue = hashValue % size;

	return hashValue;

}

/*Register parameter for management. Assigns a name, type
and whether it is required or not. Returns 1 on success,
0 otherwise (duplicate name, memory allocation failure)
*/
int PM_manage(ParameterManager *p, char *pname, param_t ptype, int required){
	int index;
	Parameter *temp;
	Parameter *new;

	index = hashVal(pname, p->size);

	if (strcmp(p->table[index]->name, "empty") == 0)
	{
		strcpy(p->table[index]->name, pname);
		p->table[index]->type = ptype;
		p->table[index]->required = required;
		p->table[index]->assigned = 0;
		p->table[index]->next = NULL;

		return 1;

	}
	else
	{
		temp = p->table[index];

		if (strcmp(temp->name, pname) == 0)
		{
			fprintf(stderr, "Error Duplicate Name\n");
			return 0;
		}

		while(temp->next != NULL){
			temp = temp->next;
			if (strcmp(temp->name, pname) == 0)
			{
				fprintf(stderr, "Error Duplicate Name\n");
				return 0;
			}
		}
		new = malloc(sizeof(Parameter));
		new->name = malloc(sizeof(char)*50);

		if (new == NULL || new->name == NULL)
		{
			fprintf(stderr, "Error Memory Allocation Failure\n");
			return 0;
		}

		strcpy(new->name, pname);
		new->type = ptype;
		new->value.int_val = 0;
		new->required = required;
		new->assigned = 0;
		new->next = NULL;

		temp->next = new;

		return 1;

	}
	
}

/*Returns 1 if parameter name passed has an assigned value, 0 otherwise (no value, unknown parameter)*/
int PM_hasValue(ParameterManager *p, char *pname){
	int index;
	Parameter *temp;

	index = hashVal(pname, p->size);

	temp = p->table[index];
	if (strcmp(temp->name, pname) == 0)
	{
		if(temp->assigned == 1){
			return 1;
		}
		else{
			return 0;
		}
	}
	else{
		while(temp->next != NULL){
			temp = temp->next;
			if (strcmp(temp->name, pname) == 0)
			{
				if(temp->assigned == 1){
					return 1;
				}
				else{
					return 0;
				}
			}
		}
	}

	return 0;
}

/*Obtain the value assigned to pname. Returns the value assigned to pname */
union param_value PM_getValue(ParameterManager *p, char *pname){
	union param_value val;

	int index;
	Parameter *temp;

	index = hashVal(pname, p->size);

	temp = p->table[index];
	if (strcmp(temp->name, pname) == 0)
	{
		val = temp->value;
	}
	else{
		while(temp->next != NULL){
			temp = temp->next;
			if (strcmp(temp->name, pname) == 0)
			{
				val = temp->value;
			}
		}
	}

	return val;
}

/*Obtain the next item in a parameter list. Returns the next item. Returns NULL if no items remain in the list*/
char * PL_next(ParameterList *l){
	char * token;
	int i;
	ParameterList *temp = l;
	int index = l->index;
	if(l->size != 0 && l->index < l->size+1){
		for (i = 1; i <index; i++)
		{
			l = l->next;
		}
		token = l->item;
		temp->index++;
		return token;
	}else{
		return NULL;
	}
}

/*
Author: Ahmed El Shantaly
Date: March 13, 2015
*/
#include "list.h"

/*Creates node. Takes in a string and makes a node and returns it*/
theList * createNode(char * node){
    theList *ptr;
    ptr = malloc(sizeof(theList));

    ptr->node = malloc(sizeof(char)*strlen(node));
    ptr->value = malloc(sizeof(char)*50);
    strncpy(ptr->node, node, strlen(node));

    ptr->next = NULL;

    return ptr;
}

/*Takes the head and a node and adds the node to the back of the list*/
theList * addToBack(theList *  head, theList * toBeAdded){
    theList *temp;
    temp = head;

    while(temp->next != NULL){
        temp = temp->next;
    }
    temp->next = toBeAdded;

    return head;
}

/*Assigns a value to to corresponding variable name*/
void addValue(theList * head, int pos, char * value){
    int i = 0;
    theList *temp;
    temp = head;
    for (i = 0; i < pos; i++)
    {
        temp = temp->next;
    }
    strcpy(temp->value, value);

}

/*returns an array of string that has all the variable names*/
char ** getListVal(theList * head){
    char **array = (char**)malloc(sizeof(char*)*50);; 
    int i = 0;
    head = head->next;
    while(head != NULL){
        array[i] = (char*)malloc(sizeof(char)*50);
        strcpy(array[i],head->node);
        head = head->next;
        i++;
    }
    return array;
}

/*returns an array of string that has all the values for the variables*/
char ** getListValT(theList * head){
    char **array = (char**)malloc(sizeof(char*)*50);; 
    int i = 0;
    head = head->next;
    while(head != NULL){
        array[i] = (char*)malloc(sizeof(char)*50);
        strcpy(array[i],head->value);
        head = head->next;
        i++;
    }
    return array;
}

/*makes a string (title) from a list*/
char * makeTitle(theList * head){
    char *title = malloc(sizeof(char)*100);
    head = head->next;
    while(head != NULL){
        strcat(title, head->node);
        strcat(title, " ");
        head = head->next;
    }
    return title;
}

/*gets index of variable in the list to know where to assign its value later*/
int compList(theList * head, char * node){
    pos = 1;
    theList *temp;
    temp = head;
    temp = temp->next;

    while(temp != NULL){
        if (strcmp(temp->node, node) == 0)
        {
            return 1;
        }
        pos++;
        temp = temp->next;
    }
    return 0;
}

%{
	/*
	Author: Ahmed El Shantaly
	Date: March 13, 2015
	*/
	#include "y.tab.h"
%}
%option nounput yylineno

%%
=					return EQUAL;
"title"				return TITLE;
"fields"			return FIELDS;
"buttons"			return BUTTONS;


[_a-zA-Z0-9]+		{yylval.id = yytext; return STRING;}
[{]					return LBRAC;
[}]					return RBRAC;
["]					return QUOT;
[,]					return COMMA;
[;]					return COLON;
#.*\n				{}
[\n]				{}
[ \t]				{}
[\0]				{}
.					{ECHO; printf("unexpected character\n");}

%%

int yywrap(void)
{
	return 1;
}


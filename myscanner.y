%{
/*
Name: Ahmed El Shantaly
ID: 0836885
Date: March 13, 2015
Assignment 3
*/
void yyerror (char *s);
#include "list.h"

theList * title = NULL;
theList * fields = NULL;
theList * buttons = NULL;
theList *temp = NULL;

int check = 1;
int fcounter = 0;
int bcounter = 0;
int fcount = 0;
int bcount = 0;
int error = 0;

%}

%union {int num; char* id;}         /* Yacc definitions */
%start STATEMENT
%token EQUAL
%token LBRAC
%token RBRAC
%token QUOT
%token COMMA
%token STRING
%token COLON
%token SPACE
%token TITLE
%token FIELDS
%token BUTTONS
%type <id> STATEMENT EXPRESSION STRINGLOOP STRING 
%type <id> LISTLOOP ITEM

%%
/*Yac grammer that defines the config file expectations to successfully parse the file.
It stores the values as it parses the files in the lists declared in main.*/
STATEMENT	: EXPRESSION	{}
			| STATEMENT EXPRESSION {}

EXPRESSION	: TITLE EQUAL QUOT STRINGLOOP QUOT COLON	{check = 2;}
			| FIELDS EQUAL LBRAC LISTLOOP RBRAC COLON	{check = 3;}
			| BUTTONS EQUAL LBRAC LISTLOOP RBRAC COLON	{check = 4;}
			| STRINGTYPE EQUAL QUOT STRINGLOOP QUOT COLON	{}
        	;

LISTLOOP	: ITEM					{}
			| ITEM COMMA			{}
			| LISTLOOP ITEM COMMA	{}
			| LISTLOOP ITEM			{}
			;

ITEM		: QUOT STRINGLOOP QUOT	{}
			;

STRINGLOOP	: STRING 				
			{
				if(check == 1){
					temp = createNode($1);
					if(temp){
						title = addToBack(title, temp);
					}
				}
				else if(check == 2){
					temp = createNode($1);
					if(temp){
						fields = addToBack(fields, temp);
					}
					fcounter++;
				}
				else if(check == 3){
					temp = createNode($1);
					if(temp){
						buttons = addToBack(buttons, temp);
					}
					bcounter++;
				}
				else if(check == 4){
					if(pass == 1){
						if (strcmp($1,"integer") == 0 || strcmp($1,"string") == 0 || strcmp($1,"float") == 0) {
							addValue(fields, pos, $1);
							fcount++;
							if (fcount == fcounter)
							{
								check = 5;
							}
						}
						else{
							error = 1;
						}
					}
					else{
						error = 1;
					}
				}
				else if(check == 5){
					if(pass == 1){
						addValue(buttons, pos, $1);
						bcount++;
						if (bcount == bcounter)
						{
							check = 6;
						}
					}
					else{
						error = 1;
					}
				}
			}
			| STRINGLOOP STRING 	
			{
				if(check == 1){
					temp = createNode($2);
					if(temp){
						title = addToBack(title, temp);
					}
				}
				else{
                    error = 1;
				}
			}
			;
STRINGTYPE	: STRING
			{
				if (check == 4)
				{
					if(compList(fields, $1)){
						pass = 1;
					}
					else{
						pass = 0;
					}
				}
				else if (check == 5)
				{
					if(compList(buttons, $1)){
						pass = 1;
					}
					else{
						pass = 0;
					}
				}
				
			}

%%

extern int yylex();
extern int yylineno;
extern char* yytext;
extern FILE *yyin;

/*Creates 3 lists to store the values from the parsed file. Calls yyparse to parse the file. Generates the files required and compiles them.*/
int main (int argc, char * argv[]) {
	FILE* fp, *out, *out1;
	int width;
	int height;
	int i = 0;
	char * guiTitle = malloc(sizeof(char)*100);
	char * fileIn = malloc(sizeof(char)*100);
	char * fileOut = malloc(sizeof(char)*100);
	char * fileOut1 = malloc(sizeof(char)*100);
    char * command = malloc(100000);
	char ** fieldsArray;
	char ** buttonsArray;
	char ** fieldsArrayT;
	char ** buttonsArrayT;

	strcat(fileIn, argv[2]);
	strcat(fileIn, ".config");

	strcat(fileOut, argv[4]);
    strcat(fileOut, "/");
    strcat(fileOut, argv[3]);
	strcat(fileOut, ".java");
	strcat(fileOut1, argv[4]);
    strcat(fileOut1, "/");
    strcat(fileOut1, argv[3]);
	strcat(fileOut1, "FieldEdit.java");

	title = createNode("0");
	fields = createNode("0");
	buttons = createNode("0");

	fp = fopen(fileIn, "r");
	yyin = fp;
	yyparse();

	guiTitle = makeTitle(title);
	fieldsArray = getListVal(fields);
	buttonsArray = getListVal(buttons);
	fieldsArrayT = getListValT(fields);
	buttonsArrayT = getListValT(buttons);

	if (error != 1)
	{
		/*Sets the Dimentions for the generated GUI to be the best size possible*/
        width = 450;
        if(bcounter < 5){
            height = 330 + (20 *fcounter + (30));
        }
        else if(bcounter%4 == 0){
            height = 330 + (20 *fcounter + (((int)(bcounter/4)) * 30));
        }
        else{
            height = 330 + (20 *fcounter + (((int)(bcounter/4)+1) * 30));
        }

        out = fopen(fileOut, "w+");


        fprintf(out, "\n");
        fprintf(out, "import java.awt.BorderLayout;\n");
        fprintf(out, "import java.awt.Dimension;\n");
        fprintf(out, "import java.awt.FlowLayout;\n");
        fprintf(out, "import java.awt.GridLayout;\n");
        fprintf(out, "import java.text.NumberFormat;\n");
        fprintf(out, "import java.util.ArrayList;\n");
        fprintf(out, "import javax.swing.*;\n");
        fprintf(out, "import java.awt.event.ActionListener;\n");
        fprintf(out, "import java.awt.event.ActionEvent;\n");
        fprintf(out, "import javax.swing.border.Border;\n");
        fprintf(out, "\n");

        fprintf(out, "/**\n");
        fprintf(out, " *\n");
        fprintf(out, " * @author Shantaly\n");
        fprintf(out, " */\n");
        fprintf(out, "public class %s extends JFrame implements %sFieldEdit{\n", argv[3], argv[3]);
        fprintf(out, "    \n");
        fprintf(out, "    private int width = %d;\n", width);
        fprintf(out, "    private int height = %d;\n", height);
        fprintf(out, "    private String title = \"%s\";\n", guiTitle);
                        for(i = 0; i < fcounter ; i++){
        fprintf(out, "    private JTextField nameField%d;\n", i);
                        }
        fprintf(out, "    private JPanel rightPanel = new JPanel();\n");
        fprintf(out, "    private JPanel leftPanel = new JPanel();\n");
        fprintf(out, "    private JPanel topPanel = new JPanel();\n");
        fprintf(out, "    private JPanel bottomPanel = new JPanel();\n");

        fprintf(out, "    private JPanel middlePanel = new JPanel();\n");
        fprintf(out, "    private Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);\n");
        fprintf(out, "    \n");
        fprintf(out, "    private JTextArea output = new JTextArea(14, 5);\n");
        fprintf(out, "    private JScrollPane scrolledText = new JScrollPane(output);\n");
        fprintf(out, "    \n");

        fprintf(out, "    public %s(){\n", argv[3]);
        fprintf(out, "        \n");
        fprintf(out, "        setTitle(title);\n");
        fprintf(out, "        setSize(width, height);\n");
        fprintf(out, "        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        fprintf(out, "        setLayout(new BorderLayout());\n");
        fprintf(out, "        setResizable(false);\n");
        fprintf(out, "        setLocationRelativeTo(null);\n");
        fprintf(out, "        rightPanel.setLayout(new GridLayout(0,1,10,10));\n");
        fprintf(out, "        leftPanel.setLayout(new GridLayout(0,1,10,10));\n");
        fprintf(out, "        topPanel.setLayout(new BorderLayout());\n");
        fprintf(out, "        middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));\n");
        fprintf(out, "        bottomPanel.setLayout(new BorderLayout());\n");
        fprintf(out, "        \n");
        fprintf(out, "        topPanel.setBorder(padding);\n");
        fprintf(out, "        \n");
                        for(i = 0; i < fcounter ; i++){
        fprintf(out, "        JLabel name%d = new JLabel(\"%s\");\n", i, fieldsArray[i]);
        fprintf(out, "        nameField%d = new JTextField();\n", i);
        fprintf(out, "        \n");
        fprintf(out, "        nameField%d.setColumns(25);\n", i);
        fprintf(out, "        leftPanel.add(name%d);\n", i);
        fprintf(out, "        rightPanel.add(nameField%d);\n", i);
        fprintf(out, "        topPanel.add(leftPanel, BorderLayout.WEST);\n");
        fprintf(out, "        topPanel.add(rightPanel, BorderLayout.EAST);\n");
        fprintf(out, "        add(topPanel, BorderLayout.NORTH);\n");
        fprintf(out, "        \n");
                        }
        fprintf(out, "        \n");
                        for(i = 0; i < bcounter ; i++){
        fprintf(out, "        JButton btn%d = new JButton(\"%s\");\n", i, buttonsArray[i]);
        fprintf(out, "        btn%d.setPreferredSize(new Dimension(100, 20));\n", i);
        fprintf(out, "        btn%d.addActionListener(new %s(this));\n", i, buttonsArrayT[i]);
        fprintf(out, "        middlePanel.add(btn%d);\n", i);
        fprintf(out, "        add(middlePanel, BorderLayout.CENTER);\n");
        fprintf(out, "        \n");
                        }
        fprintf(out, "        \n");
        fprintf(out, "        bottomPanel.add(new JLabel(\"STATUS\", SwingConstants.CENTER), BorderLayout.CENTER);\n");
        fprintf(out, "        output.setEditable(false);\n");
        fprintf(out, "        scrolledText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);\n");
        fprintf(out, "        scrolledText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);\n");
        fprintf(out, "        bottomPanel.add(scrolledText, BorderLayout.SOUTH);\n");
        fprintf(out, "        add(bottomPanel, BorderLayout.SOUTH);\n");
        fprintf(out, "    }\n");
        fprintf(out, "    \n");
        fprintf(out, "     public class IllegalFieldValueException extends Exception{\n");
        fprintf(out, "        public IllegalFieldValueException() {}\n");

        fprintf(out, "        public IllegalFieldValueException(String message){\n");
        fprintf(out, "            super(message);\n");
        fprintf(out, "        }\n");
        fprintf(out, "    }\n");
        fprintf(out, "        \n");
        
        fprintf(out, "    public void appendToStatusArea(String message){\n");
        fprintf(out, "        output.append(message);\n");
        fprintf(out, "    }\n");
                        for(i = 0; i < fcounter ; i++){
        fprintf(out, "    public String getDC%s() throws IllegalFieldValueException{\n", fieldsArray[i]);
                        if (strcmp(fieldsArrayT[i],"integer") == 0) {
        fprintf(out, "        try {\n"); 
        fprintf(out, "           Integer.parseInt(this.nameField%d.getText());\n", i);
        fprintf(out, "           return this.nameField%d.getText();\n", i);
        fprintf(out, "        } catch(NumberFormatException e) {\n");
        fprintf(out, "           throw new IllegalFieldValueException(this.nameField%d.getText());\n", i);
        fprintf(out, "        }\n");
                        }
                        else if (strcmp(fieldsArrayT[i],"string") == 0) {
        fprintf(out, "        if (this.nameField%d.getText().isEmpty())\n", i);
        fprintf(out, "        {\n");
        fprintf(out, "           throw new IllegalFieldValueException(this.nameField%d.getText());\n", i);
        fprintf(out, "        }\n");
        fprintf(out, "        else{\n");
        fprintf(out, "           return this.nameField%d.getText();\n", i);
        fprintf(out, "        }\n");
                        }
                        else if (strcmp(fieldsArrayT[i],"float") == 0) {
        fprintf(out, "        try {\n"); 
        fprintf(out, "           Double.parseDouble(this.nameField%d.getText());\n", i);
        fprintf(out, "           return this.nameField%d.getText();\n", i);
        fprintf(out, "        } catch(NumberFormatException e) {\n");
        fprintf(out, "           throw new IllegalFieldValueException(this.nameField%d.getText());\n", i);
        fprintf(out, "        }\n");
                        }
        fprintf(out, "    }\n");
                        }
                        for(i = 0; i < fcounter ; i++){
        fprintf(out, "    public void setDC%s(String text){\n", fieldsArray[i]);
        fprintf(out, "        this.nameField%d.setText(text);\n", i);
        fprintf(out, "    }\n");
                        }

        fprintf(out, "    public static void main(String[] args) {\n");
        fprintf(out, "        %s gui = new %s();\n", argv[3], argv[3]);
        fprintf(out, "        gui.setVisible(true);\n");
        fprintf(out, "    }\n");
        fprintf(out, "    \n");
        fprintf(out, "    \n");
        fprintf(out, "}\n");

        fclose(out);

        /*Makes file and generates the interface java file*/
        out1 =fopen(fileOut1, "w");

        fprintf(out1, "\n");
        fprintf(out1, "public interface %sFieldEdit {\n", argv[3]);
        fprintf(out1, "    public void appendToStatusArea(String message);\n");
                        for(i = 0; i < fcounter ; i++){
        fprintf(out1, "    public String getDC%s() throws Exception;\n", fieldsArray[i]);
                        }
                        for(i = 0; i < fcounter ; i++){
        fprintf(out1, "    public void setDC%s(String text);\n", fieldsArray[i]);
                        }
        fprintf(out1, "}\n");
        
        fclose(out1); 
    }else{
        error = 1;
    }

    if (error != 1)
    {
        strcat(command, argv[5]);
        strcat(command, " ");
        if ( argv[6] != NULL)
        {
            strcat(command, argv[6]);
        }
        strcat(command, " -d ");
        strcat(command, argv[4]);
        strcat(command, " ");
        strcat(command, argv[4]);
        strcat(command, "/");
        strcat(command, argv[3]);
        strcat(command, ".java");
        strcat(command, " ");
        strcat(command, argv[4]);
        strcat(command, "/");
        strcat(command, argv[3]);
        strcat(command, "FieldEdit");
        strcat(command, ".java");
        strcat(command, " ");
        for (i = 0; i < bcounter; i++)
        {
            strcat(command, argv[4]);
            strcat(command, "/");
            strcat(command,  buttonsArrayT[i]);
            strcat(command, ".java");
            strcat(command, " ");
        }

        system(command);
    }
    

	return error;
}

void yyerror (char *s) {error = 1;}

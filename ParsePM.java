/*
Author: Ahmed El Shantaly
Date: March 13, 2015
*/
import java.awt.TextArea;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 *
 * @author Shantaly
 */

public class ParsePM {
    
    private static final String DEFAULT_TITLE = "title";
    private static final String DEFAULT_FIELD = "fields";
    private static final String DEFAULT_BUTTON = "buttons";
    private static final char DEFAULT_COMMENT = '#';
    public enum param_t { INT_TYPE, REAL_TYPE, BOOLEAN_TYPE, STRING_TYPE,  LIST_TYPE}
    private int made = 0;
    private String title = "";
    private ArrayList <String> fields = new ArrayList <String>();
    private ArrayList <String> buttons = new ArrayList <String>();
    
    private ArrayList <String> fieldTypes = new ArrayList <String>();
    private ArrayList <String> buttonTypes = new ArrayList <String>();
    private String project = "";

    private String file = new String();

    /*Parses the config file using the IDE compiler (Parameter Manager Library) and if there is no
    rrors The JavaMake class is called to generate the required files.*/
    public ParsePM(JTextArea textEdit, String project, String workingDir){
        file = textEdit.getText();
        this.project = project;
        int val1 = firstParse();
        int val2 = 0;
        if (val1 == 1) {
            val2 = secondParse();
        }
        
        if(val1 == 1 && val2 == 1){
 
            JavaMake test = new JavaMake(title, fields, buttons, fieldTypes, buttonTypes, project, workingDir);
            made = 1;  
        }else{
            System.out.println("GUI cannot be launched due to error");
            made = 0;
        }
        
        
        
    }
    /*First parse gets the values of the title, fields, and buttons and manages them for the second parse.*/
    private int firstParse(){
        JNILIB parameterManager = new JNILIB();
        String fieldItem = "";
        String buttonItem = "";
        
        parameterManager.createPM(3);
        
        parameterManager.manage(DEFAULT_TITLE, param_t.STRING_TYPE.ordinal(), 1);
        parameterManager.manage(DEFAULT_FIELD, param_t.LIST_TYPE.ordinal(), 1);
        parameterManager.manage(DEFAULT_BUTTON, param_t.LIST_TYPE.ordinal(), 1);
            
        if(parameterManager.parseFrom(file, DEFAULT_COMMENT) == 0){
            System.out.println("error");
            return 0;
        }      

        title = parameterManager.getStrValue(DEFAULT_TITLE);        
        
        fieldItem = parameterManager.getListValue(DEFAULT_FIELD);
        while(fieldItem != null){
            fields.add(fieldItem);
            fieldItem = parameterManager.getListValue(DEFAULT_FIELD);
        }
        
        buttonItem = parameterManager.getListValue(DEFAULT_BUTTON);
        while(buttonItem != null){
            buttons.add(buttonItem);
            buttonItem = parameterManager.getListValue(DEFAULT_BUTTON);
        }
        
        
        
        return 1;
        
    }
    
    /*Second parse gets the values for the types of the fields and button listeners parsed in the first parse.*/
    private int secondParse(){
        JNILIB parameterManager = new JNILIB();
        int size;
        
        size = fields.size() + buttons.size();
        parameterManager.createPM(size);
        
        for(String s : fields){
            parameterManager.manage(s, param_t.STRING_TYPE.ordinal(), 1);
        }
        for(String s : buttons){
            parameterManager.manage(s, param_t.STRING_TYPE.ordinal(), 1);
        }
        
        if(parameterManager.parseFrom(file, DEFAULT_COMMENT) == 0){
            System.out.println("error");
            return 0;
        }
        
        for(String s : fields){
            if (parameterManager.getStrValue(s).equalsIgnoreCase("integer") || 
                parameterManager.getStrValue(s).equalsIgnoreCase("string") || 
                parameterManager.getStrValue(s).equalsIgnoreCase("float")) {
                
                fieldTypes.add(parameterManager.getStrValue(s));
            }
            else
            {
                return 0;
            }
        }
        for(String s : buttons){
            buttonTypes.add(parameterManager.getStrValue(s));
        }
        parameterManager.destroy();
        
        return 1;
    }

    /*returns back a flag depending if the files where generated or not.
    1 if the file generated successfully, 0 if the parsing fails*/
    public int getMade(){
        return this.made;
    }

    /*Returns the arraylist of the button listeners*/
    public ArrayList <String> getArrayListB(){
        return this.buttonTypes;
    }
}

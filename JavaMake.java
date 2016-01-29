/*
Author: Ahmed El Shantaly
Date: March 13, 2015
*/
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Shantaly
 */
public class JavaMake {
    private int width;
    private int height;
    private String title = new String();
    private ArrayList <String> fields = new ArrayList <String>();
    private ArrayList <String> buttons = new ArrayList <String>();
    private ArrayList <String> fieldTypes = new ArrayList <String>();
    private ArrayList <String> buttonTypes = new ArrayList <String>();
    private String project = new String();
    private String workingDir = new String();
    private PrintWriter out = null;
    
    /*Generates the Java file with the interface file based on the config file parsed*/
    public JavaMake(String title, ArrayList <String> fields, ArrayList <String> buttons, ArrayList <String> fieldTypes, ArrayList <String> buttonTypes, String project, String workingDir){
        this.title = title;
        this.fields = fields;
        this.buttons = buttons;
        this.fieldTypes = fieldTypes;
        this.buttonTypes = buttonTypes;
        this.project = project;
        this.workingDir = workingDir;
        
        /*Sets the Dimentions for the generated GUI to be the best size possible*/
        width = 450;
        if(buttons.size() < 5){
            height = 330 + (20 *fields.size() + (30));
        }
        else if(buttons.size()%4 == 0){
            height = 330 + (20 *fields.size() + (((int)(buttons.size()/4)) * 30));
        }
        else{
            height = 330 + (20 *fields.size() + (((int)(buttons.size()/4)+1) * 30));
        }

        /*Makes file and generates the java file*/
        try{
            out = new PrintWriter(new FileOutputStream(workingDir+"/" +project+".java"));
        }
        catch(FileNotFoundException e){
            System.out.println("Error making file");
        }

        out.println("");
        out.println("import java.awt.BorderLayout;");
        out.println("import java.awt.Dimension;");
        out.println("import java.awt.FlowLayout;");
        out.println("import java.awt.GridLayout;");
        out.println("import java.text.NumberFormat;");
        out.println("import java.util.ArrayList;");
        out.println("import javax.swing.*;");
        out.println("import java.awt.event.ActionListener;");
        out.println("import java.awt.event.ActionEvent;");
        out.println("import javax.swing.border.Border;");
        out.println("");

        out.println("/**");
        out.println(" *");
        out.println(" * @author Shantaly");
        out.println(" */");
        out.println("public class " +project+ " extends JFrame implements "+project+"FieldEdit{");
        out.println("    ");
        out.println("    private int width = "+width+";");
        out.println("    private int height = "+height+";");
        out.println("    private String title = \""+title+"\";");
                        for(int i = 0; i < fields.size() ; i++){
        out.println("    private JTextField nameField"+i+";");
                        }
        out.println("    private JPanel rightPanel = new JPanel();");
        out.println("    private JPanel leftPanel = new JPanel();");
        out.println("    private JPanel topPanel = new JPanel();");
        out.println("    private JPanel bottomPanel = new JPanel();");

        out.println("    private JPanel middlePanel = new JPanel();");
        out.println("    private Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);");
        out.println("    ");
        out.println("    private JTextArea output = new JTextArea(14, 5);");
        out.println("    private JScrollPane scrolledText = new JScrollPane(output);");
        out.println("    ");

        out.println("    public " +project+ "(){");
        out.println("        ");
        out.println("        setTitle(title);");
        out.println("        setSize(width, height);");
        out.println("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        ");
        out.println("        setLayout(new BorderLayout());");
        out.println("        setResizable(false);");
        out.println("        setLocationRelativeTo(null);");
        out.println("        rightPanel.setLayout(new GridLayout(0,1,10,10));");
        out.println("        leftPanel.setLayout(new GridLayout(0,1,10,10));");
        out.println("        topPanel.setLayout(new BorderLayout());");
        out.println("        middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));");
        out.println("        bottomPanel.setLayout(new BorderLayout());");
        out.println("        ");
        out.println("        topPanel.setBorder(padding);");
        out.println("        ");
                        for(int i = 0; i < fields.size() ; i++){
        out.println("        JLabel name"+i+" = new JLabel(\""+fields.get(i)+"\");");
        out.println("        nameField"+i+" = new JTextField();");
        out.println("        ");
        out.println("        nameField"+i+".setColumns(25);");
        out.println("        leftPanel.add(name"+i+");");
        out.println("        rightPanel.add(nameField"+i+");");
        out.println("        topPanel.add(leftPanel, BorderLayout.WEST);");
        out.println("        topPanel.add(rightPanel, BorderLayout.EAST);");
        out.println("        add(topPanel, BorderLayout.NORTH);");
        out.println("        ");

                        }
        out.println("        ");
                        for(int i = 0; i < buttons.size() ; i++){
        out.println("        JButton btn"+i+" = new JButton(\""+buttons.get(i)+"\");");
        out.println("        btn"+i+".setPreferredSize(new Dimension(100, 20));");
        out.println("        btn"+i+".addActionListener(new "+buttonTypes.get(i)+"(this));");
        out.println("        middlePanel.add(btn"+i+");");
        out.println("        add(middlePanel, BorderLayout.CENTER);");
        out.println("        ");
                        }
        out.println("        ");
        out.println("        bottomPanel.add(new JLabel(\"STATUS\", SwingConstants.CENTER), BorderLayout.CENTER);");
        out.println("        output.setEditable(false);");
        out.println("        scrolledText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);");
        out.println("        scrolledText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);");
        out.println("        bottomPanel.add(scrolledText, BorderLayout.SOUTH);");
        out.println("        add(bottomPanel, BorderLayout.SOUTH);");
        out.println("    }");
        out.println("    ");
        
        out.println("     public class IllegalFieldValueException extends Exception{");
        out.println("        public IllegalFieldValueException() {}");

        out.println("        public IllegalFieldValueException(String message){");
        out.println("            super(message);");
        out.println("        }");
        out.println("    }");

        out.println("    public void appendToStatusArea(String message){");
        out.println("        output.append(message);");
        out.println("    }");
                        for(int i = 0; i < fields.size() ; i++){

        out.println("    public String getDC"+fields.get(i)+"() throws IllegalFieldValueException{");
                        if (fieldTypes.get(i).equalsIgnoreCase("integer")) {
        out.println("        try {"); 
        out.println("           Integer.parseInt(this.nameField"+i+".getText());");
        out.println("           return this.nameField"+i+".getText();");
        out.println("        } catch(NumberFormatException e) {");
        out.println("           throw new IllegalFieldValueException(this.nameField"+i+".getText());");
        out.println("        }");
                        }
                        else if (fieldTypes.get(i).equalsIgnoreCase("string")) {
        out.println("        if (this.nameField"+i+".getText().isEmpty())");
        out.println("        {");
        out.println("           throw new IllegalFieldValueException(this.nameField"+i+".getText());");
        out.println("        }");
        out.println("        else{");
        out.println("           return this.nameField"+i+".getText();");
        out.println("        }");
                        }
                        else if (fieldTypes.get(i).equalsIgnoreCase("float")) {
        out.println("        try {"); 
        out.println("           Double.parseDouble(this.nameField"+i+".getText());");
        out.println("           return this.nameField"+i+".getText();");
        out.println("        } catch(NumberFormatException e) {");
        out.println("           throw new IllegalFieldValueException(this.nameField"+i+".getText());");
        out.println("        }");
                        }
        out.println("    }");
                        }
                        for(int i = 0; i < fields.size() ; i++){
        out.println("    public void setDC"+fields.get(i)+"(String text){");
        out.println("        this.nameField"+i+".setText(text);");
        out.println("    }");
                        }
        

        out.println("    public static void main(String[] args) {");
        out.println("        "+project+" gui = new "+project+"();");
        out.println("        gui.setVisible(true);");
        out.println("    }");
        out.println("    ");
        out.println("    ");
        out.println("}");

        out.close();
        /*Makes file and generates the interface java file*/
        try{
            out = new PrintWriter(new FileOutputStream(workingDir+"/" +project+"FieldEdit.java"));
        }
        catch(FileNotFoundException e){
            System.out.println("Error making file");
        }
        
        out.println("");
        out.println("public interface "+project+"FieldEdit {");
        out.println("    public void appendToStatusArea(String message);");
                        for(int i = 0; i < fields.size() ; i++){
        out.println("    public String getDC"+fields.get(i)+"() throws Exception;");
                        }
                        for(int i = 0; i < fields.size() ; i++){
        out.println("    public void setDC"+fields.get(i)+"(String text);");
                        }
        out.println("}");
        
        out.close();
    }    
    
}

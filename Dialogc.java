/*
Author: Ahmed El Shantaly
Date: March 13, 2015
*/
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.StringTokenizer; 
import java.util.Scanner;
/**
 *
 * @author Shantaly
 */
public class Dialogc extends JFrame implements ActionListener, DocumentListener{

    /**
     * @param args the command line arguments
     */
    public static final int LINES = 40;
    public static final int CHAR_PER_LINE = 30;
    
    public static final int WIDTH = 600;
    public static final int HEIGHT = 740;
    
    private JPanel northPanel = new JPanel();
    /*Menu Bar*/
    private JMenuBar menuBar = new JMenuBar( );
    /*File*/
    private JMenu mfile = new JMenu("File");
    private JMenuItem fNew = new JMenuItem("New");
    private JMenuItem fOpen = new JMenuItem("Open");
    private JMenuItem fSave = new JMenuItem("Save");
    private JMenuItem fSaveAs = new JMenuItem("Save As");
    private JMenuItem fQuit = new JMenuItem("Quit");
    /*Compile*/
    private JMenu mCompile = new JMenu("Compile");
    private JMenuItem cCompile = new JMenuItem("Compile");
    private JMenuItem cCompileRun = new JMenuItem("Compile and Run");
    /*Config*/
    private JMenu mConfig = new JMenu("Config");
    private JMenuItem gJavaCompiler = new JMenuItem("Java Compiler");
    private JMenuItem gCompileOptions = new JMenuItem("Compile options");
    private JMenuItem gJavaRT = new JMenuItem("Java Run-Time");
    private JMenuItem gRTOptions = new JMenuItem("Run-Time options");
    private JMenuItem gDirectory = new JMenuItem("Working Directory");
    private JMenu gCompileMode = new JMenu("Compile Mode");
    private ButtonGroup optionsCompiler = new ButtonGroup( );
    private JRadioButton ideCompiler = new JRadioButton("IDE Compiler");
    private JRadioButton lexyacc = new JRadioButton("Lex&Yacc Compiler");
    private int chosen = 1;

    /*Help*/
    private JMenu mHelp = new JMenu("Help");
    private JMenuItem hHelp = new JMenuItem("Help");
    private JMenuItem hAbout = new JMenuItem("About");
    
    private JToolBar toolBar = new JToolBar("Still draggable");
    
    private JPanel southPanel = new JPanel();
    
    private String fileName = new String(" ");
    private String filePath = new String();

    private TitledBorder fileTitle = new TitledBorder(BorderFactory.createTitledBorder(fileName));
    private JTextArea textEdit =  new JTextArea(LINES, CHAR_PER_LINE);
    private JScrollPane scrolledText = new JScrollPane(textEdit);
    
    private JPanel editor = new JPanel();
    
    private Border loweredbevel = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    private JPanel status = new JPanel();
    private JLabel fileOpened = new JLabel(fileName);
    private JLabel statusAt = new JLabel();

    
    private JDialog checkMod = new JDialog();
    private Boolean modified = false;
    private Boolean fileOpen = false;
    private Object[] options = {"Cancel", "Discard", "Save"};
    private int done;
    
    private String compilerPath = "/usr/bin/javac";
    private String compilerOptions = "";
    private String runTimePath = "/usr/bin/java";
    private String runTimeOptions = "";
    private String workingDir = ".";

    private int finished = 0;

    
    public static void main(String[] args) {
        Dialogc gui = new Dialogc();
        gui.setVisible(true);
    }

    /*Creates Dialogc GUI basic Jframe with JPanels and sets the basic layout.*/
    public Dialogc(){
        super("Dialogc");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(exitListener);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        northPanel.setLayout(new BorderLayout());
        southPanel.setLayout(new BorderLayout());

        addMenuItems();
        northPanel.add(menuBar, BorderLayout.NORTH);
      
        addButtons();  
        northPanel.add(toolBar, BorderLayout.CENTER);
        
        add(northPanel, BorderLayout.NORTH);
        
        editor.setBorder(fileTitle);
        editor.setLayout(new BorderLayout());
        textEdit.setBackground(Color.WHITE);
        textEdit.getDocument().addDocumentListener(this);

        scrolledText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolledText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        editor.add(scrolledText,BorderLayout.NORTH );
        
        southPanel.add(editor, BorderLayout.NORTH);
        
        status.setBorder(loweredbevel);
        status.add(new JLabel("Current Project:"));
        status.add(fileOpened);
        status.add(statusAt);
        southPanel.add(status, BorderLayout.CENTER);
        add(southPanel, BorderLayout.CENTER);
        
        
    }
    /*Adds Menu Items to GUI*/
    protected void addMenuItems() {
        mfile.add(fNew);
        mfile.add(fOpen);
        mfile.add(fSave);
        mfile.add(fSaveAs);
        mfile.add(fQuit);
        fNew.addActionListener(this);
        fOpen.addActionListener(this);
        fSave.addActionListener(this);
        fSaveAs.addActionListener(this);
        fQuit.addActionListener(this);
        fNew.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F1, 
        java.awt.Event.SHIFT_MASK ));
        fOpen.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F2, 
        java.awt.Event.SHIFT_MASK ));
        fSave.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F3, 
        java.awt.Event.SHIFT_MASK ));
        fSaveAs.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F4, 
        java.awt.Event.SHIFT_MASK ));
        fQuit.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F5, 
        java.awt.Event.SHIFT_MASK ));
        menuBar.add(mfile);
        
        mCompile.add(cCompile);
        mCompile.add(cCompileRun);
        cCompile.addActionListener(this);
        cCompileRun.addActionListener(this);
        cCompile.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F6, 
        java.awt.Event.SHIFT_MASK ));
        cCompileRun.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F7, 
        java.awt.Event.SHIFT_MASK ));
        menuBar.add(mCompile);
        
        mConfig.add(gJavaCompiler);
        mConfig.add(gCompileOptions);
        mConfig.add(gJavaRT);
        mConfig.add(gRTOptions);
        mConfig.add(gDirectory);
        gJavaCompiler.addActionListener(this);
        gCompileOptions.addActionListener(this);
        gJavaRT.addActionListener(this);
        gRTOptions.addActionListener(this);
        gDirectory.addActionListener(this);
        gJavaCompiler.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F8, 
        java.awt.Event.SHIFT_MASK ));
        gCompileOptions.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F9, 
        java.awt.Event.SHIFT_MASK ));
        gJavaRT.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F10, 
        java.awt.Event.SHIFT_MASK ));
        gRTOptions.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F12, 
        java.awt.Event.SHIFT_MASK ));
        gDirectory.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_1, 
        java.awt.Event.SHIFT_MASK ));
        menuBar.add(mConfig);

        ideCompiler.setActionCommand("IDE Compiler");
        lexyacc.setSelected(true);
        lexyacc.setActionCommand("lexyacc");

        optionsCompiler.add(ideCompiler);
        optionsCompiler.add(lexyacc);
        ideCompiler.addActionListener(this);
        lexyacc.addActionListener(this);
        gCompileMode.add(ideCompiler);
        gCompileMode.add(lexyacc);
        mConfig.add(gCompileMode);


        mHelp.add(hHelp);
        mHelp.add(hAbout);
        hHelp.addActionListener(this);
        hAbout.addActionListener(this);
        gDirectory.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_2, 
        java.awt.Event.SHIFT_MASK ));
        gDirectory.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_3, 
        java.awt.Event.SHIFT_MASK ));
        menuBar.add(mHelp);
    }
    
    /*Adds Buttons to GUI*/
    protected void addButtons() {
        JButton button = null;

        button = makeButton("New24", "New", "New", "New");
        toolBar.add(button);

        button = makeButton("Open24", "Open", "Open", "Open");
        toolBar.add(button);
        
        button = makeButton("Save24", "Save", "Save", "Save");
        toolBar.add(button);
        
        button = makeButton("SaveAs24", "Save As", "Save As", "Save As");
        toolBar.add(button);
        
        button = makeButton("Play24", "Compile and Run", "Compile and Run", "Compile");
        toolBar.add(button);
     
    }
    
    /*Makes Buttons using .gif files and adds actioncommand and text that replaces the 
    picture in case the .gif files are not reachable*/
    protected JButton makeButton(String imageName, String actionCommand, String toolTipText, String altText) {
        String imgLocation = "icons/" + imageName + ".gif";
        URL imageURL = Dialogc.class.getResource(imgLocation);

        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        if (imageURL != null) {                      
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }
    
    /*Document events based on the JTextArea that has the .config file opened in.
    Checks and sets modified flag to true*/
    public void insertUpdate(DocumentEvent e)
    {
        modified = true;
        statusAt.setText("[Modified]");
        status.repaint();
    }
    public void removeUpdate(DocumentEvent e)
    {
        modified = true;
        statusAt.setText("[Modified]");
        status.repaint();
    }
    public void changedUpdate(DocumentEvent e)
    {
        modified = true;
        statusAt.setText("[Modified]");
        status.repaint();
    }
    
    /*Listeners that listen for all the buttons and menu buttons on the GUI and handles 
    everyone of them*/
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("New")) {
            if(fileOpen == false){
                System.out.println("file not open");
                fileNamePrompt();
            }else{
                System.out.println("file open");
                if(modified == true){
                    done = checkModified();
                    System.out.println(done);

                    if(done == 1 || done == 2){
						if(fileNamePrompt() == 1){	
							textEdit.setText(null);
							modified = false;
							fileOpen = false;
							fileTitle.setTitle(" ");
							editor.repaint();
							filePath = " ";
							fileName = " ";
							statusAt.setText("[Saved]");
							status.repaint();
						}
                    }
                }else{
					if(fileNamePrompt() == 1){
						textEdit.setText(null);
						modified = false;
						fileOpen = false;
						fileTitle.setTitle(" ");
						editor.repaint();
						filePath = " ";
						fileName = " ";
						statusAt.setText("[Saved]");
						status.repaint();
					}
                }
            }
        }
        else if (action.equals("Open")){
            if(modified == true){
                done = checkModified();
                if(done == 1 || done == 2){
                    try {
                        showOpenDialog();
                    } catch (IOException ex) {
                        unexpectedMessage();
                    }
                }
                
            }else{
               try {
                    showOpenDialog();
                } catch (IOException ex) {
                    unexpectedMessage();
                } 
            }
            
        }
        else if (action.equals("Save")){
            if(!filePath.isEmpty()){
                saveBtn();
            }else{
                try {
                    showSaveDialog();
                } catch (IOException ex) {
                    unexpectedMessage();
                }
            }
        }
        else if(action.equals("Save As")){
            try {
                showSaveDialog();
            } catch (IOException ex) {
                unexpectedMessage();
            }
        }
        else if(action.equals("Quit")){
            if(modified == true){
                done = checkModified();
                if(done == 1 || done == 2){
                    checkQuit();
                }
            }
            else{
                checkQuit();
            }
            
        }
        else if(action.equals("Compile")){
            if(!fileName.isEmpty() && !filePath.isEmpty()){
                if(modified == true){
                    done = checkModified();
                }
                else{
                    done = 1;
                }
                if(done == 1 || done == 2){ 
                    if (workingDir.equals(".")) {
                        workingDir = System.getProperty("user.dir");
                    }
                    if (chosen == 0) {
                        ParsePM compile = new ParsePM(textEdit,fileName.substring(0, fileName.indexOf('.')), workingDir);
                        if (compile.getMade() == 1) {
                            String command = new String(compilerPath + " " + compilerOptions + " -d " + workingDir+" " + workingDir +"/" +fileName.substring(0, fileName.indexOf('.')) + ".java"
                                +" " + workingDir +"/" +fileName.substring(0, fileName.indexOf('.')) +"FieldEdit" + ".java");
                            ArrayList <String> listeners = new ArrayList <String>();
                            listeners = compile.getArrayListB();
                            for (String s : listeners) {
                                command = command + " " + workingDir + "/" + s +".java" ;
                            }
                            System.out.println(command);
                            try {
                                Process p = Runtime.getRuntime().exec(command);
                                p.waitFor();
                                BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                String line = null;
                                while ((line = in.readLine()) != null) {
                                    System.out.println(line);
                                }

                                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                line = null;
                                while ((line = in.readLine()) != null) {
                                    System.out.println(line);
                                }
                                JOptionPane.showMessageDialog(Dialogc.this, "Compiled");
                                finished = 1;
                            } catch (IOException ei) {
                                unexpectedMessage();
                            } catch (InterruptedException ex) {
                                unexpectedMessage();
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(Dialogc.this, "Parse Error!");
                        }
                    }
                    else if (chosen == 1){
                        try {
                            String yaccCommand =  new String("./yadc " + "-d " +filePath.substring(0,filePath.lastIndexOf(File.separator))+"/"+fileName.substring(0, fileName.indexOf('.'))
                             + " " + fileName.substring(0, fileName.indexOf('.')) + " " + workingDir + " " + compilerPath + " " + compilerOptions);
                            System.out.println(yaccCommand);
                            Process p0 = Runtime.getRuntime().exec(yaccCommand);
                            p0.waitFor();

                            BufferedReader in = new BufferedReader(new InputStreamReader(p0.getErrorStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) {
                                System.out.println(line);
                            }

                            in = new BufferedReader(new InputStreamReader(p0.getInputStream()));
                            line = null;
                            while ((line = in.readLine()) != null) {
                                System.out.println(line);
                            }

                            if(p0.exitValue() == 1){
                                JOptionPane.showMessageDialog(Dialogc.this, "Parse Error!");
                            }else{
                                JOptionPane.showMessageDialog(Dialogc.this, "Compiled");
                            }
                        } catch (IOException ei) {
                            unexpectedMessage();
                        } catch (InterruptedException ex) {
                            unexpectedMessage();
                        }
                    }
                    
                }
            }else{
                JOptionPane.showMessageDialog(Dialogc.this, "Error no file open");
            }
            
        }
        else if(action.equals("Compile and Run")){
            if(!fileName.isEmpty()&& !filePath.isEmpty()){
                if(modified == true){
                    done = checkModified();
                }
                else{
                    done = 1;
                }
                if(done == 1 || done == 2){ 
                    if (workingDir.equals(".")) {
                        workingDir = System.getProperty("user.dir");
                    }
                    if (chosen == 0) {
                        ParsePM compile = new ParsePM(textEdit,fileName.substring(0, fileName.indexOf('.')), workingDir);
                        if (compile.getMade() == 1) {
                            String command = new String(compilerPath + " " + compilerOptions + " -d " + workingDir+" " + workingDir +"/" +fileName.substring(0, fileName.indexOf('.')) + ".java"
                                +" " + workingDir +"/" +fileName.substring(0, fileName.indexOf('.')) +"FieldEdit" + ".java");
                            ArrayList <String> listeners = new ArrayList <String>();
                            listeners = compile.getArrayListB();
                            for (String s : listeners) {
                                command = command + " " + workingDir + "/" + s +".java" ;
                            }
                            System.out.println(command);
                            try {
                                Process p = Runtime.getRuntime().exec(command);
                                p.waitFor();
                                BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                String line = null;
                                while ((line = in.readLine()) != null) {
                                    System.out.println(line);
                                }

                                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                line = null;
                                while ((line = in.readLine()) != null) {
                                    System.out.println(line);
                                }
                                JOptionPane.showMessageDialog(Dialogc.this, "Compiled");
                                finished = 1;
                            } catch (IOException ei) {
                                unexpectedMessage();
                            } catch (InterruptedException ex) {
                                unexpectedMessage();
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(Dialogc.this, "Parse Error!");
                        }
                    }
                    else if (chosen == 1){
                        try {
                            String yaccCommand =  new String("./yadc " + "-d " +filePath.substring(0,filePath.lastIndexOf(File.separator))+"/"+fileName.substring(0, fileName.indexOf('.'))
                             + " " + fileName.substring(0, fileName.indexOf('.')) + " " + workingDir + " " + compilerPath + " " + compilerOptions);
                            System.out.println(yaccCommand);
                            Process p0 = Runtime.getRuntime().exec(yaccCommand);
                            p0.waitFor();

                            BufferedReader in = new BufferedReader(new InputStreamReader(p0.getErrorStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) {
                                System.out.println(line);
                            }

                            in = new BufferedReader(new InputStreamReader(p0.getInputStream()));
                            line = null;
                            while ((line = in.readLine()) != null) {
                                System.out.println(line);
                            }

                            if(p0.exitValue() == 1){
                                JOptionPane.showMessageDialog(Dialogc.this, "Parse Error!");
                            }else{
                                JOptionPane.showMessageDialog(Dialogc.this, "Compiled");
                                finished = 1;
                            }
                        } catch (IOException ei) {
                            unexpectedMessage();
                        } catch (InterruptedException ex) {
                            unexpectedMessage();
                        }
                    }
                    if (finished == 1) {
                        if (workingDir.equals(".")) {
                            workingDir = System.getProperty("user.dir");
                        }
                        String command1 = new String(runTimePath + " " + "-cp " + runTimeOptions + workingDir+":"+ workingDir+ " " + fileName.substring(0, fileName.indexOf('.')));
                        System.out.println(command1);
                        try {
                            Process p1 = Runtime.getRuntime().exec(command1);
                            p1.waitFor();
                            BufferedReader in = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) {
                                System.out.println(line);
                            }

                            in = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                            line = null;
                            while ((line = in.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException ei) {
                            unexpectedMessage();
                        } catch (InterruptedException ex) {
                            unexpectedMessage();
                        }
                        finished = 0;
                    }
                    
                } 
            }else{
                JOptionPane.showMessageDialog(Dialogc.this, "Error no file open");
            }
            
        }
        else if(action.equals("Java Compiler")){
            compilerPath = JOptionPane.showInputDialog(Dialogc.this, "Specify the external Java Compiler", compilerPath);
        }
        else if(action.equals("Compile options")){
            compilerOptions = JOptionPane.showInputDialog(Dialogc.this, "Specify the Compiler Options", compilerOptions);
        }
        else if(action.equals("Java Run-Time")){
            runTimePath = JOptionPane.showInputDialog(Dialogc.this, "Specify the external Java Run-time command", runTimePath);
        }
        else if(action.equals("Run-Time options")){
            runTimeOptions = JOptionPane.showInputDialog(Dialogc.this, "Specify the Compiler Options", runTimeOptions);
        }
        else if(action.equals("Working Directory")){
			workingDir = JOptionPane.showInputDialog(Dialogc.this, "Specify the Working Directory", workingDir);
		}
		else if(action.equals("IDE Compiler")){
            chosen = 0;
        }
        else if(action.equals("lexyacc")){
            chosen = 1;
        }
        else if(action.equals("Help")){
			try{
				String help = new Scanner(new File("README.txt")).useDelimiter("\\Z").next();
				JOptionPane.showMessageDialog(Dialogc.this, help,"README!", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception ew){
				unexpectedMessage();
			}
			
		}
		else if(action.equals("About")){
			JOptionPane.showMessageDialog(Dialogc.this, "Ahmed El Shantaly\n0836885\nMarch 13, 2015\nAssignment 3" ,"Author!", JOptionPane.INFORMATION_MESSAGE);
		}
    }
    
    /*Opens an Open Dialog for the user to choose a file (.config) to open in the GUI as the project*/
    private void showOpenDialog() throws FileNotFoundException, IOException {
            JFileChooser fileChooser = new JFileChooser(filePath);
            fileChooser.setDialogTitle("Open");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Config files (.config)", "config");
            fileChooser.setFileFilter(filter);
            
            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try
                {
                    textEdit.setText(null);
                    File file = fileChooser.getSelectedFile();
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();

                    fileName = fileChooser.getName(file);
                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String line = in.readLine();
                    while(line != null){
                      textEdit.append(line + "\n");
                      line = in.readLine();
                    }
                    fileTitle.setTitle(fileName);
                    editor.repaint();
                    modified = false;
                    fileOpen = true;
                    fileOpened.setText(fileName.substring(0, fileName.indexOf('.')));
                    statusAt.setText("[Saved]");
                    status.repaint();
                }
                catch (IOException e)
                {
                JOptionPane.showMessageDialog(this, "The file could not be opened!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                System.out.println("Open command cancelled by user.");
            }
    }
    
    /*Opens a save as dialog for the user to save the project in the Dialog to a new file*/
    private void showSaveDialog() throws FileNotFoundException, IOException {
            JFileChooser fileChooser = new JFileChooser(filePath);
            fileChooser.setSelectedFile(new File(fileName));
            fileChooser.setDialogTitle("Save As");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Config files (.config)", "config");
            fileChooser.setFileFilter(filter);
            
            int returnVal = fileChooser.showSaveDialog(this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    String fileExt = ".config";
                    File file = new File(addFileExtIfNecessary(fileChooser.getSelectedFile().toString(),fileExt));
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    fileName = fileChooser.getName(file);
                    BufferedWriter writer = new BufferedWriter( new FileWriter( file ));
                    textEdit.write( writer);
                    writer.close( );
                    JOptionPane.showMessageDialog(this, "The Message was Saved Successfully!",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                    fileTitle.setTitle(fileName);
                    editor.repaint();
                    modified = false;
                    fileOpen = true;
                    fileOpened.setText(fileName);
                    statusAt.setText("[Saved]");
                    status.repaint();
                }
                catch (IOException e)
                {
                    JOptionPane.showMessageDialog(this, "The file could not be Saved!", "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }else{
                System.out.println("Save As command cancelled by user.");
            }
          
    }
    
    /*Saves the current file (updates)*/
    private void saveBtn(){
        File file = null;
        FileWriter out=null;
        
        try {
            file = new File(filePath);
            out = new FileWriter(file);     
            out.write(textEdit.getText());
            out.close();
            modified = false;
            statusAt.setText("[Saved]");
            status.repaint();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "The file could not be Saved!","Error!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The file could not be Saved!","Error!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*Asks the user if they want to save the changes made to a file*/
    private int checkModified(){
        int choice;
        choice = JOptionPane.showOptionDialog(Dialogc.this, "Do you want to save your changes?", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if(choice == 2){
            saveBtn();
        }
        
        return choice;

    }

    /*Asks the user for a name for a new file*/
    private int fileNamePrompt(){
        String name = JOptionPane.showInputDialog(Dialogc.this, "Enter new file name");
        if(name != null){
			name = addFileExtIfNecessary(name, ".config");
			fileName = name;
			fileTitle.setTitle(fileName);
			filePath = "";
			fileOpened.setText(fileName);
			statusAt.setText("[Not Saved]");
			status.repaint();
			editor.repaint();
			System.out.println("The name is - "+fileName);
			return 1;
		}else{
			return 0;
		}
        
    }
    
    /*Exit listner that listens for the the window to close and checks if the user whats to quit*/
    private WindowListener exitListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            if(modified == true){
                done = checkModified();
                if(done == 1 || done == 2){
                    checkQuit();
                }
            }
            
            else{
                checkQuit();
            }
        }
    };

    /*Error message dialog*/
    private void unexpectedMessage(){
        JOptionPane.showMessageDialog(Dialogc.this, "Error something unexpected happened","Error!", JOptionPane.INFORMATION_MESSAGE);
        finished = 0;
    }
    
    /*Quit Checker makes sure the user wants to quit*/
    private void checkQuit(){
        int confirm;
        confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == 0) {
            System.exit(0);
         }
    }
    
    /*Adds .config extention if it doesnt when necessary*/
    private String addFileExtIfNecessary(String file,String ext) {
        if(file.lastIndexOf('.') == -1){
            file += ext;
        }
            
        return file;
    }
}

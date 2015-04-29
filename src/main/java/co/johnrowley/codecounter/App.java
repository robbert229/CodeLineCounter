package co.johnrowley.codecounter;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;
import javax.swing.table.*;

/* 
 * This program is written by John Rowley.
 * As far as the extra credit, this does indeed fullfill the requirements of 
 * subdirectories.
 */

public class App extends JFrame
{
    //Gui related variables
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;

    private JMenuItem saveMenuItem;
    private JMenuItem quitMenuItem;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel resultPanel;
    private JScrollPane resultScrollPane;

    private JButton quitButton;
    private JButton startButton;
    private JButton selectDirectoryButton;
    private JComboBox extensionBox;

    private JTable resultTable;
    private DefaultTableModel resultTableModel;
    private JLabel fileCountLabel;
    private JLabel totalLinesLabel;

    //Logic related Variables
    private ArrayList<CodeFile> codeFiles;
    private String directory;

    public App(){
        initUI();
        codeFiles = new ArrayList<CodeFile>();
    }

    private void saveToFile(File f){
        try {
            PrintStream printer = new PrintStream(f);
            int totalLines = 0;
            for(CodeFile cf : codeFiles){
                printer.println(cf);
                totalLines += cf.lines();
            }
            printer.println("");
            printer.println("Total Lines: " + totalLines);
            printer.println("File Count: " + codeFiles.size());
            printer.close();
        } catch (FileNotFoundException fnfe){
            System.out.println(fnfe);
        } catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    private void initUI(){
        setTitle("Code Line Counter");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JFrame p = this;

        fileCountLabel = new JLabel("File Count: ");
        totalLinesLabel = new JLabel("Total Lines: ");

        quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                System.exit(0);
            }
        });
        
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                if(directory != null){
                    String extension = (String)extensionBox.getSelectedItem();
                    
                    Counter c = new Counter(extension);
                    FileWalker fw = new FileWalker(c);
                    fw.walk(directory);

                    System.out.println(directory);
                    codeFiles.clear();
                    codeFiles.addAll(c.getCodeFiles());
                    
                    fileCountLabel.setText("File Count: " + codeFiles.size());
                    int totalLines = 0;
                    
                    while(resultTableModel.getRowCount() > 0){
                        resultTableModel.removeRow(0);
                    }

                    for(CodeFile cf : codeFiles){
                        resultTableModel.addRow(new Object[]{cf.filename(), cf.lines()});
                        totalLines += cf.lines();
                    }
                    totalLinesLabel.setText("Total Lines: " + totalLines);
                } else {
                    new TextDialog(p,"Error","No directory has been selected yet.");
                }
            }
        });

        selectDirectoryButton = new JButton("Select Directory");
        selectDirectoryButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                JFileChooser selectDirectoryDialog = new JFileChooser();
                selectDirectoryDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int result = selectDirectoryDialog.showOpenDialog(selectDirectoryButton);
                if(result == JFileChooser.APPROVE_OPTION){
                    directory = selectDirectoryDialog.getSelectedFile().getPath();
                }
            }
        });
        
        String[] extensions = {"java", "cs", "c"};
        extensionBox = new JComboBox(extensions);
        extensionBox.setSelectedIndex(0);

        String[] cols = {"Path", "Line Count"};
        resultTableModel = new DefaultTableModel(cols,0);
        resultTable = new JTable(resultTableModel);
        resultTable.setFillsViewportHeight(true);
        
        /*
         * Building the menu. There exists a main menu bar with two menus on
         * it, one named file the other named help. File allows the user to
         * save the results in JSON, and to exit the program. Help allows
         * users to open the about, and help modals.
         */
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        this.setJMenuBar(menuBar);

        saveMenuItem = new JMenuItem("Save As");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                System.out.println("Save!");
                JFileChooser saveFileDialog = new JFileChooser();
                
                int result = saveFileDialog.showSaveDialog(saveMenuItem);
                if(result == JFileChooser.APPROVE_OPTION){
                    saveToFile(saveFileDialog.getSelectedFile());
                }
            }
        });
        quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                System.out.println("Quit!");
                System.exit(0);
            }
        });

        fileMenu.add(saveMenuItem);
        fileMenu.add(quitMenuItem);

        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                new TextDialog(p, "Help",
                    "<html>" +
                    "<body style='width: 500px'>" +
                    "<h1>Help</h1>" + 
                    "To count the lines of code in a directory first set the" +
                    "drop down box to the extension that you desire. Then " +
                    "click the " +
                    "select directory button, then click the start button. " +
                    "The table will then be populated with all the " +
                    "files in the directory, and subdirecties along with the " +
                    "number of lines that each file contains. The program " + 
                    "counts lines of code by stripping away all comments, " + 
                    "and newlines. It then counts the remaining lines, so " +
                    "any lines with remaining content(not empty) counts as " + 
                    "a line of code. " +  
                    "</body>" +
                    "</html>"
                );
            }
        });
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                new TextDialog(p, "About", 
                    "<html>" + 
                    "<body style='width: 500px'>" + 
                    "<h1>About</h1>" + 
                    "This application was written by John Rowley.<br>" +
                    "<br>" +
                    "<h2>The MIT License (MIT)</h2><br>" +
                    "<br>" +
                    "Copyright (c) 2015 John Rowley<br>" +
                    "<br>" +
                    "Permission is hereby granted, free of charge, to any person obtaining a copy<br>" +
                    "of this software and associated documentation files (the \"Software\"), to deal<br>" +
                    "in the Software without restriction, including without limitation the rights<br>" +
                    "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell<br>" +
                    "copies of the Software, and to permit persons to whom the Software is<br>" +
                    "furnished to do so, subject to the following conditions:<br>" +
                    "<br>" +
                    "The above copyright notice and this permission notice shall be included in all<br>" +
                    "copies or substantial portions of the Software.<br>" +
                    "<br>" + 
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR<br>" +
                    "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,<br>" +
                    "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE<br>" +
                    "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER<br>" +
                    "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,<br>" + 
                    "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE<br>" +
                    "SOFTWARE." + 
                    "</body>" + 
                    "</html>");
            }
        });
        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);

        /*
         * Building the core gui. There exists 3 containers, the mainPanel,
         * the logPanel, and the 
         */
        mainPanel = new JPanel();
        buttonPanel = new JPanel();
        resultPanel = new JPanel();
        resultScrollPane = new JScrollPane(resultTable);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        resultPanel.add(resultScrollPane);
        resultPanel.add(Box.createRigidArea(new Dimension(0,5)));
        resultPanel.add(fileCountLabel);
        resultPanel.add(Box.createRigidArea(new Dimension(0,5)));
        resultPanel.add(totalLinesLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
        mainPanel.add(resultPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));

        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(selectDirectoryButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(extensionBox);
        buttonPanel.add(Box.createRigidArea(new Dimension(50,0)));
        buttonPanel.add(quitButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));

        add(mainPanel);
        pack();
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                App a = new App();
                a.setVisible(true);
            }
        });

        /*if(args.length == 2){
            FileWalker fw = new FileWalker(new Counter(args[1]));
            fw.walk(args[0]);
        } else {
            System.out.println("File and Extension not specified");
            return;
        }*/
    }
}

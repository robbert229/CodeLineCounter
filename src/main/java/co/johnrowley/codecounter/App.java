package co.johnrowley.codecounter;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class App extends JFrame
{
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;

    private JMenuItem saveMenuItem;
    private JMenuItem quitMenuItem;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JScrollPane resultScrollPane;

    private JButton quitButton;
    private JButton startButton;
    private JButton selectDirectoryButton;
    private JTable resultTable;

    public App(){
        initUI();
    }

    private void saveToFile(File f){
        try {
            PrintStream printer = new PrintStream(f);
            printer.println("Saved!");
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

            }
        });

        selectDirectoryButton = new JButton("Select Directory");
        selectDirectoryButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){

            }
        });
        
        String[] cols = {"Path", "Line Count"};
        Object[][] data = {{"Nothing Yet","0"}};
        resultTable = new JTable(data, cols);
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
                //saveFileDialog.setFileFilter(new FileNameExtensionFilter("Text File","txt"));
                
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
                System.out.println("Help!");
            }
        });
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event){
                System.out.println("About!");
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
        resultScrollPane = new JScrollPane(resultTable);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        //resultScrollPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));

        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));
        mainPanel.add(resultScrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0,5)));

        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(selectDirectoryButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50,0)));
        buttonPanel.add(quitButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));

        //logPanel.add(resultTable);

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

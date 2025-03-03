package com.atomicbanana;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
/**
 * Hello world!
 *
 */
public class App implements ActionListener, KeyListener
{
    JFrame frame;

    //create notepad text area
    JTextArea a1;
    JScrollPane scrollPane;
    
    //create notepad menu bar
    JMenuBar menuBar;
    JMenu menuFile, menuEdit, menuFormat, menuColor;
    JMenuItem iNew, iOpen, iSave, iSaveAs, iExit;
    JMenuItem iWordWrap;
    JMenu menuFont, menuFontSize;
    JMenuItem iFontSize8, iFontSize12, iFontSize14, iFontSize16, iFontSize24, iFontSize48;
    JMenuItem iWhite, iBlack, iBlue;
    JMenuItem iUndo, iRedo;

    //file
    String fileName = null;
    String fileAddress = null;;

    //setting
    boolean wordWrapOn = false;
    int fontSize = 24;
    String fontName;
    UndoManager um = new UndoManager();

    public App(){
        frame = new JFrame("My Notepad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 960);

        //create notepad text area
        a1 = new JTextArea();
        //setup for undo manager
        a1.getDocument().addUndoableEditListener(
            new UndoableEditListener() {
                public void undoableEditHappened(UndoableEditEvent e){
                    um.addEdit(e.getEdit());
                }
            }
        );
        // add key listener
        a1.addKeyListener(this);
        try {
            registerNotoFont();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        a1.setFont(new Font("Noto Sans TC Thin",Font.PLAIN,fontSize));
        scrollPane = new JScrollPane(a1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);
        
        //create notepad menu bar
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuFormat = new JMenu("Format");
        menuColor = new JMenu("Color");
        
        //create menu bar items 
        iNew = new JMenuItem("New");
        iNew.addActionListener(this);
        iNew.setActionCommand("New");
        iOpen = new JMenuItem("Open");
        iOpen.addActionListener(this);
        iOpen.setActionCommand("Open");
        iSave = new JMenuItem("Save (Ctrl + s)");
        iSave.addActionListener(this);
        iSave.setActionCommand("Save");
        iSaveAs = new JMenuItem("Save As (Ctrl + Shift + s)");
        iSaveAs.addActionListener(this);
        iSaveAs.setActionCommand("Save As");
        iExit = new JMenuItem("Exit");
        iExit.addActionListener(this);
        iExit.setActionCommand("Exit");
        menuFile.add(iNew);
        menuFile.add(iOpen);
        menuFile.add(iSave);
        menuFile.add(iSaveAs);
        menuFile.add(iExit);
        menuBar.add(menuFile);

        iUndo = new JMenuItem("Undo (Ctrl + z)");
        iUndo.addActionListener(this);
        iUndo.setActionCommand("Undo");
        iRedo = new JMenuItem("Redo (Ctrl + y)");
        iRedo.addActionListener(this);
        iRedo.setActionCommand("Redo");
        menuEdit.add(iUndo);
        menuEdit.add(iRedo);
        menuBar.add(menuEdit);

        iWordWrap = new JMenuItem("Word Wrap: OFF");
        iWordWrap.addActionListener(this);
        iWordWrap.setActionCommand("Word Wrap");
        menuFont = new JMenu("Font");
       
        menuFontSize = new JMenu("Font Size");
        for(String fontName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
            JMenuItem temp = new JMenuItem(fontName);
            temp.addActionListener(this);
            temp.setActionCommand("Set Font");
            menuFont.add(temp);
        }
        MenuScroller.setScrollerFor(menuFont);

        iFontSize8 = new JMenuItem("8");
        iFontSize8.addActionListener(this);
        iFontSize8.setActionCommand("8");
        iFontSize12 = new JMenuItem("12");
        iFontSize12.addActionListener(this);
        iFontSize12.setActionCommand("12");
        iFontSize14 = new JMenuItem("14");
        iFontSize14.addActionListener(this);
        iFontSize14.setActionCommand("14");
        iFontSize16 = new JMenuItem("16");
        iFontSize16.addActionListener(this);
        iFontSize16.setActionCommand("16");
        iFontSize24 = new JMenuItem("24");
        iFontSize24.addActionListener(this);
        iFontSize24.setActionCommand("24");
        iFontSize48 = new JMenuItem("48");
        iFontSize48.addActionListener(this);
        iFontSize48.setActionCommand("48"); 

        menuFontSize.add(iFontSize8);
        menuFontSize.add(iFontSize12);
        menuFontSize.add(iFontSize14);
        menuFontSize.add(iFontSize16);
        menuFontSize.add(iFontSize24);
        menuFontSize.add(iFontSize48);
        menuFormat.add(iWordWrap);
        menuFormat.add(menuFontSize);
        menuFormat.add(menuFont);
        menuBar.add(menuFormat);

        iWhite = new JMenuItem("White");
        iWhite.addActionListener(this);
        iWhite.setActionCommand("White");
        iBlack = new JMenuItem("Black");
        iBlack.addActionListener(this);
        iBlack.setActionCommand("Black");
        iBlue = new JMenuItem("Blue");
        iBlue.addActionListener(this);
        iBlue.setActionCommand("Blue");
        menuColor.add(iWhite);
        menuColor.add(iBlack);
        menuColor.add(iBlue);
        menuBar.add(menuColor);

        frame.setVisible(true);
    }

    public static void main( String[] args )
    {
        App notepad = new App();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){
            case "New" :
                a1.setText("");
                frame.setTitle("New");
                fileName = null;
                fileAddress = null;
            break;
            case "Open":Open();break;
            case "Save":Save();break;
            case "Save As":SaveAs();break;
            case "Exit":Exit();break;
            case "Word Wrap":WordWrap();break;
            case "8":setFontSize(8);break;
            case "12":setFontSize(12);break;
            case "14":setFontSize(14);break;
            case "16":setFontSize(16);break;
            case "24":setFontSize(24);break;
            case "48":setFontSize(48);break;
            case "Set Font":
                if(e.getSource().getClass().equals(JMenuItem.class)){
                    JMenuItem temp = (JMenuItem) e.getSource();
                    setFont(temp.getText());
                }
            break;
            case "White":setColor(command);break;
            case "Black":setColor(command);break;
            case "Blue":setColor(command);break;
            case "Undo": um.undo();;break;
            case "Redo": um.redo();;break;
            default:
            break;
        }
        
    }

    void Open(){
        FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
        fd.setVisible(true);

        if(fd.getFile() != null){
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            frame.setTitle(fileName);

            try(BufferedReader reader = new BufferedReader(new FileReader(fileAddress + fileName));){
                a1.setText("");
                String line = null;
                while((line = reader.readLine()) != null){
                    a1.append(line + "\n");
                }
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }    
    }

    void SaveAs(){
        FileDialog fd = new FileDialog(frame, "Save", FileDialog.SAVE);
        fd.setVisible(true);
        if(fd.getFile() != null){
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            frame.setTitle(fileName);

            try (FileWriter fw = new FileWriter(fileAddress + fileName)) {
                fw.write(a1.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void Save(){
        if(fileName == null || fileAddress == null){
            SaveAs();
        } else {
            try (FileWriter fw = new FileWriter(fileAddress + fileName)) {
                fw.write(a1.getText());
                frame.setTitle(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void Exit(){
        System.exit(0);
    }

    void WordWrap(){
        if(wordWrapOn == false){
            wordWrapOn = true;
            a1.setLineWrap(true);
            a1.setWrapStyleWord(true);
            iWordWrap.setText("Word Wrap: ON");
        } else {
            wordWrapOn = false;
            a1.setLineWrap(false);
            a1.setWrapStyleWord(false);
            iWordWrap.setText("Word Wrap: OFF");
        }
    }

    void setFontSize(int size){
        fontSize = size;
        a1.setFont(new Font(fontName, Font.PLAIN, fontSize));
    }

    void setFont(String name){
        fontName = name;
        Font font = new Font(fontName, Font.PLAIN, fontSize);
        a1.setFont(font);
    }

    void registerNotoFont() throws FontFormatException, IOException{
        fontName="FiraCode-VariableFont_wght";
        InputStream fontFile = getClass().getClassLoader().getResourceAsStream("fonts/NotoColorEmoji-Regular.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT,fontFile);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        fontFile = getClass().getClassLoader().getResourceAsStream("fonts/Noto_Sans/NotoSans-Italic-VariableFont_wdth,wght.ttf");
        font = Font.createFont(Font.TRUETYPE_FONT,fontFile);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        fontFile = getClass().getClassLoader().getResourceAsStream("fonts/Noto_Sans/NotoSans-VariableFont_wdth,wght.ttf");
        font = Font.createFont(Font.TRUETYPE_FONT,fontFile);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT,getClass().getClassLoader().getResourceAsStream("fonts/Noto_Sans/NotoSansTC-VariableFont_wght.ttf")));
    }

    void setColor(String color){
        switch(color){
            case "White":
                a1.setBackground(Color.WHITE);
                a1.setForeground(Color.BLACK);
                break;
            case "Black":
                a1.setBackground(Color.BLACK);
                a1.setForeground(Color.WHITE);
                break;
            case "Blue":
                a1.setBackground(Color.BLUE);
                a1.setForeground(Color.WHITE);
                break;
            default:
                break;
        }
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_Z){
            um.undo();
        }
        if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_Y){
            um.redo();
        }
        if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_S){
            Save();
        }
        if(e.isControlDown() && e.isShiftDown() && e.getKeyCode()==KeyEvent.VK_S){
            SaveAs();
        }
        if(e.isAltDown()){
           menuFile.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Color;
import java.util.Stack;

public class GUI implements ActionListener {

    JFrame window;
    JTextPane textArea; 
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenu menuEdit, menuFormat;
    JMenuItem iUndo, iRedo, iTextColor;
    JCheckBoxMenuItem iBold, iItalic, iUnderline;  
    JMenuItem iFontArial, iFontCSMS, iFontTNR, iFontSize8, iFontSize12, iFontSize16, iFontSize20, iFontSize24, iFontSize30;
    JMenu menuFont, menuFontSize;

    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();
    private boolean ignoreChange = false;

    Format format = new Format(this);

    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        createWindow();
        createTextArea();
        createMenuBar();
        createEditMenu();
        createFormatMenu();

        format.selectedFont = "Arial";
        format.createFont(16);
        window.setVisible(true);
    }

    public void createWindow() {
        window = new JFrame("22BCE170's Text Editor");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createTextArea() {
        textArea = new JTextPane();  
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (!ignoreChange) {
                    pushToUndoStack(e);
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (!ignoreChange) {
                    pushToUndoStack(e);
                }
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void pushToUndoStack(DocumentEvent e) {
                try {
                    String change = e.getDocument().getText(e.getOffset(), e.getLength());
                    undoStack.push(change);
                    redoStack.clear();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane);
        undoStack.push(textArea.getText());
    }

    public void createMenuBar() {
        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        menuEdit = new JMenu("EDIT");
        menuBar.add(menuEdit);

        iUndo = new JMenuItem("Undo");
        iUndo.addActionListener((ActionListener) this);
        iUndo.setActionCommand("Undo");
        iUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        menuEdit.add(iUndo);

        iRedo = new JMenuItem("Redo");
        iRedo.addActionListener((ActionListener) this);
        iRedo.setActionCommand("Redo");
        iRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        menuEdit.add(iRedo);

        menuFormat = new JMenu("FORMAT");
        menuBar.add(menuFormat);

        iBold = new JCheckBoxMenuItem("Bold");
        iBold.addActionListener(this);
        iBold.setActionCommand("Bold");
        menuFormat.add(iBold);

        iItalic = new JCheckBoxMenuItem("Italic");
        iItalic.addActionListener(this);
        iItalic.setActionCommand("Italic");
        menuFormat.add(iItalic);

        iUnderline = new JCheckBoxMenuItem("Underline");
        iUnderline.addActionListener(this);
        iUnderline.setActionCommand("Underline");
        menuFormat.add(iUnderline);

        iTextColor = new JMenuItem("Text Color");
        iTextColor.addActionListener(this);
        iTextColor.setActionCommand("TextColor");
        menuFormat.add(iTextColor);
    }

    public void createEditMenu() {
    }

    public void createFormatMenu() {
        menuFont = new JMenu("Font");
        menuFormat.add(menuFont);

        iFontArial = new JMenuItem("Arial");
        iFontArial.addActionListener((ActionListener) this);
        iFontArial.setActionCommand("Arial");
        menuFont.add(iFontArial);

        iFontCSMS = new JMenuItem("Comic Sans MS");
        iFontCSMS.addActionListener((ActionListener) this);
        iFontCSMS.setActionCommand("Comic Sans MS");
        menuFont.add(iFontCSMS);

        iFontTNR = new JMenuItem("Times New Roman");
        iFontTNR.addActionListener((ActionListener) this);
        iFontTNR.setActionCommand("Times New Roman");
        menuFont.add(iFontTNR);

        menuFontSize = new JMenu("Font Size");
        menuFormat.add(menuFontSize);

        iFontSize8 = new JMenuItem("8");
        iFontSize8.addActionListener(this);
        iFontSize8.setActionCommand("size8");
        menuFontSize.add(iFontSize8);

        iFontSize12 = new JMenuItem("12");
        iFontSize12.addActionListener(this);
        iFontSize12.setActionCommand("size12");
        menuFontSize.add(iFontSize12);

        iFontSize16 = new JMenuItem("16");
        iFontSize16.addActionListener(this);
        iFontSize16.setActionCommand("size16");
        menuFontSize.add(iFontSize16);

        iFontSize20 = new JMenuItem("20");
        iFontSize20.addActionListener(this);
        iFontSize20.setActionCommand("size20");
        menuFontSize.add(iFontSize20);

        iFontSize24 = new JMenuItem("24");
        iFontSize24.addActionListener(this);
        iFontSize24.setActionCommand("size24");
        menuFontSize.add(iFontSize24);

        iFontSize30 = new JMenuItem("30");
        iFontSize30.addActionListener(this);
        iFontSize30.setActionCommand("size30");
        menuFontSize.add(iFontSize30);
    }

    private void setStyle(String style) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBold(attributes, style.equals("bold"));
        StyleConstants.setItalic(attributes, style.equals("italic"));
        StyleConstants.setUnderline(attributes, style.equals("underline"));
        textArea.setCharacterAttributes(attributes, false);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Undo":
                if (!undoStack.isEmpty()) {
                    ignoreChange = true;
                    String curr = textArea.getText();
                    if (!curr.isEmpty()) {
                        String change = undoStack.pop();
                        if (curr.endsWith(change)) {
                            curr = curr.substring(0, curr.length() - change.length());
                        }
                        textArea.setText(curr);
                        redoStack.push(change);
                    }
                   ignoreChange = false;
                }
                break;
            case "Redo":
                if (!redoStack.isEmpty()) {
                    ignoreChange = true;
                    String curr = textArea.getText();
                    String change = redoStack.pop();
                    curr += change;
                    textArea.setText(curr);
                    undoStack.push(change);
                    ignoreChange = false;
                }
                break;
            case "Arial":
                format.setFont(command);
                break;
            case "Comic Sans MS":
                format.setFont(command);
                break;
            case "Times New Roman":
                format.setFont(command);
                break;
            case "size8":
                format.createFont(8);
                break;
            case "size12":
                format.createFont(12);
                break;
            case "size16":
                format.createFont(16);
                break;
            case "size20":
                format.createFont(20);
                break;
            case "size24":
                format.createFont(24);
                break;
            case "size30":
                format.createFont(30);
                break;
            case "Bold":
                setStyle("bold");
                break;
            case "Italic":
                setStyle("italic");
                break;
            case "Underline":
                setStyle("underline");
                break;
            case "TextColor":
                Color newColor = JColorChooser.showDialog(window, "Choose Text Color", Color.BLACK);
                if (newColor != null) {
                    textArea.setForeground(newColor);
                }
                break;
        }
    }
}
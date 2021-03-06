package graphicalUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import main.GameEngine;

/**
 * Creation of the graphical user interface in Swing.
 * Displays toolbar, with menus and sub-menus, buttons and toggle buttons.
 * Keyboard shortcuts and listeners specified also.
 */
public class GraphicalUI {

    // private static instance variables
    private static GameEngine gameEngine;
    private static ArrayList<String> messages;

    // instance variables for swing
    private JMenuBar menuBar;
    private JMenu fileMenu, subMenu;
    private JMenuItem menuItem;
    private JPanel boardPanel, labelPanel, buttonPanel, gridPanel;
    private JButton undoButton, resetButton;
    private JToggleButton coordinatesButton;
    private JLabel objectiveLabel, playerLabel, feedbackLabel;
    private static JMenu competitiveFileMenu, creationFileMenu;
    public static JLabel aiLabel;
    public static JLabel aiTimeLabel;
    public static JLabel feedback;

    // variables for mode options
    private static boolean bounds, competitive; // for toggle buttons
    private static boolean mixedStones, deleteStones; // problem creation
    private static boolean problemSettings; // user selected objective and bounds
    private static boolean rowNumbers;

    // public static instance variables for swing
    static JFrame frame;
    static JLabel player, objective, currentAILabel;
    static BoardJPanel boardJP;
    static Container pane;
    static JButton passButton;
    static JToggleButton creationButton, competitiveButton, boundsButton;
    static JButton aiMoveButton;
    
    // static variables
    static String aiType;
    static String aiType2; // for AI vs AI mode
    static String saveName;
    static String saveLocation;
    static String[] heuristics;
    static int[] heuristicIndices;

    /**
     * Start the GUI, show frame.
     */
    public void start(final GameEngine gE) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GraphicalUI.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Initialise GUI with GameEngine instance.
     *
     * @param gE instance of the controller with board representation
     */
    public GraphicalUI(GameEngine gE) {
        setGameEngine(gE);
        initialise();
    }

    /**
     * Initialise the contents of the frame.
     */
    private void initialise() {

        // Define size of window in 16:9 ratio
        int xInit, yInit, width, height;
        width = 1200;
        height = (width / 16) * 9;
        xInit = 100;
        yInit = 100;

        // Set stones to alternate as default
        mixedStones = true;

        // Initial mode as creation
        competitive = false;

        // Default AI as minimax
        aiType = "MiniMax";

        // Set inital save name
        saveName = "Untitled";

        // Initalise message log ArrayList
        messages = new ArrayList<>();
        messages.add("");
        messages.add("");
        messages.add("Click to place stones.");

        // START OF FRAME //
        // Frame to hold all elements
        frame = new JFrame();
        Insets ins = frame.getInsets();
        Dimension frameSize = new Dimension(width + ins.left + ins.right,
                height + ins.left + ins.right);
        frame.setBounds(xInit, yInit, width, height);
        frame.setSize(frameSize);
        frame.setPreferredSize(frameSize);
        frame.setMinimumSize(frameSize);
        // Inner class to make sure all processes are terminated when the
        // program is closed
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent close) {
                Runtime.getRuntime().halt(0);
            }
        });
        // Set window title
        setFrameTitle(saveName);

        // START OF MENUBAR //
        // Create the menu bar
        menuBar = new JMenuBar();

        // Build the file menu
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(fileMenu);

        // Menu items for new boards
        subMenu = new JMenu("New Problem");
        subMenu.getAccessibleContext().setAccessibleDescription(
                "Creates a new problem.");
        subMenu.setMnemonic(KeyEvent.VK_N);

        // Menu item for new game
        menuItem = new JMenuItem("Default (9x9)", KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Creates a New board with default size of 9x9");
        menuItem.addActionListener(new NewMenuListener());
        subMenu.add(menuItem);

        // Menu item for custom sized new game
        menuItem = new JMenuItem("Custom...", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Creates a New board with specified size");
        menuItem.addActionListener(new NewMenuListener());
        subMenu.add(menuItem);

        fileMenu.add(subMenu);

        // Menu item for loading the board
        menuItem = new JMenuItem("Load Problem", KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Loads a Go problem board");
        menuItem.addActionListener(new FileMenuListener());
        fileMenu.add(menuItem);

        // Menu items for saving the board
        menuItem = new JMenuItem("Save Problem", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Saves current board");
        menuItem.addActionListener(new FileMenuListener());
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Save Problem As...", KeyEvent.VK_V);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Saves current board to specified path");
        menuItem.addActionListener(new FileMenuListener());
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        // Menu item for exiting the program
        menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Exits the program");
        menuItem.addActionListener(new FileMenuListener());
        fileMenu.add(menuItem);

        // Build menu for competitive play
        competitiveFileMenu = new JMenu("Competitive Play");
        competitiveFileMenu.setMnemonic(KeyEvent.VK_1);
        competitiveFileMenu.getAccessibleContext().setAccessibleDescription(
                "Menu with options during competitive play mode");
        competitiveFileMenu.setEnabled(competitive);
        menuBar.add(competitiveFileMenu);

        // Menu item for swapping player colour
        menuItem = new JMenuItem("Swap Player Colour", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.SHIFT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Swap human and AI playing colours");
        menuItem.addActionListener(new CompetitiveMenuListener());
        competitiveFileMenu.add(menuItem);

        // Menu item for making AI move
        menuItem = new JMenuItem("Make AI Move", KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                ActionEvent.SHIFT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Force AI to move first");
        menuItem.addActionListener(new CompetitiveMenuListener());
        competitiveFileMenu.add(menuItem);

        competitiveFileMenu.addSeparator();

        // Menu item to switch AI type
        menuItem = new JMenuItem("Select AI Type", KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                ActionEvent.SHIFT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Choose algorithm that AI uses to move");
        menuItem.addActionListener(new CompetitiveMenuListener());
        competitiveFileMenu.add(menuItem);

        // Build menu for problem creation
        creationFileMenu = new JMenu("Problem Creation");
        creationFileMenu.setMnemonic(KeyEvent.VK_2);
        creationFileMenu.getAccessibleContext().setAccessibleDescription(
                "Menu with options during problem creation mode");
        menuBar.add(creationFileMenu);

        // Menu items for problem settings
        menuItem = new JMenuItem("Objective & Bounds", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Allow user to specify board objective and bounds");
        menuItem.addActionListener(new ProblemSettingsListener());
        creationFileMenu.add(menuItem);

        menuItem = new JMenuItem("Alter Bounds", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Allow user to alter board objective");
        menuItem.addActionListener(new ProblemSettingsListener());
        creationFileMenu.add(menuItem);

        creationFileMenu.addSeparator();

        // Menu item for heuristic chooser
        menuItem = new JMenuItem("Choose Heuristics", KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Select heuristics");
        menuItem.addActionListener(new ProblemSettingsListener());
        creationFileMenu.add(menuItem);

        creationFileMenu.addSeparator();

        // Menu item for using black stones
        menuItem = new JMenuItem("Use Black Stones", KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Place only black stones down");
        menuItem.addActionListener(new CreationMenuListener());
        creationFileMenu.add(menuItem);

        // Menu item for using white stones
        menuItem = new JMenuItem("Use White Stones", KeyEvent.VK_W);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Place only white stones down");
        menuItem.addActionListener(new CreationMenuListener());
        creationFileMenu.add(menuItem);

        // Menu item for using white/black stones
        menuItem = new JMenuItem("Use Mixed Stones", KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Places stones in black and white order");
        menuItem.addActionListener(new CreationMenuListener());
        creationFileMenu.add(menuItem);

        creationFileMenu.addSeparator();

        // Menu item for removing stones
        menuItem = new JMenuItem("Delete Stones", KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Allow user to delete stones");
        menuItem.addActionListener(new CreationMenuListener());
        creationFileMenu.add(menuItem);

        // Build debug menu
        fileMenu = new JMenu("Debug");
        fileMenu.setMnemonic(KeyEvent.VK_D);
        fileMenu.getAccessibleContext()
                .setAccessibleDescription(
                        "Menu used for debugging the program");
        menuBar.add(fileMenu);

        // Menu item to show the log
        menuItem = new JMenuItem("Show Log", KeyEvent.VK_A);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Shows the log of the program");
        menuItem.addActionListener(new DebugMenuListener());
        fileMenu.add(menuItem);

        // Build help menu in the menu bar
        fileMenu = new JMenu("Help");
        fileMenu.setMnemonic(KeyEvent.VK_H);
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "There should be helpful things here.");
        menuBar.add(fileMenu);

        // Menu item for keyboard shortcuts
        menuItem = new JMenuItem("Show Keyboard Shortcuts", KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Display keybaord shortcuts");
        menuItem.addActionListener(new HelpMenuListener());
        fileMenu.add(menuItem);

        // Add entire menu bar to frame
        frame.setJMenuBar(menuBar);

        // END OF MENUBAR //
        
        // START OF PANE LAYOUT //
        // Border layout for frame
        pane = frame.getContentPane();
        pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        pane.setLayout(new BorderLayout());

        // START OF BOARD PANEL //
        // Left panel for the board
        boardPanel = new JPanel();
        boardPanel.setBackground(Color.lightGray);
        boardPanel.setBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED));
        boardPanel.setPreferredSize(new Dimension(height, width / 8 * 5));

        boardJP = new BoardJPanel(getGameEngine());
        boardPanel.add(boardJP);

        pane.add(boardPanel, BorderLayout.WEST);

        // END OF BOARD PANEL //
        
        // START OF BUTTON PANEL //
        // Right panel for buttons
        buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED));

        // START OF LABEL PANEL //
        // Top right panel for choosing player and move
        labelPanel = new JPanel(new GridLayout(0, 2));
        labelPanel.setBackground(Color.lightGray);
        labelPanel.setBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED));

        // Labels for player chooser
        objectiveLabel = new JLabel("      Objective:");
        objective = new JLabel();
        objective.setText("No set objective");

        // Add objective labels to panel
        labelPanel.add(objectiveLabel);
        labelPanel.add(objective);

        // Add padding to panel
        labelPanel.add(new JPanel());
        labelPanel.add(new JPanel());

        // Labels to show whose turn it is
        playerLabel = new JLabel("      Player: ");
        player = new JLabel("Black to move");

        // Add labels to panel
        labelPanel.add(playerLabel);
        labelPanel.add(player);

        // Add padding to panel
        labelPanel.add(new JPanel());
        labelPanel.add(new JPanel());

        // Labels to show invalid moves
        feedbackLabel = new JLabel("     User Message: ");
        feedback = new JLabel("Click to place stones");

        // Add labels to panel
        labelPanel.add(feedbackLabel);
        labelPanel.add(feedback);

        // Add padding to panel
        labelPanel.add(new JPanel());
        labelPanel.add(new JPanel());

        // Labels to show current AI type
        aiLabel = new JLabel("    AI Type: ");
        aiLabel.setVisible(false);
        currentAILabel = new JLabel(aiType);
        currentAILabel.setVisible(false);

        // Add labels to panel
        labelPanel.add(aiLabel);
        labelPanel.add(currentAILabel);

        // Add label panel to top of button panel
        buttonPanel.add(labelPanel, BorderLayout.NORTH);

        // END OF LABEL PANEL //
        
        // START OF GRID PANEL //
        // Grid panel for some buttons
        gridPanel = new JPanel(new GridLayout(0, 2));

        // Add padding to panel
        gridPanel.add(new JPanel());
        gridPanel.add(new JPanel());

        // Toggle button to select competitive mode
        competitiveButton = new JToggleButton("Competitive Play Mode");
        competitiveButton.setMnemonic(KeyEvent.VK_P);
        gridPanel.add(competitiveButton);
        competitiveButton.addActionListener(new GridToggleListener());

        // Toggle button to select creation mode
        creationButton = new JToggleButton("Problem Creation Mode");
        creationButton.setMnemonic(KeyEvent.VK_C);
        creationButton.setSelected(true);
        gridPanel.add(creationButton);
        creationButton.addActionListener(new GridToggleListener());

        // Button to undo last move
        undoButton = new JButton("Undo");
        undoButton.setMnemonic(KeyEvent.VK_U);
        gridPanel.add(undoButton);
        undoButton.addActionListener(new GridListener());

        // Button to reset problem
        resetButton = new JButton("Reset");
        resetButton.setMnemonic(KeyEvent.VK_R);
        gridPanel.add(resetButton);
        resetButton.addActionListener(new GridListener());

        // Button to allow players to pass
        passButton = new JButton("Pass");
        passButton.setMnemonic(KeyEvent.VK_A);
        passButton.setEnabled(false);
        gridPanel.add(passButton);
        passButton.addActionListener(new GridListener());

        // Button to let AI move
        aiMoveButton = new JButton("Next AI Move");
        aiMoveButton.setMnemonic(KeyEvent.VK_I);
        aiMoveButton.setEnabled(false);
        gridPanel.add(aiMoveButton);
        aiMoveButton.addActionListener(new GridListener());

        // Button to show bounds of problem
        boundsButton = new JToggleButton("Show Bounds");
        boundsButton.setMnemonic(KeyEvent.VK_B);
        boundsButton.setEnabled(false);
        gridPanel.add(boundsButton);
        boundsButton.addActionListener(new GridToggleListener());

        // Toggle button to show coordinates of problem
        coordinatesButton = new JToggleButton("Show Coordinates");
        coordinatesButton.setMnemonic(KeyEvent.VK_O);
        gridPanel.add(coordinatesButton);
        coordinatesButton.addActionListener(new GridToggleListener());

        // Add grid button panel to button panel
        buttonPanel.add(gridPanel, BorderLayout.SOUTH);

        // Add right hand panel to pane
        pane.add(buttonPanel);

        // END OF BUTTON PANEL //
        // END OF GRIDBAD LAYOUT //
        // END OF FRAME //
    }

    // Game engine getter and setter for external action listeners
    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    public static void setGameEngine(GameEngine ge) {
        gameEngine = ge;
    }

    // Getters for GUI booleans for modes
    public static boolean getBounds() {
        return bounds;
    }

    public static boolean getCompetitive() {
        return competitive;
    }

    public static boolean getMixedStones() {
        return mixedStones;
    }

    public static boolean getDeleteStones() {
        return deleteStones;
    }

    public static boolean getProblemSettings() {
        return problemSettings;
    }

    // Setters for GUI booleans for modes
    public static void setProblemSettings(boolean b) {
        problemSettings = b;
    }

    public static void setBounds(boolean b) {
        bounds = b;
    }
    
    public static void setMixedStones(boolean b) {
        mixedStones = b;
    }

    public static void setDeleteStones(boolean b) {
        deleteStones = b;
    }

    public static void setFrameTitle(String s) {
        frame.setTitle("GoProblemSolver: " + s);
    }


    // Change the mode of the gui, true changes to competitive, false to creation
    public static void changeMode(boolean competitiveMode) {
        // Set competitive mode tools
        competitiveFileMenu.setEnabled(competitiveMode);
        aiLabel.setVisible(competitiveMode);
        currentAILabel.setVisible(competitiveMode);
        passButton.setEnabled(competitiveMode);
        // Set creation mode tools
        creationFileMenu.setEnabled(!competitiveMode);
        // Set buttons and mode
        competitive = competitiveMode;
        competitiveButton.setSelected(competitiveMode);
        creationButton.setSelected(!competitiveMode);
        // Set user feedback message
        if (competitiveMode) {
            GraphicalUI.updateMessage("Entered Competitive Mode");
        } else {
            GraphicalUI.updateMessage("Entered Problem Creation Mode");

        }
    }

    // Getter and setter for the row numbers
    public static void toggleRowNumbers() {
        rowNumbers = !rowNumbers;
    }

    public static boolean getRowNumbers() {
        return rowNumbers;
    }

    // User message methods
    public static void updateMessage(String s) {
        messages.add(s);
        int l = messages.size();
        feedback.setText(messages.get(l - 1));
    }

    public static void resetMessage() {
        messages = new ArrayList<>();
        messages.add("");
        messages.add("");
        updateMessage("Board has been reset");
    }

    public static ArrayList<String> getMessages() {
        return messages;
    }

    // Method to enable/disable bounds button
    public static void updateBoundsButton(boolean b) {
        boundsButton.setEnabled(b);
        boundsButton.setText("Show Bounds");
        boundsButton.setSelected(false);
    }

    public static void turnOffBounds() {
        if (boundsButton.getText().equals("Hide Bounds")) {
            boundsButton.doClick();
        }
    }

}

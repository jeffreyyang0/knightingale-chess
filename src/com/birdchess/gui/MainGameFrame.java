package com.birdchess.gui;

//Import all the necessary classes
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.*;

import com.birdchess.common.Game;

/** The MainGameFrame class - creates the frame to hold all the panels
 * The frame used to hold all the necessary panels of the chess game.
 * @author Adrian Leung, Charley Huang, and Jeffrey Yang
 * @version January 21, 2013
 */ 
public class MainGameFrame extends JFrame implements ActionListener{
    // Create all the menu bar variables.
    private JMenuItem newSingleplayerOption, newMultiplayerOption, saveOption, loadOption, mainMenuOption,
    exitOption, instructionMenuItem, aboutMenuItem, switchOption, undoOption, makeAssistMoveOption;
    private JMenuItem simpleSingleOption, simpleMultiOption, simpleExitOption;
    
    private JMenuBar mainMenu;
    private JMenuBar simpleMainMenu;
    
    //Create variables to change the difficulty of the AI
    private JCheckBoxMenuItem easyOption, mediumOption, hardOption, veryHardOption, customDifficultyOption;
    
    //The necessary panels to create the GUI
    private MainGamePanel gamePanel;
    private MainMenuPanel menu;
    private PlayerPanel left;
	private PlayerPanel right;
	
	//A file chooser to have a functional save game.
    private JFileChooser fileChooser;
    
    /**
     * Constructs a new MainGameFrame that has all the GUI
     */
	public MainGameFrame()
	{
		super ("Knightingale");
		setResizable (false);
		
		//Add a main menu to the game frame.
		menu = new MainMenuPanel(this);
		getContentPane ().add(menu, BorderLayout.CENTER);
		
		//Adds an option to start a single player game.
        newSingleplayerOption = new JMenuItem ("New Singleplayer");
        
        //Create a keyboard hot key for this option
        newSingleplayerOption.setAccelerator (
                KeyStroke.getKeyStroke
                (KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newSingleplayerOption.addActionListener (this);
        
        //Adds an option to start a multi-player game.
        newMultiplayerOption = new JMenuItem ("New Multiplayer");
        
        //Create a keyboard hot key for this option.
        newMultiplayerOption.setAccelerator (
                KeyStroke.getKeyStroke
                (KeyEvent.VK_P, InputEvent.CTRL_MASK));
        newMultiplayerOption.addActionListener (this);

        //Adds an option to save a game.
        saveOption = new JMenuItem ("Save Game");
        
        //Create a keyboard hot key for this option.
        saveOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveOption.addActionListener (this);

        //Adds an option to load a saved game.
        loadOption = new JMenuItem ("Load Game");
        
        //Create a keyboard hot key for this option.
        loadOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_L, InputEvent.CTRL_MASK));
        loadOption.addActionListener (this);

        //Adds an option to go back to the main menu.
        mainMenuOption = new JMenuItem ("Main Menu");
        
        //Create a keyboard hot key for this option.
        mainMenuOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_M, InputEvent.CTRL_MASK));
        mainMenuOption.addActionListener (this);

        //Adds an option to exit the game.
        exitOption = new JMenuItem ("Exit");
        
        //Create a keyboard hot key for this option.
        exitOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_X, InputEvent.CTRL_MASK));
        exitOption.addActionListener (this);
        
        //Adds an option to undo a move.
        undoOption = new JMenuItem ("Undo Move");
        
        //Create a keyboard hot key for this option.
        undoOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        undoOption.addActionListener (this);
        
        //Adds an option to help a player make a move.
        makeAssistMoveOption = new JMenuItem ("Help Me Make A Move!");
        
        //Create a keyboard hot key for this option.
        makeAssistMoveOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_H, InputEvent.CTRL_MASK));
        makeAssistMoveOption.addActionListener (this);
        
        //Adds an option to switch sides.
        switchOption = new JMenuItem ("Switch Sides");
        
        //Create a keyboard hot key for this option.
        switchOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_W, InputEvent.CTRL_MASK));
        switchOption.addActionListener (this);
        
        //Creates an option to set the AI to easy and sets the initial difficulty to easy.
        easyOption = new JCheckBoxMenuItem ("Easy");
        easyOption.setState(true);
        easyOption.addActionListener (this);
        
        //Creates an option to set the AI to medium.
        mediumOption = new JCheckBoxMenuItem ("Medium");
        mediumOption.addActionListener (this);
        
        //Creates an option to set the AI to hard.
        hardOption = new JCheckBoxMenuItem ("Hard");
        hardOption.addActionListener (this);
        
        //Creates an option to set the AI to very hard.
        veryHardOption = new JCheckBoxMenuItem ("Very Hard");
        veryHardOption.addActionListener (this);
        
        //Creates an option to set the AI to a custom difficulty using search depth.
        customDifficultyOption = new JCheckBoxMenuItem ("Custom...");
        customDifficultyOption.addActionListener (this);

        // Create the help menu with instructions and about items
        JMenu instructionMenu = new JMenu ("About");
        aboutMenuItem = new JMenuItem ("About");
        aboutMenuItem.addActionListener (this);
        instructionMenu.add (aboutMenuItem);
        
        //Add the difficulty menu to the menu bar.
        JMenu difficultyMenu = new JMenu ("AI Difficulty Level");
        
        //Add all the difficulty options to the difficulty menu.
        difficultyMenu.add(easyOption);
        difficultyMenu.add(mediumOption);
        difficultyMenu.add(hardOption);
        difficultyMenu.add(veryHardOption);
        difficultyMenu.addSeparator ();
        difficultyMenu.add(customDifficultyOption);

        // Add each menu item to the main menu
        JMenu gameMenu = new JMenu ("Game");
        
        //Add the game options to the game menu.
        gameMenu.add (newSingleplayerOption);
        gameMenu.add (newMultiplayerOption);
        gameMenu.addSeparator ();
        
        //Add the save and load options to the game menu.
        gameMenu.add (saveOption);
        gameMenu.add (loadOption);
        gameMenu.addSeparator ();
        
        //Add the return to main menu option to the game menu.
        gameMenu.add (mainMenuOption);
        gameMenu.addSeparator ();
        
        //Add an exit option to the game menu.
        gameMenu.add (exitOption);
        
        //Adds the game options to the game menu.
        JMenu optionsMenu = new JMenu ("Options");
        
        //Adds the undo option to the options menu.
        optionsMenu.add(undoOption);
        optionsMenu.addSeparator();
        
        //Adds the help me make a move option to the options menu.
        optionsMenu.add(makeAssistMoveOption);
        optionsMenu.addSeparator();
        
        //Adds the switch sides option to the options menu.
        optionsMenu.add (switchOption);
        
        //Add a settings option to the menu bar.
        JMenu settingsMenu = new JMenu ("Settings");
        
        //Add the difficulty menu to the settings menu.
        settingsMenu.add(difficultyMenu);
        
        //Create the menu bar.
        mainMenu = new JMenuBar ();
        
        //Add all the menus to the menu bar.
        mainMenu.add (gameMenu);
        mainMenu.add (optionsMenu);
        mainMenu.add (settingsMenu);
        mainMenu.add (instructionMenu);
        
        // Make a new restricted menu bar for the menu screen only
        simpleMainMenu = new JMenuBar();
        
        // Add basic options
        JMenu simpleGameMenu = new JMenu ("Game");
        
        // Add an option to start a single-player game
        simpleSingleOption = new JMenuItem ("New Singleplayer");
        simpleSingleOption.setAccelerator (
                KeyStroke.getKeyStroke
                (KeyEvent.VK_N, InputEvent.CTRL_MASK));
        simpleSingleOption.addActionListener (this);
        
        // Add an option to start a multi-player game
        simpleMultiOption = new JMenuItem ("New Multiplayer");
        simpleMultiOption.setAccelerator (
                KeyStroke.getKeyStroke
                (KeyEvent.VK_P, InputEvent.CTRL_MASK));
        simpleMultiOption.addActionListener (this);
        
        simpleExitOption = new JMenuItem ("Exit");
        simpleExitOption.setAccelerator
            (KeyStroke.getKeyStroke (KeyEvent.VK_X, InputEvent.CTRL_MASK));
        simpleExitOption.addActionListener (this);
        
        simpleGameMenu.add (simpleSingleOption);
        simpleGameMenu.add (simpleMultiOption);
        simpleGameMenu.addSeparator ();
        simpleGameMenu.add (simpleExitOption);
        
        simpleMainMenu.add(simpleGameMenu);
        
        // Set the menu bar to the main menu
        setJMenuBar (simpleMainMenu);
        
        //Initialize the file chooser.
        fileChooser = new JFileChooser();
    }
	
	/**
	 * Switches the panels to display the chess game
	 */
	public void switchToGame ()
	{
		//Starts a new single player game.
		gamePanel = new MainGamePanel(this, Game.GAME_SINGLEPLAYER);
		
		//Adds the player panels.
		left = new PlayerPanel(Color.WHITE, this);
		right = new PlayerPanel(Color.BLACK, this);
		
		//Removes the menu from the frame.
		getContentPane ().remove(menu);
		
		// Use the full menu bar
		setJMenuBar (mainMenu);
		
		//Adds the game interface to the frame.
		getContentPane ().add(left, BorderLayout.WEST);
        getContentPane ().add (gamePanel, BorderLayout.CENTER);
        getContentPane ().add(right, BorderLayout.EAST);        
        pack();
        setVisible(true);
	}
	
	/**
	 * Switches the panels to display the main menu
	 */
	public void switchToMenu ()
	{
		//Creates a new main menu.
		menu = new MainMenuPanel(this);

		//Removes the game interface from the frame.
		getContentPane ().remove(gamePanel);
		getContentPane ().remove(left);
		getContentPane ().remove(right);
		
		// Use the restricted menu bar
		setJMenuBar (simpleMainMenu);
		
		//Adds the main menu to the frame.
		getContentPane ().add(menu, BorderLayout.CENTER);		
		pack();
        setVisible(true);
	}

	/**
	 * Clears the check boxes for the AI difficulty menu
	 */
	private void clearAIDifficultyCheckboxes ()
	{
		//Clears all the check boxes.
		easyOption.setState(false);
		mediumOption.setState(false);
		hardOption.setState(false);
		veryHardOption.setState(false);
		customDifficultyOption.setState(false);
	}

	/**
	 * Responds to a Menu Event, needed because frame uses ActionListener
	 * @param event the event that triggered this method
	 */
    public void actionPerformed (ActionEvent event)
    {
    	//Creates a new single player game when the single player option is selected.
        if (event.getSource () == newSingleplayerOption)
        {
        	gamePanel.newGame (Game.GAME_SINGLEPLAYER);
        	clearAIDifficultyCheckboxes();
    		easyOption.setState(true);
        }
        
        else if (event.getSource () == simpleSingleOption)
        {
        	switchToGame();
        	gamePanel.newGame (Game.GAME_SINGLEPLAYER);
        	clearAIDifficultyCheckboxes();
    		easyOption.setState(true);
        }
        
        //Creates a new multi-player game when the multi-player option is selected.
        else if (event.getSource () == newMultiplayerOption)
        {
        	gamePanel.newGame (Game.GAME_MULTIPLAYER);
        	clearAIDifficultyCheckboxes();
    		easyOption.setState(true);
        }
        
        else if (event.getSource () == simpleMultiOption)
        {
        	switchToGame();
        	gamePanel.newGame (Game.GAME_MULTIPLAYER);
        	clearAIDifficultyCheckboxes();
    		easyOption.setState(true);
        }
        
        //When save is selected, store the game state to a text file
        else if (event.getSource () == saveOption)
        {
        	int result = fileChooser.showSaveDialog(this);
        	
        	//Saves the game to the selected file.
        	if (result == JFileChooser.APPROVE_OPTION) {
        		File file = fileChooser.getSelectedFile();
        		
        		//Tries to save the game.
        		try {
        			gamePanel.saveGameToFile(file);
        		}
        		//Displays an error message if an error occurs.
        		catch (Exception e) {
        			JOptionPane.showMessageDialog (this,
                            "Error while saving game",
                            "Knightingale",
                            JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		
        		//Display a message when the save is successful.
        		JOptionPane.showMessageDialog (this,
                        "Game successfully saved",
                        "Knightingale",
                        JOptionPane.INFORMATION_MESSAGE);
        	}
        }
        
        //When load is selected, load a game state from a text file
        else if (event.getSource () == loadOption)
        {
        	int result = fileChooser.showOpenDialog(this);
        	
        	//Loads the game from the selected file.
        	if (result == JFileChooser.APPROVE_OPTION) {
        		File file = fileChooser.getSelectedFile();
        		
        		//Tries to load the game.
        		try {
        			gamePanel.loadGameFromFile(file);
        		}
        		//Displays an error message if an error occurs.
        		catch (Exception e) {        		
        			e.printStackTrace();
        			JOptionPane.showMessageDialog (this,
                            "Corrupt save file",
                            "Knightingale",
                            JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		
        		//Displays a message when the load is successful.
        		JOptionPane.showMessageDialog (this,
                        "Game successfully loaded",
                        "Knightingale",
                        JOptionPane.INFORMATION_MESSAGE);
        		
        		//Clear the AI difficulty check boxes.
        		clearAIDifficultyCheckboxes();
        		
        		//Set the AI to easy.
        		easyOption.setState(true);   		
        	}
        }
        
        //When main menu is selected, exit to the main menu
        else if (event.getSource () == mainMenuOption)
        {
        	switchToMenu();
        }
        
        //Undo the last move if undo is selected.
        else if (event.getSource () == undoOption)
        {
        	//Checks if an undo is possible, and displays a message when it is not.
        	boolean undoSucceeded = gamePanel.undoLastMove();
        	if (!undoSucceeded) {
        		JOptionPane.showMessageDialog (this,
                        "There is nothing to undo!",
                        "Knightingale",
                        JOptionPane.INFORMATION_MESSAGE);
        	}
        }
        
        //Assists the player in making a move when help me make a move is selected.
        else if (event.getSource () == makeAssistMoveOption)
        {
        	gamePanel.makeAssistantMove();
        }
        
        //When switch sides is selected, swap the player's side with the CPU
        else if (event.getSource () == switchOption)
        {
        	gamePanel.switchSides();
        }
        
        //Sets the AI to easy when easy is selected.
        else if (event.getSource () == easyOption)
        {
        	clearAIDifficultyCheckboxes();
        	easyOption.setState(true);
        	gamePanel.setAiDifficulty(Game.AI_EASY);
        }
        
        //Sets the AI to medium when medium is selected.
        else if (event.getSource () == mediumOption)
        {
        	clearAIDifficultyCheckboxes();
        	mediumOption.setState(true);
        	gamePanel.setAiDifficulty(Game.AI_MEDIUM);
        }
        
        //Sets the AI to hard when hard is selected.
        else if (event.getSource () == hardOption)
        {
        	clearAIDifficultyCheckboxes();
        	hardOption.setState(true);
        	gamePanel.setAiDifficulty(Game.AI_HARD);
        }
        
        //Sets the AI to very hard when very hard is selected.
        else if (event.getSource () == veryHardOption)
        {
        	clearAIDifficultyCheckboxes();
        	veryHardOption.setState(true);
        	gamePanel.setAiDifficulty(Game.AI_VERYHARD);
        }
        
        //Sets a custom difficulty when custom difficulty is selected.
        else if (event.getSource () == customDifficultyOption)
        {
        	//Asks the player for an input.
        	String input = JOptionPane.showInputDialog(this, "Set how many moves you would like the AI to look ahead."
        			+ "\n(The hard AI has a search depth of 5)", "Set AI Difficulty", 1);
        	//Sets the depth to the new custom input.
        	if (input != null) {
        		int depth;
        		try {
        			depth = Integer.parseInt(input);
        		} 
        		//Checks if the input is valid.
        		catch (Exception e) {
        			return;
        		}
        		//If the player sets a difficulty between 4 and 9, add 10 to the difficulty.
        		if (depth >= 4 && depth <= 9) {
        			clearAIDifficultyCheckboxes();
        			customDifficultyOption.setState(true);
        			gamePanel.setAiDifficulty(10 + depth);
        			JOptionPane.showMessageDialog (this,
                            "New AI difficulty set!",
                            "Knightingale",
                            JOptionPane.INFORMATION_MESSAGE);
        		}
        		//Asks for another input if the input is not between 4 and 9.
        		else {
        			JOptionPane.showMessageDialog (this,
                            "Please enter a value between 4 and 9.",
                            "Knightingale",
                            JOptionPane.INFORMATION_MESSAGE);
        		}
        	}
        }
        
        //When exit is selected, close the frame
        else if (event.getSource () == exitOption || event.getSource () == simpleExitOption)
        {
            System.exit (0);
        }
        
        //When instructions is selected, display the game concept and goals
        else if (event.getSource () == instructionMenuItem)
        {
            JOptionPane.showMessageDialog (this,
                    "",
                    "Instructions",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
        //When about is selected, display information about the
        //game creators and date
        else if (event.getSource () == aboutMenuItem)
        {
            JOptionPane.showMessageDialog (this, "Knightingale v1.0\n\n" +
                    		"Written by:\n" +
                    		"          Jeffrey Yang\n" +
                    		"          Charley Huang\n" +
                    		"          Adrian Leung\n\n" +
                    "\u00a9 2013",
                    "About Knightingale",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
        repaint();
	}
    
    /**
     * Program entry point for the GUI of Knightingale
     * @param args program arugments
     */
	public static void main(String[] args) {
		// Program entry point, code to create JFrame		
		MainGameFrame frame = new MainGameFrame ();
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.pack ();
        frame.setVisible (true);
        //Centre the frame on the screen
        frame.setLocationRelativeTo (null);
	}
	
	/**
	 * Repaints the side player panels of the game
	 */
	public void updateSidePanels ()
	{
		left.paintImmediately(0, 0, 200, 720);
		right.paintImmediately(0, 0, 200, 720);
	}
	
	/**
	 * Gets an image resource given the file name
	 * @param file the name of the file
	 * @return a reference to the Image resource
	 */
	public Image getImage (String file)
	{
		Image image;

		URL url = getClass().getResource(file);
		if (url != null)
			image = getToolkit().getImage(url);
		else
			image = new ImageIcon (file).getImage();

		return image;
	}
}

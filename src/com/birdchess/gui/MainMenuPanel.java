package com.birdchess.gui;

//Import the classes needed for a main menu
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.*;

import javax.swing.*;


/** The MainGamePanel class - creates the chess board for the game
 * Plays a chess game using this class as the main interface
 * @author Adrian Leung, Charley Huang, and Jeffrey Yang
 * @version January 21, 2013
 */ 
public class MainMenuPanel extends JPanel {

	//Set up all the images needed in the main menu.
	private Image background;
	private Image pawnHelp;
	private Image rookHelp;
	private Image knightHelp;
	private Image bishopHelp;
	private Image queenHelp;
	private Image kingHelp;
	private Image generalHelp;
	private Image credits;
	private Image startButton;
	private Image helpButton;
	private Image creditsButton;

	//A boolean to check if the credits screen is desired.
	private boolean creditsClicked;

	//The frame used to store the menu panel.
	private MainGameFrame mainFrame;
	private int helpScreen;

	/**
	 * Constructs a new menu panel given its parent frame
	 * @param mainFrame a reference to the parent frame
	 */
	public MainMenuPanel(MainGameFrame mainFrame)
	{
		//Sets the frame used for the main menu.
		this.mainFrame = mainFrame;

		//Set a size for the main menu.
		setPreferredSize (new Dimension (1120,720));

		//Adds a mouse listener for mouse interaction.
		this.addMouseListener (new MouseHandler ());

		//Set variables needed to take care of different events.
		creditsClicked = false;
		helpScreen = 0;

		// Load all images
		background = mainFrame.getImage("0-MainMenuBack-psd.png");
		pawnHelp = mainFrame.getImage("pawnhelp.png");
		rookHelp = mainFrame.getImage("rookhelp.png");
		knightHelp = mainFrame.getImage("knighthelp.png");
		bishopHelp = mainFrame.getImage("bishophelp.png");
		queenHelp = mainFrame.getImage("queenhelp.png");
		kingHelp = mainFrame.getImage("kinghelp.png");
		generalHelp = mainFrame.getImage("generalhelp.png");
		credits = mainFrame.getImage("credits.png");
		startButton = mainFrame.getImage("startbutton.png");
		helpButton = mainFrame.getImage("helpbutton.png");
		creditsButton = mainFrame.getImage("creditsbutton.png");
	}

	/**
	 * Paints the menu panel onto the screen
	 * @param g the graphics context to draw onto
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);

		//Set up the needed fonts.
		Font large = new Font ("SansSerif", Font.BOLD, 48);
		Font bold = new Font ("SansSerif", Font.BOLD, 12);

		//Draw the background image.
		g.drawImage (background,0,0,this);

		//Draw the title.
		g.setColor(Color.BLACK);
		g.setFont (large);
		g.drawString ("Knightingale", 450, 75);

		//Draw the three necessary buttons.
		g.drawImage(startButton , 450,125,this);
		g.drawImage(helpButton,450,290,this);
		g.drawImage(creditsButton, 450,450,this);

		g.setFont (bold);

		//Draws the pawn help screen.
		if (helpScreen == 1)
		{
			g.drawImage (pawnHelp ,0,0,this);

			//Draw a button to go to the next help screen.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.setColor (Color.BLACK);
			g.drawString ("Next",1055,700);
			repaint();
		}

		//Draws the rook help screen.
		else if (helpScreen == 2)
		{
			g.drawImage (rookHelp ,0,0,this);

			//Draw a button to go to the next or previous help screen.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.fillRect (910,675,100,50);
			g.setColor (Color.BLACK);
			g.drawString ("Next",1055,700);
			g.drawString("Previous", 935, 700);
			repaint();
		}

		//Draws the knight help screen.
		else if (helpScreen == 3)
		{
			g.drawImage (knightHelp ,0,0,this);

			//Draw a button to go to the next or previous help screen.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.fillRect (910,675,100,50);
			g.setColor (Color.BLACK);
			g.drawString ("Next",1055,700);
			g.drawString("Previous", 935, 700);
			repaint();
		}

		//Draws the bishop help screen.
		else if (helpScreen == 4)
		{
			g.drawImage (bishopHelp ,0,0,this);

			//Draw a button to go to the next or previous help screen.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.fillRect (910,675,100,50);
			g.setColor (Color.BLACK);
			g.drawString ("Next",1055,700);
			g.drawString("Previous", 935, 700);
			repaint();
		}

		//Draws the queen help screen.
		else if (helpScreen == 5)
		{
			g.drawImage (queenHelp ,0,0,this);

			//Draw a button to go to the next or previous help screen.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.fillRect (910,675,100,50);
			g.setColor (Color.BLACK);
			g.drawString ("Next",1055,700);
			g.drawString("Previous", 935, 700);
			repaint();
		}

		//Draws the king help screen.
		else if (helpScreen == 6)
		{
			g.drawImage (kingHelp ,0,0,this);

			//Draw a button to go to the next or previous help screen.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.fillRect (910,675,100,50);
			g.setColor (Color.BLACK);
			g.drawString ("Next",1055,700);
			g.drawString("Previous", 935, 700);
			repaint();
		}

		//Draws the final general help screen.
		else if (helpScreen == 7)
		{
			g.drawImage (generalHelp ,0,0,this);

			//Draw a button to go to the previous help screen or main menu.
			g.setColor (Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.fillRect (910,675,100,50);
			g.setColor (Color.BLACK);
			g.drawString ("Main Menu",1035,700);
			g.drawString("Previous", 935, 700);
			repaint();
		}

		//Draws the credits.
		if (creditsClicked == true)
		{
			g.drawImage (credits ,0,0,this);

			//Draw a button to go to the main menu.
			g.setColor(Color.CYAN);
			g.fillRect(1020,675, 100, 50);
			g.setColor (Color.BLACK);
			g.drawString ("Main Menu",1035,700);
			repaint();
		}
	}

	/**
	 * Handles mouse clicks on the panel
	 * @author Adrian Leung, Charley Huang, and Jeffrey Yang
	 * @version January 21, 2013
	 */ 
	private class MouseHandler extends MouseAdapter
	{
		/**
		 * Responds to a mousePressed event
		 * @param event Information about the mouse pressed event.
		 */
		public void mousePressed (MouseEvent event)
		{
			//Set the coordinates of the mouse click.
			int xPos = event.getX ();
			int yPos = event.getY ();

			//Start a new game when the start game button is pressed.
			if(xPos > 450 && xPos<750 && yPos>100 && yPos<250 && helpScreen == 0)
			{
				mainFrame.switchToGame();	    	
			}

			//Draws the help screen when help is selected.
			else if (xPos > 450 && xPos < 750 && yPos >275 && yPos < 425 && creditsClicked == false)
			{
				//Sets the help screen value to draw the first help screen.
				if (helpScreen == 0)
				{
					helpScreen = 1;
					repaint();
				}

			}

			//Advance to the next help screen when the next button is pressed.
			if (helpScreen >= 1 && helpScreen <=6 && xPos > 1020 && xPos < 1120 && yPos > 675 && yPos < 775)
			{
				helpScreen ++;
				repaint();
			}

			//Return to the previous help screen when previous is pressed.
			else if (helpScreen > 1 && xPos >910 && xPos < 1010 && yPos > 675 && yPos < 775)
			{
				helpScreen--;
				repaint();
			}

			//Returns to the main menu when the main menu button is pressed on the final help screen.
			else if (helpScreen == 7 && xPos > 1020 && xPos < 1120 && yPos > 675 && yPos < 775)
			{
				helpScreen = 0;
				repaint();
			}

			//Sets the credits value to display the credits screen.
			if (xPos > 450 && xPos < 750 && yPos > 450 && yPos < 600 && helpScreen == 0)
			{
				creditsClicked = true;
				repaint();
			}

			//Returns to the main menu when the main menu button is pressed on the credits screen.
			if (creditsClicked == true && xPos > 1020 && xPos < 1120&& yPos > 675 && yPos < 775)
			{
				creditsClicked = false;
				repaint();
			}

		}
	} 

}

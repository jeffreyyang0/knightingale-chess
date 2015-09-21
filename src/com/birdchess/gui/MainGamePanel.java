package com.birdchess.gui;

import javax.swing.*;

import java.awt.event.*;

import com.birdchess.ai.board.Board;
import com.birdchess.ai.board.Piece;
import com.birdchess.common.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;

/** The MainGamePanel class - creates the chess board for the game
 * Plays a chess game using this class as the main interface
 * @author Adrian Leung, Charley Huang, and Jeffrey Yang
 * @version January 21, 2013
 */ 
public class MainGamePanel extends JPanel {
	// A JPanel for the chess board

	//Set the variables for the square size and the screen size.
	private final int NO_OF_ROWS = 8;
	private final int SQUARE_SIZE = 90;
	public final Dimension SCREEN_SIZE = new Dimension ((NO_OF_ROWS)*SQUARE_SIZE,(NO_OF_ROWS*SQUARE_SIZE));

	//Create a new game object.
	private Game game;

	//Create the variables needed for the mouse interaction.
	private int selectedRow;
	private int selectedColumn;
	private Piece selectedPiece;
	private Piece highlightedPiece;
	private Point mousePos;

	//A list of all the legal moves for a piece.
	private ArrayList<Move> legalMoves;

	//Variables used to animate a piece.
	private Point animatingPiecePos;
	private Piece animatingPiece;

	//The main frame
	private MainGameFrame parentWindow;

	//The winner
	private Color winner;

	//The current game mode.
	private int gameMode;

	//All the necessary images in the game board.
	private final Image blackRook;
	private final Image blackBishop;
	private final Image blackQueen;
	private final Image blackKing;
	private final Image blackKnight;
	private final Image blackPawn;
	private final Image whiteRook;
	private final Image whiteBishop;
	private final Image whiteQueen;
	private final Image whiteKing;
	private final Image whiteKnight;
	private final Image whitePawn;
	private final Image grayTile;
	private final Image beigeTile;
	private final Image target;
	private final Image background;

	/**
	 * A constructor to create the game board.
	 * @param parent the main frame
	 * @param gameMode the current game mode
	 */
	public MainGamePanel(MainGameFrame parent, int gameMode)
	{
		//Add the mouse listeners and set the size of the board.
		setPreferredSize(SCREEN_SIZE);
		this.addMouseListener (new MouseHandler ());
		this.addMouseMotionListener (new MouseMotionHandler ());

		//Initialize the variables.
		parentWindow = parent;
		newGame (gameMode);

		blackRook = parentWindow.getImage("rookblack.gif");
		blackBishop = parentWindow.getImage("orangebishopbirdblack.gif");
		blackQueen = parentWindow.getImage("queenblack.gif");
		blackKing = parentWindow.getImage("kingblack.gif");
		blackKnight = parentWindow.getImage("knightblack.gif");
		blackPawn = parentWindow.getImage("pawnblack.gif");
		whiteRook = parentWindow.getImage("rookwhite.gif");
		whiteBishop = parentWindow.getImage("orangebishopbirdwhite.gif");
		whiteQueen = parentWindow.getImage("queenwhite.gif");
		whiteKing = parentWindow.getImage("kingwhite.gif");
		whiteKnight = parentWindow.getImage("knightwhite.gif");
		whitePawn = parentWindow.getImage("pawnwhite.gif");
		grayTile = parentWindow.getImage("GrayTile.png");
		beigeTile = parentWindow.getImage("BeigeTile.png");
		target = parentWindow.getImage("Target.png");
		background = parentWindow.getImage("Background Tiling.png");
	}

	/**
	 * Creates a new game in the desired game mode
	 * @param gameMode the desired game mode.
	 */
	public void newGame(int gameMode)
	{
		//Starts a new game with an easy ai.
		game = new Game(gameMode, Game.AI_EASY);

		//Initialize the game variables.
		this.gameMode = gameMode;
		winner = null;
		repaint();
	}

	/**
	 * Loads a saved game from a file
	 * @param file the saved game to load
	 * @throws IOException when the save file can not be found.
	 */
	public void loadGameFromFile (File file) throws IOException
	{
		game.loadGameFromFile(file);
		this.gameMode = game.getGameMode();
		winner = game.getWinner();
		repaint();
	}

	/**
	 * Saves the current game to a file
	 * @param file the file to save the game to.
	 * @throws IOException when the save file can not be found.
	 */
	public void saveGameToFile (File file) throws IOException
	{
		game.saveGameToFile(file);
	}	

	/**
	 * Undoes the last move
	 * @return if the undo was successful
	 */
	public boolean undoLastMove ()
	{
		boolean result = game.undoLastMove();
		repaint();
		return result;
	}

	/**
	 * Sets the difficulty of the computer player
	 * @param difficulty the difficulty the AI will be set to
	 */
	public void setAiDifficulty (int difficulty)
	{
		game.setAiDifficulty(difficulty);
	}

	/**
	 * Makes a move for the current human player
	 */
	public void makeAssistantMove ()
	{
		//If there is no winner
		if (winner == null)
		{
			Player playerToMove = game.getPlayer(game.getLastMoved() == Color.WHITE ? Color.BLACK : Color.WHITE);
			Player otherPlayer = game.getPlayer(game.getLastMoved());

			//Gets the best move.
			Move assistMove = game.getAssistantMove();

			playerToMove.endTimedMove();

			//Animates the move.
			animatePiece (assistMove.source, assistMove.target);

			//Moves the piece to the target spot.
			game.makeMove(assistMove);

			playerToMove.incrementMovesMade();
			repaint();
			parentWindow.updateSidePanels();

			//Checks if it is possible to win.
			processPotentialWin();

			//If the game is single player.
			if (gameMode == Game.GAME_SINGLEPLAYER) 
			{
				otherPlayer.startTimedMove();

				//Moves the computer's piece after the player's piece is moved.
				Move nextCpuMove = game.getComputerPlayer().getNextMove(game.getGameState());

				otherPlayer.endTimedMove();

				//Animates the computer's piece.
				animatePiece (nextCpuMove.source, nextCpuMove.target);

				//Makes the move.
				game.makeMove(nextCpuMove);

				otherPlayer.incrementMovesMade();
				playerToMove.startTimedMove();

				repaint();
				parentWindow.updateSidePanels();

				//Checks if it is possible to win.
				processPotentialWin();
			} else {
				// Start the timing of the other player if it's a multiplayer game
				otherPlayer.startTimedMove();
			}
		}
	}

	/**
	 * Makes a player switch sides with the computer in the middle of a game
	 */
	public void switchSides ()
	{	
		//Checks if the player can switch sides
		boolean hasSwitched = game.switchSides();

		Player cpuToMove = game.getPlayer(game.getLastMoved());
		Player humanPlayer = game.getPlayer(game.getLastMoved() == Color.WHITE ? Color.BLACK : Color.WHITE);

		//Displays a message when a player has successfully switched sides.
		if (winner == null && hasSwitched) {
			JOptionPane.showMessageDialog (this,
					"Sides have been switched!\nYou are now " +
							(game.getComputerPlayer().getColor() == Color.WHITE
							? "Black" : "White") + ".",
							"Knightingale",
							JOptionPane.INFORMATION_MESSAGE);

			//Generates a move for the computer after switching sides.
			Move nextCpuMove = game.getComputerPlayer().getNextMove(game.getGameState());

			cpuToMove.endTimedMove();

			//Animates the piece.
			animatePiece (nextCpuMove.source, nextCpuMove.target);

			//Moves the piece.
			game.makeMove(nextCpuMove);

			cpuToMove.incrementMovesMade();
			humanPlayer.startTimedMove();

			repaint();
			parentWindow.updateSidePanels();

			//Checks if it is possible to win.
			processPotentialWin();
		} 
		else if (winner == null) {
			//Prevents side switching in multiplayer
			JOptionPane.showMessageDialog (this,
					"This feature is only applicable in singleplayer mode!",
					"Knightingale",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * The method used to draw all the images in the panel onto the screen.
	 * @param g the graphics context to draw content to
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);
		//Create fonts used for text in the game.
		Font large = new Font ("SansSerif", Font.BOLD, 16);

		//Creates a new board objects in order to draw the pieces.
		Board board = game.getGameState();

		//Draws the tiles of a chess board.
		g.drawImage (background, 0, 0, this);

		//Highlights the legal moves of the selected piece using a light pink border.
		if (selectedPiece != null)
		{
			for (Move move: legalMoves)
			{				
				g.drawImage (target, move.target.col *SQUARE_SIZE, move.target.row *SQUARE_SIZE, this);
			}
		}

		//Goes through the entire board, drawing pieces in their positions.
		for (int row = 0 ; row < NO_OF_ROWS ; row++)
		{
			for (int column = 0 ; column < NO_OF_ROWS ; column++)
			{

				//Retrieves the type of piece on the current square.
				Piece piece = board.getPiece(row,column);	

				// Find the x and y positions for each row and column
				int xPos = (column) * SQUARE_SIZE;
				int yPos = (row) * SQUARE_SIZE;

				//The code to animate each moving piece
				if (piece!= null)
				{

					//Set the original x and y co-ordinates
					int originalX = xPos;
					int originalY = yPos;

					//Checks if a piece is being animated.
					if (piece == animatingPiece)
					{
						//Move the animated piece by small increments.
						xPos = animatingPiecePos.x;
						yPos = animatingPiecePos.y;
					}

					//Set the variables needed to prevent calling a method multiple times.
					String type = piece.typeOfPiece();
					Color color = piece.getColor();

					//Checks if the current piece is a pawn.
					if (type.equals ("Pawn"))
					{
						//Draws a black pawn if the pawn is black.
						if (color.equals (Color.BLACK))
							g.drawImage (blackPawn, xPos, yPos, this);

						//Draws a white pawn if the pawn is white.
						else
							g.drawImage (whitePawn, xPos, yPos, this);
					}

					//Checks if the current piece is a rook.
					else if (type.equals ("Rook"))
					{
						//Draws a black rook if the rook is black.
						if (color.equals (Color.BLACK))
							g.drawImage (blackRook, xPos, yPos, this);
						//Draws a white rook if the rook is white.
						else
							g.drawImage (whiteRook, xPos, yPos, this);
					}

					//Checks if the current piece is a knight.
					else if (type.equals ("Knight"))
					{
						//Draws a black knight if the knight is black.
						if (color.equals (Color.BLACK))
							g.drawImage (blackKnight, xPos, yPos, this);

						//Draws a white knight if the knight is white.
						else
							g.drawImage (whiteKnight, xPos, yPos, this);
					}

					//Checks if the current piece is a bishop.
					else if (type.equals ("Bishop"))
					{
						//Draws a black bishop if the bishop is black.
						if (color.equals (Color.BLACK))
							g.drawImage (blackBishop, xPos, yPos, this);

						//Draws a white bishop if the bishop is white.
						else
							g.drawImage (whiteBishop, xPos, yPos, this);

					}

					//Checks if the current piece is a queen.
					else if (type.equals ("Queen"))
					{
						//Draws a black queen if the queen is black.
						if (color.equals (Color.BLACK))
							g.drawImage (blackQueen, xPos, yPos, this);

						//Draws a white queen if the queen is white.
						else
							g.drawImage (whiteQueen, xPos, yPos, this);
					}

					//Checks if the current piece is a king.
					else if (type.equals ("King"))
					{
						//Draws a black king if the king is black.
						if (color.equals (Color.BLACK))
							g.drawImage (blackKing, xPos, yPos, this);

						//Draws a white king if the king is black.
						else
							g.drawImage (whiteKing, xPos, yPos, this);


					}

					//Resets the x and y position.
					xPos = originalX;
					yPos = originalY;
				}
			}
		}

		//Code to display the type of piece that the mouse is over.
		if (highlightedPiece != null)
		{
			//Set the offsets for the help box.
			int xOffset = 5;
			int yOffset = 5;

			//Makes sure that the help box does not show off of the game board, both horizontally and vertically.
			if (mousePos.x / SQUARE_SIZE >= 6) {
				xOffset = -150;
			}
			if (mousePos.y / SQUARE_SIZE >= 6) {
				yOffset = -30;
			}

			//Create a new font for the help boxes
			Font overlay = new Font ("Tahoma", Font.BOLD, 16);
			g.setFont (overlay);

			//Create new colors for the help box.
			Color beige = new Color(250, 250, 210, 200);
			Color transBlack = new Color (0,0,0,150);

			//Draws the help box for black pieces.
			if (highlightedPiece.getColor() == Color.BLACK)
			{
				g.setColor(transBlack);
				g.fillRect(mousePos.x + xOffset-5, mousePos.y + yOffset-5, 150, 30);
				g.setColor(Color.WHITE);
				g.drawString("Black " + highlightedPiece.typeOfPiece(), mousePos.x+20+xOffset, mousePos.y+15+yOffset);
			}

			//Draws the help box for white pieces.
			else
			{
				g.setColor(beige);
				g.fillRect(mousePos.x + xOffset-5, mousePos.y + yOffset-5, 150, 30);
				g.setColor (Color.BLACK);
				g.drawString("White " + highlightedPiece.typeOfPiece(), mousePos.x+20+xOffset, mousePos.y+15+yOffset);
			}

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
		 * @param event Information about the mouse pressed event
		 */
		public void mousePressed (MouseEvent event)
		{
			if (winner != null)
				return;

			Board board = game.getGameState();         
			// Convert mouse-pressed location to board row and column 
			Point pressedOnPoint = event.getPoint();
			int pressedOnColumn = event.getX () / SQUARE_SIZE;
			int pressedOnRow = event.getY () / SQUARE_SIZE;

			// Check if the selected square is empty or has a player piece
			if (selectedPiece == null && board.getPiece(pressedOnRow,pressedOnColumn) != null)
			{
				// A piece on the board was selected
				selectedRow = pressedOnRow;
				selectedColumn = pressedOnColumn;                
				selectedPiece = board.getPiece(selectedRow,selectedColumn); 
				legalMoves = selectedPiece.generateLegalMoves(board, new Position (selectedRow,selectedColumn));
			}
			else  
			{
				// User pressed mouse on an empty square of the board.
				// Move a selected piece (if any) to that empty square. 
				if (selectedPiece != null)
				{
					Move move = new Move(new Position(selectedRow,selectedColumn),new Position(pressedOnRow,pressedOnColumn), null);
					int canMakeMove = game.canMakeMove(move);
					switch (canMakeMove) {
					//Checks if the move is legal, and animate it.
					case Game.E_SUCCESS:
						Player playerMoved = game.getPlayer(board.getPiece(move.source).getColor());
						Player otherPlayer = game.getPlayer(board.getPiece(move.source).getColor() == Color.WHITE
								? Color.BLACK : Color.WHITE);

						playerMoved.endTimedMove();
						playerMoved.incrementMovesMade();

						animatePiece (move.source, move.target);
						game.makeMove(move);

						parentWindow.updateSidePanels();
						paintImmediately(0, 0, 720, 720);

						//Check if the game has ended
						boolean hasGameJustEnded = processPotentialWin();

						//Allow the computer to move if the game has not ended.
						if (gameMode == Game.GAME_SINGLEPLAYER && !hasGameJustEnded) {
							otherPlayer.startTimedMove();

							playerMoved.setStatus("");
							otherPlayer.setStatus(" Thinking...");

							parentWindow.updateSidePanels();

							Move cpuMove = game.getComputerPlayer().getNextMove(board);

							otherPlayer.endTimedMove();

							otherPlayer.incrementMovesMade();

							animatePiece (cpuMove.source, cpuMove.target);
							game.makeMove(cpuMove);
							parentWindow.repaint();

							playerMoved.startTimedMove();

							//Checks if the game has ended
							processPotentialWin();
						} else {
							otherPlayer.startTimedMove();
						}

						break;

						//Checks if you will be moving into a check.
					case Game.E_MOVE_INTO_CHECK:
						JOptionPane.showMessageDialog (parentWindow,
								"You cannot move into check!",
								"Illegal Move",
								JOptionPane.INFORMATION_MESSAGE);
						break;

						//Checks if it is your turn before you move.
					case Game.E_WRONG_TURN:
						JOptionPane.showMessageDialog (parentWindow,
								"It's the other player's turn to move!",
								"Illegal Move",
								JOptionPane.INFORMATION_MESSAGE);
						break;

						//Checks if the move is possible.
					case Game.E_BAD_MOVE:
						break;
					default:
						break;
					}

					setCursor(Cursor.getDefaultCursor ());  // set mouse cursor back to its normal image
					selectedPiece = null;
				}
			}
			repaint();
		}
	}

	/**
	 * A method to check if the game is in stalemate, or is still in progress
	 * @return a boolean to show whether the game has ended
	 */
	private boolean processPotentialWin ()
	{
		//Checks if the game has a winner.
		winner = game.getWinner();
		if (winner == null)
			return false;

		//Checks for a stale mate.
		if (winner == Game.STATE_STALEMATE) {
			//Displays a game over message.
			JOptionPane.showMessageDialog (parentWindow,
					"Stalemate!",
					"Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		//Checks if the game has surpassed 50 moves without captures or pawns moving.
		else if (winner == Game.STATE_FIFTY_MOVE_DRAW) {
			//Displays a game over message.
			JOptionPane.showMessageDialog (parentWindow,
					"Draw game: No captures have been made and no pawns have been moved in the last fifty moves!",
					"Game Over", JOptionPane.INFORMATION_MESSAGE);			

		} 
		//Checks if the same piece positioning has occurred three turns in a row.
		else if (winner == Game.STATE_THREEFOLD_REP_DRAW) {
			//Displays a game over message.
			JOptionPane.showMessageDialog (parentWindow,
					"Draw game: The same piece positioning has occurred three times consecutively.",
					"Game Over",  JOptionPane.INFORMATION_MESSAGE);
		}
		//Checks if there are enough pieces on the board.
		else if (winner == Game.STATE_INSUFFICIENT_MATERIAL) {
			//Displays a game over message.
			JOptionPane.showMessageDialog (parentWindow,
					"Draw game: There are not enough pieces on the board for either side to win!",
					"Game Over",  JOptionPane.INFORMATION_MESSAGE);
		} 
		// A checkmate has occurred.
		else {
			//Displays the winner.
			String side = winner == Color.WHITE ? "White" : "Black";
			JOptionPane.showMessageDialog (parentWindow,
					"Checkmate by " + side + "!",
					"Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		return true;
	}

	/**
	 * Handles mouse movement events
	 * @author Adrian Leung, Charley Huang, and Jeffrey Yang
	 * @version January 21, 2013
	 */ 
	private class MouseMotionHandler extends MouseMotionAdapter
	{
		/**
		 * Changes the mouse cursor to a hand, if a piece has been selected to move, and the mouse is over a valid (ie., empty) square.
		 *@param event information about the mouse released event
		 */    
		public void mouseMoved (MouseEvent event)
		{     
			Board board = game.getGameState();    

			// Convert mouse-pressed position to board row and column
			mousePos = event.getPoint();   
			int column = mousePos.x / SQUARE_SIZE;
			int row = mousePos.y / SQUARE_SIZE;
			highlightedPiece = board.getPiece(row,column);

			if (selectedPiece !=null) 
			{ 

				// If mouse is over an empty square on the board, then the piece can be moved there, 
				// so set the mouse cursor to a hand                 
				if (highlightedPiece == null) 
					setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
				else
					setCursor(Cursor.getDefaultCursor()); 
			}

			if (mousePos.x < 5 || mousePos.x >= 710 || mousePos.y < 5
					|| mousePos.y >= 710)
				highlightedPiece = null;

			repaint();
		}

	}

	/**
	 * A method to animate the pieces as they move.
	 * @param start the starting position of the piece.
	 * @param finish the ending position of the piece. 
	 */
	public void animatePiece (Position start, Position finish)
	{
		//Sets the piece being animated.
		animatingPiece = game.getGameState().getPiece (start);

		//Sets the x and y coordinates of the start and finish positions of the piece.
		double x = start.col * SQUARE_SIZE;
		double y = start.row * SQUARE_SIZE;
		double xAdd = (finish.col * SQUARE_SIZE - x) / 40.0;
		double yAdd = (finish.row * SQUARE_SIZE - y) / 40.0;

		//Sets a point for the piece being animated.
		animatingPiecePos = new Point();

		//Continues to animate the piece while it is not at its final position.
		while (Math.abs(x - finish.col * SQUARE_SIZE) > 0.1 || Math.abs(y - finish.row * SQUARE_SIZE) > 0.1)
		{
			//Draw the piece as it moves to the final position.
			x += xAdd;
			y += yAdd;
			animatingPiecePos.x = (int) x;
			animatingPiecePos.y = (int) y;
			paintImmediately(0, 0, 720, 720);

		}
		//Resets the animated piece.
		animatingPiece = null;
	}
}



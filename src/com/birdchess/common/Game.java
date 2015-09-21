package com.birdchess.common;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.birdchess.ai.board.Bishop;
import com.birdchess.ai.board.Board;
import com.birdchess.ai.board.King;
import com.birdchess.ai.board.Knight;
import com.birdchess.ai.board.Pawn;
import com.birdchess.ai.board.Piece;
import com.birdchess.ai.board.Queen;
import com.birdchess.ai.board.Rook;
import com.birdchess.ai.core.CpuPlayer;

/**
 * Represents a chess game, containing all relevant information about the board
 * and the players
 * @author Jeffrey Yang and Charley Huang
 * @version January 2013
 */
public class Game {
	// A static variable holding the most recent instance of this object,
	// so that other objects could have more efficient access
	public static Game currentGame;
	
	// References to objects representing the players
	private Player white;
	private Player black;
	private Player computerPlayer;
	
	// Colour of the player that most recently moved
	private Color lastMoved;
	
	private Board board;
	
	// The type of this game (singleplayer or multiplayer)
	private int gameType;
	
	// The most recent move that was inspected for if it was valid
	private Move lastMoveChecked;
	
	// The colour of the winner if it exists; also used to represent the state of the game
	// ie if it's a stalemate
	private Color winner;
	
	// Information about the computer player
	private Color cpuColor;
	private int cpuDepth;
	
	// A reference to a computer player instance that can help the player make a move at
	// any time
	private CpuPlayer playerAssistant;
	
	// A list of previous boards, used for the undo feature
	private LinkedList<Board> pastBoards;
	
	// Error codes for inspected moves
	public static final int E_SUCCESS = 0;
	public static final int E_MOVE_INTO_CHECK = 1;
	public static final int E_WRONG_TURN = 2;
	public static final int E_BAD_MOVE = 3;
	
	// Constants for the type of the game
	public static final int GAME_SINGLEPLAYER = 0;
	public static final int GAME_MULTIPLAYER = 1;
	
	// Constants for levels of the AI
	public static final int AI_EASY = 4;
	public static final int AI_MEDIUM = 5;
	public static final int AI_HARD = 6;
	public static final int AI_VERYHARD = 7;
	
	// Constants for the state of the game
	public static final Color STATE_STALEMATE = Color.ORANGE;
	public static final Color STATE_THREEFOLD_REP_DRAW = Color.BLUE;
	public static final Color STATE_FIFTY_MOVE_DRAW = Color.YELLOW;
	public static final Color STATE_INSUFFICIENT_MATERIAL = Color.GREEN;
	
	/**
	 * Constructs a new game object given its mode and the depth of the AI
	 * @param gameMode the game mode (singleplayer or multiplayer)
	 * @param depth the depth of the computer player and computer assistant
	 */
	public Game (int gameMode, int depth)
	{
		// Create the human player
		white = new Player (Color.WHITE);
		
		// Initialize data fields
		board = new Board ();
		lastMoved = null;
		winner = null;
		currentGame = this;
		cpuDepth = depth;
		gameType = gameMode;
		pastBoards = new LinkedList<Board>();
		
		playerAssistant = new CpuPlayer(null, depth);
		
		// Create the computer or other human player
		if (gameMode == GAME_SINGLEPLAYER) {
			black = computerPlayer = new CpuPlayer(Color.BLACK, depth);
			lastMoved = cpuColor = Color.BLACK;
		} else {
			lastMoved = Color.BLACK;
			black = new Player (Color.BLACK);
			computerPlayer = null;
			cpuColor = null;
		}
		
		// Update the side panels in the game
		updatePieceCounts();
		updateStatusMessages();
		
		// Begin timing how long it takes for white to make its move
		white.startTimedMove();
	}
	
	/**
	 * Load the game state from a saved file
	 * @param input the reference to the file to load
	 * @throws IOException if the file is corrupt
	 */
	public void loadGameFromFile (File input) throws IOException
	{
		// Use the returned Scanner to read the last part of the file
		// after the board object has used it to read in information
		// about piece positioning
		Scanner file = board.loadBoardFromFile(input);
		
		// Read general information about the game
		gameType = file.nextInt();
		lastMoved = letterToColor(file.next());
		winner = letterToColor(file.next());
		cpuColor = letterToColor(file.next());
		cpuDepth = file.nextInt();
		pastBoards.clear();
		
		// Reset the AI setting to easy (default)
		playerAssistant.setDepth(AI_EASY);
		
		if (gameType == GAME_SINGLEPLAYER) {
			if (cpuColor == Color.WHITE) {
				white = computerPlayer = new CpuPlayer(cpuColor, AI_EASY);
				black = new Player (Color.BLACK);
				black.startTimedMove();
			} else {
				black = computerPlayer = new CpuPlayer(cpuColor, AI_EASY);
				white = new Player (Color.WHITE);
				white.startTimedMove();
			}
		} else {
			computerPlayer = null;
			white = new Player (Color.WHITE);
			black = new Player (Color.BLACK);
			
			getPlayer(lastMoved == Color.WHITE ? Color.BLACK : Color.WHITE).startTimedMove();
		}

		file.close();
		
		// Update the displays on the side panels
		updatePieceCounts ();
		updateStatusMessages ();
	}
	
	/**
	 * Updates the piece counters for each type of piece for each player for the side
	 * panels to display
	 */
	private void updatePieceCounts ()
	{
		// Reset counters to zero
		white.resetPieceCount();
		black.resetPieceCount();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// For every board position, if it contains a piece, increment the counter
				// for that type of piece for the owner by 1
				Piece piece = board.getPiece(i, j);
				if (piece != null) {
					getPlayer(piece.getColor()).incrementPieceCount(piece.getPieceID());
				}
			}
		}
	}
	
	/**
	 * Updates the status messages displayed in the side panels of the GUI
	 */
	private void updateStatusMessages ()
	{
		if (winner == null) {
			// If no one has won, display whose turn it is
			if (lastMoved == Color.WHITE) {
				white.setStatus("");
				black.setStatus("  Your Turn");
			} else {
				white.setStatus("  Your Turn");
				black.setStatus("");
			}
		} else if (winner == Color.WHITE) {
			// Display checkmates accordingly
			white.setStatus("Checkmate!");
			black.setStatus("");
		} else if (winner == Color.BLACK) {
			white.setStatus("");
			black.setStatus("Checkmate!");
		} else if (winner == Game.STATE_STALEMATE) {
			// Display other game states accordingly
			white.setStatus("Stalemate");
			black.setStatus("Stalemate");
		} else {
			white.setStatus("Draw Game");
			black.setStatus("Draw Game");
		}
	}
	
	/**
	 * Sets the difficulty of the AI for the computer player and computer assistant
	 * @param difficulty the new level of difficulty for the AI
	 */
	public void setAiDifficulty (int difficulty)
	{
		// Set the difficulty according to the presets
		if (difficulty == Game.AI_EASY) {
			playerAssistant.setDepth(4);
			playerAssistant.setQuiescentDepth(1);
			if (computerPlayer != null) {
				getComputerPlayer().setDepth(4);
				getComputerPlayer().setQuiescentDepth(1);
			}
		} else if (difficulty == Game.AI_MEDIUM) {
			playerAssistant.setDepth(4);
			playerAssistant.setQuiescentDepth(3);
			if (computerPlayer != null) {
				getComputerPlayer().setDepth(4);
				getComputerPlayer().setQuiescentDepth(3);
			}
		} else if (difficulty == Game.AI_HARD) {
			playerAssistant.setDepth(5);
			playerAssistant.setQuiescentDepth(3);
			if (computerPlayer != null) {
				getComputerPlayer().setDepth(5);
				getComputerPlayer().setQuiescentDepth(3);
			}
		} else if (difficulty == Game.AI_VERYHARD) {
			playerAssistant.setDepth(5);
			playerAssistant.setQuiescentDepth(5);
			if (computerPlayer != null) {
				getComputerPlayer().setDepth(5);
				getComputerPlayer().setQuiescentDepth(5);
			}
		} else if (difficulty >= 14 && difficulty <= 19) {
			// For user-entered levels 14 to 19, set normal depth to the appropriate value
			// between 4 and 9, and disable quiescent searching
			difficulty = difficulty % 10;
			playerAssistant.setDepth(difficulty);
			playerAssistant.setQuiescentDepth(1);
			if (computerPlayer != null) {
				getComputerPlayer().setDepth(difficulty);
				getComputerPlayer().setQuiescentDepth(1);
			}
		}
	}
	
	/**
	 * Switches the colours of the human and the computer player in a singleplayer game
	 * @return true if the switch has taken place internally, false otherwise
	 */
	public boolean switchSides ()
	{
		// Allow the switch only if the game is in singleplayer mode
		if (gameType == GAME_SINGLEPLAYER) {
			// Update the colour of the computer player and set appropriate values
			if (cpuColor == Color.WHITE) {
				cpuColor = lastMoved = Color.BLACK;
				black.setIsHuman(false);
				white.setIsHuman(true);
				black.setUpgradePiece(new Queen(Color.BLACK));
			} else {
				cpuColor = lastMoved = Color.WHITE;
				black.setIsHuman(true);
				white.setIsHuman(false);
				white.setUpgradePiece(new Queen(Color.WHITE));
			}

			getComputerPlayer().setColor(cpuColor);
			
			// Moves prior to the switch cannot be undone
			pastBoards.clear();
			
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates the optimal move for the current player using the same engine as the computer
	 * player
	 * @return an object specifying the computed optimal move
	 */
	public Move getAssistantMove ()
	{
		// Update the colour of the AI
		if (lastMoved == null || lastMoved == Color.BLACK)
			playerAssistant.setColor(Color.WHITE);
		else
			playerAssistant.setColor(Color.BLACK);
		
		// Add the current board to the undo list
		pastBoards.add(new Board(board));
		
		// Return the computed best move
		return playerAssistant.getNextMove(board);
	}
	
	/**
	 * Gets a reference to the computer player
	 * @return a reference to the computer player, or null if it does not exist
	 */
	public CpuPlayer getComputerPlayer ()
	{
		return (CpuPlayer) computerPlayer;
	}
	
	/**
	 * Gets a reference to the player playing white
	 * @return the reference to the player playing white
	 */
	public Player getWhitePlayer ()
	{
		return white;
	}
	
	/**
	 * Gets a reference to the player playing black
	 * @return the reference to the player playing black
	 */
	public Player getBlackPlayer ()
	{
		return black;
	}
	
	/**
	 * Gets a reference to the player of the specified colour
	 * @param color the specified colour of the player object to get
	 * @return a reference to the player of the specified colour
	 */
	public Player getPlayer (Color color)
	{
		return color == Color.WHITE ? white : black;
	}
	
	/**
	 * Makes a move in the current game
	 * @param move the move to make
	 */
	public void makeMove (Move move)
	{
		// If a move was just checked for validity (for human players), use that version
		// of the move, because it contains potentially special information about how to move
		// the rook in the event of a castle, or what to remove in an en passant capture
		if (lastMoveChecked != null) {
			move = lastMoveChecked;
			pastBoards.add(new Board(board));
		}
		
		// Update who moved last
		lastMoved = board.getPiece(move.source).getColor();
		
		// Make the move in the Board object
		board.makeMove(move);
		
		// Check for checkmates and stalemates
		winner = board.checkForWins(lastMoved);
		
		// Check for fifty-move and three-fold repetition and two-kings-only draws
		int piecesCount = board.getPiecesCount();
		if (winner == null) {
			if (board.getConsecPassiveMovesCount() >= 50) {
				winner = Game.STATE_FIFTY_MOVE_DRAW;
			} else if (piecesCount == 2) {
				winner = Game.STATE_INSUFFICIENT_MATERIAL;
			} else if (gameType == Game.GAME_SINGLEPLAYER) {
				int i = pastBoards.size() - 1;
				if (i >= 3 && board.equals(pastBoards.get(i-1)) && board.equals(pastBoards.get(i-3)))
					winner = Game.STATE_THREEFOLD_REP_DRAW;
			} else {
				int i = pastBoards.size() - 1;
				if (i >= 7 && board.equals(pastBoards.get(i-3)) && board.equals(pastBoards.get(i-7)))
					winner = Game.STATE_THREEFOLD_REP_DRAW;
			}
		}
		// Check for other guaranteed draws, i.e. two kings and a bishop or knight
		if (winner == null) {
			if (piecesCount == 3 || piecesCount == 4) {
				int bishopCount = 0;
				int knightCount = 0;
				for (int r = 0; r < 8; r++) {
					for (int c = 0; c < 8; c++) {
						if (board.getPiece(r, c) instanceof Bishop)
							bishopCount++;
						else if (board.getPiece(r, c) instanceof Knight)
							knightCount++;
					}
				}
				if ((piecesCount == 3 && bishopCount + knightCount == 1) || knightCount == 2)
					winner = Game.STATE_INSUFFICIENT_MATERIAL;
			}

			lastMoveChecked = null;
		}
		
		updatePieceCounts();
		updateStatusMessages();
	}

	/**
	 * Undoes the last move in the game if possible
	 * @return true if the undo was successful, false otherwise
	 */
	public boolean undoLastMove ()
	{
		// Undo the move if the game has not ended and the undo list is not empty
		if (winner == null && pastBoards.size() != 0) {
			board = pastBoards.removeLast();
			// Update who moved last
			if (gameType != GAME_SINGLEPLAYER)
				lastMoved = lastMoved == Color.WHITE ? Color.BLACK : Color.WHITE;
			updatePieceCounts();
			updateStatusMessages();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks whether a move is legal. All human-produced moves through the GUI must be
	 * checked through this first before calling makeMove
	 * @param move the move to check
	 * @return Game.E_WRONG_TURN if the move moves a piece of the wrong colour
	 * 			Game.E_MOVE_INTO_CHECK if the move is a move into check
	 * 			Game.E_SUCCESS if the move is legal
	 * 			Game.E_BAD_MOVE if the move was simply against movement rules of chess
	 */
	public int canMakeMove (Move move)
	{
		// Generate a list of legal moves that the piece that is being moved has
		Color playerSide = board.getPiece(move.source).getColor();
		Collection<Move> moves = board.getPiece(move.source).generateLegalMoves(board, move.source);

		// If the given move can be found within the list, and does not violate any rules,
		// return the appropriate constant and store the legal move
		for (Move m : moves) {
			if (m.equals(move)) {
				if (lastMoved == playerSide)
					return E_WRONG_TURN;
				
				Board b = new Board(board);
				b.makeMove(m);
				King king;
				Position pos;
				
				if (playerSide == Color.WHITE)
					pos = b.getWhiteKingPos();
				else
					pos = b.getBlackKingPos();
				king = (King) b.getPiece(pos);
				
				if (king.isInCheck(b, pos))
					return E_MOVE_INTO_CHECK;
				
				// Store the legal move so that makeMove can use it directly
				lastMoveChecked = m;
				return E_SUCCESS;
			}
		}
		
		return E_BAD_MOVE;
	}
	
	/**
	 * Gets the collection of legal moves the piece at the given position could make
	 * @param piece the position of the piece
	 * @return a collection of legal moves the piece could make
	 */
	public Collection<Move> getLegalMoves (Position piece)
	{
		return board.getPiece(piece).generateLegalMoves(board, piece);
	}
	
	/**
	 * Gets the Board object representing the current game
	 * @return the Board in the current game
	 */
	public Board getGameState ()
	{
		return board;
	}
	
	/**
	 * Gets the winner or state of the current game if it exists
	 * @return the colour of the winner, if it exists, or
	 * 			Game.STATE_STALEMATE if the game has stalemated, or
	 * 			Game.STATE_THREEFOLD_REP_DRAW if the game has tied from repeated moves, or
	 * 			Game.STATE_FIFTY_MOVE_DRAW if the game has tied from too many passive moves, or
	 * 			Game.STATE_INSUFFICIENT_MATERIAL if there are not enough pieces for anyone to win, or
	 * 			null if the game is still in progress
	 */
	public Color getWinner ()
	{
		return winner;
	}
	
	/**
	 * Gets the type of the current game
	 * @return Game.GAME_SINGLEPLAYER if it's a singleplayer game, or
	 * 			Game.GAME_MULTIPLAYER if it's a multiplayer game
	 */
	public int getGameMode ()
	{
		return gameType;
	}
	
	/**
	 * Converts a given colour to a corresponding String representation
	 * @param color the colour to convert
	 * @return a String representation of the colour
	 */
	private String colorToLetter (Color color)
	{
		if (color == null)
			return "n";
		else if (color == Color.WHITE)
			return "w";
		else if (color == Color.BLACK)
			return "b";
		else if (color == Game.STATE_STALEMATE)
			return "s";
		else if (color == Game.STATE_FIFTY_MOVE_DRAW)
			return "fd";
		else if (color == Game.STATE_THREEFOLD_REP_DRAW)
			return "rd";
		else if (color == Game.STATE_INSUFFICIENT_MATERIAL)
			return "id";
		
		return null;
	}
	
	/**
	 * Converts the colour of a given Piece to a corresponding String representation
	 * @param piece the piece with the colour to convert
	 * @return a String representation of the colour
	 */
	private String colorToLetter (Piece piece)
	{
		if (piece == null)
			return "n";
		return piece.getColor() == Color.WHITE ? "w" : "b";
	}
	
	/**
	 * Converts a string to its corresponding colour
	 * @param letter the String with the letters to convert
	 * @return a Color object corresponding to the given string
	 */
	private Color letterToColor (String letter)
	{
		if (letter.equals("n"))
			return null;
		else if (letter.equals("w"))
			return Color.WHITE;
		else if (letter.equals("b"))
			return Color.BLACK;
		else if (letter.equals("s"))
			return Game.STATE_STALEMATE;
		else if (letter.equals("fd"))
			return Game.STATE_FIFTY_MOVE_DRAW;
		else if (letter.equals("rd"))
			return Game.STATE_THREEFOLD_REP_DRAW;
		else if (letter.equals("id"))
			return Game.STATE_INSUFFICIENT_MATERIAL;
		
		throw new IllegalArgumentException(letter);
	}
	
	/**
	 * Gets the colour of the player that currently last moved
	 * @return the colour of the player that currently last moved
	 */
	public Color getLastMoved()
	{
		return lastMoved;
	}
	
	/**
	 * Saves the current game state to a file
	 * @param output the reference to the file to write the game to
	 * @throws IOException if there is a write error
	 */
	public void saveGameToFile (File output) throws IOException
	{
		// Open the file
		BufferedWriter file = new BufferedWriter (new FileWriter(output));
		
		file.write("Knightingale v1.0 Saved Game");
		file.newLine();
		
		// Write what's in every board position into the file
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Integer timesMoved = board.getPiecesMovedCount().get(board.getPiece(i, j));
				file.write("" + (board.getPiece(i, j) == null ? -1 : board.getPiece(i, j).getPieceID())
						+ " " + (timesMoved == null ? 0 : timesMoved.intValue())
						+ " " + colorToLetter(board.getPiece(i, j)));
				file.newLine();
			}
		}
		
		// Write the locations of the kings
		file.write("" + board.getWhiteKingPos().row + " " + board.getWhiteKingPos().col
				+ " " + board.getBlackKingPos().row + " " + board.getBlackKingPos().col);
		file.newLine();
		
		// Write general information about the current game, e.g. who moved last
		file.write("" + gameType + " " + colorToLetter(lastMoved) + " " + colorToLetter(winner)
				+ " " + colorToLetter(cpuColor) + " " + cpuDepth);
		
		file.newLine();
		
		file.flush();
		file.close();
	}
}

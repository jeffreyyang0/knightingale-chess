package com.birdchess.common;

import java.awt.Color;

import com.birdchess.ai.board.Board;
import com.birdchess.ai.board.Piece;
import com.birdchess.ai.board.Queen;

/**
 * Represents a player in a chess game
 * @author Jeffrey Yang and Charley Huang
 * @version January 2013
 */
public class Player {
	// General information about the player
	private Piece upgradePiece;
	private String status;
	private String name;
	private double totalTimeTaken;
	private int movesMade;
	private long startTime;
	protected Color side;
	protected int [] piecesCount;
	protected boolean isHuman;
	
	/**
	 * Constructs a new player given its colour
	 * @param color the colour of the player
	 */
	public Player (Color color)
	{
		// Default pawn promotions to queens
		upgradePiece = new Queen (color);
		this.side = color;
		piecesCount = new int [6];
		name = side == Color.WHITE ? "White Player" : "Black Player";
		isHuman = true;
		totalTimeTaken = 0;
	}
	
	/**
	 * Gets the colour of this player
	 * @return the colour of this player
	 */
	public Color getColor ()
	{
		return side;
	}
	
	/**
	 * Sets the colour of this player
	 * @param color the colour to set this player to
	 */
	public void setColor (Color color)
	{
		side = color;
	}
	
	/**
	 * Gets the type of piece pawns of this player upgrade into
	 * @return the type of piece pawns of this player upgrade into
	 */
	public Piece getUpgradePiece ()
	{
		return upgradePiece;
	}
	
	/**
	 * Sets the type of piece pawns of this player upgrade into
	 * @param piece the type of piece to set pawns of this player to upgrade into
	 */
	public void setUpgradePiece (Piece piece)
	{
		upgradePiece = piece;
	}
	
	/**
	 * Gets the name of this player
	 * @return "White Player" if the player plays white,
	 * 			"Black Player" otherwise
	 */
	public String getName ()
	{
		return name;
	}
	
	/**
	 * Generates the next move that this player should make given a board
	 * @param b the given board
	 * @return the next move that this player should make given the board
	 */
	public Move getNextMove (Board b)
	{
		// This method is overridden in CpuPlayer
		return null;
	}
	
	/**
	 * Increments the piece count of a type of piece for this player by 1
	 * @param piece the type of piece to increment the counter for
	 */
	public void incrementPieceCount (int piece)
	{
		piecesCount[piece]++;
	}
	
	/**
	 * Decrements the piece count of a type of piece for this player by 1
	 * @param piece the type of piece to decrement the counter for
	 */
	public void decrementPieceCount (int piece)
	{
		piecesCount[piece]++;
	}
	
	/**
	 * Resets the piece counters for this player all to zero
	 */
	public void resetPieceCount ()
	{
		piecesCount = new int [6];
	}
	
	/**
	 * Gets the piece count of a given type of piece for this player
	 * @param piece the given type of piece
	 * @return the piece count of the given type of piece for this player
	 */
	public int getPieceCount (int piece)
	{
		return piecesCount[piece];
	}
	
	/**
	 * Sets the status message for this player, displayed on the side panel
	 * @param status the new status message for this player
	 */
	public void setStatus (String status)
	{
		this.status = status;
	}
	
	/**
	 * Gets the current status message for this player
	 * @return the current status message for this player
	 */
	public String getStatus ()
	{
		return status;
	}
	
	/**
	 * Gets the total amount of time in seconds this player has taken making moves
	 * @return the total amount of time in seconds this player has taken making moves
	 */
	public int getTotalTimeTaken ()
	{
		return (int) totalTimeTaken;
	}
	
	/**
	 * Starts the timing for the player to see how long it takes to make the move
	 */
	public void startTimedMove ()
	{
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * Ends the timing for the player and adds the time elapsed since the timing began
	 * to the player's total amount of time taken
	 */
	public void endTimedMove ()
	{
		long stop = System.currentTimeMillis();
		totalTimeTaken += (stop - startTime) / 1000.0;
	}
	
	/**
	 * Increments the total number of moves made by this player by 1
	 */
	public void incrementMovesMade ()
	{
		movesMade++;
	}
	
	/**
	 * Gets the total number of moves made by this player
	 * @return the total number of moves made by this player
	 */
	public int getMovesMade ()
	{
		return movesMade;
	}
	
	/**
	 * Checks whether this object represents a human player
	 * @return true if this object represents a human player, false otherwise
	 */
	public boolean isHuman ()
	{
		return isHuman;
	}
	
	/**
	 * Sets whether this object represents a human player
	 * @param isHuman whether this object is human or not
	 */
	public void setIsHuman (boolean isHuman)
	{
		this.isHuman = isHuman;
	}
}

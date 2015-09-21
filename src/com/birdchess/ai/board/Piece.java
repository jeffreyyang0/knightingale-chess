package com.birdchess.ai.board;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.birdchess.common.Move;
import com.birdchess.common.Position;

/**
 * Creates the framework for generic behavior of all chess pieces, and 
 * implements some generic methods that are constant with all pieces
 * 
 * @author Charley Huang and Jeffrey Yang
 * @version January 22, 2013
 */

public abstract class Piece
{
	//Track the colour of the chess piece (white/black)
	protected Color color;


	/**
	 * Constructs a new piece object of a given colour
	 * @param color the colour of the piece to construct
	 */
	public Piece (Color color)
	{
		//Set the color of the chess piece
		this.color = color;
	}


	/** 
	 * Creates an ArrayList of all possible and legal moves of the piece, 
	 * based on the given board and current position of the chess piece
	 * 
	 * @param board the board to find the legal moves in
	 * @param pos the position of the chess piece on the board
	 * @return an ArrayList of all legal moves the piece can make at its current
	 * position
	 */
	public abstract ArrayList<Move> 
	generateLegalMoves (Board board, Position pos);

	
	/** 
	 * Gets the weighted value of the piece based on standard evaluations
	 * of chess pieces found through research
	 * 
	 * @return the value of the piece expressed as an integer value
	 */
	public abstract int getPieceValue ();
	

	/** 
	 * Checks to see if a position is on a given board
	 * 
	 * @param board the board to check the position's validity on
	 * @param p the position to check on the board
	 * @return whether or not the position is on the given board
	 */
	protected boolean isOnBoard (Board board, Position p)
	{
		//Ensure the value of the position's row and column are within the
		//parameters of the board
		return p.row >= 0 && p.row < board.length () && p.col >= 0
				&& p.col < board.width ();
	}

	
	/**
	 * Checks to see if a row and column value are on a given board
	 * 
	 * @param board the board to check the row and column value against
	 * @param r the row to check on the board
	 * @param c the column to check on the board
	 * @return if the row and column are within the boundaries of the board
	 */
	protected boolean isOnBoard (Board board, int r, int c)
	{
		//Check to see if the row and column values 
		//are within the board's dimensions
		return r >= 0 && r < board.length () && c >= 0
				&& c < board.width ();
	}

	
	/**
	 * Generates a name for the type of piece
	 * @return a string representation for the name of this piece
	 */
	public abstract String typeOfPiece ();
	
	
	/**
	 * Generates the identity of the current piece
	 * @return the identity of the current piece
	 */
	public abstract int getPieceID ();

	
	/** 
	 * Gets the colour of the current piece (white/black)
	 * 
	 * @return the colour of the current piece (white/black)
	 */
	public Color getColor ()
	{
		return color;
	}

	
	/**
	 * Compares two pieces to see if their standard generated hash codes match
	 * @param other the other piece to compare with the current piece
	 * @return if the two pieces are equal or not
	 */
	public boolean equals (Object other)
	{
		//Check to see if the pieces are equal
		if (other instanceof Piece && this.hashCode() == other.hashCode())
			return true;
		
		//False if the pieces are not the same
		return false;
	}
}

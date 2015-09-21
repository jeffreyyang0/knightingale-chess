package com.birdchess.ai.board;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import com.birdchess.common.Move;
import com.birdchess.common.Position;

/**
 * Creates a new Knight object that extends the behavior of a Piece object
 * with specialized movements
 * 
 * @author Charley Huang and Jeffrey Yang
 * @version January 22, 2013
 */

public class Knight extends Piece
{
	/**
	 * Constructs a new knight object given a colour
	 * @param color the colour of the knight to construct
	 */
	public Knight(Color color)
	{
		//Call the Piece constructor to construct a new Knight object
		super(color);
	}

	
	/**
	 * Generates a list of legal moves this knight can make given
	 * the board and its position
	 * 
	 * @param board the board that this object is on
	 * @param pos the position of this object on the given board
	 * @return a list of legal moves that can be made by this object 
	 * given its board and position
	 */
	public ArrayList<Move> generateLegalMoves(Board board, Position pos)
	{
		//Declare the ArrayList of legal movements for a Knight
		ArrayList<Move> legalMoves = new ArrayList<Move>();

		
		//Check for legal movements for the Knight (L shaped moves)
		for (int addRow = 1; addRow <= 2; addRow ++)
			for (int addCol = 1; addCol <= 2; addCol ++)
				//Do not allow diagonal movement
				if (addRow != addCol)
				{
					//Only allow legal movements that are on the grid, and add
					//all possible movements of the knight for a certain
					//amount of rows and columns away from the original location
					if (isOnBoard (board, pos.row + addRow, pos.col + addCol))
					{
						//Add the coordinates to the map of legal moves
						legalMoves.add(new Move(pos, new Position
								(pos.row + addRow, pos.col + addCol), board));
					}

					
					//Check to see if the move is on the board
					if (isOnBoard (board, pos.row + addRow, pos.col - addCol))
					{
						//Add the coordinates to the map of legal moves
						legalMoves.add(new Move(pos, new Position
								(pos.row + addRow, pos.col - addCol), board));
					}

					
					//Check to see if the move is on the board
					if (isOnBoard (board, pos.row - addRow, pos.col + addCol))
					{
						//Add the coordinates to the map of legal moves
						legalMoves.add(new Move(pos, new Position
								(pos.row - addRow, pos.col + addCol), board));
					}

					
					//Check to see if the move is on the board
					if (isOnBoard (board, pos.row - addRow, pos.col - addCol))
					{
						//Add the coordinates to the map of legal moves
						legalMoves.add(new Move(pos, new Position
								(pos.row - addRow, pos.col - addCol), board));
					}
				}
		
		
		//Remove all allied pieces from the list of legal moves
		for (int i = 0; i < legalMoves.size(); i++)
		{
			//Track the moves and the piece located at the position of the move
			Move move = legalMoves.get(i);
			Piece piece = board.getPiece(move.target);
			
			//When an allied piece is targeted, remove the move from the list
			//of legal movements
			if (piece != null && piece.getColor () == 
					board.getPiece (move.source).getColor ())
			{
				legalMoves.remove(i);
				
				//Subtract from the index to prevent logic errors
				i--;
			}
		}
		
		return legalMoves;
	}

	
	/**
	 * Generates the name for this type of piece
	 * @return a string representation for the name of this piece
	 */
	public String typeOfPiece ()
	{
		return "Knight";
	}

	
	/** 
	 * Generates the weighted value of a Knight based on board evaluation
	 * @return the value of the Knight expressed as an integer
	 */
	public int getPieceValue()
	{
		//A Knight is worth 320
		return 320;
	}
	
	
	/**
	 * Generates the ID of the Knight to be used for the positioning scoring
	 * table
	 * @return the ID of the Knight
	 */
	public int getPieceID()
	{
		return Board.KNIGHT;
	}
}

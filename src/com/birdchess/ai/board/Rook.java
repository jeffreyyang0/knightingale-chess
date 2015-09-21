package com.birdchess.ai.board;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import com.birdchess.common.Move;
import com.birdchess.common.Position;

/**
 * Creates a new Rook object that extends the behavior of a Piece object
 * with specialized movements
 * 
 * @author Charley Huang and Jeffrey Yang
 * @version January 22, 2013
 */

public class Rook extends Piece
{
	/**
	 * Constructs a new rook object given a colour
	 * @param color the colour of the rook to construct
	 */
	public Rook(Color color)
	{
		//Call the Piece constructor to construct a new Rook object
		super(color);
	}

	
	/**
	 * Generates a list of legal moves this rook can make 
	 * given the board and its position
	 * 
	 * @param board the board that this object is on
	 * @param pos the position of this object on the given board
	 * @return a list of legal moves that can be made by this object 
	 * given its board and position
	 */
	public ArrayList<Move> generateLegalMoves(Board board, Position pos)
	{
		//Declare the ArrayList of legal moves
		ArrayList<Move> legalMoves = new ArrayList<Move>();

		//Track the location to move the piece to
		int moveToRow = pos.row;
		int moveToCol = pos.col;

		//Initialize a HashSet of movements
		HashSet <Integer> moves = new HashSet <Integer> ();

		//Go through all possible horizontal and vertical directions of movement
		for (int addRow = -1; addRow <= 1; addRow ++)
			for (int addCol = -1; addCol <= 1; addCol ++)
				//Check for proper horizontal and vertical movement
				if (Math.abs (addRow) != Math.abs(addCol))
				{
					//Add all possible and legal movements towards a certain 
					//direction of the piece
					while 
						(isOnBoard (board, moveToRow + addRow, moveToCol + addCol)
								&& board.getPiece 
								(moveToRow + addRow, moveToCol + addCol) == null)
					{
						//Add to the row and column target for unlimited
						//horizontal and vertical movement up to an obstacle
						moveToRow += addRow;
						moveToCol += addCol;

						//Add the coordinates to the map of legal moves
						legalMoves.add(new Move(pos, new Position
								(moveToRow, moveToCol), board));
					}
					

					//If a piece is found to be in the path of movement, 
					//add the coordinates of the piece that can be captured
					if (isOnBoard (board, moveToRow + addRow, moveToCol + addCol))
					{
						//Add the coordinates to the map of legal moves
						legalMoves.add(new Move(pos, new Position
								(moveToRow + addRow, moveToCol + addCol), board));
					}

					
					//Reset the coordinates of the area to move
					moveToRow = pos.row;
					moveToCol = pos.col;
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
	 * For castling purposes, check to see if the rook has not moved from its
	 * original location
	 * @param b the board to check if the rook has moved or not in
	 * @return if the rook has made one or more movements
	 */
	public boolean hasNotMoved (Board b)
	{
		//When the rook has moved, return true
		if (b.piecesMovedCount.get(this) != null)
			return false;
		
		return true;
	}

	
	/**
	 * Generates a string stating the type of piece
	 * @return the type of the current piece, a Rook
	 */
	public String typeOfPiece ()
	{
		return "Rook";
	}
	
	
	/** 
	 * Generates the weighted value of a Rook based on board evaluation
	 * @return the value of the Rook expressed as an integer
	 */
	public int getPieceValue()
	{
		//A rook is worth 500
		return 500;
	}
	
	
	/**
	 * Generates the ID of the Rook to be used for the positioning scoring
	 * table
	 * @return the ID of the Rook
	 */
	public int getPieceID()
	{
		return Board.ROOK;
	}
}

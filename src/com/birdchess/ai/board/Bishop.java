package com.birdchess.ai.board;

import java.awt.Color;
import java.util.ArrayList;

import com.birdchess.common.Move;
import com.birdchess.common.Position;

/**
 * Creates a new Bishop object that extends the behavior of a Piece object
 * with specialized movements
 * 
 * @author Charley Huang and Jeffrey Yang
 * @version January 22, 2013
 */
public class Bishop extends Piece
{
	/**
	 * Constructs a new bishop object given a colour
	 * @param color the colour of the bishop to construct
	 */
	public Bishop(Color color)
	{
		//Call the Piece constructor to construct a new Rook object
		super(color);
	}

	
	/**
	 * Generates a list of legal moves this bishop can make given 
	 * the board and its position
	 * 
	 * @param board the board that this object is on
	 * @param pos the position of this object on the given board
	 * @return a list of legal moves that can be made by this 
	 * object given its board and position
	 */
	public ArrayList<Move> generateLegalMoves(Board board, Position pos)
	{
		//Declare the ArrayList of legal movements for the Bishop
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		
		
		// Track the location to move the piece to
		int moveToRow = pos.row;
		int moveToCol = pos.col;

		// Go through all possible diagonal directions of movement
		for (int addRow = -1; addRow <= 1; addRow += 2)
			for (int addCol = -1; addCol <= 1; addCol += 2)
			{
				// Add all possible movements towards a certain 
				// direction of the piece, without obstruction by other pieces
				while (isOnBoard (board, moveToRow + addRow, moveToCol + addCol) 
						&& board.getPiece 
						(moveToRow + addRow, moveToCol + addCol) == null)
				{
					//Continue in the current direction
					moveToRow += addRow;
					moveToCol += addCol;

					// Add the coordinates to the map of legal moves
					legalMoves.add(new Move(pos, new Position
							(moveToRow, moveToCol), board));
				}

				// If a piece is found to be in the path of movement, 
				// add the coordinates of the piece that can be captured
				if (isOnBoard (board, moveToRow + addRow, moveToCol + addCol))
				{
					// Add the coordinates to the map of legal moves
					legalMoves.add(new Move(pos, new Position
							(moveToRow + addRow, moveToCol + addCol), board));
				}

				// Reset the coordinates of the area to move
				moveToRow = pos.row;
				moveToCol = pos.col;
			}
		
		
		
		// Remove all allied pieces from the list of legal moves
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
		return "Bishop";
	}
	
	
	/** 
	 * Generates the weighted value of a Bishop based on board evaluation
	 * @return the value of the Bishop expressed as an integer
	 */
	public int getPieceValue()
	{
		return 325;
	}

	
	/**
	 * Generates the ID of the Bishop to be used for the positioning scoring
	 * table
	 * @return the ID of the Bishop
	 */
	public int getPieceID()
	{
		return Board.BISHOP;
	}
}

package com.birdchess.ai.board;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import com.birdchess.common.Move;
import com.birdchess.common.Position;

/**
 * Creates a new Pawn object that extends the behavior of the Piece class
 * 
 * @author Charley Huang and Jeffrey Yang
 * @version January 22, 2013
 */
public class Pawn extends Piece
{
	/**
	 * Constructs a new pawn object given a colour
	 * @param color the colour of the pawn to construct
	 */
	public Pawn(Color color)
	{
		//Call the Piece class constructor 
		super(color);
	}
	

	/**
	 * Generates a list of legal moves this pawn can make given 
	 * the board and its position
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

		//Behavior for the movement of white pawns
		if (color == Color.WHITE)
		{
			//Check to see if the pawn can perform an En Passant capture
			
			//Store the piece to the left of the pawn
			Piece left = board.getPiece (pos.row, pos.col - 1);
			
			//En Passant is possible when the piece to the left of the current
			//pawn is also a pawn, of the opposite colour, and the current
			//pawn can be captured
			if (left != null && left.getClass() == this.getClass() && 
					((Pawn)left).canBePassed(board) && left.color != color)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row - 1, pos.col - 1), new Move(new Position
								(pos.row, pos.col - 1), null, null), board));
			}
			
			
			//Store the piece to the right of the pawn
			Piece right = board.getPiece (pos.row, pos.col + 1);
			
			//En Passant is possible when the piece to the right of the current
			//pawn is also a pawn, of the opposite colour, and the current
			//pawn can be captured
			if (right != null && right.getClass() == this.getClass() && 
					((Pawn)right).canBePassed(board) && right.color != color)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row - 1, pos.col + 1), new Move(new Position
								(pos.row, pos.col + 1), null, null), board));
			}

			

			//Add the locations of pieces that the pawn can capture
			
			//Check the position to the north-east of the pawn for a piece
			//that is available for capture
			if (board.getPiece(pos.row - 1, pos.col + 1) != null)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row - 1, pos.col + 1), board));
			}

			//Check the position to the north-west of the pawn for a piece
			//that is available for capture
			if (board.getPiece(pos.row - 1, pos.col - 1) != null)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row - 1, pos.col - 1), board));
			}

			
			//Check to see if the pawn can advance forward
			if (board.getPiece(pos.row - 1, pos.col) == null)
			{
				//Add forward movement (single square) for the pawn and 
				//add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row - 1, pos.col), board));

				//Check to see if the pawn can advance two squares
				if (board.getPiece(pos.row - 2, pos.col) == null 
						&& pos.row == board.length () - 2)
				{
					//Add the coordinates to the map of legal moves
					legalMoves.add(new Move(pos, new Position
							(pos.row - 2, pos.col), board));
				}
			}
		}


		//Behavior for the movement of black pawns
		else
		{
			//Check to see if the pawn can perform an En Passant capture
			
			//Store the piece to the left of the pawn
			Piece left = board.getPiece (pos.row, pos.col - 1);
			
			//En Passant is possible when the piece to the left of the current
			//pawn is also a pawn, of the opposite colour, and the current
			//pawn can be captured
			if (left != null && left.getClass() == this.getClass() && 
					((Pawn)left).canBePassed(board) && left.color != color)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row + 1, pos.col - 1), new Move(new Position
								(pos.row, pos.col - 1), null, null), board));
			}
			
			
			//Store the piece to the right of the pawn
			Piece right = board.getPiece (pos.row, pos.col + 1);
			
			//En Passant is possible when the piece to the right of the current
			//pawn is also a pawn, of the opposite colour, and the current
			//pawn can be captured
			if (right != null && right.typeOfPiece().equals ("Pawn") &&
					((Pawn)right).canBePassed(board) && right.color != color)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row + 1, pos.col + 1), new Move(new Position
								(pos.row, pos.col + 1), null, null), board));
			}


			
			//Add the locations of pieces that the pawn can capture

			//Check the position to the south-east of the pawn for a piece
			//that is available for capture
			if (board.getPiece(pos.row + 1, pos.col + 1) != null)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row + 1, pos.col + 1), board));
			}

			
			//Check the position to the south-west of the pawn for a piece
			//that is available for capture
			if (board.getPiece(pos.row + 1, pos.col - 1) != null)
			{
				//Add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row + 1, pos.col - 1), board));
			}

			
			//Check to see if the pawn can advance forward
			if (board.getPiece(pos.row + 1, pos.col) == null)
			{
				//Add forward movement (single square) for the pawn and 
				//add the coordinates to the map of legal moves
				legalMoves.add(new Move(pos, new Position
						(pos.row + 1, pos.col), board));

				//Check to see if the pawn can advance two squares
				if (board.getPiece(pos.row + 2, pos.col) == null && pos.row == 1)
				{
					//Add the coordinates to the map of legal moves
					legalMoves.add(new Move(pos, new Position
							(pos.row + 2, pos.col), board));
				}
			}
		}
		
		
		
		//Remove all allied pieces from the list of legal moves
		for (int i = 0; i < legalMoves.size(); i++)
		{
			//Keep track of the moves and the piece at the position of the move
			Move move = legalMoves.get(i);
			Piece piece = board.getPiece(move.target);
			
			//Remove any movements that would capture an allied piece
			if (piece != null && piece.getColor () == 
					board.getPiece (move.source).getColor ())
			{
				legalMoves.remove(i);
				
				//Subtract one from the index to account for the removal
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
		return "Pawn";
	}

	
	/**
	 * Generates the value for a pawn based on standard evaluations
	 * @return the value of the piece
	 */
	public int getPieceValue()
	{
		//A pawn is worth 100 points in board evaluation
		return 100;
	}
	
	
	/** 
	 * Check to see if the piece can be passed, or another pawn can perform
	 * an En Passant capture
	 * 
	 * @param board the board to check for En Passant
	 * @return if the pawn can be passed
	 */
	public boolean canBePassed (Board board)
	{
		//Find how many times the pawn has moved
		Integer timesMoved = board.piecesMovedCount.get(this);
		
		//The pawn can only be captured when the pawn has 
		//moved forward two spaces 
		if (timesMoved != null && timesMoved == 1)
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * Generates the ID of the Pawn to be used for the positioning scoring
	 * table
	 * @return the ID of the Pawn
	 */
	public int getPieceID()
	{
		return Board.PAWN;
	}
}

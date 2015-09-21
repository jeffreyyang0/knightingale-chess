package com.birdchess.ai.board;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.birdchess.common.Move;
import com.birdchess.common.Position;

/**
 * Creates a new King object that extends the behavior of a Piece object
 * with specialized movements
 * 
 * @author Charley Huang and Jeffrey Yang
 * @version January 2013
 */
public class King extends Piece
{
	/**
	 * Constructs a new king object given a colour
	 * @param color the colour of the king to construct
	 */
	public King(Color color)
	{
		//Call the Piece constructor to construct a new King object
		super(color);
	}

	
	/**
	 * Generates a list of legal moves this king can make given 
	 * the board and its position
	 * 
	 * @param board the board that this object is on
	 * @param pos the position of this object on the given board
	 * @return a list of legal moves that can be made by this 
	 * object given its board and position
	 */
	public ArrayList<Move> generateLegalMoves(Board board, Position pos)
	{
		//Declare the ArrayList of legal movements for the King
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		
		
		//Check to see if the king can castle
		int canCastle = canCastle (board, pos);

		//If the King can castle, either King-side or Queen-side
		if (canCastle != 0)
		{
			//Add the coordinates to the map of legal moves for Queen-side
			//castling
			if (canCastle == 1)
				legalMoves.add(new Move(pos, new Position(pos.row, 2), 
						new Move(new Position(pos.row, 0), 
								new Position(pos.row, 3), null), board));
			
			//Add the coordinates to the map of legal moves for King-side
			//castling
			else
				legalMoves.add(new Move(pos, new Position(pos.row, 6), 
						new Move(new Position(pos.row, 7), 
								new Position(pos.row, 5), null), board));
		}

		
		
		//Check all around the king for legal movements
		for (int addRow = -1; addRow <= 1; addRow ++)
			for (int addCol = -1; addCol <= 1; addCol ++)
			{
				//Track the location that the King can move to
				int moveToRow = pos.row + addRow;
				int moveToCol = pos.col + addCol;
				
				//Ensure that the location is on the board
				if (!isOnBoard (board, moveToRow, moveToCol))
					continue;
				
				//Track the new position of the King
				Position newPos = new Position(moveToRow, moveToCol);
				
				//Make a move based on the current location and the location
				//to move to
				Move current = new Move(pos, newPos, board);
				
				//Create a new board, identical to the current board
				Board newBoard = new Board (board);
				
				//Make the move in the current board
				newBoard.makeMove(current);
				
				//Ensure that the movement will not put the king in check
				if (!isInCheck (newBoard, newPos))
				{
					//Add the coordinates to the map of legal moves
					legalMoves.add(current);
				}
			}
		
		
		
		// Remove all allied pieces from the list of legal moves, and illegal
		// suicidal moves
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
	 * Check to see if the King is currently being checked
	 * 
	 * @param board the board to check for check conditions
	 * @param pos the position of the King on the board
	 * @return if the King is in check
	 */
	public boolean isInCheck (Board board, Position pos)
	{
		//Go through the entire board, searching for the legal moves of Pieces
		for (int row = 0; row < board.length(); row ++) {
			for (int col = 0; col < board.width(); col ++)
			{
				//Track the current position
				Position currentPos = new Position (row, col);
				
				//Find the piece at the current position on the board
				Piece current = board.getPiece(currentPos);
				
				
				//In the event that there is an enemy piece at the position
				if (current != null && current.getColor() != this.color) 
				{
					//Declare the ArrayList of moves for the piece at the
					//position
					ArrayList<Move> moves = null;
					
					//If the current piece is not a King, as Kings cannot
					//check each other, generate the legal movements 
					//of the Piece
					if (!(current instanceof King))
						moves = current.generateLegalMoves(board, currentPos);

					//When the piece can move, search for check conditions
					if (moves != null) 
					{
						//Check to see if the piece is a pawn
						if (current instanceof Pawn) 
						{
							//When the piece is a pawn, look through the moves
							//to see if it can target the King and place it
							//in check
							for (Move move : moves) 
							{
								//Look for diagonal capture opportunities
								if (move.target.equals(pos) && 
										(move.source.col - move.target.col == 1
									|| move.source.col - move.target.col == -1)) 
								{
									return true;
								}
							}
						} 
						
						
						//When the current piece is not a pawn
						else 
						{
							//Look through the possible movements of the piece
							for (Move move : moves) 
							{
								//If the piece is able to target the King,
								//then it is in check
								if (move.target.equals(pos)) 
								{
									return true;
								}
							}
						}
					}
				}
			}
			
			
			//Search around the King for pieces
			for (int r = -1; r <= 1; r++) {
				for (int c = -1; c <= 1; c++) {
					//Get the piece at the location 
					Piece p = board.getPiece(pos.row + r, pos.col + c);
					
					//Search for the presence of an enemy King
					if (p instanceof King && p.color != this.color)
						return true;
				}
			}
		}
		
		return false;
	}

	
	/**
	 * Check to see if the King has the proper conditions to castle
	 * 
	 * @param board the board to check for proper castling conditions in
	 * @param pos the position of the King in the board
	 * @return 0 if the King cannot castle on either side
	 * 1 if the King can perform a Queen-side castle
	 * 2 if the King can perform a King-side castle
	 */
	public int canCastle (Board board, Position pos)
	{
		//Declare the piece to check for castling conditions
		Piece pieceToCheck;

		//Check the King for opportunities to castle
		if (!board.piecesMovedCount.containsKey(this) && !isInCheck(board, pos))
		{
			//Look through the entire row that the King is on
			for (int column = 0; column <= 7; column += 7)
			{
				//Store the piece at the current location
				pieceToCheck = board.getPiece (pos.row, column);
				
				//If the piece is a rook, check to see if any other pieces are
				//in the way of the king and the rook
				if (pieceToCheck != null && 
						pieceToCheck.typeOfPiece().equals("Rook"))
				{
					//Check for obstructions
					boolean obstruction = false;
					
					//Check to see if the rook is in the proper location for
					//Queen-side castling
					if (column == 0)
					{
						for (int addCol = -3; addCol < 0; addCol ++)
							//Ensure there is no piece obstructing the castle and 
							//the king cannot castle out of, through, or into check
							if (board.getPiece(pos.row, pos.col + addCol) != null
							|| isInCheck (board, new Position
									(pos.row, pos.col + addCol)))
								obstruction = true;
						
						//Allow for Queen-side castling
						if (obstruction == false && 
								((Rook)pieceToCheck).hasNotMoved(board))
							return 1;
					}
					
					
					//Check to see if the rook is in the proper location for
					//King-side castling
					else
					{
						for (int addCol = 1; addCol <= 2; addCol ++)
							//Ensure there is no piece obstructing the castle and 
							//the king cannot castle out of, through, or into check
							if (board.getPiece(pos.row, pos.col + addCol) != null
							|| isInCheck (board, new Position
									(pos.row, pos.col + addCol)))
								obstruction = true;
						
						//Allow for King-side castling
						if (obstruction == false && 
								((Rook)pieceToCheck).hasNotMoved(board))
							return 2;
					}
				}
			}
		}
		
		//In the case of no castling allowed
		return 0;
	}

	
	/**
	 * Generates the name for this type of piece
	 * @return a string representation for the name of this piece
	 */
	public String typeOfPiece ()
	{
		return "King";
	}

	
	/** 
	 * Generates the weighted value of a King based on board evaluation
	 * @return the value of the King expressed as an integer
	 */
	public int getPieceValue()
	{
		//The King is worth far more than any other piece
		return 400000;
	}
	
	
	/**
	 * Generates the ID of the King to be used for the positioning scoring
	 * table
	 * @return the ID of the King
	 */
	public int getPieceID()
	{
		return Board.KING;
	}
}

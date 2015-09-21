package com.birdchess.common;

import java.awt.Color;

import com.birdchess.ai.board.Board;
import com.birdchess.ai.board.Piece;

/**
 * Describes any move on a Board, including castle and en passant moves
 * @author Jeffrey Yang and Charley Huang
 * @version January 2013
 */
public class Move implements Comparable<Move> {
	// Positions of what is being moved where
	public Position source;
	public Position target;
	
	// An additional Move variable to hold special instructions in the event of
	// a castle or en passant capture
	public Move extra;
	
	// The board on which this move is to take place
	public Board board;
	
	/**
	 * Constructs a new move object given the source, target, and board
	 * @param src the position of the piece to move
	 * @param tgt the position to move the piece
	 * @param board the board on which the move takes place
	 */
	public Move (Position src, Position tgt, Board board)
	{
		source = src;
		target = tgt;
		extra = null;
		this.board = board;
	}
	
	/**
	 * Constructs a new move object given the source, target, a tied move, and the board
	 * @param src the position of the piece to move
	 * @param tgt the position to move the piece
	 * @param ex another Move that is to be made at the same time as this move
	 * @param board the board on which the move takes place
	 */
	public Move (Position src, Position tgt, Move ex, Board board)
	{
		source = src;
		target = tgt;
		extra = ex;
		this.board = board;
	}
	
	/**
	 * Compares whether this object is equal to another based on the positions of their source and target
	 * @param other the other object to compare to
	 * @return true if the other object is a Move and has the same source and target positions,
	 * 			false otherwise
	 */
	public boolean equals (Object other)
	{
		if (other instanceof Move) {
			Move o = (Move) other;
			if (this.source.equals(o.source) && this.target.equals(o.target))
				return true;
		}
		return false;
	}

	/**
	 * Compares this Move to another based on a rough estimate of which is better
	 * @param other the other Move to compare to
	 * @return a negative value if this move is roughly better,
	 * 			0 if the two moves are roughly equal, and
	 * 			a positive value if this move is roughly worse
	 */
	public int compareTo(Move other) {
		int difference = 0;
		Piece currentTarget = board.getPiece(this.target);
		Piece otherTarget = board.getPiece(other.target);
		Piece currentSource = board.getPiece(this.source);
		Piece otherSource = board.getPiece(other.source);
		
		// If either moves are capture moves, take into consideration the difference between the
		// value of the capturing piece and the value of the piece to be captured
		if (currentTarget != null)
			difference += currentSource.getPieceValue() - currentTarget.getPieceValue();
		if (otherTarget != null)
			difference += otherTarget.getPieceValue() - otherSource.getPieceValue();
		
		// Take into consideration the change in position score moving from the source to the target
		if (currentSource.getColor() == Color.WHITE) {
			difference += Board.whitePiecePositionScores[board.getTableVersion()][currentSource.getPieceID()][this.source.row][this.source.col]
					- Board.whitePiecePositionScores[board.getTableVersion()][currentSource.getPieceID()][this.target.row][this.target.col];
		} else {
			difference += Board.whitePiecePositionScores[board.getTableVersion()][currentSource.getPieceID()][7-this.source.row][this.source.col]
					- Board.whitePiecePositionScores[board.getTableVersion()][currentSource.getPieceID()][7-this.target.row][this.target.col];
		}
		
		return difference;
	}
}

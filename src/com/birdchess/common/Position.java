package com.birdchess.common;

/**
 * Represents a position on a chess board
 * @author Jeffrey Yang and Charley Huang
 * @version January 2013
 */
public class Position {
	public int row;
	public int col;
	
	/**
	 * Constructs a new Position given its row and column
	 * @param r the row value
	 * @param c the column value
	 */
	public Position (int r, int c)
	{
		row = r;
		col = c;
	}
	
	/**
	 * Constructs a new Position given an existing Position
	 * @param p the existing Position to copy-construct
	 */
	public Position (Position p)
	{
		row = p.row;
		col = p.col;
	}
	
	/**
	 * Checks whether this object is equal to another
	 * @param other the other object to compare to
	 * @return true if the other object is a Position and has the same row and column values as this,
	 * 			false otherwise
	 */
	public boolean equals (Object other)
	{
		if (other instanceof Position) {
			Position o = (Position) other;
			if (this.row == o.row && this.col == o.col)
				return true;
		}
		return false;
	}
}

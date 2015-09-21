package com.birdchess.ai.board;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import com.birdchess.common.Game;
import com.birdchess.common.Move;
import com.birdchess.common.Position;
import com.birdchess.ai.board.*;

/**
 * Represents a board in a chess game
 * @author Charley Huang and Jeffrey Yang
 * @version January 2013
 */
public class Board 
{
	// A hard-coded table of each piece's position score at every stage of the game
	// 		score = whitePiecePositionScores[version][pieceID][row][col]
	public static final int whitePiecePositionScores [] [] [] [] = {
		// Early game version
		{
			// Bishop position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{7,3,3,3,3,3,3,7},
				{7,6,3,6,3,6,7,7},
				{7,3,7,3,3,3,3,7},
				{1,3,3,3,3,3,3,1},
				{1,1,1,1,1,1,1,1},
				{3,3,3,3,3,3,3,3}
			},
			// King position scores for white
			{
				{0,0,0,0,0,0,0,0},
				{10,10,10,10,10,10,10,10},
				{0,0,0,0,0,0,0,0},
				{0,10,10,10,10,10,10,0},
				{0,10,10,10,10,10,10,0},
				{0,10,10,10,10,10,10,0},
				{10,10,10,10,10,10,10,10},
				{60,60,60,25,30,25,60,60}
			},
			// Knight position scores for white
			{
				{7,7,7,7,7,7,7,7},
				{1,1,1,1,1,1,1,1},
				{3,3,3,3,3,3,3,3},
				{3,4,3,4,4,3,4,4},
				{3,4,3,4,3,4,4,3},
				{3,3,3,3,3,3,3,3},
				{1,1,1,1,1,1,1,1},
				{0,0,0,0,0,0,0,0}
			},
			// Pawn position scores for white
			{
				{300,300,300,300,300,300,300,300},
				{40,40,40,40,40,40,40,40},
				{20,10,20,10,20,10,20,10},
				{5,4,5,4,5,4,5,4},
				{4,4,3,4,4,4,4,4},
				{2,2,4,2,5,2,4,2},
				{1,2,3,3,2,3,3,1},
				{0,0,0,0,0,0,0,0}
			},
			// Queen position scores for white
			{
				{6,6,6,6,6,6,6,6},
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,8},
				{6,6,6,6,6,6,6,6},
				{6,6,6,6,6,6,6,6},
				{6,6,6,6,6,6,6,6},
				{0,0,0,3,3,0,0,0}
			},
			// Rook position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{3,1,1,1,1,1,1,3},
				{1,3,3,3,3,3,3,1},
				{1,3,3,3,3,3,3,1},
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{1,3,3,3,3,3,3,1},
				{1,0,4,4,4,4,0,1}
			}
		},
		// Mid game version
		{
			// Bishop position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{7,3,3,3,3,3,3,7},
				{7,6,3,6,3,6,7,7},
				{7,3,7,3,3,3,3,7},
				{1,3,3,3,3,3,3,1},
				{1,1,1,1,1,1,1,1},
				{3,3,3,3,3,3,3,3}
			},
			// King position scores for white
			{
				{0,0,0,0,0,0,0,0},
				{10,10,10,10,10,10,10,10},
				{0,0,0,0,0,0,0,0},
				{0,10,10,10,10,10,10,0},
				{0,10,10,10,10,10,10,0},
				{0,10,10,10,10,10,10,0},
				{10,10,10,10,10,10,10,10},
				{60,60,60,25,30,25,60,60}
			},
			// Knight position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{1,1,1,1,1,1,1,1},
				{3,3,3,3,3,3,3,3},
				{3,4,3,4,4,3,4,4},
				{3,4,3,4,3,4,4,3},
				{3,3,3,3,3,3,3,3},
				{1,1,1,1,1,1,1,1},
				{0,0,0,0,0,0,0,0}
			},
			// Pawn position scores for white
			{
				{300,300,300,300,300,300,300,300},
				{110,110,110,110,110,110,110,110},
				{20,10,20,10,20,10,20,10},
				{10,20,10,20,10,20,10,20},
				{4,4,3,4,4,4,4,4},
				{2,2,4,2,5,2,4,2},
				{1,2,3,3,2,3,3,1},
				{0,0,0,0,0,0,0,0}
			},
			// Queen position scores for white
			{
				{6,6,6,6,6,6,6,6},
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,8},
				{6,6,6,6,6,6,6,6},
				{6,6,6,6,6,6,6,6},
				{6,6,6,6,6,6,6,6},
				{0,0,0,3,3,0,0,0}
			},
			// Rook position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{3,1,1,1,1,1,1,3},
				{1,3,3,3,3,3,3,1},
				{1,3,3,3,3,3,3,1},
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{1,3,3,3,3,3,3,1},
				{1,0,4,4,4,4,0,1}
			}
		},
		// Late game version
		{
			// Bishop position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{3,3,3,3,3,3,3,3},
				{7,3,3,3,3,3,3,7},
				{7,6,3,6,3,6,7,7},
				{7,3,7,3,3,3,3,7},
				{1,3,3,3,3,3,3,1},
				{1,1,1,1,1,1,1,1},
				{3,3,3,3,3,3,3,3}
			},
			// King position scores for white
			{
				{0,5,5,5,5,5,5,0},
				{10,10,10,10,10,10,10,10},
				{10,10,10,10,10,10,10,10},
				{0,12,14,14,14,14,12,0},
				{0,12,14,14,14,14,12,0},
				{0,12,14,14,14,14,12,0},
				{10,10,10,10,10,10,10,10},
				{0,5,5,5,5,5,5,0}
			},
			// Knight position scores for white
			{
				{2,2,2,2,2,2,2,2},
				{1,1,1,1,1,1,1,1},
				{3,3,3,3,3,3,3,3},
				{3,4,3,4,4,3,4,4},
				{3,4,3,4,3,4,4,3},
				{3,3,3,3,3,3,3,3},
				{1,1,1,1,1,1,1,1},
				{2,2,2,2,2,2,2,2}
			},
			// Pawn position scores for white
			{
				{400,400,400,400,400,400,400,400},
				{300,300,300,300,300,300,300,300},
				{160,160,160,160,160,160,160,160},
				{80,80,80,80,80,80,80,80},
				{40,40,40,40,40,40,40,40},
				{20,20,20,20,20,20,20,20},
				{10,10,10,10,10,10,10,10},
				{0,0,0,0,0,0,0,0}
			},
			// Queen position scores for white
			{
				{6,6,6,6,6,6,6,6},
				{6,20,20,20,20,20,20,6},
				{6,20,25,25,25,25,20,6},
				{6,20,25,30,30,25,20,6},
				{6,20,25,30,30,25,20,6},
				{6,20,25,25,25,25,20,6},
				{6,20,20,20,20,20,20,6},
				{6,6,6,6,6,6,6,6}
			},
			// Rook position scores for white
			{
				{3,3,3,3,3,3,3,3},
				{3,1,1,1,1,1,1,3},
				{1,4,4,3,3,4,4,1},
				{1,4,4,3,3,4,4,1},
				{3,4,4,3,3,4,4,3},
				{3,4,4,3,3,4,4,3},
				{1,3,3,3,3,3,3,1},
				{1,3,3,3,3,3,3,1}
			}
		}
	};
	
	// Constants used for the position score table
	public static final int BISHOP = 0;
	public static final int KING = 1;
	public static final int KNIGHT = 2;
	public static final int PAWN = 3;
	public static final int QUEEN = 4;
	public static final int ROOK = 5;
	
	// An array of pieces to represent the board
	private Piece [][] board;
	
	// A map to keep track of how many times each piece moved
	protected HashMap<Piece, Integer> piecesMovedCount;
	
	// Positions of the kings
	protected Position whiteKing;
	protected Position blackKing;
	
	// Piece counts
	protected int pieceCount;
	protected int whitePieceCount;
	protected int blackPieceCount;
	
	protected int consecutiveUselessMovesCount;
	
	/**
	 * Constructs a new chess board object with pieces in their default starting positions
	 */
	public Board ()
	{
		board = new Piece[8][8];
		startNewGame();
		
		piecesMovedCount = new HashMap<Piece, Integer>();
		consecutiveUselessMovesCount = 0;
	}
	
	/**
	 * Resets the pieces to their default starting positions
	 */
	public void startNewGame()
	{
		// Black
		board [0][0] = new Rook (Color.BLACK);
		board [0][1] = new Knight (Color.BLACK);
		board [0][2] = new Bishop (Color.BLACK);
		board [0][3] = new Queen (Color.BLACK);
		board [0][4] = new King (Color.BLACK);
		board [0][5] = new Bishop (Color.BLACK);
		board [0][6] = new Knight (Color.BLACK);
		board [0][7] = new Rook (Color.BLACK);
		board [1][0] = new Pawn (Color.BLACK);
		board [1][1] = new Pawn (Color.BLACK);
		board [1][2] = new Pawn (Color.BLACK);
		board [1][3] = new Pawn (Color.BLACK);
		board [1][4] = new Pawn (Color.BLACK);
		board [1][5] = new Pawn (Color.BLACK);
		board [1][6] = new Pawn (Color.BLACK);
		board [1][7] = new Pawn (Color.BLACK);
		// White
		board [7][0] = new Rook (Color.WHITE);
		board [7][1] = new Knight (Color.WHITE);
		board [7][2] = new Bishop (Color.WHITE);
		board [7][3] = new Queen (Color.WHITE);
		board [7][4] = new King (Color.WHITE);
		board [7][5] = new Bishop (Color.WHITE);
		board [7][6] = new Knight (Color.WHITE);
		board [7][7] = new Rook (Color.WHITE);
		board [6][0] = new Pawn (Color.WHITE);
		board [6][1] = new Pawn (Color.WHITE);
		board [6][2] = new Pawn (Color.WHITE);
		board [6][3] = new Pawn (Color.WHITE);
		board [6][4] = new Pawn (Color.WHITE);
		board [6][5] = new Pawn (Color.WHITE);
		board [6][6] = new Pawn (Color.WHITE);
		board [6][7] = new Pawn (Color.WHITE);
		
		pieceCount = 32;
		whitePieceCount = blackPieceCount = 16;
		
		blackKing = new Position (0, 4);
		whiteKing = new Position (7, 4);
	}

	/**
	 * Constructs a new board object from a pre-existing board object
	 * @param b the pre-existing object to construct the board from
	 */
	public Board (Board b)
	{
		board = new Piece[8][8];
		
		// Copy over the pieces where they are on the board
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c] = b.board[r][c];
		
		// Copy over other useful information
		piecesMovedCount = new HashMap<Piece, Integer>(b.piecesMovedCount);
		whiteKing = new Position (b.whiteKing);
		blackKing = new Position (b.blackKing);
		this.consecutiveUselessMovesCount = b.consecutiveUselessMovesCount;
		this.pieceCount = b.pieceCount;
		this.whitePieceCount = b.whitePieceCount;
		this.blackPieceCount = b.blackPieceCount;
	}
	
	/**
	 * Loads a board from a file
	 * @param input the handle to the file to open
	 * @return the Scanner that was used to go through the first part of the file describing the board
	 * @throws IOException if the file is corrupt
	 */
	public Scanner loadBoardFromFile (File input) throws IOException
	{
		// Open the file
		Scanner file = new Scanner (input);
		String version = file.nextLine();
		pieceCount = 0;
		
		// Reset data structures
		board = new Piece[8][8];
		piecesMovedCount = new HashMap<Piece, Integer>();
		consecutiveUselessMovesCount = 0;
		
		// Loop to read in information for every board position
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int piece = file.nextInt();
				int timesMoved = file.nextInt();
				String color = file.next();
				Color side = color.equals("w") ? Color.WHITE : Color.black;
				
				// Increase piece counters if there is a piece at this position
				if (piece != -1)
					incrementPieceCount(side);
				
				// Initialize this position of the board depending on what the piece is
				switch (piece)
				{
				case Board.BISHOP:
					board[i][j] = new Bishop (side);
					break;
				case Board.KING:
					board[i][j] = new King (side);
					break;
				case Board.KNIGHT:
					board[i][j] = new Knight (side);
					break;
				case Board.PAWN:
					board[i][j] = new Pawn (side);
					break;
				case Board.QUEEN:
					board[i][j] = new Queen (side);
					break;
				case Board.ROOK:
					board[i][j] = new Rook (side);
					break;
				case -1:
					board[i][j] = null;
					break;
				default:
					throw new IOException ("Bad save file");
				}
				
				// If the piece has moved, put how many times it has moved into the map
				if (timesMoved != 0) {
					piecesMovedCount.put(board[i][j], timesMoved);
				}
			}
		}
		
		whiteKing = new Position (file.nextInt(), file.nextInt());
		blackKing = new Position (file.nextInt(), file.nextInt());
		
		// Return the Scanner so that it may be further used to read other parts of the file
		return file;
	}
	
	/**
	 * Gets the Piece object at a given location
	 * @param p the given location specified in a Position object
	 * @return the piece at the given location, or null if it's empty
	 */
	public Piece getPiece (Position p)
	{
		if (p.row >= 0 && p.row < board.length && p.col >= 0 && p.col < board[0].length)
			return board[p.row][p.col];
		return null;
	}
	
	/**
	 * Gets the Piece object at a given location
	 * @param r the row of the location
	 * @param c the column of the location
	 * @return the piece at the given location, or null if it's empty
	 */
	public Piece getPiece (int r, int c)
	{
		if (r >= 0 && r < board.length && c >= 0 && c < board[0].length)
			return board[r][c];
		return null;
	}
	
	/**
	 * Gets the length of the board
	 * @return the length of the board
	 */
	public int length ()
	{
		return board.length;
	}
	
	/**
	 * Gets the width of the board
	 * @return the width of the board
	 */
	public int width ()
	{
		return board[0].length;
	}
	
	/**
	 * Generates all possible moves by a given colour in this board
	 * @param side the side or colour for which to generate the moves
	 * @return a priority queue of all possible moves the given side can make, in order from best to worst
	 * 			using a very rough estimate
	 */
	public PriorityQueue<Move> getAllPossibleMovesBySide (Color side)
	{
		// For some reason making a list first is a lot faster
		List<Move> moves = new ArrayList<Move>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != null && board[row][col].getColor() == side) {
					// Add all possible moves each piece of the specified colour can make to the big list
					moves.addAll(board[row][col].generateLegalMoves(this, new Position(row, col)));
				}
			}
		}
		
		return new PriorityQueue<Move> (moves);
	}
	
	/**
	 * Generates all possible capture moves by a given colour in this board
	 * @param side the side or colour for which to generate the capture moves
	 * @return a priority queue of all possible capture moves the given side can make, in order from best to worst
	 * 			using a very rough estimate
	 */
	public PriorityQueue<Move> getAllCaptureMovesBySide (Color side)
	{
		// For some reason making a list first is a lot faster
		List<Move> moves = new ArrayList<Move>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != null && board[row][col].getColor() == side) {
					// Add all possible moves each piece of the specified colour can make to a temporary list
					List<Move> legalMoves = board[row][col].generateLegalMoves(this, new Position(row, col));
					// Delete the moves that are not capture moves
					for (int i = 0; i < legalMoves.size(); i++) {
						Move current = legalMoves.get(i);
						if (getPiece(current.target) == null) {
							legalMoves.remove(i);
							i--;
						}
					}
					// Add the remaining moves to the big list to return
					moves.addAll(legalMoves);
				}
			}
		}
		
		return new PriorityQueue<Move> (moves);
	}
	
	/**
	 * Checks for checkmates and stalemates after a player has made a move
	 * @param currentPlayerColor the colour of the player that just made a move
	 * @return the colour of the winner if there is one, or
	 * 			Game.STATE_STALEMATE if there is a stalemate, or
	 * 			null if there is neither
	 */
	public Color checkForWins (Color currentPlayerColor)
	{
		Color otherPlayerColor = (currentPlayerColor == Color.BLACK ? Color.WHITE : Color.BLACK);
		King king;
		
		// Check to see if the player that just moved has just checkmated his opponent
		if (otherPlayerColor == Color.WHITE) {
			king = (King) getPiece(whiteKing);
			if (king.isInCheck(this, whiteKing) && isCheckmated (otherPlayerColor, whiteKing))
				return currentPlayerColor;
		} else {
			king = (King) getPiece(blackKing);
			if (king.isInCheck(this, blackKing) && isCheckmated (otherPlayerColor, blackKing))
				return currentPlayerColor;
		}
		
		// Check if the player that just moved has created a stalemate, leaving his opponent with no
		// legal moves
		PriorityQueue<Move> moves = this.getAllPossibleMovesBySide(otherPlayerColor);
		if (moves.size() == 0)
			return Game.STATE_STALEMATE;
		// Check if the moves that the opponent can supposedly make are actually legal, i.e. if the opponent
		// is in check after making the move
		for (Move move : moves) {
			Board testBoard = new Board(this);
			testBoard.makeMove(move);
			Position kingPos;
			if (otherPlayerColor == Color.WHITE)
				kingPos = testBoard.getWhiteKingPos();
			else
				kingPos = testBoard.getBlackKingPos();
			king = (King) testBoard.getPiece(kingPos);
			// If a legal move is found, return null for no stalemate or checkmate
			if (!king.isInCheck(testBoard, kingPos))
				return null;
		}
		
		// Return stalemate if no legal moves could be found
		return Game.STATE_STALEMATE;
	}

	/**
	 * Checks to see if a side has been checkmated
	 * @param side the side or colour to check
	 * @param king the position of the side's king
	 * @return true if the side has been checkmated, false otherwise
	 */
	private boolean isCheckmated (Color side, Position king)
	{
		// Look at all possible moves by this side
		Collection<Move> moves = this.getAllPossibleMovesBySide(side);
		for (Move move : moves) {
			Board newBoard = new Board(this);
			newBoard.makeMove(move);
			// If any of this side's possible moves results in it no longer being in check,
			// return false for no checkmate
			if (!((King) newBoard.getPiece(side == Color.BLACK ? newBoard.blackKing : newBoard.whiteKing)).isInCheck(newBoard, (side == Color.BLACK ? newBoard.blackKing : newBoard.whiteKing)))
				return false;
		}
		return true;
	}
	
	/**
	 * Calculates the score of this board for a given colour
	 * @param cpuColor the colour to calculate the score for
	 * @return a greater value if this board favours the given colour more, and
	 * 			a lesser value if this board does not favour the given colour as much (relative to other boards)
	 */
	public int getScore (Color cpuColor)
	{
		// If the side to calculate the colour for has no king, return a very low score
		if (cpuColor == Color.WHITE) {
			if (board[whiteKing.row][whiteKing.col] == null)
				return Integer.MIN_VALUE + 1;
		} else {
			if (board[blackKing.row][blackKing.col] == null)
				return Integer.MIN_VALUE + 1;
		}
		
		int score = 0;
		
		// Look at every board position
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				if (board[r][c] != null) {
					// If this piece has the same colour as the given side, add this piece's material and position
					// score to the total
					if (board[r][c].getColor() == cpuColor) {
						score += board[r][c].getPieceValue();
						
						if (cpuColor == Color.WHITE) {
							score += whitePiecePositionScores[getTableVersion()][board[r][c].getPieceID()][r][c];
						} else {
							score += whitePiecePositionScores[getTableVersion()][board[r][c].getPieceID()][7-r][c];
						}
					} else {
						// If this piece belongs to the opponent, subtract this piece's material and position
						// score from the total
						score -= board[r][c].getPieceValue();

						if (cpuColor == Color.BLACK) {
							score -= whitePiecePositionScores[getTableVersion()][board[r][c].getPieceID()][r][c];
						} else {
							score -= whitePiecePositionScores[getTableVersion()][board[r][c].getPieceID()][7-r][c];
						}
					}
				}
			}
		}
		
		// If there aren't very many pieces left, factor in each piece's Manhattan distance to the opponent's king
		// so that checkmates can become more likely
		if (pieceCount <= 16) {
			// Increase the weight as there become less pieces remaining
			int distWeight = (16-pieceCount)*10;
			int distScore = 0;
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[r].length; c++) {
					if (board[r][c] != null) {
						if (board[r][c].getColor() == cpuColor) {
							// If this is the given side's piece, subtract its distance to the opponent's king
							// from the total score
							if (cpuColor == Color.WHITE) {
								distScore -= Math.abs(r - blackKing.row) + Math.abs(c - blackKing.col);
							} else {
								distScore -= Math.abs(r - whiteKing.row) + Math.abs(c - whiteKing.col);
							}
						} else {
							// If this is the other side's piece, add its distance to the given side's king
							// to the total score
							if (cpuColor == Color.WHITE) {
								distScore += Math.abs(r - whiteKing.row) + Math.abs(c - whiteKing.col);
							} else {
								distScore += Math.abs(r - blackKing.row) + Math.abs(c - blackKing.col);
							}
						}
					}
				}
				
			}
			score += distScore * distWeight;
		}
		
		return score;
	}
	
	/**
	 * Makes a move on this board
	 * @param move the given move to make
	 */
	public void makeMove (Move move)
	{
		Piece src = getPiece(move.source);

		// Update the move counter for the piece to be moved
		if (!piecesMovedCount.containsKey(src))
			piecesMovedCount.put(src, 1);
		else
			piecesMovedCount.put(src, piecesMovedCount.get(src) + 1);
		
		// Update king position trackers if the piece to move is a king
		if (src instanceof King) {
			if (src.getColor() == Color.WHITE)
				whiteKing = move.target;
			else
				blackKing = move.target;
		} else if (src instanceof Pawn) {
			// Promote the piece if it's a pawn and has advanced to the last row
			if (move.target.row == 0 || move.target.row == 7)
				board[move.source.row][move.source.col] = Game.currentGame.getPlayer(src.getColor()).getUpgradePiece();
			consecutiveUselessMovesCount = 0;
		}
		
		// Decrement piece counters if the move is a capture
		if (board[move.target.row][move.target.col] != null) {
			consecutiveUselessMovesCount = 0;
			decrementPieceCount(board[move.target.row][move.target.col].getColor());
		} else {
			consecutiveUselessMovesCount++;
		}
		
		// Move the piece to move in the array
		board[move.target.row][move.target.col] = board[move.source.row][move.source.col];
		board[move.source.row][move.source.col] = null;
		
		// If this is a special move (en passant or castle), perform additional necessary movements
		if (move.extra != null) {
			if (move.extra.target != null) {
				// Move the rook in a castle
				board[move.extra.target.row][move.extra.target.col] = board[move.extra.source.row][move.extra.source.col];
			} else {
				// Decrement piece counters for the en passant capture
				decrementPieceCount(board[move.extra.source.row][move.extra.source.col].getColor());
			}
			board[move.extra.source.row][move.extra.source.col] = null;
			consecutiveUselessMovesCount = 0;
		}
	}
	
	/**
	 * Gets the position of the white king on this board
	 * @return the position of the white king on this board
	 */
	public Position getWhiteKingPos ()
	{
		return whiteKing;
	}
	
	/**
	 * Gets the position of the black king on this board
	 * @return the position of the black king on this board
	 */
	public Position getBlackKingPos ()
	{
		return blackKing;
	}
	
	/**
	 * Gets the map of each piece to the number of times it moved
	 * @return the map of each piece to the number of times it moved
	 */
	public HashMap<Piece, Integer> getPiecesMovedCount ()
	{
		return piecesMovedCount;
	}
	
	/**
	 * Compares whether this board is equal to another object
	 * @param otherObject the other object
	 * @return true if the other object is a board and has identical piece positionings as this one,
	 * 			false otherwise
	 */
	public boolean equals (Object otherObject)
	{
		if (!(otherObject instanceof Board))
			return false;
		
		Board other = (Board) otherObject;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (this.board[row][col] != other.board[row][col])
					return false;
			}
		}
		
		return true;
	}
	/**
	 * Gets the number of consecutive non-capture or pawn-related moves from the last move made
	 * @return the number of consecutive non-capture or pawn-related moves from the last move made
	 */
	public int getConsecPassiveMovesCount ()
	{
		return consecutiveUselessMovesCount;
	}
	
	/**
	 * Gets the number of total pieces on the board
	 * @return the number of total pieces on the board
	 */
	public int getPiecesCount ()
	{
		return pieceCount;
	}
	
	/**
	 * Gets the number of total pieces on the board of a given colour
	 * @param side the given side or colour
	 * @return the number of total pieces on the board of the given colour
	 */
	public int getPiecesCount (Color side)
	{
		return side == Color.WHITE ? whitePieceCount : blackPieceCount;
	}
	
	/**
	 * Adds one to the piece counts for a given colour
	 * @param side the given colour for which to increment the piece counts
	 */
	public void incrementPieceCount (Color side)
	{
		if (side == Color.WHITE)
			whitePieceCount++;
		else
			blackPieceCount++;
		pieceCount++;
	}
	
	/**
	 * Subtracts one from the piece counts of a given colour
	 * @param side the given colour for which to subtract the piece counts
	 */
	public void decrementPieceCount (Color side)
	{
		if (side == Color.WHITE)
			whitePieceCount--;
		else
			blackPieceCount--;
		pieceCount--;
	}
	
	/**
	 * Gets the version of the position score table that should be used based on how many
	 * pieces are on the board
	 * @return the index of the version of the table that should be used
	 */
	public int getTableVersion()
	{
		if (pieceCount > 20)
			return 0;
		else if (pieceCount > 16)
			return 1;
		else
			return 2;
	}
}

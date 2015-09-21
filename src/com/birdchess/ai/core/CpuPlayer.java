package com.birdchess.ai.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.birdchess.ai.board.Board;
import com.birdchess.ai.board.King;
import com.birdchess.common.Move;
import com.birdchess.common.Player;
import com.birdchess.common.Position;

/**
 * Represents a computer chess player
 * @author Jeffrey Yang and Charley Huang
 * @version January 2013
 */
public class CpuPlayer extends Player {
	// The depth of the minimax search
	protected int maxDepth;
	
	// The depth of the quiescent search
	protected int captureSearchDepth;
	
	protected int cpuCount;
	
	// Scores for every possible move for a given board
	protected int [] moveScores;
	
	ThreadPoolExecutor threadPool;
	
	/**
	 * Constructs a new computer player object given its colour and search depth
	 * @param color the colour of the computer player (Color.WHITE or Color.BLACK)
	 * @param depth the depth of the search
	 */
	public CpuPlayer (Color color, int depth)
	{
		super (color);
		maxDepth = depth;
		captureSearchDepth = 1;
		cpuCount = Runtime.getRuntime().availableProcessors();
		
		threadPool = new ThreadPoolExecutor(cpuCount, cpuCount + 1, 10, TimeUnit.SECONDS,
						new ArrayBlockingQueue<Runnable>(cpuCount + 1));
		isHuman = false;
	}
	
	/**
	 * Represents a worker thread used to search a part of the minimax tree
	 * @author Jeffrey Yang and Charley Huang
	 * @version January 2013
	 */
	protected class CpuThread implements Runnable
	{
		// A copy of the starting board
		Board b;
		
		// The list of moves to score
		List<Move> moves;
		
		int scoreIndex;
		
		/**
		 * Constructs a new worker thread in preparation for a part of the minimax search
		 * @param b the board that the search begins in
		 * @param moves a list of possible moves that can be made on on the given board; 
		 * 			all to be scored
		 * @param startIndex the index for the first move in the given list, in the array of
		 * 			score values
		 */
		public CpuThread (Board b, List<Move> moves, int startIndex)
		{
			this.b = b;
			this.moves = moves;
			this.scoreIndex = startIndex;
		}
		
		/**
		 * Begins this object's given portion of the minimax search
		 */
		public void run() {
			Color opponent = (side == Color.BLACK ? Color.WHITE : Color.BLACK);
			
			// For every move assigned to this worker, make the move and evaluate and record the
			// score of the board after making the move
			for (Move currentMove : moves) {				
				Board nextBoard = new Board (b);
				nextBoard.makeMove(currentMove);
				
				int score = -findMoveScore (nextBoard, maxDepth, opponent, Integer.MIN_VALUE, Integer.MAX_VALUE);
				
				moveScores[scoreIndex++] = score;
			}
		}
	}
	
	/**
	 * Calculates the optimal move that this computer player should make on a given board
	 * @param b the given board
	 * @return an object specifying the move to make
	 */
	public Move getNextMove (Board b)
	{		
		long startTime = System.currentTimeMillis();
		
		// Generate all possible moves for this player on the board
		ArrayList<Move> legalMoves = new ArrayList<Move>(b.getAllPossibleMovesBySide(side));
		
		// From this list, eliminate the illegal moves (moves that would result in the king
		// being in check after the move is made
		for (int i = 0; i < legalMoves.size(); i++) {
			Move current = legalMoves.get(i);
			Board test = new Board(b);
			test.makeMove(current);
			King king;
			Position pos;
			if (side == Color.white)
				pos = test.getWhiteKingPos();
			else
				pos = test.getBlackKingPos();
			king = (King) test.getPiece(pos);
			if (king.isInCheck(test, pos)) {
				legalMoves.remove(i);
				i--;
			}
		}
		
		moveScores = new int [legalMoves.size()];
		
		// One thread per CPU core
		int jobsPerThread = legalMoves.size() / cpuCount;
		
		// Used to allow the algorithm to wait for all threads to finish before continuing
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
		
		// Divide and assign top nodes of the search tree to the threads
		for (int thread = 0; thread < cpuCount; thread++)
		{
			CpuThread jobs;
			if (thread == cpuCount - 1) {
				jobs = new CpuThread(b, legalMoves.subList(thread * jobsPerThread, legalMoves.size()), thread * jobsPerThread);
			} else {
				jobs = new CpuThread(b, legalMoves.subList(thread * jobsPerThread, thread * jobsPerThread + jobsPerThread), thread * jobsPerThread);
			}
			futures.add (threadPool.submit(jobs));
		}
		
		// Wait for all threads to finish
		try {
			for (Future<?> future : futures)
				future.get();
		} catch (Exception e) {
			
		}
		
		// Find the move with the highest calculated score
		int bestMoveIndex = 0;
		for (int i = 1; i < moveScores.length; i++) {
			if (moveScores[i] > moveScores[bestMoveIndex])
				bestMoveIndex = i;
		}
		
		System.out.println("AI took: " + (System.currentTimeMillis() - startTime) + "ms");
		
		// Return the move with the highest score
		return legalMoves.get(bestMoveIndex);
	}
	
	/**
	 * Recursively calculates the scores of moves using the minimax algorithm with alpha-beta pruning
	 * @param b the current board to look ahead in
	 * @param depth the current depth in this branch of the search
	 * @param c the colour of the player who is making the next move on the given board
	 * @param lowerLimit the lower limit of the scores of this board's child boards
	 * @param upperLimit the upper limit of the scores of the board's child boards
	 * @return the score of this move (the score of the board after a particular move)
	 */
	public int findMoveScore (Board b, int depth, Color c, int lowerLimit, int upperLimit)
	{
		// After the normal maximum depth is reached, proceed to quiescent search
		if (depth == 1)
			// return b.getScore(side)*(c == side ? 1 : -1);
			return findMoveScoreCaptureOnly (b, captureSearchDepth, c, lowerLimit, upperLimit);
		
		int bestScore = Integer.MIN_VALUE;
		
		Color opponent = (c == Color.BLACK ? Color.WHITE : Color.BLACK);
		
		// Look at all possible moves by the given player on the given board
		PriorityQueue<Move> legalMoves = b.getAllPossibleMovesBySide(c);
		while (!legalMoves.isEmpty())
		{
			Move currentMove = legalMoves.remove();
			Board nextBoard = new Board (b);
			nextBoard.makeMove(currentMove);
			
			// Recursively find the score of each possible move from the given board
			int currentScore = -findMoveScore (nextBoard, depth - 1, opponent, -upperLimit, -lowerLimit);
			
			// Keep track of the highest
			if (currentScore > bestScore)
				bestScore = currentScore;
			
			// Update bounds for alpha-beta pruning
			if (currentScore > lowerLimit)
				lowerLimit = currentScore;
			
			if (lowerLimit >= upperLimit)
				return lowerLimit;
		}
		
		// The score of this move or board is the optimal of those of its children
		return bestScore;
	}
	
	/**
	 * Recursively calculates the scores of moves using the minimax algorithm with alpha-beta pruning
	 * This is the quiescent stage in which only possible capture moves are checked
	 * @param b the current board to look ahead in
	 * @param depth the current depth in this branch of the search
	 * @param c the colour of the player who is making the next move on the given board
	 * @param lowerLimit the lower limit of the scores of this board's child boards
	 * @param upperLimit the upper limit of the scores of the board's child boards
	 * @return the score of this move (the score of the board after a particular move)
	 */
	public int findMoveScoreCaptureOnly (Board b, int depth, Color c, int lowerLimit, int upperLimit)
	{
		// After the quiescent search has reached its maximum depth, use the static evaluation to obtain
		// the score of the current board
		if (depth == 1)
			return b.getScore(side)*(c == side ? 1 : -1);
		
		int bestScore = Integer.MIN_VALUE;
		
		Color opponent = (c == Color.BLACK ? Color.WHITE : Color.BLACK);
		
		// Go through capture moves only
		PriorityQueue<Move> captureMoves = b.getAllCaptureMovesBySide(c);
		if (captureMoves.size() == 0)
			return b.getScore(side)*(c == side ? 1 : -1);
		
		while (!captureMoves.isEmpty())
		{
			Move currentMove = captureMoves.remove();
			Board nextBoard = new Board (b);
			nextBoard.makeMove(currentMove);
			
			// Evaluate the scores of all of this board's possible capture moves by the given player
			int currentScore = -findMoveScoreCaptureOnly (nextBoard, depth - 1, opponent, -upperLimit, -lowerLimit);
			
			if (currentScore > bestScore)
				bestScore = currentScore;
			
			// Update bounds for alpha-beta pruning
			if (currentScore > lowerLimit)
				lowerLimit = currentScore;
			
			if (lowerLimit >= upperLimit)
				return lowerLimit;
		}
		
		return bestScore;
	}
	
	/**
	 * Sets the depth of the normal minimax search
	 * @param depth a new depth for the minimax search
	 */
	public void setDepth (int depth)
	{
		maxDepth = depth;
	}
	
	/**
	 * Sets the depth of the quiescent minimax search
	 * @param depth a new depth for the quiescent minimax search
	 */
	public void setQuiescentDepth (int depth)
	{
		captureSearchDepth = depth;
	}
}

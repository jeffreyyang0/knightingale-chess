package com.birdchess.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.birdchess.ai.board.*;
import com.birdchess.ai.core.CpuPlayer;
import com.birdchess.common.*;

/** The PlayerPanel class - displays player-specific information about
 * the current game
 * @author Jeffrey Yang, Charley Huang, and Adrian Leung
 * @version January 21, 2013
 */
public class PlayerPanel extends JPanel {
	// Define and initialize variables
	Color playerColor;
	private final Image whiteKing;
	private final Image blackKing;

	private JComboBox promotionBox;

	/**
	 * Constructs a new PlayerPanel for a player given the colour of the player
	 * @param color the colour of the player
	 */
	public PlayerPanel (Color color, MainGameFrame parent)
	{
		// Set the layout
		setLayout(null);
		setPreferredSize(new Dimension(200, 720));
		playerColor = color;
		promotionBox = new JComboBox (new String [] {"Queen", "Knight", "Rook", "Bishop"});
		promotionBox.setBounds(40, 475, 100, 25);

		whiteKing = parent.getImage("whiteplayer.png");
		blackKing = parent.getImage("blackplayer.png");
		
		// Add the event listener for the pawn promotion drop-down list
		promotionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) promotionBox.getSelectedItem();

				Player player = Game.currentGame.getPlayer(playerColor);

				// Apply the new selection to the player
				if (str.equals("Queen"))
					player.setUpgradePiece(new Queen(playerColor));
				else if (str.equals("Knight"))
					player.setUpgradePiece(new Knight(playerColor));
				else if (str.equals("Rook"))
					player.setUpgradePiece(new Rook(playerColor));
				else if (str.equals("Bishop"))
					player.setUpgradePiece(new Bishop(playerColor));
			}
		});

		this.add (promotionBox);
	}

	/**
	 * Paints this PlayerPanel onto the screen
	 * @param g the graphics context to paint onto
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent (g);

		Player player = Game.currentGame.getPlayer(playerColor);

		// Set up the necessary fonts
		Font large = new Font ("Tahoma", Font.BOLD, 16);
		Font impact = new Font ("Impact", Font.PLAIN, 19);
		Font statusFont = new Font ("Verdana", Font.BOLD, 18);
		Font textFontBold = new Font ("Arial", Font.BOLD, 14);
		Font textFont = new Font ("Arial", Font.PLAIN, 13);

		// Draw the player heading
		g.setFont(large);
		g.drawString(player.getName(), 50, 25);

		// Draw the player image
		if (playerColor == Color.WHITE)
			g.drawImage (whiteKing,40,50,this);
		else
			g.drawImage (blackKing,40,50,this);

		// Draw the player's status message
		g.setFont(statusFont);
		g.drawString(player.getStatus(), 40, 220);

		// Draw move statistics
		g.setFont(textFontBold);
		g.drawString("Total Time Taken", 30, 300);
		g.drawString("Total Moves Made", 30, 350);

		g.setFont(impact);
		g.drawString(formatTime(player.getTotalTimeTaken()), 43, 325);
		g.drawString("" + player.getMovesMade(), 70, 375);

		// Draw the pawn promotion box if this is a human player
		if (player.isHuman()) {
			g.setFont(textFontBold);
			g.drawString("Pawn Promotions", 30, 460);
			promotionBox.setVisible(true);
		} else {
			promotionBox.setVisible(false);
		}

		// Draw piece statistics
		g.setFont(textFontBold);
		g.drawString("Piece Count", 30, 583);
		g.setFont(textFont);
		g.drawString("Pawn:", 30, 610);
		g.drawString("" + player.getPieceCount(Board.PAWN), 150, 610);
		g.drawString("Rook:", 30, 630);
		g.drawString("" + player.getPieceCount(Board.ROOK), 150, 630);
		g.drawString("Knight:", 30, 650);
		g.drawString("" + player.getPieceCount(Board.KNIGHT), 150, 650);
		g.drawString("Bishop:", 30, 670);
		g.drawString("" + player.getPieceCount(Board.BISHOP), 150, 670);
		g.drawString("Queen:", 30, 690);
		g.drawString("" + player.getPieceCount(Board.QUEEN), 150, 690);
	}

	/**
	 * Formats a given time in seconds into a String representation
	 * @param seconds the time in seconds
	 * @return the time as a String, of the form hh:mm:ss
	 */
	private String formatTime (int seconds)
	{
		int h = seconds / 3600;
		seconds -= h * 3600;
		int s = seconds % 60;
		int m = seconds / 60;
		return String.format("%02d:%02d:%02d", h, m, s);
	}
}

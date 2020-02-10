import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JFrame {

	public static int size;
	public static int players = 2;
	public static int playerTurn = 1;
	public static boolean gameStart = true;
	public static boolean trivialWin = false;

	// Components:
	private Container contents;
	private JButton[][] squares = new JButton[size][size];
	private JLabel invalidMoveLbl = new JLabel("Invalid Move! \t");
	private JLabel playerTurnLbl = new JLabel("Player " + playerTurn + "'s Turn \t");
	private Object[] options = { "Restart Game", "Exit Game" };

	// Colors:
	private Color colorGray = Color.decode("#d3d3d3"); // Gray squares for checker board pattern

	// Current Position:
	// Upper left corner of board is (0,0).
	private int row = 0;
	private int col = 0;

	// Images:
	private ImageIcon circle = new ImageIcon("playerTokens/Red_Circle.png"); // Player 1 Token
	private ImageIcon circleEmpty = new ImageIcon("playerTokens/Red_Circle_Empty.png");
	private ImageIcon triangle = new ImageIcon("playerTokens/Blue_Triangle.png"); // Player 2 Token
	private ImageIcon triangleEmpty = new ImageIcon("playerTokens/Blue_Triangle_Empty.png");
	private ImageIcon octagon = new ImageIcon("playerTokens/Green_Octagon.png"); // Player 3 Token
	private ImageIcon octagonEmpty = new ImageIcon("playerTokens/Green_Octagon_Empty.png");
	private ImageIcon star = new ImageIcon("playerTokens/Yellow_Star.png"); // Player 4 Token
	private ImageIcon starEmpty = new ImageIcon("playerTokens/Yellow_Star_Empty.png");

	public static void main(String[] args) {
		boardSize();
		new GameBoard();
	}

	// Get user input for the size of board
	public static void boardSize() {
		String boardSizeInput = JOptionPane
				.showInputDialog("Please enter a number greater than 2 for the n x n game board size: ");

		if (boardSizeInput == null) {
			System.exit(0);
		}

		while (Integer.parseInt(boardSizeInput) < 3) {
			boardSizeInput = JOptionPane.showInputDialog(
					"Invalid input! Please enter a number greater than 2 for the n x n game board size: ");

			if (boardSizeInput == null) {
				System.exit(0);
			}
		}

		size = Integer.parseInt(boardSizeInput);
	}

	// Constructor
	public GameBoard() {
		super("Self-Avoiding Walk Game");
		createGUI();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Size and display window:

		if (size <= 6) {
			setSize(400, 400);
		}

		if (size < 15 && size >= 7) {
			setSize(600, 600);
		}
		if (size >= 15) {
			setSize(700, 700);
		}
		setResizable(false);
		setLocationRelativeTo(null); // Centers window
		setVisible(true);
	}

	// Create the Game Board GUI
	public void createGUI() {

		contents = getContentPane();
		contents.setLayout(new GridLayout(size, size));

		// Set Up Menu Bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu game = new JMenu("Game");
		menuBar.add(game);
		JMenuItem clearBoard = new JMenuItem("New Game");
		game.add(clearBoard);
		JMenuItem boardSize = new JMenuItem("Change Size");
		game.add(boardSize);
		JMenuItem exit = new JMenuItem("Exit");
		game.add(exit);

		JMenu help = new JMenu("Help");
		menuBar.add(help);
		JMenuItem rules = new JMenuItem("How to Play");
		help.add(rules);
		JMenuItem about = new JMenuItem("About");
		help.add(about);

		JMenu playerSelect = new JMenu("Players: " + players);
		menuBar.add(playerSelect);
		JMenuItem players2 = new JMenuItem("2 Player Game");
		playerSelect.add(players2);
		JMenuItem players3 = new JMenuItem("3 Player Game");
		playerSelect.add(players3);
		JMenuItem players4 = new JMenuItem("4 Player Game");
		playerSelect.add(players4);

		menuBar.add(Box.createHorizontalGlue());

		playerTurnLbl.setFont(new Font("Lucida Grande", Font.BOLD, 12)); // Player 1's Turn
		menuBar.add(playerTurnLbl);
		playerTurnLbl.setVisible(true);

		invalidMoveLbl.setFont(new Font("Lucida Grande", Font.BOLD, 12)); // Invalid Move Alert
		invalidMoveLbl.setForeground(Color.RED);
		menuBar.add(invalidMoveLbl);
		invalidMoveLbl.setVisible(false);

		// Exit Button Action
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// Board Size Button Action
		boardSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});

		// Reset Board Button Action
		clearBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetBoard();
			}
		});

		// Rules Button Action
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"1. Players take turns moving up, down, left, or right to generate a self-avoiding walk.\n\tClick on a square to make your move.\n\n2. A player who makes a self-intersecting move loses the game. \n\n3. A player can win the game by making a self-intersecting move that creates a self-avoiding polygon.\n\tThis is only valid after at least 4 moves.",
						"Self-Avoiding Walk Game Rules", JOptionPane.PLAIN_MESSAGE);
			}
		});

		// About Button Action
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Self-Avoiding Walk Multiplayer Game\nDeveloped by Adam Binder\nCopyright 2019", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// 2 Player Button Action
		players2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				players = 2;
				resetBoard();
			}
		});

		// 3 Player Button Action
		players3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				players = 3;
				resetBoard();
			}
		});

		// 4 Player Button Action
		players4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				players = 4;
				resetBoard();
			}
		});

		// Create event handlers:
		ButtonHandler buttonHandler = new ButtonHandler();

		// Create and add board components:
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = new JButton();
				squares[i][j].setBorder(null);
				squares[i][j].setOpaque(true);
				squares[i][j].setBorderPainted(false);
				if ((i + j) % 2 != 0) {
					squares[i][j].setBackground(colorGray);
					squares[i][j].setBorder(null);
					squares[i][j].setOpaque(true);
				}
				contents.add(squares[i][j]);
				squares[i][j].addActionListener(buttonHandler);
			}
		}
	}

	// Checks if move is valid (up/down or left/right)
	private boolean isValidMove(int i, int j) {
		int rowDelta = Math.abs(i - row);
		int colDelta = Math.abs(j - col);

		if ((rowDelta == 1) && (colDelta == 0)) {
			invalidMoveLbl.setVisible(false);
			return true;
		}
		if ((colDelta == 1) && (rowDelta == 0)) {
			invalidMoveLbl.setVisible(false);
			return true;
		}
		invalidMoveLbl.setVisible(true);
		return false;
	}

	// Start the SAW; First move can be placed anywhere on game board
	private void setSaw(int i, int j) {
		squares[i][j].setIcon(resizeIcon(circleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
		row = i;
		col = j;
		playerMoves.setStartPoint(i, j);
		playerTurn = 2;
		playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
	}

	// Processes player moves
	private void processClick(int i, int j) {
		if (isValidMove(i, j) == false) {
			return;
		}

		// 2 Player Game Logic
		if (players == 2) {

			if (playerTurn == 1) {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(triangle, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(circleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn++;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}

			else {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(circle, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(triangleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn--;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}
		}

		// 3 Player Game Logic

		if (players == 3) {

			if (playerTurn == 1) {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(octagon, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(circleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn++;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}

			else if (playerTurn == 2) {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(circle, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(triangleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn++;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}

			else {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(triangle, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(octagonEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn = 1;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}
		}

		// 4 Player Game Logic

		if (players == 4) {

			if (playerTurn == 1) {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(star, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(circleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn++;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}

			else if (playerTurn == 2) {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(circle, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(triangleEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn++;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}

			else if (playerTurn == 3) {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(triangle, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(octagonEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn++;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			} else {
				checkSaw(i, j);
				if (trivialWin == true) {
					invalidMoveLbl.setVisible(true);
					trivialWin = false;
					return;
				}
				squares[row][col].setIcon(resizeIcon(octagon, squares[i][j].getWidth(), squares[i][j].getHeight()));
				squares[i][j].setIcon(resizeIcon(starEmpty, squares[i][j].getWidth(), squares[i][j].getHeight()));
				row = i;
				col = j;
				playerTurn = 1;
				playerTurnLbl.setText("Player " + playerTurn + "'s Turn \t");
			}
		}

	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();

			if (gameStart == true) {
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						if (source == squares[i][j]) {
							setSaw(i, j);
							gameStart = false;
							return;
						}
					}
				}
			} else {
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						if (source == squares[i][j]) {
							processClick(i, j);
							return;
						}
					}
				}
			}
		}
	}

	static Saw playerMoves = new Saw();

	// Adds coordinates to HashMap and checks if a win/lose has occurred
	public void checkSaw(int x, int y) {
		if (!playerMoves.hasCoord(x, y)) {
			playerMoves.addCoord(x, y);
		} else if (playerMoves.isSAP(x, y)) {
			if (playerMoves.getSawLength() <= 3) {
				trivialWin = true;
				return;
			} else {
				squares[x][y].setBackground(Color.decode("#DED258"));
				winDialog();
			}
		} else {
			squares[x][y].setBackground(Color.decode("#FFCCCB"));
			loseDialog();

		}

	}

	// Win messages for each player and asks if they would play again
	public void winDialog() {

		if (JOptionPane.showOptionDialog(null,
				"Player " + playerTurn
						+ " created a SAP and wins the game! Congratulations!\nWould you like to play again?",
				"Game Over", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]) == JOptionPane.YES_OPTION) {
			restartGame();
		} else {
			System.exit(0);
		}

	}

	// Loser dialog
	public void loseDialog() {
		if (JOptionPane.showOptionDialog(null,
				"Player " + playerTurn + " made a self-intersection and lost the game!\nWould you like to play again?",
				"Game Over", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]) == JOptionPane.YES_OPTION) {
			restartGame();
		} else {
			System.exit(0);
		}

	}

	// Restarts game and prompts board game size input
	public void restartGame() {
		playerTurn = 1;
		gameStart = true;
		dispose();
		playerMoves.clear();
		boardSize();
		new GameBoard();
	}

	// Restarts game without prompting for new game size input
	public void resetBoard() {
		playerTurn = 1;
		gameStart = true;
		dispose();
		playerMoves.clear();
		new GameBoard();
	}

	// Resizes player tokens to fit game board squares
	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance((int) (resizedWidth / 1.5), (int) (resizedHeight / 1.5),
				java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

}

package hw2;

import api.PlayerPosition;
import api.BallType;
import static api.PlayerPosition.*;
import static api.BallType.*;

/**
 * Class that models the game of three-cushion billiards.
 * 
 * @author Nathan Krieger
 */
public class ThreeCushion {

	/**
	 * Holds player A cue ball.
	 */
	private static BallType cueBallA;

	/**
	 * Holds player B cue ball.
	 */
	private static BallType cueBallB;

	/**
	 * Keeps track of the lag winner
	 */
	private static PlayerPosition gameLagWinner;

	/**
	 * Holds the amount of points needed to win.
	 */
	private static int playerPointsToWin;

	/**
	 * Keeps track of the current player.
	 */
	private PlayerPosition player;

	/**
	 * Holds the current inning.
	 */
	private int inningCount;

	/**
	 * Returns true if in an inning, otherwise false.
	 */
	private boolean isInningStarted;

	/**
	 * True if the game has started.
	 */
	private boolean isGameStarted;
	/**
	 * Holds player A score for current game.
	 */
	private int playerAScore;

	/**
	 * Holds player B score for current game.
	 */
	private int playerBScore;

	/**
	 * True if the current shot is a break shot.
	 */
	private boolean breakShot;

	/**
	 * True if shot is in progress.
	 */
	private boolean shotStarted;

	/**
	 * True if the current shot has scored.
	 */
	private boolean isShotValid;

	/**
	 * Counts the number of times the cushion is hit on current shot.
	 */
	private int cushionHit;

	/**
	 * The first object struck by cue ball.
	 */
	private BallType object1;

	/**
	 * The second object struck by cue ball.
	 */
	private BallType object2;

	/**
	 * True if there was a foul committed.
	 */
	private boolean isFoul;

	/**
	 * Holds the inning cue ball.
	 */
	private BallType inningCueBall;

	/**
	 * True if current shot is a bank shot.
	 */
	private boolean bankShot;

	/**
	 * Creates a new game of three-cushion billiards with a given lag winner and the
	 * predetermined number of points required to win the game.
	 * 
	 * @param lagWinner
	 * @param pointsToWin
	 */
	public ThreeCushion(PlayerPosition lagWinner, int pointsToWin) {
		playerPointsToWin = pointsToWin;
		gameLagWinner = lagWinner;
		inningCount = 1;
		isInningStarted = false;
	}

	/**
	 * A contest held to decide who breaks. Winner chooses their cue ball and
	 * decides if they want to break or let the opponent break.
	 * 
	 * @param selfBreak
	 * @param cueBall
	 */
	public void lagWinnerChooses(boolean selfBreak, BallType cueBall) {
		// If selfBreak is false, lag winner chooses to let the other person break.
		// Lag winner chooses cue ball either way.
		if (isGameOver()) {
			if (gameLagWinner == PLAYER_A) {
				cueBallA = cueBall;
			} else {
				cueBallB = cueBall;
			}
			if (!selfBreak) {
				if (gameLagWinner == PLAYER_A) {
					// Player B breaks.
					player = PLAYER_B;
					inningCueBall = cueBallB;
				} else if (gameLagWinner == PLAYER_B) {
					// Player A breaks.
					player = PLAYER_A;
					inningCueBall = cueBallA;
				}
			} else {
				player = gameLagWinner;
				inningCueBall = cueBall;
			}
			// Storing cue ball color for each player.
			if (cueBallA == WHITE) {
				cueBallB = YELLOW;
			} else if (cueBallA == YELLOW) {
				cueBallB = WHITE;
			}
			breakShot = true;
		}
		isGameStarted = true;
	}

	/**
	 * Indicates the cue stick has struck the given ball.
	 * 
	 * @param ball
	 */
	public void cueStickStrike(BallType ball) {
		// THE PLAYER MUST STRIKE THE RIGHT CUE BALL.
		if (!isGameOver()) {
			isShotValid = false;
			isFoul = false;
			isGameStarted = true;
			 // HIT WRONG BALL
			if (shotStarted || inningCueBall != ball) {
				foul();
				// VALID
			} else {
				isInningStarted = true;
			}
			shotStarted = true;
		}
	}

	/**
	 * Indicates the player's cue ball has struck the given ball.
	 * 
	 * @param ball
	 */
	public void cueBallStrike(BallType ball) {
		if (shotStarted) {
			// Rule 3A.
			if (object1 == null && cushionHit > 0 && breakShot) {
				foul();
			}
			if (object1 == null) {
				object1 = ball;
			} else if (object2 == null && ball != object1) {
				object2 = ball;
			}
			// Rule 3A.
			if (object1 != RED && breakShot) {
				foul();
			}
			if (breakShot && object1 == RED && cushionHit >= 3 && object2 != null) {
				isShotValid = true;
			} else if (!breakShot && object1 != inningCueBall && cushionHit >= 3 && object2 != null) {
				isShotValid = true;
			}
		}
	}

	/**
	 * Indicates the given ball has impacted the given cushion.
	 */
	public void cueBallImpactCushion() {
		cushionHit++;
		if (cushionHit >= 3 && object1 == null && object2 == null) {
			//True unless proven false.
			bankShot = true;
		}
	}

	/**
	 * Indicates that all balls have stopped motion.
	 * 
	 */
	public void endShot() {
		breakShot = false;
		if (shotStarted) {
			if (bankShot && object1 != null && object2 != null) {
				bankShot = true;
			} else {
				bankShot = false;
			}
			shotStarted = false;
			if (!isShotValid && !isFoul) {
				isInningStarted = false;
				inningCount++;
				// Switch players.
				if (player == PLAYER_A) {
					player = PLAYER_B;
					inningCueBall = cueBallB;
				} else if (player == PLAYER_B) {
					player = PLAYER_A;
					inningCueBall = cueBallA;
				}
			} else if (isShotValid && !isFoul) {
				if (player == PLAYER_A) {
					playerAScore++;
				} else if (player == PLAYER_B) {
					playerBScore++;
				}
			}
		}
		if (playerAScore >= playerPointsToWin || playerBScore >= playerPointsToWin) {
			//Reset variables at end of game.
			isGameStarted = false;
			breakShot = false;
			isInningStarted = false;
			bankShot = false;
		}
		//Reset variables at end of shot.
		isFoul = false;
		cushionHit = 0;
		object1 = null;
		object2 = null;
	}

	/**
	 * A foul immediately ends the player's inning, even if the current shot has not
	 * yet ended.
	 */
	public void foul() {
		if (!isGameOver()) {
			isInningStarted = false;
			isShotValid = false;
			isFoul = true;
			breakShot = false;
			inningCount++;
			// PLAYER SWITCH
			if (player == PLAYER_A) {
				player = PLAYER_B;
				if (inningCueBall == WHITE) {
					inningCueBall = YELLOW;
				} else if (inningCueBall == YELLOW) {
					inningCueBall = WHITE;
				}
			} else if (player == PLAYER_B) {
				player = PLAYER_A;
				if (inningCueBall == WHITE) {
					inningCueBall = YELLOW;
				} else if (inningCueBall == YELLOW) {
					inningCueBall = WHITE;
				}
			}
		}
	}

	/**
	 * Gets the number of points scored by Player A.
	 * 
	 * @return The score of player A.
	 */
	public int getPlayerAScore() {
		return playerAScore;
	}

	/**
	 * Gets the number of points scored by Player B.
	 * 
	 * @return The score of player B.
	 */
	public int getPlayerBScore() {
		return playerBScore;
	}

	/**
	 * Gets the inning number.
	 * 
	 * @return The current inning.
	 */
	public int getInning() {
		return inningCount;
	}

	/**
	 * Gets the cue ball of the current player.
	 * 
	 * @return Current cue ball.
	 */
	public BallType getCueBall() {
		return inningCueBall;
	}

	/**
	 * Gets the current player.
	 * 
	 * @return Current player
	 */
	public PlayerPosition getInningPlayer() {
		return player;
	}

	/**
	 * Returns true if and only if this is the break shot (i.e., the first shot of
	 * the game).
	 * 
	 * @return True if the current shot is a break shot, else, false.
	 */
	public boolean isBreakShot() {
		return breakShot;
	}

	/**
	 * Returns true if and only if the most recently completed shot was a bank shot.
	 * 
	 * @return True if recent shot was a bank shot.
	 */
	public boolean isBankShot() {
		return bankShot;
	}

	/**
	 * Returns true if a shot has been taken (see cueStickStrike()), but not ended
	 * (see endShot()).
	 * 
	 * @return True if shot is started.
	 */
	public boolean isShotStarted() {
		return shotStarted;
	}

	/**
	 * Returns true if the shooting player has taken their first shot of the inning.
	 * 
	 * @return True if inning is started.
	 */
	public boolean isInningStarted() {
		return isInningStarted;
	}

	/**
	 * Returns true if the game is over (i.e., one of the players has reached the
	 * designated number of points to win).
	 * 
	 * @return True if game is over.
	 */
	public boolean isGameOver() {
		return !isGameStarted;
	}

	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * <p>
	 * <tt>Player A*: X Player B: Y, Inning: Z</tt>
	 * <p>
	 * The asterisks next to the player's name indicates which player is at the
	 * table this inning. The number after the player's name is their score. Z is
	 * the inning number. Other messages will appear at the end of the string.
	 * 
	 * @return one-line string representation of the game state
	 */
	public String toString() {
		String fmt = "Player A%s: %d, Player B%s: %d, Inning: %d %s%s";
		String playerATurn = "";
		String playerBTurn = "";
		String inningStatus = "";
		String gameStatus = "";
		if (getInningPlayer() == PLAYER_A) {
			playerATurn = "*";
		} else if (getInningPlayer() == PLAYER_B) {
			playerBTurn = "*";
		}
		if (isInningStarted()) {
			inningStatus = "started";
		} else {
			inningStatus = "not started";
		}
		if (isGameOver()) {
			gameStatus = ", game result final";
		}
		return String.format(fmt, playerATurn, getPlayerAScore(), playerBTurn, getPlayerBScore(), getInning(),
				inningStatus, gameStatus);
	}
}

/**
 * ---------------------------------------------------------------------------
 * File name: Player.java
 * Project name: Jeopardy
 * ---------------------------------------------------------------------------
 * Creator's name and email: Oluwaseyi Ariyo, ariyoseyiariyo@gmail.com 
 * Creation Date: Apr 16, 2023
 * ---------------------------------------------------------------------------
 */

/**
 * Player class to store the player data.
 *
 * <hr>
 * Date created: Apr 16, 2023
 * <hr>
 */
public class Player {
	private String userName; // Stores the username
	private int score; // Stores the score of the player
	
	/**
	 * Constructor method for the player object.     
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 *
	 * <hr>
	 * @param userName the username to be assigned
	 */
	public Player(String userName) {
		this.userName = userName; // Assigns the username passed as argument
	}
	
	/**
	 * Returns the score value.      
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 *
	 * <hr>
	 * @return the current score of the player
	 */
	public int getScore() {
		return score; // Returns the current score variable
	}
	
	/**
	 * Adds the value passed to the method to the score variable.     
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 *
	 * <hr>
	 * @param points the number of points to be added to the score
	 */
	public void addPoints(int points) {
		score += points; // Adds the passed value to the score variable
	}
	
	/**
	 * Subtracts the value passed to the method from the score variable.     
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 *
	 * <hr>
	 * @param points the number of points to be subtracted from the score
	 */
	public void subtractPoints(int points) {
		score -= points; // Subtracts the passed value from the score variable
	}
	
	/**
	 * Returns the username variable.    
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 *
	 * <hr>
	 * @return the current username of the player
	 */
	public String getUserName() {
		return userName; // Returns the current username variable
	}
}

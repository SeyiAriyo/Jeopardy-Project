import java.util.Random;

/**
 * ---------------------------------------------------------------------------
 * File name: QuestionHandler.java
 * Project name: Jeopardy
 * ---------------------------------------------------------------------------
 * Creator's name and email: Oluwaseyi Ariyo, ariyoseyiariyo@gmail.com 
 * Creation Date: Apr 18, 2023
 * ---------------------------------------------------------------------------
 */

/**
 * This class is responsible for handling questions and high scores for the Jeopardy game.
 *
 * <hr>
 * Date created: Apr 18, 2023
 * <hr>
 */
public class QuestionHandler
{
    // Instance variables
	String[][][][] questions; // A four-dimensional array to hold all of the game questions
	String[] highScoreData; // An array to hold the current high score and name
	FileManager fileManager; // A FileManager object to read and write to files

	/**
	 * Constructor for the QuestionHandler class.
	 * Initializes the instance variables by reading from files.
	 */
	public QuestionHandler()
	{
		// Create a new FileManager object
		fileManager = new FileManager();

		// Read the questions from the file and initialize the questions array
		questions = fileManager.getQuestionsFromFile();

		// Read the high score data from the file and initialize the highScoreData array
		highScoreData = fileManager.getHighScoreInfoFromFile();
	}

	/**
	 * This method retrieves a single question from the questions array at a given category and value level.
	 * @param category The category of the question.
	 * @param value The point value of the question.
	 * @return An array of Strings containing the question and its answer choices.
	 */
	public String[] getQuestion(int category, int value)
	{
		// Create a new Random object to generate a random index
		Random random = new Random();

		// Get the length of the questions array at the specified category and value level
		int length = questions[category][value].length;

		// Generate a random index within the length of the questions array
	    int randomIndex = random.nextInt(length);

	    // Create a new array to hold the question and its answer choices
	     String[] question = new String[5];
	     question[0] = questions[category][value][randomIndex][0];
	     question[1] = questions[category][value][randomIndex][1];
	     question[2] = questions[category][value][randomIndex][2];
	     question[3] = questions[category][value][randomIndex][3];
	     question[4] = questions[category][value][randomIndex][4];
	     return question;
	}

	/**
	 * This method retrieves the current high score value.
	 * @return The current high score value.
	 */
	public int getHighScore()
	{
	     int highScore = Integer.parseInt(highScoreData[1]);
	     return highScore;
	}

	/**
	 * This method retrieves the name associated with the current high score value.
	 * @return The name associated with the current high score value.
	 */
	public String getHighScoreName()
	{
	     String highScore = highScoreData[0];
	     return highScore;
	}

	/**
	 * This method writes a new high score value to the file.
	 * @param highScoreName The name associated with the new high score value.
	 * @param highScore The new high score value.
	 */
	public void newHighScore(String highScoreName, int highScore) 
	{
		// Create a new array to hold the high score data
	    String[] returnHighScore = new String[]{highScoreName, Integer.toString(highScore)};
	    
	    // Write the high score data to the file
	    fileManager.writeHighScoreToFile(returnHighScore);
	}
}


import java.io.File;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * ---------------------------------------------------------------------------
 * File name: FileManager.java
 * Project name: Jeopardy
 * ---------------------------------------------------------------------------
 * Creator's name and email: Oluwaseyi Ariyo, ariyoseyiariyo@gmail.com 
 * Creation Date: Apr 16, 2023
 * ---------------------------------------------------------------------------
 */

/**
 * FileManager class is responsible for reading the question and high score files
 *
 * <hr>
 * Date created: Apr 16, 2023
 * <hr>
 * @author Forrest Cline
 */
public class FileManager
{
	// Declare class variables
	private String questionFileContents;
	private String highScoreFileContents;
	
	/**
	 * Constructor that reads the questions and high scores files and stores their contents
	 * in instance variables.
	 * 
	 * <hr>
	 * Date created: Apr 16, 2023
	 * <hr>
	 */
	public FileManager()
	{
		// Call the methods to read the question and high score files
		readQuestionsFile();
		readHighScoreFile();
	}
	
	/**
	 * Method to read the questions file
	 * 
	 *  <hr>
	 * Date created: Apr 16, 2023
	 * <hr>
	 */
	private void readQuestionsFile()
	{
		try {
			// Use the Files class to read the contents of the questions file
			String read = Files.readString(Path.of("Questions.txt"));
			// Store the contents of the file in the questionFileContents variable
			questionFileContents = read;
			
		} catch (IOException e) {
			// Print the error message to the console
			System.out.println(e);
			// Show an error message dialog to the user
			JOptionPane.showMessageDialog(null, "Unable to Locate the Questions", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * This method reads the contents of the HighScore.txt file and stores them in the highScoreFileContents field.
	 * If there is an error while reading the file, an error message is displayed using JOptionPane.
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 * <hr>
	 */
	private void readHighScoreFile()
	{
	    try {
	        // read contents of HighScore.txt file and assign it to highScoreFileContents
	        String read = Files.readString(Path.of("HighScore.txt"));
	        highScoreFileContents = read;
	        
	    } catch (IOException e) {
	        // if there's an exception, print the error message to console and show an error message dialog
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "Unable to Locate the High Scores", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	/**
	 * This method takes a category name as a String and returns the index of that category in the categories array.
	 *
	 * <hr>
	 * Date created: Apr 16, 2023
	 * <hr>
	 * 
	 * @param category The category name to find the index of
	 * @return The index of the category in the categories array, or -1 if the category is not found
	 */
	private int getCategoryIndex(String category) //{"Math","Computer History","Food","Animals","U.S.History","Sports"}
	{
	    switch (category) {
	        // assign category indexes based on the category name
	        case "Math": return 0;
	        case "Computer History": return 1;
	        case "Food": return 2;
	        case "Animals": return 3;
	        case "U.S. History": return 4;
	        case "Sports": return 5;
	        default: return -1; // return -1 if category is not found
	    }
	}

	/**
	 * Returns the index of the value in the list of point values.
	 *
	 * <hr>
	 * Date created: May 9, 2023
	 * <hr>
	 * @param value The point value of a question.
	 * @return The index of the point value in the list of point values, or -1 if not found.
	 */
	private int getValueIndex (String value)
	{
	    // Use a switch statement to map the value to an index in the list of point values
	    switch(value)
	    {
	        case "200": return 0;
	        case "400": return 1;
	        case "600": return 2;
	        case "800": return 3;
	        case "1000": return 4;
	        default : return -1; // Return -1 if the value is not found in the list of point values
	    }
	}

	/**
	 * Reads questions from a file and returns them as a 4D array.
	 * The first two dimensions of the array represent the category and difficulty level of the question.
	 * The third dimension represents the index of the question within the category and difficulty level.
	 * The fourth dimension contains the question and its possible answers.
	 *
	 * <hr>
	 * Date created: May 9, 2023
	 * <hr>
	 * 
	 * @return a 4D array of questions
	 */
	public String[][][][] getQuestionsFromFile() {

	    // Split the raw data into lines
	    String[] lines = questionFileContents.split("\\r?\\n");

	    // Initialize the 4-dimensional array with known sizes for the first two dimensions
	    // The first dimension represents the category (e.g., history, science, etc.)
	    // The second dimension represents the difficulty level (e.g., 200, 400, 600, 800, or 1000)
	    // The third dimension represents the question index for the given category and difficulty level
	    // The fourth dimension represents the component of the question, where index 0 is the question itself
	    // and indices 1 through 4 are the four possible answer choices
	    String[][][][] myArray = new String[6][5][10][5];

	    // Keep track of the current question index for each category and difficulty level
	    int[][] currentIndex = new int[6][5];

	    // Process each line of the data
	    for (String line : lines) {
	        // Split the line into its components
	        String[] parts = line.split("%");

	        // Extract the category, value, question, and answers from the line
	        String category = parts[0];
	        String value = parts[1];
	        String question = parts[2];
	        String[] answers = Arrays.copyOfRange(parts, 4, parts.length);

	        // Check if we need to expand the array for this category and difficulty level
	        // If the current index is greater than or equal to the length of the array, we need to expand it
	        if (currentIndex[getCategoryIndex(category)][getValueIndex(value)] >= myArray[getCategoryIndex(category)][getValueIndex(value)].length) {
	            myArray[getCategoryIndex(category)][getValueIndex(value)] = Arrays.copyOf(myArray[getCategoryIndex(category)][getValueIndex(value)], myArray[getCategoryIndex(category)][getValueIndex(value)].length * 2);
	        }

	        // Add the question and answers to the array
	        // The question is added to the 0th index of the 4th dimension
	        // The answers are added to indices 1-4 of the 4th dimension
	        myArray[getCategoryIndex(category)][getValueIndex(value)][currentIndex[getCategoryIndex(category)][getValueIndex(value)]][0] = question;
	        for (int i = 0; i < answers.length; i++) {
	            myArray[getCategoryIndex(category)][getValueIndex(value)][currentIndex[getCategoryIndex(category)][getValueIndex(value)]][i+1] = parts[3]+" "+answers[i];
	        }

	        // Increment the current index for this category and difficulty level
	        currentIndex[getCategoryIndex(category)][getValueIndex(value)]++;
	    }

	    // Trim the array to remove any unused entries
	    // This is done by copying each sub-array up to the current index
	    for (int i = 0; i < myArray.length; i++) {
	        for (int j = 0; j < myArray[i].length; j++) {
	            myArray[i][j] = Arrays.copyOf(myArray[i][j], currentIndex[i][j]);
	        }
	    }

	    // Return the fully-populated 4-dimensional array of questions and answers
	    return myArray;
	}

	/**
	 * Returns an array of high score information read from the high score file.
	 *
	 * <hr>
	 * Date created: May 9, 2023
	 * <hr>
	 * 
	 * @return an array of strings containing the high score information
	 */
	public String[] getHighScoreInfoFromFile()
	{
	    // Split the raw data into lines using "%" as the delimiter
	    String[] lines = highScoreFileContents.split("%");
	    
	    return lines;
	    
	}

	/**
	 * Writes new high score data to the high score file.
	 *
	 * <hr>
	 * Date created: May 9, 2023
	 * <hr>
	 * 
	 * @param newHighScoreData an array of strings containing the new high score data
	 */
	public void writeHighScoreToFile(String[] newHighScoreData)
	{
	    try {
	        File file = new File("HighScore.txt");
	        FileWriter writer = new FileWriter(file, false); // false means overwrite the existing file
	        String data = newHighScoreData[0] + "%" + newHighScoreData[1]; // Combine the new high score data into a single string using "%" as the delimiter
	        writer.write(data); // Write the data to the file
	        writer.close(); // Close the FileWriter
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "Unable to write to file","ERROR", JOptionPane.ERROR_MESSAGE); // Display an error message if there was an error writing to the file
	    }
	}

}



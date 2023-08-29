import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * The GUI class creates and manages the main graphical user interface for the Jeopardy game.
 *
 * <hr>
 * Date created: Apr 8, 2023
 * <hr>
 * Creator's name and email: Oluwaseyi Ariyo, ariyoseyiariyo@gmail.com 
 * <hr>
 */
public class GUI extends JFrame {

    // Variables for building the GUI
    private final String[] columnNames = {"Math","Computer History","Food","Animals","U.S.History","Sports"}; // column names for the JTable
    private final Object[][] data = { // data to populate the JTable
            {"Math","Computer History","Food","Animals","U.S. History","Sports"},
            {"$200", "$200", "$200", "$200", "$200", "$200"},
            {"$400", "$400", "$400", "$400", "$400", "$400"},
            {"$600", "$600", "$600", "$600", "$600", "$600"},
            {"$800", "$800", "$800", "$800", "$800", "$800"},
            {"$1000", "$1000", "$1000", "$1000", "$1000", "$1000"}
    };
    private final Border border = BorderFactory.createLineBorder(Color.BLACK); 			// a border for the JTable cells
    private Timer timer; 																// a timer to keep track of the time for each question
    private int TIMER_DELAY = 1000; 													// the delay for the timer (in milliseconds)
    private int seconds = 30; 															// the number of seconds for each question
    private int correctAnswerIndex; 													// the index of the correct answer for each question
    private int scoreValue; 															// the value of the current question
    private String question; 															// the current question
    private String[] answers; 															// the answers for the current question
    private JTable table; 																// the JTable that displays the categories and question values
    private JPanel mainPanel, questionPanel, scorePanel, endGamePanel, topRibbonPanel; 	// the main game panel and subpanels
    private JLabel timerLbl, highScoreNameLbl, highScoreLbl, playerLbl, scoreLbl; 		// labels for displaying the timer, high score, player name, and score
    private JButton[] answerButtons; 													// an array of buttons for the answer choices
    private boolean newHighScore = false; 												// a flag for whether a new high score has been achieved
    private Player player; 																// the current player
    private QuestionHandler questionHandler; 											// a question handler object for generating and displaying questions
    

   
    /**
	 *  Constructor for the GUI class.
     * Sets up the main game panel and table, and prompts the user for their name.     
	 *
	 * <hr>
	 * Date created: Apr 8, 2023
	 *
	 */
    public GUI() {
        // Sets the title of the Frame
        super("Jeopardy");

        // Set the size of the frame and make it visible
        setSize(1200, 625);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Create the QuestionHandler object
        questionHandler = new QuestionHandler();

        // Creates the Main Game Panel and Table
        initializeMainPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Prompt the user for their name
        String playerName = JOptionPane.showInputDialog(this, "Please enter your name:");

        // Loop until the user enters a non-empty name or chooses to exit
        while (playerName == null || playerName.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Must enter a username, Do you wish to exit?");

            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Goodbye!");
                this.dispose();
                return;
            } else {
                playerName = JOptionPane.showInputDialog(this, "Please enter your name:");
            }
        }

        // Create the player object
        createPlayer(playerName);
    }
    
    /**
     * Initializes the main panel, which contains the game board and score panel.
     * 
     * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void initializeMainPanel() {
        // Creates a new JPanel with a BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Initializes the top ribbon panel and adds it to the main panel at the north border
        initializeTopRibbon();
        mainPanel.add(topRibbonPanel, BorderLayout.NORTH);

        // Initializes the game board table and adds it to the main panel at the center border
        initializeBoardTable();
        mainPanel.add(table, BorderLayout.CENTER);

        // Initializes the score panel and adds it to the main panel at the south border
        initializeScorePanel();
        mainPanel.add(scorePanel, BorderLayout.SOUTH);
    }
    
    /**
     * Initializes the top ribbon panel containing the Rules and About buttons.
     * Uses a grid layout with 1 row and 2 columns and adds the buttons to the panel.
     * Sets the action listeners for the buttons to display messages to the user.
     *
     * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void initializeTopRibbon()
    {
        // Create a new panel with a grid layout of 1 row and 2 columns
        topRibbonPanel = new JPanel(new GridLayout(1,2,10,10));
        
        // Create a new Rules button and set its size and border
        JButton rulesBtn = new JButton("Rules");
        rulesBtn.setBounds(20,30,50,20);
        rulesBtn.setBorder(border);
        
        // Add the Rules button to the top ribbon panel and set its action listener
        topRibbonPanel.add(rulesBtn);
        rulesBtn.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Display a message to the user with the game rules
                JOptionPane.showMessageDialog(rootPane, 
                    "1. Enter players name. \n"
                    + "2. Pick a category and point value. \n"
                    + "3. Read the question and choose the choice you think is correct. \n"
                    +"4. A dialog box will notify you if your answer is correct or incorrect and add or" 
                    + " take away points depending on the outcome. \n"
                    +"5. Keep going until all options are answered. \n"
                    +"6. The player with the most points after everything has been answered, wins :)!");
            }
        });
        
        // Create a new About button and set its size and border
        JButton aboutBtn = new JButton("About");
        aboutBtn.setBounds(20,30,50,20);
        aboutBtn.setBorder(border);
        
        // Add the About button to the top ribbon panel and set its action listener
        topRibbonPanel.add(aboutBtn);
        aboutBtn.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Display a message to the user with information about the creators of the game
                JOptionPane.showMessageDialog(rootPane, 
                    "Jeopardy Game\n Creators:\n Oluwaseyi Ariyo\n Kwame Boahene\n Jada Carter\n Forrest Cline\n Tiffany Cusick\n Jeffrey Corrigan\n Troy Davis");
            }
        });
        
    }

    
    /**
     * Creates the Score Panel that displays the high score, high score name, current player, and current score.
     * 
     * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void initializeScorePanel()
    {
        // Create a new JPanel with a 2x2 grid layout and set it as the score panel
        scorePanel = new JPanel(new GridLayout(2,2,10,10));
        
        // Create labels for the high score and high score name
        highScoreLbl = new JLabel(""+questionHandler.getHighScore()); // Convert high score to string and set it as text for label
        highScoreNameLbl = new JLabel(questionHandler.getHighScoreName()); // Set high score name as text for label
        
        // Create labels for the current player and current score
        playerLbl = new JLabel("Player: ");
        scoreLbl = new JLabel("$"+0); // Set initial score to 0 and add dollar sign
        
        // Set font size and type for labels
        highScoreLbl.setFont(new Font("Arial", Font.BOLD, 18));
        highScoreNameLbl.setFont(new Font("Arial", Font.BOLD, 18));
        playerLbl.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLbl.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Center align the labels
        highScoreLbl.setHorizontalAlignment (JLabel.CENTER);
        highScoreNameLbl.setHorizontalAlignment (JLabel.CENTER);
        playerLbl.setHorizontalAlignment (JLabel.CENTER);
        scoreLbl.setHorizontalAlignment (JLabel.CENTER);
        
        // Add labels to the score panel
        scorePanel.add (highScoreNameLbl);
        scorePanel.add (playerLbl);
        scorePanel.add (highScoreLbl);
        scorePanel.add (scoreLbl);
    }

    /**
     * Initializes the board table with the categories and point values. 
     * Sets the table to be uneditable and sets the cell renderer to center align text and set the color scheme. 
     *
     * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void initializeBoardTable()
    {
        // create the JTable object with the given data and column names
        table = new JTable(data,columnNames) {
            // override isCellEditable method to make the cells uneditable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // override prepareRenderer method to center align text and set the color scheme of the cells
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                component.setForeground(Color.WHITE);
                component.setBackground(Color.BLUE);
                ((JLabel) component).setHorizontalAlignment (JLabel.CENTER);
                if (getValueAt(row, column) == null) {
                    component.setBackground(Color.BLUE);
                }
                return component;
            }
        };

        // set the row height, font, and fills viewport height
        table.setRowHeight(85);
        table.setFont(new Font("Arial", Font.PLAIN, 24));
        table.setFillsViewportHeight(true);

        // add a mouse listener to the table
        table.addMouseListener(new MyMouseAdapter());
    }

    
    /**
     * The MyMouseAdapter class is a custom implementation of the MouseAdapter class that handles mouse click events on the Jeopardy game board table.
     * When a user clicks on a valid question cell in the table, the corresponding question panel is displayed and the board panel is hidden.
     * * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private class MyMouseAdapter extends MouseAdapter
    {
        /**
         * Handles the mouseClicked event for the Jeopardy game board table.
         * When a user clicks on a valid question cell in the table, the corresponding question panel is displayed and the board panel is hidden.
         * @param e The MouseEvent object that contains information about the mouse click event.
         */
    	 @Override
    	    public void mouseClicked(MouseEvent e) {
    	        // Get the row and column that was clicked
    	        int row = table.rowAtPoint(e.getPoint());
    	        int column = table.columnAtPoint(e.getPoint());
    	        
    	        // Check if the clicked cell is valid and not already selected
    	        if (row >= 1 && column >= 0 && table.getValueAt(row, column) != null) 
    	        {
    	            // Remove the score value from the board and get the corresponding question panel
    	            table.setValueAt(null, row, column);
    	            scoreValue = getScoreValue(row);
    	            questionPanel = createQuestionPanel(questionHandler.getQuestion (column, row-1));
    	            
    	            // Switch to question panel
    	            getContentPane().removeAll();
    	            getContentPane().add(questionPanel, BorderLayout.CENTER);
    	            revalidate();
    	            repaint();
    	        }
    	    }
    	}
    
    /**
     * Creates a JPanel that displays the given question and answer choices.
     *
     * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     * @param QA an array of Strings representing the question and answer choices
     * @return a JPanel containing the question and answer choices
     */
    private JPanel createQuestionPanel(String[] QA) {
        // Shuffle the answer choices to randomize their order
        shuffleAnswers(QA);

        // Create a new JPanel with a BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Create a JTextPane to display the question text
        JTextPane questionPane = new JTextPane();
        questionPane.setText(question);
        questionPane.setBackground(Color.BLUE);
        questionPane.setForeground(Color.WHITE);
        questionPane.setFont(new Font("Arial", Font.BOLD, 40));
        questionPane.setBorder(border);

        // Center the text horizontally
        StyledDocument doc = questionPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Center the text vertically
        StyledDocument doc1 = questionPane.getStyledDocument();
        SimpleAttributeSet vertical = new SimpleAttributeSet();
        StyleConstants.setAlignment(vertical, StyleConstants.ALIGN_CENTER);
        StyleConstants.setSpaceAbove(vertical, 0);
        StyleConstants.setSpaceBelow(vertical, 0);
        StyleConstants.setLineSpacing(vertical, .2f);
        Element paragraph = doc1.getParagraphElement(0);
        doc1.setParagraphAttributes(paragraph.getStartOffset(), paragraph.getEndOffset(), vertical, false);

        // Add the question pane to the center of the panel
        panel.add(questionPane, BorderLayout.CENTER);

        // Create a JPanel to hold the answer buttons
        JPanel answerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create an array of JButtons to hold the answer choices
        answerButtons = new JButton[4];
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JButton(answers[i]);
            answerButtons[i].setFont(new Font("Arial", Font.PLAIN, 36));
            answerButtons[i].addActionListener(new BtnClicked());
            answerPanel.add(answerButtons[i]);
        }

        // Create a new timer and add it to the top of the panel
        newTimer();
        panel.add(timerLbl, BorderLayout.NORTH);

        // Add the answer panel to the bottom of the panel
        panel.add(answerPanel, BorderLayout.SOUTH);

        // Return the completed panel
        return panel;
    }

    /**
     * Randomly shuffles the order of the answer choices for a given question.
     *
     * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     * @param QA a String array containing the question at index 0 and answer choices at indices 1-4
     */
    public void shuffleAnswers(String[] QA)
    {
    	// Set the question text to the first element in the array
    	question = QA[0];
    	
    	// Copy the answer choices from the input array to the 'answers' array
    	answers = Arrays.copyOfRange (QA, 1, 5);
    	
    	// The correct answer index is initially set to 0 (the first answer choice)
    	correctAnswerIndex = 0;
    	
    	// Convert the answer choices array to a List so it can be shuffled
    	List<String> answerList = Arrays.asList(answers);
    	
    	// Shuffle the answer choices
    	Collections.shuffle(answerList);
    	
    	// Update the correct answer index to reflect the new position of the correct answer choice
    	correctAnswerIndex = answerList.indexOf(QA[1]);
    	
    	// Convert the shuffled answer choices List back to an array and update the 'answers' array
    	answerList.toArray(answers);
    }

    
    /**
     * Handles the click events for the answer buttons in the question panel.
     * 
     * * <hr>
	 * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private class BtnClicked implements ActionListener {
        /**
         * Called when an answer button is clicked.
         * 
         * <hr>
         * Date created: Apr 8, 2023
         * 
         * <hr>
         * 
         * @param e the action event triggered by the button click
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            // Check if the clicked button is the correct answer button.
            for (int i = 0; i < answerButtons.length; i++) {
                if (clickedButton == answerButtons[i]) {
                    if (i == correctAnswerIndex) {
                        // The clicked button is the correct answer.
                        timer.stop();
                        updatePlayerScore(true, scoreValue);
                        closeQuestionPane();
                        JOptionPane.showMessageDialog(null, "Correct!");
                    } else {
                        // The clicked button is the wrong answer.
                        timer.stop();
                        updatePlayerScore(false, scoreValue);
                        closeQuestionPane();
                        JOptionPane.showMessageDialog(null, "Incorrect!");
                    }
                }
            }
        }
    }

    /**
     * Closes the current question panel and switches back to the main panel,
     * or ends the game if there are no more questions left.
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void closeQuestionPane()
    {
        // Check if there are any more questions left to be answered
        if(isTableEmpty(table))
        {
            // If there are no more questions, end the game
            endGame();
        }
        else {
            // If there are more questions, switch back to table panel
            getContentPane().removeAll(); // remove all components from the content pane
            getContentPane().add(mainPanel, BorderLayout.CENTER); // add main panel to the content pane
            revalidate(); // validate the container hierarchy
            repaint(); // repaint the container hierarchy
        }
    }

    /**
     * Starts a new timer with a countdown of 30 seconds, updating the timer
     * label every second. If the timer reaches 15 seconds, the label will start
     * flashing to indicate the remaining time. If the timer reaches 0 seconds,
     * the game will end and the player's score will be updated accordingly.
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void newTimer()
    {
        // If there is an existing timer, stop it
        if(timer != null)
            timer.stop();
        
        seconds = 30; // set the timer to 30 seconds
        timerLbl = new JLabel(""+seconds);
        timerLbl.setBackground(Color.RED); // set the background color of the timer label to red
        timerLbl.setOpaque(false);
        timerLbl.setFont(new Font("Arial", Font.PLAIN, 36)); // set the font of the timer label
        timerLbl.setHorizontalAlignment(JLabel.CENTER); // set the horizontal alignment of the timer label
        timer = new Timer(TIMER_DELAY, e->{
            seconds--;
            timerLbl.setText(""+seconds); // update the timer label with the remaining seconds
            if(seconds <=15)
            {
                if(seconds % 2 == 0)
                    timerLbl.setOpaque(true); // make the timer label opaque every other second when there are 15 seconds or less left
                else
                    timerLbl.setOpaque(false);
            }
            if(seconds <= 0) {
                timer.stop(); // stop the timer when the time runs out
                updatePlayerScore(false,scoreValue); // update the player's score as they ran out of time
                closeQuestionPane(); // close the question pane
                JOptionPane.showMessageDialog(null, "Ran Out of Time!"); // show a message dialog to inform the player that they ran out of time
            }
        });
        timer.start(); // start the timer
    }

    
    /**
     * Returns the value of a given score row
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     * 
     * @param row the row of the score
     * @return the value of the score
     */
    private int getScoreValue(int row) {
        switch (row) {
            case 1: 
                return 200;
            case 2: 
                return 400;
            case 3: 
                return 600;
            case 4: 
                return 800;
            case 5: 
                return 1000;
            default: 
                return 0; // default case, returns 0 for an invalid row
        }
    }

    /**
     * Checks if a given JTable is empty or not
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     * 
     * @param table the JTable to check
     * @return true if the JTable is empty, false otherwise
     */
    private boolean isTableEmpty(JTable table) {
        for (int row = 1; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                if (table.getValueAt(row, col) != null) {
                    return false; // found a non-null value, table is not empty
                }
            }
        }
        return true; // all values are null, table is empty
    }

    /**
     * This method creates a new player object with the specified username and displays the username on the player label.
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     * 
     * @param userName The username of the player to be created.
     */
    private void createPlayer(String userName) {
    	player = new Player(userName); // create a new Player object with the given username
    	playerLbl.setText(player.getUserName()); // set the label to display the player's username
    }

    /**
     * This method updates the player's score by adding or subtracting the specified value based on whether the answer was correct or not.
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     * 
     * @param correct A boolean indicating if the answer was correct or not.
     * @param value The value to be added or subtracted from the player's score.
     */
    private void updatePlayerScore(boolean correct, int value) {
    	if (correct) {
    		player.addPoints(value); // if the answer is correct, add the value to the player's score
    	} else {
    		player.subtractPoints(value); // if the answer is incorrect, subtract the value from the player's score
    	}
    	if (player.getScore() < 0) {
    	    scoreLbl.setForeground(Color.RED);
        } else {
    	    scoreLbl.setForeground(Color.BLACK);
        }
    	scoreLbl.setText("Score: " + player.getScore()); // update the score label to display the player's current score
    }

    /**
     * This method checks if the player's score is higher than the current high score, updates the high score if necessary, and displays a message.
     * 
     * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     * 
     * @param playerScore The player's current score to be compared to the high score.
     */
    private void checkHighScore(int playerScore) {
    	if (playerScore > questionHandler.getHighScore()) { // check if the player's score is higher than the current high score
    		highScoreLbl.setText("$" + playerScore); // update the label to display the player's score as the new high score
    		highScoreNameLbl.setText("New High Score: " + player.getUserName()); // update the label to display the player's username as the new high scorer
    		newHighScore = true; // set a flag to indicate that a new high score has been achieved
    		questionHandler.newHighScore(player.getUserName(), player.getScore()); // update the high score record with the new high score
    	}
    }

    
    /**
     * Displays end of game screen with player's score and option to start a new game
     * 
     * * <hr>
     * Date created: Apr 8, 2023
     * 
     * <hr>
     */
    private void endGame() {
        // create wrapper panel to hold end game panel and new game button
        JPanel endGameWrapperPanel = new JPanel(new BorderLayout());

        // create panel to display end of game message and player score
        endGamePanel = new JPanel(new GridLayout(4,1,20,20));

        // create and add end of game message label to panel
        JLabel endofGame = new JLabel("Game Over! Thank you for Playing our Game");
        endofGame.setHorizontalAlignment(JLabel.CENTER);
        endofGame.setFont(new Font("Arial", Font.BOLD, 48));
        endGamePanel.add(endofGame);

        // check if player achieved a new high score and display appropriate message and score
        checkHighScore(player.getScore());
        if(newHighScore) {
            // create and add new high score message label to panel
            JLabel newHighScoreTxt = new JLabel("NEW HIGH SCORE!");
            newHighScoreTxt.setHorizontalAlignment(JLabel.CENTER);
            newHighScoreTxt.setFont(new Font("Arial", Font.BOLD, 36));
            endGamePanel.add (newHighScoreTxt);

            // add high score name and score labels to panel
            endGamePanel.add(highScoreNameLbl);
            endGamePanel.add(highScoreLbl);
            highScoreNameLbl.setFont(new Font("Arial", Font.BOLD, 36));
            highScoreLbl.setFont(new Font("Arial", Font.BOLD, 36));
        } else {
            // add player name and score labels to panel
            endGamePanel.add(playerLbl);
            endGamePanel.add(scoreLbl);
            playerLbl.setFont(new Font("Arial", Font.BOLD, 36));
            scoreLbl.setFont(new Font("Arial", Font.BOLD, 36));
        }

        // create new game button and add action listener to start a new game
        JButton newGameBtn = new JButton("New Game");
        newGameBtn.setPreferredSize(new Dimension(100, 50)); // set button size
        newGameBtn.setBorder (border);
        newGameBtn.setBackground (Color.GREEN);
        newGameBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // close current game window and open new game window
                dispose();
                new GUI();
            }
        });

        // add new game button and end game panel to wrapper panel and update content pane
        endGameWrapperPanel.add(newGameBtn,BorderLayout.SOUTH);
        endGameWrapperPanel.add (endGamePanel,BorderLayout.CENTER);
        getContentPane().removeAll();
        getContentPane().add(endGameWrapperPanel);

        // revalidate and repaint content pane to display changes
        revalidate();
        repaint();
    }

}

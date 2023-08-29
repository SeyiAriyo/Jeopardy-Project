import javax.swing.SwingUtilities;

/**
 * ---------------------------------------------------------------------------
 * File name: Main.java
 * Project name: Jeopardy
 * ---------------------------------------------------------------------------
 * Creator's name and email: Oluwaseyi Ariyo, ariyoseyiariyo@gmail.com 
 * Creation Date: Apr 10, 2023
 * ---------------------------------------------------------------------------
 */

/**
 * Main class to run the Jeopardy game
 *
 * <hr>
 * Date created: Apr 10, 2023
 * <hr>
 * 
 * @author
 *     Forrest Cline
 */
public class Main {

    /**
     * Main method that creates an instance of the GUI object to play the game.
     *
     * <hr>
     * Date created: Apr 10, 2023
     *
     * <hr>
     * 
     * @param args
     *     arguments passed to the program (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI(); // creates an instance of the GUI
            }
        });
    }
}

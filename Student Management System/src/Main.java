import ui.MainFrame; // Import the MainFrame class from the ui package
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Student Management System.
 * This class is responsible for starting the GUI application.
 */
public class Main {

    /**
     * The main method, which is the starting point of execution.
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        
        // Use SwingUtilities.invokeLater to ensure the GUI is created and updated
        // on the Event Dispatch Thread (EDT). This is the standard, safe way
        // to start any Swing application.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Create an instance of our main window
                MainFrame frame = new MainFrame();
                
                // 2. Make the window visible
                frame.setVisible(true);
            }
        });
    }
}
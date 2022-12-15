import javax.swing.*;
import java.awt.*;

/**
 * This is the start class for the SearchToolbox application.
 * It can be compiled both as a Java application and a Java
 * applet.
 *
 * @author Johan Hagelbäck
 */
public class SearchToolbox extends JApplet {

    /**
     * @param args None
     */
    public static void main(String[] args) {
        new SearchToolbox();
    }

    public SearchToolbox() {
        //Select if you want to compile as 
        //application or applet.

        //startApplet();
        startApplication();
    }

    /**
     * Starts as an applet.
     */
    private void startApplet() {
        this.setSize(750, 430);
        this.getContentPane().setLayout(new FlowLayout());
        this.getContentPane().add(new GUI());
        this.setVisible(true);
    }

    /**
     * Starts as an application.
     */
    private void startApplication() {
        JFrame frame = new JFrame("Search Toolbox");
        frame.setSize(750, 450);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new GUI());
        frame.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

/**
 * GUI for the search visualization toolbox.
 *
 * @author Johan Hagelbäck
 */
public class GUI extends JPanel implements ActionListener, Runnable, MouseMotionListener, MouseListener {
    private MapPanel mPanel;
    private JPanel bPanel;
    private JPopupMenu pp;
    private SearchMethod method;
    private JLabel visitedLabel;
    private JLabel lengthLabel;
    private JLabel pieceLabel;
    private NumberFormat format;
    private boolean running;
    private JSlider speed;
    private JButton[] buttons;
    private Node toMove = null;

    /**
     * Creates the GUI.
     */
    public GUI() {
        format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);

        method = new DepthFirst();
        initComponents();
    }

    /**
     * Initializes all GUI components.
     */
    private void initComponents() {
        setLayout(new FlowLayout());

        bPanel = new JPanel();
        bPanel.setPreferredSize(new Dimension(180, 400));
        bPanel.setLayout(new FlowLayout());

        //Buttons
        buttons = new JButton[6];
        buttons[0] = new JButton("Depth-First Search");
        buttons[0].setActionCommand("DepthFirstSearch");
        buttons[0].setOpaque(true);
        buttons[0].addActionListener(this);
        buttons[0].setPreferredSize(new Dimension(160, 25));
        bPanel.add(buttons[0]);
        buttons[1] = new JButton("Breadth-First Search");
        buttons[1].setActionCommand("BreadthFirstSearch");
        buttons[1].addActionListener(this);
        buttons[1].setPreferredSize(new Dimension(160, 25));
        bPanel.add(buttons[1]);
        buttons[2] = new JButton("Uniform-Cost Search");
        buttons[2].setActionCommand("UniformCost");
        buttons[2].addActionListener(this);
        buttons[2].setPreferredSize(new Dimension(160, 25));
        bPanel.add(buttons[2]);
        buttons[3] = new JButton("Bidirectional");
        buttons[3].setActionCommand("Bidirectional");
        buttons[3].addActionListener(this);
        buttons[3].setPreferredSize(new Dimension(160, 25));
        bPanel.add(buttons[3]);
        buttons[4] = new JButton("Greedy Search");
        buttons[4].setActionCommand("GreedySearch");
        buttons[4].addActionListener(this);
        buttons[4].setPreferredSize(new Dimension(160, 25));
        bPanel.add(buttons[4]);
        buttons[5] = new JButton("A-star");
        buttons[5].setActionCommand("Astar");
        buttons[5].addActionListener(this);
        buttons[5].setPreferredSize(new Dimension(160, 25));
        bPanel.add(buttons[5]);

        activateButton(0);

        JPanel jp = new JPanel();
        jp.setPreferredSize(new Dimension(160, 15));
        bPanel.add(jp);

        speed = new JSlider(JSlider.HORIZONTAL, 10, 300, 100);
        speed.setToolTipText("Speed");
        speed.setPreferredSize(new Dimension(160, 35));
        bPanel.add(speed);

        JButton bt;
        bt = new JButton("Run");
        bt.setPreferredSize(new Dimension(160, 25));
        bPanel.add(bt);
        bt.addActionListener(this);

        jp = new JPanel();
        jp.setPreferredSize(new Dimension(160, 15));
        bPanel.add(jp);

        visitedLabel = new JLabel("Visited nodes: 0");
        visitedLabel.setForeground(new Color(39, 117, 219));
        visitedLabel.setPreferredSize(new Dimension(160, 20));
        bPanel.add(visitedLabel);

        lengthLabel = new JLabel("Path length: 0");
        lengthLabel.setForeground(Color.red);
        lengthLabel.setPreferredSize(new Dimension(160, 20));
        bPanel.add(lengthLabel);

        pieceLabel = new JLabel("Path segments: 0");
        pieceLabel.setForeground(Color.red);
        pieceLabel.setPreferredSize(new Dimension(160, 20));
        bPanel.add(pieceLabel);

        add(bPanel);

        mPanel = new MapPanel();
        add(mPanel);

        //Popup menu
        pp = new JPopupMenu();

        mPanel.addMouseListener(this);
        mPanel.addMouseMotionListener(this);
    }

    /**
     * Sets one the search method buttons as active.
     *
     * @param index The index number of the button
     */
    private void activateButton(int index) {
        for (JButton b : buttons) {
            b.setForeground(Color.gray);
        }
        buttons[index].setForeground(Color.blue);
    }

    /**
     * Called when the mouse is dragged in the GUI.
     * Used the the user move nodes around in the map.
     *
     * @param e mouse event
     */
    public void mouseDragged(MouseEvent e) {
        if (toMove != null) {
            int eX = e.getX();
            int eY = e.getY();

            int nX = (int) Math.round((double) eX / 10.0);
            int nY = (int) Math.round((double) eY / 10.0);

            toMove.x = nX;
            toMove.y = nY;

            mPanel.updateUI();
            mPanel.repaint();
        }
    }

    /**
     * Unused mouse handler methods.
     */
    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * Called when the mouse button is released in the GUI.
     *
     * @param e mouse event.
     */
    public void mouseReleased(MouseEvent e) {
        toMove = null;
    }

    /**
     * Called when the mouse button is pressed in the GUI.
     * Used then the user clicks in the map panel.
     *
     * @param e mouse event.
     */
    public void mousePressed(MouseEvent e) {
        //See if a node has been clicked
        Vector<Node> nodes = Map.getInstance().getNodes();

        Node node = null;
        toMove = null;
        for (Node n : nodes) {
            float nX = (float) (n.x * 10);
            float nY = (float) (n.y * 10);

            float dist = (float) Math.sqrt(Math.pow(nX - e.getX(), 2) + Math.pow(nY - e.getY(), 2));
            if (dist <= 8) {
                node = n;
                break;
            }
        }

        //Left mouse button: move nodes around
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (node != null) {
                toMove = node;
            }
        }

        //Right mouse button: show a popup menu
        if (SwingUtilities.isRightMouseButton(e)) {
            if (node != null) {
                //Show the popup menu for a specific node
                pp.removeAll();
                JMenuItem mi = new JMenuItem("Set as '" + node.label + "' as start node");
                mi.setActionCommand("Start:" + node.label);
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Set '" + node.label + "' as end node");
                mi.setActionCommand("End:" + node.label);
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Remove node '" + node.label + "'");
                mi.setActionCommand("Remove:" + node.label);
                mi.addActionListener(this);
                pp.add(mi);

                pp.show(e.getComponent(), e.getX(), e.getY());
            } else {
                //Show the default popup menu
                pp.removeAll();
                JMenuItem mi = new JMenuItem("Add Node");
                int x = (int) Math.round((double) e.getX() / 10.0);
                int y = (int) Math.round((double) e.getY() / 10.0);
                mi.setActionCommand("AddNode " + x + " " + y);
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Add Link");
                mi.setActionCommand("AddLink");
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Remove Node");
                mi.setActionCommand("RemNode");
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Remove Link");
                mi.setActionCommand("RemLink");
                mi.addActionListener(this);
                pp.add(mi);

                mi = new JMenuItem("Reload Map");
                mi.setActionCommand("Reload");
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Load");
                mi.setActionCommand("Load");
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Save");
                mi.setActionCommand("Save");
                mi.addActionListener(this);
                pp.add(mi);
                mi = new JMenuItem("Save as");
                mi.setActionCommand("Save as");
                mi.addActionListener(this);
                pp.add(mi);

                pp.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Event handler for buttons and menus.
     *
     * @param e action event
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        /* Depth first search is selected */
        if (cmd.equalsIgnoreCase("DepthFirstSearch")) {
            if (!running) {
                activateButton(0);

                method = new DepthFirst();
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                lengthLabel.setText("Path length: 0");
                pieceLabel.setText("Path pieces: 0");
            }
        }
        /* Breadth first search is selected */
        if (cmd.equalsIgnoreCase("BreadthFirstSearch")) {
            if (!running) {
                activateButton(1);

                method = new BreadthFirst();
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                lengthLabel.setText("Path length: 0");
                pieceLabel.setText("Path pieces: 0");
            }
        }
        /* Uniform-cost search is selected */
        if (cmd.equalsIgnoreCase("UniformCost")) {
            if (!running) {
                activateButton(2);

                method = new UniformCostSearch();
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                lengthLabel.setText("Path length: 0");
                pieceLabel.setText("Path pieces: 0");
            }
        }
        /* Bi-directional search is selected */
        if (cmd.equalsIgnoreCase("Bidirectional")) {
            if (!running) {
                activateButton(3);

                method = new Bidirectional();
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                lengthLabel.setText("Path length: 0");
                pieceLabel.setText("Path pieces: 0");
            }
        }
        /* Greedy Search is selected */
        if (cmd.equalsIgnoreCase("GreedySearch")) {
            if (!running) {
                activateButton(4);

                method = new GreedySearch();
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                lengthLabel.setText("Path length: 0");
                pieceLabel.setText("Path pieces: 0");
            }
        }
        /* A-star is selected */
        if (cmd.equalsIgnoreCase("Astar")) {
            if (!running) {
                activateButton(5);

                method = new Astar();
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                lengthLabel.setText("Path length: 0");
                pieceLabel.setText("Path pieces: 0");
            }
        }
        /* A search for a path is started */
        if (cmd.equalsIgnoreCase("Run")) {
            if (!running) {
                Thread thr = new Thread(this);
                thr.start();
            }
        }
        /* Change start node in the map */
        if (cmd.startsWith("Start:")) {
            if (!running) {
                String[] tokens = cmd.split(":");
                Map.getInstance().setAsStartNode(tokens[1]);
                mPanel.updateUI();
                mPanel.repaint();
            }
        }
        /* Change end node in the map */
        if (cmd.startsWith("End:")) {
            if (!running) {
                String[] tokens = cmd.split(":");
                Map.getInstance().setAsEndNode(tokens[1]);
                mPanel.updateUI();
                mPanel.repaint();
            }
        }
        /** Removes a specific node in the map */
        if (cmd.startsWith("Remove:")) {
            if (!running) {
                String[] tokens = cmd.split(":");
                boolean ok = Map.getInstance().removeNode(tokens[1]);
                if (ok) {
                    mPanel.updateUI();
                    mPanel.repaint();
                }
            }
        }
        /** Removes a specific link in the map */
        if (cmd.startsWith("RemoveLink:")) {
            if (!running) {
                String[] tokens = cmd.split(":");
                boolean ok = Map.getInstance().removeLink(tokens[1]);
                if (ok) {
                    mPanel.updateUI();
                    mPanel.repaint();
                }
            }
        }
        /** Adds a new node in the map */
        if (cmd.startsWith("AddNode")) {
            if (!running) {
                String[] tokens = cmd.split(" ");
                int x = Integer.parseInt(tokens[1]);
                int y = Integer.parseInt(tokens[2]);

                String str = Map.getInstance().getNewNodeLabel();
                if (!str.equals("0")) {
                    boolean ok = Map.getInstance().addNode(x, y, str);
                    if (ok) {
                        mPanel.updateUI();
                        mPanel.repaint();
                    }
                } else {
                    str = JOptionPane.showInputDialog(null, "Enter node label", "", 1);
                    if (str != null && !str.equals("")) {
                        boolean ok = Map.getInstance().addNode(x, y, str);
                        if (ok) {
                            mPanel.updateUI();
                            mPanel.repaint();
                        }
                    }
                }
            }
        }
        /** Removes a node in the map. An input dialog asking for node label is shown. */
        if (cmd.equalsIgnoreCase("RemNode")) {
            if (!running) {
                String str = JOptionPane.showInputDialog(null, "Enter node label", "", 1);
                if (str != null && !str.equals("")) {
                    boolean ok = Map.getInstance().removeNode(str);
                    if (ok) {
                        mPanel.updateUI();
                        mPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to find node with label '" + str + "'", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
        /** Removes a link in the map. An input dialog asking for link label is shown. */
        if (cmd.equalsIgnoreCase("RemLink")) {
            if (!running) {
                String n1 = JOptionPane.showInputDialog(null, "Enter node 1 label", "", 1);
                if (n1 != null && !n1.equals("")) {
                    String n2 = JOptionPane.showInputDialog(null, "Enter node 2 label", "", 1);
                    if (n2 != null && !n2.equals("")) {
                        String label = n1 + "-" + n2;
                        boolean ok = Map.getInstance().removeLink(label);
                        if (ok) {
                            mPanel.updateUI();
                            mPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(null, "Unable to find link '" + label + "'", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        }
        /* Adds a new link in the map. An input dialog asking for which nodes to connect is shown. */
        if (cmd.equalsIgnoreCase("AddLink")) {
            if (!running) {
                String n1 = JOptionPane.showInputDialog(null, "Enter label of node 1", "", 1);
                if (n1 != null && !n1.equals("")) {
                    String n2 = JOptionPane.showInputDialog(null, "Enter label of node 2", "", 1);
                    if (n2 != null && !n2.equals("")) {
                        boolean ok = Map.getInstance().addLink(n1, n2);
                        if (ok) {
                            mPanel.updateUI();
                            mPanel.repaint();
                        }
                    }
                }
            }
        }
        /** Reloads the current map */
        if (cmd.equalsIgnoreCase("Reload")) {
            Map.getInstance().reload();
            mPanel.updateUI();
            mPanel.repaint();
        }
        /* Loads map 1 */
        if (cmd.equalsIgnoreCase("Load1")) {
            Map.getInstance().load("1");
            mPanel.updateUI();
            mPanel.repaint();
        }
        /* Loads map 2 */
        if (cmd.equalsIgnoreCase("Load2")) {
            Map.getInstance().load("2");
            mPanel.updateUI();
            mPanel.repaint();
        }
        /* Used to dump the map data to text. Used when using the GUI to create new maps. */
        if (cmd.equalsIgnoreCase("DumpMap")) {
            Map.getInstance().dumpMap();
        }
    }

    public void run() {
        lengthLabel.setText("Path length: 0");
        pieceLabel.setText("Path pieces: 0");
        running = true;
        method.init();
        while (!method.step()) {
            try {
                mPanel.updateUI();
                mPanel.repaint();
                visitedLabel.setText("Visited: " + method.noVisited);
                Thread.sleep(speed.getValue());
            } catch (Exception ex) {

            }
        }

        Path path = method.getPath();
        path.showPathLinks();

        mPanel.updateUI();
        mPanel.repaint();

        visitedLabel.setText("Visited nodes: " + method.noVisited);
        lengthLabel.setText("Path length: " + format.format(path.getLength()));
        pieceLabel.setText("Path segments: " + path.getPieces());

        running = false;
    }
}

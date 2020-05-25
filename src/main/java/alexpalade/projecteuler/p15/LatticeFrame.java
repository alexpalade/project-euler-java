package alexpalade.projecteuler.p15;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

class LatticeFrame extends JFrame implements ActionListener {

    LatticeModel model;
    LatticePane latticePanel;
    JButton slowButton;
    Thread slowSolver;

    LatticeFrame(String title, LatticeModel m) {
        super(title);

        model = m;
        
        IconFontSwing.register(FontAwesome.getIconFont());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // add the drawing pane
        latticePanel = new LatticePane(m.n, m);
        mainPanel.add(latticePanel, BorderLayout.CENTER);

        // create right panel
        JPanel btnsPanel = new JPanel();
        btnsPanel.setBorder(new EmptyBorder(0, 20, 0, 0));
        btnsPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 0.0;
        gbc.weightx = 1.0; // Cell takes up all extra horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JButton resetButton = new JButton("Reset", IconFontSwing.buildIcon(FontAwesome.UNDO, 15));
        JButton stepButton = new JButton("Step", IconFontSwing.buildIcon(FontAwesome.STEP_FORWARD, 15));
        JButton solveButton = new JButton("Solve", IconFontSwing.buildIcon(FontAwesome.FAST_FORWARD, 15));
        slowButton = new JButton("Slow", IconFontSwing.buildIcon(FontAwesome.PLAY, 15));
        
        // remove focus lines in buttons after click
        resetButton.setFocusPainted(false);
        stepButton.setFocusPainted(false);
        solveButton.setFocusPainted(false);
        slowButton.setFocusPainted(false);
        
        resetButton.setActionCommand("Reset");
        solveButton.setActionCommand("Solve");
        slowButton.setActionCommand("Slow");
        stepButton.setActionCommand("Step");

        btnsPanel.add(resetButton, gbc);
        btnsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnsPanel.add(stepButton, gbc);
        btnsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnsPanel.add(slowButton, gbc);
        btnsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnsPanel.add(solveButton, gbc);

        mainPanel.add(btnsPanel, BorderLayout.EAST);

        resetButton.addActionListener(this);
        solveButton.addActionListener(this);
        slowButton.addActionListener(this);
        stepButton.addActionListener(this);

        add(mainPanel);
        pack(); // sets the correct window size
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Solve")) {
            stopSlowSolver();
            model.solve();
            System.out.println("Solution: " + model.getScore(0, 0));
            slowButton.setBackground(null);
            latticePanel.repaint();
        } else if (command.equals("Slow")) {
            if (slowSolver != null && slowSolver.isAlive()) {
                stopSlowSolver();
                slowButton.setBackground(null);
            } else {
                slowSolver = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (!model.nodeSolved(0, 0)) {
//                                System.out.println("Thread stepping!");
                                model.step();
                                latticePanel.repaint();
                                Thread.sleep(500);
                            }
                            slowButton.setBackground(null);
//                            System.out.println("Thread done.");
                        } catch (InterruptedException ex) {
                            System.out.println("Error in thread, was trying to sleep...");
                        }
                    }
                });
                slowButton.setBackground(Color.ORANGE);
                slowSolver.start();
            }
        } else if (command.equals("Step")) {
            stopSlowSolver();
            model.step();
            slowButton.setBackground(null);
            latticePanel.repaint();
        } else if (command.equals("Reset")) {
            stopSlowSolver();
            model.reset();
            slowButton.setBackground(null);
            latticePanel.repaint();
        }
    }

    private void stopSlowSolver() {
        if (slowSolver != null && slowSolver.isAlive()) {
            slowSolver.interrupt();
        }
    }

    public static void main(String[] args) {

        // problem input here
        int n = 4;

        // a 2x2 grid actually has 3x3 lines...
        int lines = n + 1;

        LatticeModel lm = new LatticeModel(lines);
        LatticeFrame frame = new LatticeFrame("Project Euler 15: Lattice", lm);

        frame.setVisible(true);

    }
}

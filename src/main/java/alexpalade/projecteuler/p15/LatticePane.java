package alexpalade.projecteuler.p15;

import alexpalade.projecteuler.p15.LatticeModel.Step;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

class LatticePane extends JPanel {

    final int SIZE = 400;

    private final int n;
    private final LatticeModel m;
    int pad;

    LatticePane(int n, LatticeModel m) {
        super();

        this.m = m;

        setPreferredSize(new Dimension(SIZE, SIZE));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.DARK_GRAY);

        this.n = n;
        this.pad = 10 + 120 / n;

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double gridSize = SIZE - 2 * pad;
        double cellSize = gridSize / (n - 1);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.ORANGE);

        // draw the grid lines
        for (int i = 0; i < n; i++) {
            int offset = (int) (pad + cellSize * i);
            g2d.setColor(Color.GRAY);
            g2d.drawLine(pad, offset, SIZE - pad, offset);
            g2d.drawLine(offset, pad, offset, SIZE - pad);
        }

        // draw the 2 source nodes
        {
            Step last = m.steps.get(m.currentStepIndex);
            int i = last.i;
            int j = last.j;
            int x = (int) (pad + cellSize * i);
            int y = (int) (pad + cellSize * j);
            g2d.setColor(Color.RED);
            if (i < n - 1) {
                g2d.drawLine(x, y, x + (int) cellSize, y);
            }
            if (j < n - 1) {
                g2d.drawLine(x, y, x, y + (int) cellSize);
            }
        }

        // draw the nodes
        for (int i = 0; i < n; i++) {
            int offset = (int) (pad + cellSize * i);
            for (int j = 0; j < n; j++) {
                int radius = 120 / n;
                int textOffsetX = (int) (radius * 1.2 - 8/n*Math.log(10*n));
                int textOffsetY = (int) (radius * 1.8 - 12/n*Math.log(20*n));
                int x = (int) (offset - radius / 2);
                int y = (int) (pad + j * cellSize - radius / 2);
                if (m.nodeSolved(i, j)) {

                    if (i == 0 && j == 0) {
                        g2d.setColor(Color.RED);
                    } else {
                        g2d.setColor(Color.ORANGE);
                    }

                    g2d.fillOval(x, y, radius, radius);

                    long score = m.getScore(i, j);
                    String scoreString;
                    if (score<1000) {
                        scoreString = String.valueOf(score);
                    } else {
                        scoreString = String.valueOf(score/1000) + "K";
                    }
                    
                    g2d.drawString(scoreString, x + textOffsetX, y + textOffsetY);

                } else {
                    // normal, unsolved node
                    g2d.setColor(Color.GRAY);
                    g2d.fillOval(x, y, radius, radius);
                }
            }
        }
    }
}

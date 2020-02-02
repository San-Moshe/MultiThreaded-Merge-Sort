package q1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_END;

public class RectPanel extends JPanel implements Observer {
    private static final Color COLOR_DISABLED = Color.WHITE;
    private static final Color COLOR_ENABLED = Color.BLACK;
    private JPanel contentPanel;
    private JPanel controlPanel;
    private List<List<JButton>> buttonsMatrix;
    private IMatrixViewModel viewModel;

    public RectPanel(int cols, int rows, int numOfThreads, int runs) {
        viewModel = new MatrixViewModel(rows, cols, this);
        setLayout(new BorderLayout());
        contentPanel = new JPanel(new GridLayout(rows, cols, 2, 2));
        initRectangles(rows, cols);
        initControlPanel(numOfThreads, runs);
        add(contentPanel, CENTER);
        add(controlPanel, PAGE_END);
    }

    private void initControlPanel(int numOfThreads, int runs) {
        controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JButton goBtn = new JButton("GO");
        goBtn.addActionListener(e -> {
            viewModel.startGradualReduction(numOfThreads, runs);
        });

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> viewModel.clear());

        controlPanel.add(goBtn);
        controlPanel.add(clearBtn);
    }

    private void initRectangles(int rows, int cols) {
        buttonsMatrix = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<JButton> buttonsRow = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> viewModel.toggleState(finalI, finalJ));
                buttonsRow.add(button);
                contentPanel.add(button);
            }

            buttonsMatrix.add(buttonsRow);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof boolean[][]) {
            boolean[][] matrix = (boolean[][]) arg;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j]) {
                        buttonsMatrix.get(i).get(j).setBackground(COLOR_ENABLED);
                    } else {
                        buttonsMatrix.get(i).get(j).setBackground(COLOR_DISABLED);
                    }
                }
            }
        } else if (arg instanceof Boolean) {
            buttonsMatrix.forEach(btnRow -> btnRow.forEach(btn -> btn.setEnabled(!(boolean) arg)));
        }
    }
}

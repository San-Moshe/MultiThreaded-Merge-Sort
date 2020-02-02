package q1;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final CardLayout cl = new CardLayout();
    private JPanel settingsPanel = new JPanel();
    private JPanel cardsPanel = new JPanel(cl);

    public GameFrame() throws HeadlessException {
        setMinimumSize(new Dimension(500, 500));

        JPanel contentPane = initContentPane();
        initSettingsPanel();
        contentPane.add(cardsPanel);
        setVisible(true);
    }

    private JPanel initContentPane() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        return contentPane;
    }

    private void initSettingsPanel() {
        JLabel threadsLabel = new JLabel("Threads");
        JTextField threadsText = new JTextField(5);
        JLabel matrixSizeLabel = new JLabel("Size");
        JTextField matrixSize = new JTextField(5);
        JLabel numberOfRunsLabel = new JLabel("Runs");
        JTextField numberOfRunsText = new JTextField(5);
        JButton nextButton = new JButton("Next");

        nextButton.addActionListener(e -> {
            int width = Integer.parseInt(matrixSize.getText());
            int height = Integer.parseInt(matrixSize.getText());
            int threads = Integer.parseInt(threadsText.getText());
            int runs = Integer.parseInt(numberOfRunsText.getText());
            cardsPanel.add(new RectPanel(width, height, threads, runs), "Rect");
            cl.show(cardsPanel, "Rect");
        });

        settingsPanel.setBackground(Color.RED);
        settingsPanel.add(threadsLabel);
        settingsPanel.add(threadsText);
        settingsPanel.add(matrixSizeLabel);
        settingsPanel.add(matrixSize);
        settingsPanel.add(numberOfRunsLabel);
        settingsPanel.add(numberOfRunsText);
        settingsPanel.add(nextButton);
        cardsPanel.add(settingsPanel, "Settings");
    }
}

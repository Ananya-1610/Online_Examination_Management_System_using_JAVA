import java.awt.*;
import javax.swing.*;

public class AdminPanel extends JFrame {
    private final JTextField question = new JTextField(35);
    private final JTextField option1 = new JTextField(20), option2 = new JTextField(20), option3 = new JTextField(20), option4 = new JTextField(20);
    private final JComboBox<String> subject = new JComboBox<>(new String[]{"Java", "General"});
    private final JSpinner correct = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));

    public AdminPanel() {
        setTitle("Admin Panel"); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6)); p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        p.add(new JLabel("Subject:")); p.add(subject); p.add(new JLabel("Question:")); p.add(question);
        for (JTextField f : new JTextField[]{option1, option2, option3, option4}) { p.add(new JLabel("Option:")); p.add(f); }
        p.add(new JLabel("Correct option (1-4):")); p.add(correct);
        JButton add = new JButton("Add Question");
        add.addActionListener(e -> { DatabaseManager.addQuestion((String) subject.getSelectedItem(), question.getText(), new String[]{option1.getText(), option2.getText(), option3.getText(), option4.getText()}, (Integer) correct.getValue() - 1); JOptionPane.showMessageDialog(this, "Question added."); });
        add(p, BorderLayout.CENTER); add(add, BorderLayout.SOUTH); pack(); setLocationRelativeTo(null);
    }
}

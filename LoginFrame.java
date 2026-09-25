import java.awt.*;
import java.util.List;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private final JTextField username = new JTextField(15);
    private final JPasswordField password = new JPasswordField(15);
    private final JComboBox<String> subject = new JComboBox<>(new String[]{"Java", "General"});
    private final JSpinner negative = new JSpinner(new SpinnerNumberModel(0.25, 0, 1, 0.05));

    public LoginFrame() {
        setTitle("Online Examination - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("Username:")); form.add(username);
        form.add(new JLabel("Password:")); form.add(password);
        form.add(new JLabel("Subject:")); form.add(subject);
        form.add(new JLabel("Negative mark:")); form.add(negative);
        JButton login = new JButton("Login");
        JButton history = new JButton("Result History");
        JButton admin = new JButton("Admin Panel");
        login.addActionListener(e -> login());
        history.addActionListener(e -> showHistory());
        admin.addActionListener(e -> new AdminPanel().setVisible(true));
        JPanel actions = new JPanel(); actions.add(login); actions.add(history); actions.add(admin);
        add(form, BorderLayout.CENTER); add(actions, BorderLayout.SOUTH);
        pack(); setLocationRelativeTo(null);
    }

    private void login() {
        String user = username.getText().trim();
        if (!DatabaseManager.authenticate(user, new String(password.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Invalid login. Demo accounts: student/student or admin/admin"); return;
        }
        if ("admin".equalsIgnoreCase(user)) { new AdminPanel().setVisible(true); return; }
        new OnlineExam(user, (String) subject.getSelectedItem(), (Double) negative.getValue()).setVisible(true);
        dispose();
    }

    private void showHistory() {
        String user = JOptionPane.showInputDialog(this, "Username:");
        if (user != null && !user.trim().isEmpty()) {
            List<String> rows = DatabaseManager.getHistory(user.trim());
            JOptionPane.showMessageDialog(this, rows.isEmpty() ? "No history found." : String.join("\n", rows), "Result History", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class OnlineExam extends JFrame {
    private final ArrayList<Question> questions;
    private final int[] selectedAnswers;
    private final String username;
    private final String subject;
    private final double negativeMark;
    private int currentQuestionIndex = 0;
    private int timeLeft = 60;
    private boolean examSubmitted;
    private JLabel timerLabel, questionNumberLabel, questionTextLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton previousButton, nextButton, submitButton;
    private Timer timer;

    public OnlineExam(String username, String subject, double negativeMark) {
        this.username = username;
        this.subject = subject;
        this.negativeMark = negativeMark;
        questions = DatabaseManager.loadQuestions(subject);
        Collections.shuffle(questions);
        selectedAnswers = new int[questions.size()];
        Arrays.fill(selectedAnswers, -1);
        setTitle("Online Examination System - " + subject);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        buildUI();
        displayQuestion();
        startTimer();
    }

    private void buildUI() {
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel(subject + " Examination | User: " + username, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        top.add(title, BorderLayout.CENTER);
        timerLabel = new JLabel("Time Left: 60s", SwingConstants.RIGHT);
        timerLabel.setForeground(Color.RED);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        top.add(timerLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionTextLabel = new JLabel();
        questionTextLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        center.add(questionNumberLabel);
        center.add(Box.createVerticalStrut(12));
        center.add(questionTextLabel);
        center.add(Box.createVerticalStrut(18));
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            buttonGroup.add(optionButtons[i]);
            center.add(optionButtons[i]);
            center.add(Box.createVerticalStrut(8));
        }
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");
        previousButton.addActionListener(e -> move(-1));
        nextButton.addActionListener(e -> move(1));
        submitButton.addActionListener(e -> submitExam());
        bottom.add(previousButton); bottom.add(nextButton); bottom.add(submitButton);
        add(bottom, BorderLayout.SOUTH);
    }

    private void move(int direction) {
        saveCurrentAnswer();
        int next = currentQuestionIndex + direction;
        if (next >= 0 && next < questions.size()) {
            currentQuestionIndex = next;
            displayQuestion();
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (--timeLeft <= 0) { timer.stop(); submitExam(); }
            timerLabel.setText("Time Left: " + Math.max(timeLeft, 0) + "s");
        });
        timer.start();
    }

    private void displayQuestion() {
        if (questions.isEmpty()) { JOptionPane.showMessageDialog(this, "No questions available."); dispose(); return; }
        Question q = questions.get(currentQuestionIndex);
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        questionTextLabel.setText("<html>" + q.getQuestionText() + "</html>");
        buttonGroup.clearSelection();
        String[] options = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) { optionButtons[i].setText(options[i]); optionButtons[i].setSelected(i == selectedAnswers[currentQuestionIndex]); }
        previousButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);
    }

    private void saveCurrentAnswer() {
        selectedAnswers[currentQuestionIndex] = -1;
        for (int i = 0; i < optionButtons.length; i++) if (optionButtons[i].isSelected()) { selectedAnswers[currentQuestionIndex] = i; break; }
    }

    private void submitExam() {
        if (examSubmitted) return;
        examSubmitted = true;
        if (timer != null) timer.stop();
        saveCurrentAnswer();
        int correct = 0, wrong = 0, unanswered = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (selectedAnswers[i] == -1) unanswered++;
            else if (selectedAnswers[i] == questions.get(i).getCorrectAnswerIndex()) correct++;
            else wrong++;
        }
        double score = correct - (wrong * negativeMark);
        double percentage = score * 100.0 / questions.size();
        String result = percentage >= 50 ? "PASS" : "FAIL";
        DatabaseManager.saveResult(username, subject, correct, wrong, unanswered, score, result);
        String message = String.format("Total: %d\nCorrect: %d\nWrong: %d\nUnanswered: %d\nScore: %.2f\nResult: %s", questions.size(), correct, wrong, unanswered, score, result);
        JOptionPane.showMessageDialog(this, message, "Exam Result", JOptionPane.INFORMATION_MESSAGE);
        previousButton.setEnabled(false); nextButton.setEnabled(false); submitButton.setEnabled(false);
        for (JRadioButton b : optionButtons) b.setEnabled(false);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true)); }
}

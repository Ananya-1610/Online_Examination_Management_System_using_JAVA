import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class OnlineExam extends JFrame {
    private ArrayList<Question> questions;
    private int[] selectedAnswers;
    private int currentQuestionIndex = 0;
    private int timeLeft = 60;
    private boolean examSubmitted = false;

    private JLabel timerLabel;
    private JLabel questionNumberLabel;
    private JLabel questionTextLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton previousButton;
    private JButton nextButton;
    private JButton submitButton;

    private Timer timer;

    public OnlineExam() {
        setTitle("Online Examination System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        setLocationRelativeTo(null);

        questions = new ArrayList<>();
        selectedAnswers = new int[10];

        loadQuestions();
        buildUI();
        displayQuestion();
        startTimer();
    }

    private void loadQuestions() {
        questions.add(new Question(1, "Which keyword is used to define a class in Java?",
                new String[]{"class", "struct", "interface", "object"}, 0));

        questions.add(new Question(2, "Which collection class is used to store dynamic data in Java?",
                new String[]{"ArrayList", "StringBuffer", "HashMap", "Vector"}, 0));

        questions.add(new Question(3, "What does JRadioButton represent in Swing?",
                new String[]{"A text field", "A single selectable option", "A menu item", "A panel"}, 1));

        questions.add(new Question(4, "Which method is used to start a thread in Java?",
                new String[]{"run()", "start()", "execute()", "launch()"}, 1));

        questions.add(new Question(5, "Which component groups radio buttons so only one can be selected?",
                new String[]{"ButtonGroup", "JPanel", "JComboBox", "JList"}, 0));

        questions.add(new Question(6, "Which of the following is used to handle GUI events in Swing?",
                new String[]{"EventListener", "ActionListener", "ThreadListener", "FileListener"}, 1));

        questions.add(new Question(7, "What is the correct syntax to create a new ArrayList?",
                new String[]{"new ArrayList();", "ArrayList new = ();", "new List();", "ArrayList[] list = new ArrayList();"}, 0));

        questions.add(new Question(8, "Which of these is a Java Swing container?",
                new String[]{"JFrame", "FileReader", "Thread", "ArrayList"}, 0));

        questions.add(new Question(9, "What is the default value of an uninitialized int variable?",
                new String[]{"0", "null", "false", "undefined"}, 0));

        questions.add(new Question(10, "Which method is used to display a message dialog in Swing?",
                new String[]{"showInputDialog", "JOptionPane.showMessageDialog", "println", "showDialog"}, 1));
    }

    private void buildUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Online Examination System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time Left: 60s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(Color.RED);
        topPanel.add(timerLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        questionTextLabel = new JLabel();
        questionTextLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        questionTextLabel.setVerticalAlignment(SwingConstants.TOP);
        questionTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionTextLabel.setPreferredSize(new Dimension(700, 80));

        centerPanel.add(questionNumberLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(questionTextLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
            centerPanel.add(Box.createVerticalStrut(8));
        }

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer();
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestion();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer();
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitExam();
            }
        });

        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Time Left: " + timeLeft + "s");
                } else {
                    timer.stop();
                    submitExam();
                }
            }
        });
        timer.start();
    }

    private void displayQuestion() {
        if (questions == null || questions.isEmpty()) {
            return;
        }

        Question q = questions.get(currentQuestionIndex);
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        questionTextLabel.setText(q.getQuestionText());

        String[] options = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options[i]);
            optionButtons[i].setSelected(false);
        }

        int selectedOption = selectedAnswers[currentQuestionIndex];
        if (selectedOption >= 0 && selectedOption < optionButtons.length) {
            optionButtons[selectedOption].setSelected(true);
        }

        previousButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);
    }

    private void saveCurrentAnswer() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedAnswers[currentQuestionIndex] = i;
                return;
            }
        }
        selectedAnswers[currentQuestionIndex] = -1;
    }

    private void submitExam() {
        if (examSubmitted) {
            return;
        }

        examSubmitted = true;
        timer.stop();
        saveCurrentAnswer();

        int totalQuestions = questions.size();
        int correctAnswers = 0;
        int wrongAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {
            if (selectedAnswers[i] == questions.get(i).getCorrectAnswerIndex()) {
                correctAnswers++;
            } else if (selectedAnswers[i] != -1) {
                wrongAnswers++;
            } else {
                wrongAnswers++;
            }
        }

        double percentage = (correctAnswers * 100.0) / totalQuestions;
        String resultStatus = percentage >= 50 ? "PASS" : "FAIL";

        String message = "Total Questions: " + totalQuestions + "\n"
                + "Correct Answers: " + correctAnswers + "\n"
                + "Wrong Answers: " + wrongAnswers + "\n"
                + "Percentage: " + String.format("%.2f", percentage) + "%\n"
                + "Result: " + resultStatus;

        JOptionPane.showMessageDialog(this, message, "Exam Result", JOptionPane.INFORMATION_MESSAGE);

        disableExamControls();
    }

    private void disableExamControls() {
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        submitButton.setEnabled(false);
        buttonGroup.clearSelection();
        for (JRadioButton optionButton : optionButtons) {
            optionButton.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OnlineExam exam = new OnlineExam();
                exam.setVisible(true);
            }
        });
    }
}

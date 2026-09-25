import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class OnlineExam extends JFrame {
    private ArrayList<Question> questions;
    private int[] selectedAnswers;
    private int currentQuestionIndex = 0;
    private int timeLeft = 60;
    private boolean examSubmitted = false;

    private String username;
    private String subject;
    private double negativeMark;

    private JLabel timerLabel;
    private JLabel questionNumberLabel;
    private JLabel questionTextLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton previousButton;
    private JButton nextButton;
    private JButton submitButton;

    private Timer timer;

    public OnlineExam(String username, String subject, double negativeMark) {
        this.username = username;
        this.subject = subject;
        this.negativeMark = negativeMark;

        questions = loadQuestionsForSubject(subject);
        Collections.shuffle(questions); // random questions

        selectedAnswers = new int[questions.size()];
        Arrays.fill(selectedAnswers, -1);

        setTitle("Online Examination System - " + subject);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        setLocationRelativeTo(null);

        buildUI();
        displayQuestion();
        startTimer();
    }

    private ArrayList<Question> loadQuestionsForSubject(String selectedSubject) {
        ArrayList<Question> subjectQuestions = new ArrayList<>();

        if ("Java".equalsIgnoreCase(selectedSubject)) {
            subjectQuestions.add(new Question(1, "Java",
                    "Which keyword is used to define a class in Java?",
                    new String[]{"class", "struct", "interface", "object"}, 0));

            subjectQuestions.add(new Question(2, "Java",
                    "Which collection class is used to store dynamic data in Java?",
                    new String[]{"ArrayList", "StringBuffer", "HashMap", "Vector"}, 0));

            subjectQuestions.add(new Question(3, "Java",
                    "What does JRadioButton represent in Swing?",
                    new String[]{"A text field", "A single selectable option", "A menu item", "A panel"}, 1));

            subjectQuestions.add(new Question(4, "Java",
                    "Which method is used to start a thread in Java?",
                    new String[]{"run()", "start()", "execute()", "launch()"}, 1));

            subjectQuestions.add(new Question(5, "Java",
                    "Which component groups radio buttons so only one can be selected?",
                    new String[]{"ButtonGroup", "JPanel", "JComboBox", "JList"}, 0));

            subjectQuestions.add(new Question(6, "Java",
                    "Which of the following is used to handle GUI events in Swing?",
                    new String[]{"EventListener", "ActionListener", "ThreadListener", "FileListener"}, 1));

            subjectQuestions.add(new Question(7, "Java",
                    "What is the correct syntax to create a new ArrayList?",
                    new String[]{"new ArrayList();", "ArrayList new = ();", "new List();", "ArrayList[] list = new ArrayList();"}, 0));

            subjectQuestions.add(new Question(8, "Java",
                    "Which of these is a Java Swing container?",
                    new String[]{"JFrame", "FileReader", "Thread", "ArrayList"}, 0));

            subjectQuestions.add(new Question(9, "Java",
                    "What is the default value of an uninitialized int variable?",
                    new String[]{"0", "null", "false", "undefined"}, 0));

            subjectQuestions.add(new Question(10, "Java",
                    "Which method is used to display a message dialog in Swing?",
                    new String[]{"showInputDialog", "JOptionPane.showMessageDialog", "println", "showDialog"}, 1));
        }

        else if ("General".equalsIgnoreCase(selectedSubject)) {
            subjectQuestions.add(new Question(1, "General",
                    "Which planet is known as the Red Planet?",
                    new String[]{"Earth", "Mars", "Jupiter", "Venus"}, 1));

            subjectQuestions.add(new Question(2, "General",
                    "What is the capital of France?",
                    new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));

            subjectQuestions.add(new Question(3, "General",
                    "Which gas do plants absorb from the atmosphere?",
                    new String[]{"Oxygen", "Carbon dioxide", "Nitrogen", "Hydrogen"}, 1));

            subjectQuestions.add(new Question(4, "General",
                    "Who wrote the play 'Hamlet'?",
                    new String[]{"Charles Dickens", "William Shakespeare", "Leo Tolstoy", "Jane Austen"}, 1));

            subjectQuestions.add(new Question(5, "General",
                    "Which is the largest ocean on Earth?",
                    new String[]{"Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"}, 2));

            subjectQuestions.add(new Question(6, "General",
                    "Which country is called the Land of the Rising Sun?",
                    new String[]{"China", "Japan", "Korea", "Thailand"}, 1));

            subjectQuestions.add(new Question(7, "General",
                    "What is 12 x 8?",
                    new String[]{"84", "96", "108", "120"}, 1));

            subjectQuestions.add(new Question(8, "General",
                    "Which animal is known as the 'Ship of the Desert'?",
                    new String[]{"Horse", "Camel", "Elephant", "Tiger"}, 1));

            subjectQuestions.add(new Question(9, "General",
                    "How many days are there in a leap year?",
                    new String[]{"364", "365", "366", "367"}, 2));

            subjectQuestions.add(new Question(10, "General",
                    "Which is the largest continent?",
                    new String[]{"Europe", "Africa", "Asia", "North America"}, 2));
        }

        return subjectQuestions;
    }

    private void buildUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(subject + " Exam | User: " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
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
        questionTextLabel.setPreferredSize(new Dimension(700, 90));

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
        if (timer != null) {
            timer.stop();
        }

        saveCurrentAnswer();

        int totalQuestions = questions.size();
        int correctAnswers = 0;
        int wrongAnswers = 0;
        int unanswered = 0;

        for (int i = 0; i < totalQuestions; i++) {
            if (selectedAnswers[i] == -1) {
                unanswered++;
            } else if (selectedAnswers[i] == questions.get(i).getCorrectAnswerIndex()) {
                correctAnswers++;
            } else {
                wrongAnswers++;
            }
        }

        // Negative marking logic
        double score = correctAnswers - (wrongAnswers * negativeMark);
        double percentage = (score * 100.0) / totalQuestions;

        String resultStatus = percentage >= 50 ? "PASS" : "FAIL";

        String message = "Username: " + username + "\n"
                + "Subject: " + subject + "\n"
                + "Total Questions: " + totalQuestions + "\n"
                + "Correct Answers: " + correctAnswers + "\n"
                + "Wrong Answers: " + wrongAnswers + "\n"
                + "Unanswered: " + unanswered + "\n"
                + "Score: " + String.format("%.2f", score) + "\n"
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
                new LoginFrame().setVisible(true);
            }
        });
    }

    static class LoginFrame extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JComboBox<String> subjectComboBox;
        private JSpinner negativeMarkSpinner;

        public LoginFrame() {
            setTitle("Login - Online Examination");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(0, 2, 10, 10));
            setSize(420, 250);
            setLocationRelativeTo(null);

            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Password:");
            JLabel subjectLabel = new JLabel("Subject:");
            JLabel negativeLabel = new JLabel("Negative Mark:");

            usernameField = new JTextField();
            passwordField = new JPasswordField();
            subjectComboBox = new JComboBox<>(new String[]{"Java", "General"});
            negativeMarkSpinner = new JSpinner(new SpinnerNumberModel(0.25, 0.0, 1.0, 0.05));

            JButton loginButton = new JButton("Login");

            add(usernameLabel);
            add(usernameField);
            add(passwordLabel);
            add(passwordField);
            add(subjectLabel);
            add(subjectComboBox);
            add(negativeLabel);
            add(negativeMarkSpinner);

            add(new JLabel()); // blank
            add(loginButton);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    login();
                }
            });
        }

        private void login() {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Simple login check
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }

            if (!isValidUser(username, password)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid login.\nTry:\nstudent / student\nteacher / teacher");
                return;
            }

            String subject = (String) subjectComboBox.getSelectedItem();
            double negativeMark = ((Number) negativeMarkSpinner.getValue()).doubleValue();

            OnlineExam exam = new OnlineExam(username, subject, negativeMark);
            exam.setVisible(true);
            dispose();
        }

        private boolean isValidUser(String username, String password) {
            Map<String, String> users = new HashMap<>();
            users.put("student", "student");
            users.put("teacher", "teacher");

            return users.containsKey(username) && users.get(username).equals(password);
        }
    }
}

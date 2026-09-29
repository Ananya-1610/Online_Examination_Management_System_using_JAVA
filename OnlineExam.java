
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OnlineExam extends JFrame 
{
    ArrayList<Question> questions = new ArrayList<>();
	ArrayList<Question> selectedQuestions = new ArrayList<>();
    int[] userAnswers = new int[10];
    int currentQuestion = 0;
    String selectedSubject;

    JLabel questionLabel;
    JLabel questionNumberLabel;
    JLabel timerLabel;

    JRadioButton option1;
    JRadioButton option2;
    JRadioButton option3;
    JRadioButton option4;

    ButtonGroup buttonGroup;

    JButton previousButton;
    JButton nextButton;
    JButton submitButton;

    Timer timer;
    int timeLeft = 60;

    public OnlineExam(String subject) {
        selectedSubject = subject;
        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = -1;
        }
        addQuestions();
        createGUI();
        displayQuestion();
        startTimer();
    }

    public static void login() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null,panel,"Online Examination - Login",JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("Ananya")&& password.equals("9999")) {
                selectSubject();
            } 
			else {
                JOptionPane.showMessageDialog(null,"Invalid username or password!","Login Failed",JOptionPane.ERROR_MESSAGE);
                login();
            }
        } 
		else {
            System.exit(0);
        }
    }

    public static void selectSubject() {

        String[] subjects = {"Current Affairs","Logical Reasoning","Language and Literature"};


        String subject = (String) JOptionPane.showInputDialog(
                null,
                "Select a subject:",
                "Subject Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                subjects,
                subjects[0]
        );


        if (subject != null) {

            new OnlineExam(subject);

        } else {

            System.exit(0);
        }
    }

    public void addQuestions() {
		// Current Affairs
		
		// CURRENT AFFAIRS

		questions.add(new Question(
			"Current Affairs",
			"Which Indian organisation is responsible for the country's space programme?",
			"DRDO",
			"ISRO",
			"HAL",
			"BHEL",
			2
		));

		questions.add(new Question(
			"Current Affairs",
			"Exercise 'Veer Guardian 2026' is a bilateral air exercise between India and which country?",
			"France",
			"Japan",
			"Australia",
			"United States",
			2
		));

		questions.add(new Question(
			"Current Affairs",
			"Which city hosted the first Indian leg of the 2026 Hyundai World Archery Para Series?",
			"Ahmedabad",
			"New Delhi",
			"Mumbai",
			"Chennai",
		1
		));

		questions.add(new Question(
			"Current Affairs",
			"Which organisation is India's central bank and monetary authority?",
			"SEBI",
			"NABARD",
			"RBI",
			"SIDBI",
			3
		));

		questions.add(new Question(
			"Current Affairs",
			"SEMICON India 2026 primarily focused on developments in which sector?",
			"Semiconductor and electronics",
			"Agriculture",
			"Tourism",
			"Textiles",
			1
		));

		questions.add(new Question(
			"Current Affairs",
			"Which Indian satellite was launched aboard GSLV-F17 in September 2026?",
			"EOS-05",
			"RISAT-1",
			"INSAT-3D",
			"Cartosat-2",
			1
		));

		questions.add(new Question(
			"Current Affairs",
			"Which country hosted the 2026 Asian Games?",
			"China",
			"Japan",
			"South Korea",
			"Indonesia",
			2
		));

		questions.add(new Question(
			"Current Affairs",
			"The 2026 Tenzing Norgay National Adventure Awards are associated with achievements in which field?",
			"Adventure activities",
			"Classical music",
			"Scientific research",
			"Literature",
			1
		));

		questions.add(new Question(
			"Current Affairs",
			"Which Indian institution is primarily responsible for defence research and development?",
			"ISRO",
			"DRDO",
			"CSIR",
			"UGC",
			2
		));

		questions.add(new Question(
			"Current Affairs",
			"The 'Swachh Vayu Sarvekshan' primarily evaluates cities on their efforts related to:",
			"Water conservation",
			"Air quality improvement",
			"Wastewater treatment",
			"Forest expansion",
			2
		));

		// Logical Reasoning
		questions.add(new Question(
		"Logical Reasoning",
		"Find the next number in the series: 3, 8, 15, 24, 35, ?",
		"46", "48", "50", "52", 2
		));

		questions.add(new Question(
		"Logical Reasoning",
		"If each letter of a word is replaced by the next letter in the alphabet, how is INDIA written?",
		"JOEJB",
		"JODJB",
		"HMCHZ",
		"JPEKB",
		1
		));

		questions.add(new Question(
		"Logical Reasoning",
		"Statements: All researchers are graduates. Some graduates are programmers. Which conclusion definitely follows?",
		"All researchers are programmers",
		"Some programmers are researchers",
		"All researchers are graduates",
		"No graduate is a researcher", 3
		));

		questions.add(new Question(
		"Logical Reasoning",
		"A, B, C, D and E are seated in a row. B is to the right of A. C is to the left of D. E is between B and C. Which arrangement is possible?",
		"A-B-E-C-D",
		"A-E-B-C-D",
		"B-A-E-D-C",
		"A-B-C-D-E", 1
		));

		questions.add(new Question(
		"Logical Reasoning",
		"A man walks 8 km east, turns left and walks 6 km, then turns left and walks 8 km. How far is he from his starting point?",
		"2 km", "6 km", "8 km", "14 km", 2
		));

		questions.add(new Question(
		"Logical Reasoning",
		"In a class of 40 students, Anu ranks 12th from the top. What is her rank from the bottom?",
		"27th", "28th", "29th", "30th", 3
		));

		questions.add(new Question(
		"Logical Reasoning",
		"Find the missing term: AZ, BY, CX, DW, ?",
		"EV", "FU", "EX", "EW", 1
		));

		questions.add(new Question(
		"Logical Reasoning",
		"If P > Q, Q = R, R < S and S < T, which statement is definitely true?",
		"P > T",
		"T > R",
		"Q > S",
		"P < R", 2
		));

		questions.add(new Question(
		"Logical Reasoning",
		"Five students P, Q, R, S and T have different heights. P is taller than Q but shorter than R. S is shorter than Q but taller than T. Who is the tallest?",
		"P", "Q", "R", "T", 3
		));

		questions.add(new Question(
		"Logical Reasoning",
		"A clock gains 5 minutes every hour. If it is set correctly at 8:00 AM, what time will it show at 2:00 PM?",
		"2:20 PM", "2:25 PM", "2:30 PM", "2:35 PM", 3
		));

				// Language and Literature
		questions.add(new Question(
			"Language and Literature",
			"Who wrote the novel 'Pride and Prejudice'?",
			"Charlotte Bronte",
			"Jane Austen",
			"Emily Bronte",
			"Virginia Woolf", 2
			));

		questions.add(new Question(
			"Language and Literature",
			"Who wrote 'The Jungle Book'?",
			"Rudyard Kipling",
			"Charles Dickens",
			"Mark Twain",
			"George Orwell", 1
		));

		questions.add(new Question(
			"Language and Literature",
			"What is a synonym of 'Benevolent'?",
			"Cruel", "Kind", "Angry", "Proud", 2
		));

		questions.add(new Question(
			"Language and Literature",
			"What is the antonym of 'Ancient'?",
			"Old", "Historic", "Modern", "Traditional", 3
		));

		questions.add(new Question(
			"Language and Literature",
			"Which figure of speech compares two things using 'like' or 'as'?",
			"Metaphor", "Simile", "Personification", "Irony", 2
		));

		questions.add(new Question(
			"Language and Literature",
			"Who wrote 'Romeo and Juliet'?",
			"William Shakespeare",
			"William Wordsworth",
			"John Milton",
			"Oscar Wilde", 1
		));

		questions.add(new Question(
			"Language and Literature",
			"Which of the following is a noun?",
			"Beautiful", "Quickly", "Honesty", "Run", 3
		));

		questions.add(new Question(
			"Language and Literature",
			"Who wrote the epic 'Paradise Lost'?",
			"John Milton",
			"William Shakespeare",
			"Geoffrey Chaucer",
			"T. S. Eliot", 1
		));

		questions.add(new Question(
			"Language and Literature",
			"Choose the correctly spelled word.",
			"Accomodation",
			"Acommodation",
			"Accommodation",
			"Accommadation", 3
		));

		questions.add(new Question(
			"Language and Literature",
			"What is the meaning of the idiom 'Break the ice'?",
			"To break something",
			"To start a conversation",
			"To become angry",
			"To stop talking", 2
		));

		if(selectedSubject.equals("Current Affairs")) {
		selectedQuestions = new ArrayList<>(questions.subList(0, 10));
		}
		else if(selectedSubject.equals("Logical Reasoning")) {
		selectedQuestions = new ArrayList<>(questions.subList(10, 20));
		}
		else if(selectedSubject.equals("Language and Literature")) {
		selectedQuestions = new ArrayList<>(questions.subList(20, 30));
		}
	}
		
    public void createGUI() {
        setTitle("Online Examination System - " + selectedSubject);
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
		
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Cambria", Font.BOLD, 16));
        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Cambria", Font.BOLD, 16));

        topPanel.add(questionNumberLabel,BorderLayout.WEST);
        topPanel.add(timerLabel,BorderLayout.EAST);

        mainPanel.add(topPanel,BorderLayout.NORTH);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel,BoxLayout.Y_AXIS));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Cambria", Font.BOLD, 17));
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();

        buttonGroup = new ButtonGroup();

        buttonGroup.add(option1);
        buttonGroup.add(option2);
        buttonGroup.add(option3);
        buttonGroup.add(option4);

        questionPanel.add(option1);
        questionPanel.add(option2);
        questionPanel.add(option3);
        questionPanel.add(option4);

        mainPanel.add(questionPanel,BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);

        mainPanel.add(bottomPanel,BorderLayout.SOUTH);

        previousButton.addActionListener(new ActionListener() 
			{public void actionPerformed(ActionEvent e)
			{saveAnswer();
                if (currentQuestion > 0){
					currentQuestion--;
					displayQuestion();
                        }
                    }
                }
        );

        nextButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(
                            ActionEvent e) {
								saveAnswer();
                        if (currentQuestion < questions.size() - 1) {
                            currentQuestion++;
                            displayQuestion();
                        }
                    }
                }
        );

        submitButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(
                            ActionEvent e) {
								saveAnswer();
                        int choice = JOptionPane.showConfirmDialog(
                                        OnlineExam.this,
                                        "Are you sure you want to submit?",
                                        "Submit Exam",
                                        JOptionPane.YES_NO_OPTION
                                );


                        if (choice == JOptionPane.YES_OPTION) {
                            submitExam();
                        }
                    }
                }
        );
        add(mainPanel);
        setVisible(true);
    }
	void displayQuestion() {

    Question q = selectedQuestions.get(currentQuestion);

    questionNumberLabel.setText(
        "Question " + (currentQuestion + 1) + " / " + selectedQuestions.size()
    );

    questionLabel.setText(q.getQuestion());

    option1.setText(q.getOption1());
    option2.setText(q.getOption2());
    option3.setText(q.getOption3());
    option4.setText(q.getOption4());

    buttonGroup.clearSelection();

    // Restore previous answer
    if (userAnswers[currentQuestion] == 1) {
        option1.setSelected(true);
    }
    else if (userAnswers[currentQuestion] == 2) {
        option2.setSelected(true);
    }
    else if (userAnswers[currentQuestion] == 3) {
        option3.setSelected(true);
    }
    else if (userAnswers[currentQuestion] == 4) {
        option4.setSelected(true);
    }

    previousButton.setEnabled(currentQuestion > 0);

    nextButton.setEnabled(
        currentQuestion < selectedQuestions.size() - 1
    );
}

    public void saveAnswer() {
        if (option1.isSelected()) {
            userAnswers[currentQuestion] = 1;
        } else if (option2.isSelected()) {
            userAnswers[currentQuestion] = 2;
        } else if (option3.isSelected()) {
            userAnswers[currentQuestion] = 3;
        } else if (option4.isSelected()) {
            userAnswers[currentQuestion] = 4;
        }
    }

    public void startTimer() {
        timerLabel.setText(
                "Time: 60 seconds"
        );
        timer = new Timer(1000,new ActionListener() {
                    public void actionPerformed(ActionEvent e) {timeLeft--;timerLabel.setText(
                                "Time: "+ timeLeft+ " seconds");
                        if (timeLeft <= 0) {
							timer.stop();
							saveAnswer();
							
                            JOptionPane.showMessageDialog(OnlineExam.this,"Time is over!\n"
                                    + "Your exam will be submitted automatically."
                            );
                            submitExam();
                        }
                    }
                }
        );
        timer.start();
    }

    public void submitExam() {
        if (timer != null) {
            timer.stop();
        }
        int correct = 0;
        int wrong = 0;
        int unanswered = 0;

        for (int i = 0;i < selectedQuestions.size();i++) {
            if (userAnswers[i] == -1) {
                unanswered++;
            } 
			else if (userAnswers[i]==selectedQuestions.get(i).getCorrectAnswer())
			{
                correct++;
            } 
			else {
                wrong++;
            }
        }

        double marksForCorrect = correct * 1.0;
        double negativeMarks = wrong * 0.25;
        double finalMarks = marksForCorrect - negativeMarks;

        if (finalMarks < 0) {
            finalMarks = 0;
        }
        int total = selectedQuestions.size();
        double percentage = (finalMarks / total) * 100;

        String result;
        if (percentage >= 40) {
            result = "PASS";}
		else {
            result = "FAIL";
        }
        String message = "ONLINE EXAM RESULT\n\n"
        + "Subject          : " + selectedSubject + "\n"
        + "Total Questions  : " + total + "\n"
        + "Correct Answers  : " + correct + "\n"
        + "Wrong Answers    : " + wrong + "\n"
        + "Unanswered       : " + unanswered + "\n"
        + "Negative Marks   : " + String.format("%.2f", negativeMarks) + "\n"
        + "Final Marks      : " + String.format("%.2f", finalMarks) + "\n"
        + "Percentage       : " + String.format("%.2f", percentage) + "%\n"
        + "Result           : " + result;
        JOptionPane.showMessageDialog(this,message,"Exam Result",JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
						login(); }});
    }
}


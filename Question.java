public class Question {
    private final int id;
    private final String subject;
    private final String questionText;
    private final String[] options;
    private final int correctAnswerIndex;

    public Question(int id, String questionText, String[] options, int correctAnswerIndex) {
        this(id, "Java", questionText, options, correctAnswerIndex);
    }

    public Question(int id, String subject, String questionText, String[] options, int correctAnswerIndex) {
        this.id = id;
        this.subject = subject;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }
    public int getId() { return id; }
    public String getSubject() { return subject; }
    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
}

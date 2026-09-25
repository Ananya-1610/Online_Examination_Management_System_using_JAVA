import java.sql.*;
import java.util.*;

public final class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/online_exam";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Change for your MySQL installation.
    private static final List<Question> FALLBACK = new ArrayList<>();
    private static boolean databaseAvailable;
    static {
        FALLBACK.add(new Question(1, "Java", "Which keyword defines a class?", new String[]{"class", "struct", "interface", "object"}, 0));
        FALLBACK.add(new Question(2, "Java", "Which collection stores dynamic data?", new String[]{"ArrayList", "StringBuffer", "HashMap", "Vector"}, 0));
        FALLBACK.add(new Question(3, "Java", "What does JRadioButton represent?", new String[]{"Text field", "Single selectable option", "Menu item", "Panel"}, 1));
        FALLBACK.add(new Question(4, "Java", "Which method starts a thread?", new String[]{"run()", "start()", "execute()", "launch()"}, 1));
        FALLBACK.add(new Question(5, "Java", "What groups radio buttons?", new String[]{"ButtonGroup", "JPanel", "JComboBox", "JList"}, 0));
    }
    private DatabaseManager() {}
    private static Connection connect() throws SQLException { return DriverManager.getConnection(URL, USER, PASSWORD); }
    public static boolean authenticate(String user, String pass) {
        if (("student".equals(user) && "student".equals(pass)) || ("admin".equals(user) && "admin".equals(pass))) return true;
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT 1 FROM users WHERE username=? AND password_hash=?")) { p.setString(1, user); p.setString(2, pass); try (ResultSet r = p.executeQuery()) { return r.next(); } } catch (SQLException e) { return false; }
    }
    public static ArrayList<Question> loadQuestions(String subject) {
        ArrayList<Question> result = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT id,question_text,option1,option2,option3,option4,correct_index FROM questions WHERE subject=?")) {
            databaseAvailable = true; p.setString(1, subject); try (ResultSet r = p.executeQuery()) { while (r.next()) result.add(new Question(r.getInt(1), subject, r.getString(2), new String[]{r.getString(3),r.getString(4),r.getString(5),r.getString(6)}, r.getInt(7))); }
        } catch (SQLException e) { databaseAvailable = false; }
        if (result.isEmpty()) for (Question q : FALLBACK) if (q.getSubject().equals(subject)) result.add(q);
        return result;
    }
    public static void saveResult(String user, String subject, int correct, int wrong, int unanswered, double score, String status) {
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement("INSERT INTO results(username,subject,correct,wrong,unanswered,score,status) VALUES(?,?,?,?,?,?,?)")) { p.setString(1,user);p.setString(2,subject);p.setInt(3,correct);p.setInt(4,wrong);p.setInt(5,unanswered);p.setDouble(6,score);p.setString(7,status);p.executeUpdate(); } catch (SQLException ignored) { }
    }
    public static List<String> getHistory(String user) {
        List<String> rows = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT subject,score,status,attempted_at FROM results WHERE username=? ORDER BY attempted_at DESC")) { p.setString(1,user); try (ResultSet r=p.executeQuery()) { while(r.next()) rows.add(r.getString(1)+" | Score: "+r.getDouble(2)+" | "+r.getString(3)+" | "+r.getTimestamp(4)); } } catch(SQLException ignored) { }
        return rows;
    }
    public static void addQuestion(String subject, String text, String[] options, int correct) {
        FALLBACK.add(new Question(FALLBACK.size()+1, subject, text, options, correct));
        try (Connection c=connect(); PreparedStatement p=c.prepareStatement("INSERT INTO questions(subject,question_text,option1,option2,option3,option4,correct_index) VALUES(?,?,?,?,?,?,?)")) { p.setString(1,subject);p.setString(2,text);for(int i=0;i<4;i++)p.setString(i+3,options[i]);p.setInt(7,correct);p.executeUpdate(); } catch(SQLException ignored) { }
    }
}

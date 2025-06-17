package quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizGame extends JFrame {
    private String difficulty;
    private ArrayList<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private JLabel questionLabel;
    private JButton[] options = new JButton[4];
    private String username;

    public QuizGame(String difficulty, String username) {
        this.difficulty = difficulty;
        this.username = username;
        setTitle("Quiz Game - " + difficulty);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        add(questionLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JButton();
            options[i].addActionListener(e -> checkAnswer((JButton) e.getSource()));
            add(options[i]);
        }

        loadQuestions();
        showNextQuestion();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM questions WHERE difficulty=? ORDER BY RAND() LIMIT 10")) { // 🔥 Ensure 10 questions are selected
            stmt.setString(1, difficulty);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("question"),
                        rs.getString("option_1"),
                        rs.getString("option_2"),
                        rs.getString("option_3"),
                        rs.getString("option_4"),
                        rs.getInt("correct_option")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 🎯 Ensure at least 10 questions are loaded
        if (questions.size() < 10) {
            JOptionPane.showMessageDialog(this, "⚠️ Not enough questions available! Add more questions to the database.");
            dispose();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText(q.getQuestion());
            for (int i = 0; i < 4; i++) {
                options[i].setText(q.getOptions()[i]);
            }
        } else {
            storeScore();
            JOptionPane.showMessageDialog(this, "🎉 Quiz Finished! Your Score: " + score);
            new Main();
            dispose();
        }
    }

    private void checkAnswer(JButton button) {
        int selectedOption = java.util.Arrays.asList(options).indexOf(button) + 1;
        if (selectedOption == questions.get(currentQuestionIndex).getCorrectOption()) {
            score++;
        }
        currentQuestionIndex++;
        showNextQuestion();
    }

    private void storeScore() {
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO scores (username, score, difficulty) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.setString(3, difficulty);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizGame("Beginner", "TestUser"));
    }
}
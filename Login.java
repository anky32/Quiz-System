package quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login - Quiz Application");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> authenticateUser());
        add(loginButton);

        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(e -> {
            new Signup();
            dispose();
        });
        add(signupButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username=?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getString("password").equals(hashPassword(password))) {
                JOptionPane.showMessageDialog(this, "Login Successful!");

                // 🎯 Ask user for quiz difficulty
                String[] difficultyOptions = {"Beginner", "Intermediate", "Advanced"};
                String difficulty = (String) JOptionPane.showInputDialog(
                        this, "Select Difficulty:", "Quiz Difficulty",
                        JOptionPane.QUESTION_MESSAGE, null, difficultyOptions, "Beginner");

                // 🎯 Start QuizGame with selected difficulty and username
                if (difficulty != null) {
                    new QuizGame(difficulty, username);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}

package quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signup extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Signup() {
        setTitle("Sign Up - Quiz Application");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            new Login();
            dispose();
        });
        add(loginButton);

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty!");
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username=?")) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists! Choose a different one.");
            } else {
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashPassword(password));
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Registration Successful! Please log in.");
                    new Login();
                    dispose();
                }
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
        new Signup();
    }
}

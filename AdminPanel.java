package quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminPanel extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField questionField, option1Field, option2Field, option3Field, option4Field, correctOptionField;
    private JComboBox<String> difficultyBox;

    public AdminPanel() {
        setTitle("Admin Panel - Manage Quiz");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table Model
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Correct Option", "Difficulty"});
        table = new JTable(model);
        loadQuestions();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.add(new JLabel("Question:"));
        questionField = new JTextField();
        formPanel.add(questionField);

        formPanel.add(new JLabel("Option 1:"));
        option1Field = new JTextField();
        formPanel.add(option1Field);

        formPanel.add(new JLabel("Option 2:"));
        option2Field = new JTextField();
        formPanel.add(option2Field);

        formPanel.add(new JLabel("Option 3:"));
        option3Field = new JTextField();
        formPanel.add(option3Field);

        formPanel.add(new JLabel("Option 4:"));
        option4Field = new JTextField();
        formPanel.add(option4Field);

        formPanel.add(new JLabel("Correct Option (1-4):"));
        correctOptionField = new JTextField();
        formPanel.add(correctOptionField);

        formPanel.add(new JLabel("Difficulty:"));
        difficultyBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        formPanel.add(difficultyBox);

        add(formPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Question");
        addButton.addActionListener(e -> addQuestion());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Selected");
        updateButton.addActionListener(e -> updateQuestion());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteQuestion());
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadQuestions() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option_1"),
                        rs.getString("option_2"),
                        rs.getString("option_3"),
                        rs.getString("option_4"),
                        rs.getInt("correct_option"),
                        rs.getString("difficulty")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addQuestion() {
        // 🎯 Validate Inputs
        if (questionField.getText().trim().isEmpty() ||
            option1Field.getText().trim().isEmpty() ||
            option2Field.getText().trim().isEmpty() ||
            option3Field.getText().trim().isEmpty() ||
            option4Field.getText().trim().isEmpty() ||
            correctOptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ All fields must be filled out!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int correctOption;
        try {
            correctOption = Integer.parseInt(correctOptionField.getText());
            if (correctOption < 1 || correctOption > 4) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Correct Option must be a number between 1 and 4!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO questions (question, option_1, option_2, option_3, option_4, correct_option, difficulty) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, questionField.getText());
            stmt.setString(2, option1Field.getText());
            stmt.setString(3, option2Field.getText());
            stmt.setString(4, option3Field.getText());
            stmt.setString(5, option4Field.getText());
            stmt.setInt(6, correctOption);
            stmt.setString(7, difficultyBox.getSelectedItem().toString());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Question Added Successfully!");
            loadQuestions();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }

    private void updateQuestion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a question to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int questionId = (int) table.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("UPDATE questions SET question=?, option_1=?, option_2=?, option_3=?, option_4=?, correct_option=?, difficulty=? WHERE id=?")) {
            stmt.setString(1, questionField.getText());
            stmt.setString(2, option1Field.getText());
            stmt.setString(3, option2Field.getText());
            stmt.setString(4, option3Field.getText());
            stmt.setString(5, option4Field.getText());
            stmt.setInt(6, Integer.parseInt(correctOptionField.getText()));
            stmt.setString(7, difficultyBox.getSelectedItem().toString());
            stmt.setInt(8, questionId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Question Updated Successfully!");
            loadQuestions();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }

    private void deleteQuestion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a question to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int questionId = (int) table.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM questions WHERE id=?")) {
                stmt.setInt(1, questionId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Question Deleted Successfully!");
                loadQuestions();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new AdminPanel();
    }
}

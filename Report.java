package quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Report extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public Report() {
        setTitle("Player Score Report");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table Model
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Username", "Score", "Difficulty", "Date"});
        table = new JTable(model);
        loadScores();

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            new Main();
            dispose();
        });
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadScores() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT username, score, difficulty, timestamp FROM scores ORDER BY score DESC")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getString("difficulty"),
                        rs.getTimestamp("timestamp")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Report();
    }
}

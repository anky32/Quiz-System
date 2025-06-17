//package quiz;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class Main extends JFrame {
//    public Main() {
//        setTitle("Quiz Game - Main Menu");
//        setSize(700, 450);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // Create a panel to hold buttons in the center
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5)); // 5 rows, 1 column, spacing
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 1)); // Add padding
//
//        // Create buttons
//        JButton loginButton = new JButton("Login");
//        JButton signupButton = new JButton("Signup");
//        JButton adminPanelButton = new JButton("Admin Panel");
//        JButton reportButton = new JButton("View High Scores");
//        JButton exitButton = new JButton("Exit");
//
//        // Add action listeners
//        loginButton.addActionListener(e -> new Login());
//        signupButton.addActionListener(e -> new Signup());
//        adminPanelButton.addActionListener(e -> new AdminPanel());
//        reportButton.addActionListener(e -> new Report());
//        exitButton.addActionListener(e -> System.exit(0));
//
//        // Style buttons (uniform size)
//        Font buttonFont = new Font("Cascadia Mono SemiBold", Font.BOLD, 15);
//        loginButton.setFont(buttonFont);
//        signupButton.setFont(buttonFont);
//        adminPanelButton.setFont(buttonFont);
//        reportButton.setFont(buttonFont);
//        exitButton.setFont(buttonFont);
//
//        // Add buttons to the panel
//        buttonPanel.add(loginButton);
//        buttonPanel.add(signupButton);
//        buttonPanel.add(adminPanelButton);
//        buttonPanel.add(reportButton);
//        buttonPanel.add(exitButton);
//
//        // Add the panel to the center of the frame
//        add(buttonPanel, BorderLayout.CENTER);
//
//        setLocationRelativeTo(null); // Center the window
//        setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(Main::new);
//    }
//}



package quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    public Main() {
        setTitle("Quiz Game - Main Menu");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Add padding

        // Create buttons
        JButton loginButton = createStyledButton("Login");
        JButton signupButton = createStyledButton("Signup");
        JButton adminPanelButton = createStyledButton("Admin Panel");
        JButton reportButton = createStyledButton("View High Scores");
        JButton exitButton = createStyledButton("Exit");

        // Add button actions
        loginButton.addActionListener(e -> new Login());
        signupButton.addActionListener(e -> new Signup());
        adminPanelButton.addActionListener(e -> new AdminPanel());
        reportButton.addActionListener(e -> new Report());
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to panel
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        buttonPanel.add(adminPanelButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(exitButton);

        // Center panel in frame
        add(buttonPanel);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // 🎨 Styled button method
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180)); // Light Blue
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Darker blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Back to normal
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}


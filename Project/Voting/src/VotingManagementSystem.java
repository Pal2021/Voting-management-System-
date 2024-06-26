import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VotingManagementSystem {
    private JFrame frame;
    @SuppressWarnings("unused")
    private Connection connection;

    public VotingManagementSystem() {
        frame = new JFrame("Voting Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(240, 240, 240));
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); 

        JLabel titleLabel = new JLabel("Welcome to Voting Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, gbc);

        gbc.gridy++;

        JLabel voterLabel = new JLabel("For Voters:");
        voterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(voterLabel, gbc);

        gbc.gridy++;

        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(200, 40)); 
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                openRegistrationScreen();
            }
        });
        panel.add(registerButton, gbc);

        gbc.gridy++;

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 40)); 
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
                openLoginScreen();
            }
        });
        panel.add(loginButton, gbc);

        gbc.gridy++;

        JLabel adminLabel = new JLabel("For Admin:");
        adminLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(adminLabel, gbc);

        gbc.gridy++;

        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setPreferredSize(new Dimension(200, 40));
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                openAdminLoginScreen();
            }
        });
        panel.add(adminLoginButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void openRegistrationScreen() {
        JFrame registrationFrame = new JFrame("Registration");
        registrationFrame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField fullNameField = new JTextField();
        panel.add(fullNameLabel);
        panel.add(fullNameField);

        JLabel voterIdProofLabel = new JLabel("Voter ID Proof Image Path:");
        JTextField voterIdProofField = new JTextField();
        panel.add(voterIdProofLabel);
        panel.add(voterIdProofField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
                registerUser(emailField.getText(), new String(passwordField.getPassword()), fullNameField.getText(), voterIdProofField.getText());
            }
        });
        panel.add(registerButton);

        registrationFrame.add(panel);
        registrationFrame.setVisible(true);
    }

    private void registerUser(String email, String password, String fullName, String voterIdProofImagePath) {
    
        Connection connection = null;
        try {
           
            String jdbcURL = "jdbc:mysql://localhost:3306/voting_management";
            String username = "root";
            String dbPassword = "159Atg45@"; 
            connection = DriverManager.getConnection(jdbcURL, username, dbPassword);

            String sql = "INSERT INTO users (email, password, full_name, voter_id_proof_path) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, voterIdProofImagePath);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "User registered successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to register user.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void openLoginScreen() {
        JPanel loginPanel = new LoginScreen().getLoginPanel();

        frame.getContentPane().removeAll();

        frame.add(loginPanel);

        frame.revalidate();
        frame.repaint();
    }

    private void openAdminLoginScreen() {
     
        String adminUsername = "admin";
        String adminPassword = "admin123";

        String inputUsername = JOptionPane.showInputDialog(frame, "Enter Admin Username:");
        String inputPassword = JOptionPane.showInputDialog(frame, "Enter Admin Password:");

        if (inputUsername != null && inputPassword != null &&
                inputUsername.equals(adminUsername) && inputPassword.equals(adminPassword)) {
            try {
              
                String jdbcURL = "jdbc:mysql://localhost:3306/voting_management";
                String username = "root";
                String dbPassword = "159Atg45@";
                Connection connection = DriverManager.getConnection(jdbcURL, username, dbPassword);

                Admin admin = new Admin(frame, connection);
                admin.openAdminView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Admin Credentials!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VotingManagementSystem();
            }
        });
    }
}

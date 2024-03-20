import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Best_Classification extends JFrame implements ActionListener {

    private JPanel startPanel;
    private JPanel loginPanel;
    private JPanel signupPanel;
    private JTextField usernameFieldSignup;
    private JPasswordField passwordFieldSignup;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JFileChooser fileChooser;
    private JLabel backgroundLabel;
    private JLabel loadingLabel;
    List<String> techniques = Arrays.asList("no technique", "PCA", "IncPCA", "ICA", "LDA", "SMOTE");
    List<String> models = Arrays.asList("Naive Bayes", "SVM", "MLP", "Tree", "KNN", "LogReg");
    StringBuilder pythonOutput = new StringBuilder();

    public Best_Classification() {
        super("Best Classification AI");

        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        createSignupPanel();
        signupPanel.setVisible(false);
        createLoginPanel();
        loginPanel.setVisible(false);
        createStartPanel();
        

        // Set file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
            }

            public String getDescription() {
                return "CSV files (*.csv)";
            }
        });

        // Set fixed size for the window
        setSize(1280, 720);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createStartPanel() {
        startPanel = new JPanel(new GridLayout(3, 2));
        startPanel.setBorder(BorderFactory.createEmptyBorder(250, 350, 250, 350));

        JButton GoToSignupPanel = new JButton("Signup");
        GoToSignupPanel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
        GoToSignupPanel.addActionListener(this);

        JButton GoToLoginPanel = new JButton("Login");
        GoToLoginPanel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
        GoToLoginPanel.addActionListener(this);

        startPanel.add(GoToSignupPanel);
        startPanel.add(GoToLoginPanel);

        setContentPane(startPanel);
        ImageIcon startBackgroundImage = new ImageIcon("resources\\background.gif");
        backgroundLabel = new JLabel(startBackgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(startPanel, BorderLayout.CENTER);
        setContentPane(backgroundLabel);
    }

    private void createSignupPanel() {
        signupPanel = new JPanel(new GridLayout(3, 2));
        signupPanel.setBorder(BorderFactory.createEmptyBorder(250, 350, 250, 350));
        JLabel usernameLabelSignup = new JLabel("Username:");
        usernameLabelSignup.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 16));
        JLabel passwordLabelSignup = new JLabel("Password:");
        passwordLabelSignup.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 16));

        usernameFieldSignup = new JTextField();
        usernameFieldSignup.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 18));
        passwordFieldSignup = new JPasswordField();
        passwordFieldSignup.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 18));
    
        JButton signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
        signupButton.addActionListener(this);

        signupPanel.add(usernameLabelSignup);
        signupPanel.add(usernameFieldSignup);
        signupPanel.add(passwordLabelSignup);
        signupPanel.add(passwordFieldSignup);
        signupPanel.add(signupButton);
    
        setContentPane(signupPanel);

    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(250, 350, 250, 350));
    
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 18));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 18));
    
        usernameField = new JTextField();
        usernameField.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 18));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 18));
    
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
        loginButton.addActionListener(this);
    
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
    
        
    

    }

    private void createMainPanel() {    
        // Load GIF image for background
        ImageIcon backgroundImage =  new ImageIcon("resources\\background.gif");

        // Create a JLabel with the image
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        // Create components
        JButton openButton = new JButton("Analyse All");
        openButton.addActionListener(this);
        openButton.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 32));

        // Create two bigger buttons on top
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);

        JButton Button1 = new JButton("Analyse Models");
        Button1.addActionListener(this);
        Button1.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
        Button1.setPreferredSize(new Dimension(300, 70));

        JButton Button2 = new JButton("Analyse Techniques");
        Button2.addActionListener(this);
        Button2.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 24));
        Button2.setPreferredSize(new Dimension(300, 70));

        // Change layout to GridBagLayout for more flexibility
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 10, 5);
        topPanel.add(Button1, gbc);

        gbc.gridx = 1;
        topPanel.add(Button2, gbc);

        // Layout components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(150, 312, 270, 312));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Decrease the size of the "Analyse All" button
        openButton.setPreferredSize(new Dimension(150, 50));

        // Initialize loadingLabel
        loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 42));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setVisible(false);

        // Add loadingLabel to a new panel
        JPanel loadingPanel = new JPanel();
        loadingPanel.setOpaque(false);
        loadingPanel.add(loadingLabel);

        // Add loadingPanel to mainPanel below the existing components
        mainPanel.add(loadingPanel, BorderLayout.SOUTH);

        backgroundLabel.add(mainPanel);

        mainPanel.add(openButton, BorderLayout.CENTER);

        // Set the content pane to the background label
        setContentPane(backgroundLabel);
    }


    public void actionPerformed(ActionEvent e) {    
        if (startPanel.isVisible()) {
            startPanel.setVisible(false);
            
            if (e.getActionCommand().equals("Signup")) {      
                signupPanel.setVisible(true);
                setContentPane(signupPanel);
            }
            else if (e.getActionCommand().equals("Login")) {
                loginPanel.setVisible(true);
                setContentPane(loginPanel);
            }
        }

        else if (signupPanel.isVisible()) {
            String username = usernameFieldSignup.getText();
            char[] password = passwordFieldSignup.getPassword();
            InsertData.main(new String[]{username, new String(password)});
            signupPanel.setVisible(false);
            startPanel.setVisible(true);
            setContentPane(startPanel);
            
        }
        
        else if (loginPanel.isVisible()) {

            // Check login credentials
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            String[] credentions_list = RetrieveData.credentials();
            String loginCredentials = username + ", " + new String(password);

            boolean found = false;
            for (String realCredential : credentions_list) {
                if (loginCredentials.equals(realCredential)) {
                    found = true;
                    loginPanel.setVisible(false);
                    createMainPanel();  // Show main panel after successful login
                    break;
                    
                } 
                else { }                
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else {
            // Process main panel actions
            if (e.getActionCommand().equals("Analyse All")) {
                openCSV();
            } else if (e.getActionCommand().equals("Analyse Models")) {
                techniques = Arrays.asList("none");
                openCSV();
            } else if (e.getActionCommand().equals("Analyse Techniques")) {
                models = Arrays.asList("SVM");
                openCSV();
            }
        }
    }

    private void openCSV() {
        loadingLabel.setVisible(true);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            handleCSV(fileChooser.getSelectedFile());
        }
        else {
            loadingLabel.setVisible(false);
        }
    }

    private void handleCSV(File file) {
        try {
            pythonOutput.append("technique,model,f1_score,processing_time,memory_usage").append("\n");
            for (String technique : techniques) {
                for (String model : models) {
                    // Execute the Python script passing the CSV file path
                    ProcessBuilder pb = new ProcessBuilder("python", "code\\program_analysis.py", file.getAbsolutePath(), technique, model);
                    pb.redirectErrorStream(true);
                    Process process = pb.start();

                    // Read the output from the Python script
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        pythonOutput.append(line).append("\n");
                    }

                    // Wait for the process to finish
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        JOptionPane.showMessageDialog(this, "Error analysis script", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            

            String returnedString = pythonOutput.toString();

            // Save the returned string as a CSV file
            saveStringAsCSV(returnedString, "results table\\results.csv");
             
             revalidate();
             repaint();

             // Generate image
             ProcessBuilder pb = new ProcessBuilder("python", "code\\program_plot.py");
             pb.redirectErrorStream(true);
             Process process = pb.start();

             int exitCode = process.waitFor();
            if (exitCode != 0) {
                JOptionPane.showMessageDialog(this, "Error plot script", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Show returned image
            ImageIcon returnedImage = new ImageIcon("results image\\graphs.png");
            JLabel imageLabel = new JLabel(returnedImage);
            imageLabel.setIcon(returnedImage);
            JScrollPane scrollPane = new JScrollPane(imageLabel);
            setContentPane(scrollPane);

            pack();
            setLocationRelativeTo(null);
            setVisible(true);

            // Revalidate and repaint the frame to reflect the changes
            revalidate();
            repaint();

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error running Python script: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://127.0.0.1:3306/BestClassificationAI";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "bruh9142334!";
    
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
    }

    public class InsertData {
        public static void main(String[] args) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, args[0]);
                statement.setString(2, args[1]);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class RetrieveData {
        public static void main(String[] args) {
            
        }

        protected static String[] credentials () {
            List<String> stringList = new ArrayList<>();
            try (Connection connection = DatabaseConnection.getConnection()) {
                
                String sql = "SELECT * FROM user";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    // Retrieve data from the result set
                    String column1Value = resultSet.getString("username");
                    String column2Value = resultSet.getString("password");
                    stringList.add(column1Value + ", " + column2Value);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String[] table = stringList.toArray(new String[0]);
            return table;
        }
    }

    private static void saveStringAsCSV(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Best_Classification::new);
    }
}
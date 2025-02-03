import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Account implements Serializable {
    private String accountHolder;
    private double balance;
    private String email;
    private String phoneNumber;
    private String address;
    private String nidNumber;
    private List<Transaction> transactions;

    public Account(String accountHolder, double initialDeposit, String email, String phoneNumber, String address, String nidNumber) {
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nidNumber = nidNumber;
        this.transactions = new ArrayList<>();
        this.transactions.add(new Transaction("Initial Deposit", initialDeposit, new Date()));
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getNidNumber() {
        return nidNumber;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction("Deposit", amount, new Date()));
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", -amount, new Date()));
            return true;
        }
        return false;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }
}

class Transaction implements Serializable {
    private String description;
    private double amount;
    private Date date;

    public Transaction(String description, double amount, Date date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date) + " - " + description + ": " + amount;
    }
}

class Loan implements Serializable {
    private String accountId;
    private double amount;
    private String status;
    private Date applicationDate;

    public Loan(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.status = "Pending";
        this.applicationDate = new Date();
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Account ID: " + accountId + ", Amount: " + amount + ", Status: " + status + ", Application Date: " + sdf.format(applicationDate);
    }
}

class Authentication {
    private static final String CREDENTIALS_FILE = "credentials.dat";
    private static final Map<String, String> credentials = new HashMap<>();

    static {
        loadCredentials();
    }

    public boolean login(String role, String password) {
        if (!credentials.containsKey(role)) {
            JOptionPane.showMessageDialog(null, "Invalid role.");
            return false;
        }
        if (!credentials.get(role).equals(password)) {
            JOptionPane.showMessageDialog(null, "Invalid credentials.");
            return false;
        }
        return true;
    }

    public void updatePassword(String role, String newPassword) {
        credentials.put(role, newPassword);
        saveCredentials();
    }

    private static void loadCredentials() {
        File file = new File(CREDENTIALS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Map<String, String> savedCredentials = (Map<String, String>) ois.readObject();
                credentials.putAll(savedCredentials);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            credentials.put("Admin", "Admin@123");
            credentials.put("Manager", "Manager@123");
            credentials.put("Customer", "Customer@123");
        }
    }

    private static void saveCredentials() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CREDENTIALS_FILE))) {
            oos.writeObject(credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class BankingManagementSystem {
    private static final String ACCOUNTS_FILE = "accounts.dat";
    private static final String LOANS_FILE = "loans.dat";
    private static final Map<String, Account> accounts = new HashMap<>();
    private static final Map<String, Loan> loans = new HashMap<>();
    private static final AtomicInteger accountCounter = new AtomicInteger(1);

    public static void main(String[] args) {
        loadAccounts(); // Load accounts from file at program start
        loadLoans(); // Load loans from file at program start

        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(700, 400);

            JPanel mainPanel = new JPanel(new BorderLayout());
           
			
			
            JLabel imageLabel = new JLabel();
            ImageIcon icon = new ImageIcon("E:\\JAVA\\Bank\\Banking_Management_System_Poster.jpg");
            Image img = icon.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            mainPanel.add(imageLabel, BorderLayout.WEST);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2, 10, 10));
          
			
			
            JLabel roleLabel = new JLabel("Role:");
            String[] roles = {"Admin", "Manager", "Customer"};
            JComboBox<String> roleDropdown = new JComboBox<>(roles);

            JLabel passLabel = new JLabel("Password:");
            JPasswordField passField = new JPasswordField();

            JCheckBox showPassword = new JCheckBox("Show Password");

           JButton loginButton = new JButton("Login");
            loginButton.setBackground(Color.GREEN);
            loginButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    loginButton.setBackground(Color.YELLOW);
                    loginButton.setFont(new Font("Arial", Font.BOLD, 16));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    loginButton.setBackground(Color.GREEN);
                    loginButton.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            });

            panel.add(roleLabel);
            panel.add(roleDropdown);
            panel.add(passLabel);
            panel.add(passField);
            panel.add(showPassword);
            panel.add(new JLabel());
            panel.add(loginButton);

            mainPanel.add(panel, BorderLayout.CENTER);

            loginFrame.add(mainPanel);

            // Fade-in animation for login screen
            Timer fadeTimer = new Timer(30, new ActionListener() {
                private float opacity = 0f;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (opacity < 1f) {
                        opacity += 0.05f;
                        mainPanel.setBackground(new Color(0, 0, 0, (int) (opacity * 255)));
                    } else {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            fadeTimer.start();

            loginFrame.setVisible(true);

            showPassword.addActionListener(e -> passField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*'));

            loginButton.addActionListener(e -> {
                String role = (String) roleDropdown.getSelectedItem();
                String pass = new String(passField.getPassword());

                Authentication auth = new Authentication();
                if (auth.login(role, pass)) {
                    loginFrame.dispose();
                    showMenu(role);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials.");
                }
            });
        });
    }

    private static void showMenu(String role) {
        JFrame menuFrame = new JFrame("Banking Management System");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 400);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 0, 0, 0));

        String[] options;
        if (role.equals("Admin")) {
            options = new String[]{"Create Account", "Deposit", "Withdraw", "Transfer Funds", "Check Balance", "View Account Details", "Edit Account Details", "Set Password", "Approve Loans", "Exit"};
        } else if (role.equals("Manager")) {
            options = new String[]{"Create Account", "View Account Details", "Edit Account Details", "Set Password", "Approve Loans", "Exit"};
        } else {
            options = new String[]{"Deposit", "Withdraw", "Check Balance", "Apply for Loan", "View Mini Statement", "Exit"};
        }

        
        JPanel panel = new JPanel(new GridLayout(options.length, 1));
        panel.setBackground(new Color(0, 0, 0, 0));

       for (String option : options) {
            JButton button = new JButton(option);
            button.setBackground(Color.GREEN);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(Color.YELLOW);
                    button.setFont(new Font("Arial", Font.BOLD, 16));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.GREEN);
                    button.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            });
            button.addActionListener(e -> handleMenuOption(option, role, menuFrame));
            panel.add(button);
        }

        mainPanel.add(panel, BorderLayout.CENTER);

        // Slide-in animation for menu panel
        Timer slideTimer = new Timer(10, new ActionListener() {
            private int x = -400;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (x < 0) {
                    x += 10;
                    panel.setBounds(x, 0, 400, 400);
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        slideTimer.start();

        menuFrame.add(mainPanel);
        menuFrame.setVisible(true);
    }

    private static void handleMenuOption(String option, String role, JFrame menuFrame) {
        switch (option) {
            case "Create Account":
                createAccount();
                break;
            case "Deposit":
                deposit();
                break;
            case "Withdraw":
                withdraw();
                break;
            case "Transfer Funds":
                transferFunds();
                break;
            case "Check Balance":
                checkBalance();
                break;
            case "View Account Details":
                viewAccountDetails();
                break;
            case "Edit Account Details":
                editAccountDetails();
                break;
            case "Set Password":
                setPassword(role);
                break;
            case "Apply for Loan":
                applyForLoan();
                break;
            case "Approve Loans":
                approveLoans();
                break;
            case "View Mini Statement":
                viewMiniStatement();
                break;
            case "Exit":
                menuFrame.dispose();
                main(null);
                break;
            default:
                JOptionPane.showMessageDialog(menuFrame, "Invalid choice.");
        }
    }

    private static void createAccount() {
        JFrame createAccountFrame = new JFrame("Create Account");
        createAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createAccountFrame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel nameLabel = new JLabel("Account Holder Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel nidLabel = new JLabel("NID Number:");
        JTextField nidField = new JTextField();
        JLabel depositLabel = new JLabel("Initial Deposit:");
        JTextField depositField = new JTextField();

        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneField.getText();
            String address = addressField.getText();
            String nidNumber = nidField.getText();
            String depositText = depositField.getText();

            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || nidNumber.isEmpty() || depositText.isEmpty()) {
                JOptionPane.showMessageDialog(createAccountFrame, "All fields must be filled.");
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(createAccountFrame, "Invalid email format.");
                return;
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                JOptionPane.showMessageDialog(createAccountFrame, "Invalid phone number. It should be 11 digits.");
                return;
            }

            if (!isValidNidNumber(nidNumber)) {
                JOptionPane.showMessageDialog(createAccountFrame, "Invalid NID number. It should be 9 digits.");
                return;
            }

            double depositAmount;
            try {
                depositAmount = Double.parseDouble(depositText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(createAccountFrame, "Invalid deposit amount.");
                return;
            }

            String accountId = String.format("ACC%04d", accountCounter.getAndIncrement());

            Account newAccount = new Account(name, depositAmount, email, phoneNumber, address, nidNumber);
            accounts.put(accountId, newAccount);
            saveAccounts(); 

            JOptionPane.showMessageDialog(createAccountFrame, "Account created successfully.\nAccount Number: " + accountId);
            createAccountFrame.dispose();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(nidLabel);
        panel.add(nidField);
        panel.add(depositLabel);
        panel.add(depositField);
        panel.add(new JLabel());
        panel.add(submitButton);

        createAccountFrame.add(panel);
        createAccountFrame.setVisible(true);
    }

    private static void deposit() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        String amountInput = JOptionPane.showInputDialog("Enter deposit amount:");
        double amount;
        try {
            amount = Double.parseDouble(amountInput);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid deposit amount.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
            return;
        }

        Account account = accounts.get(accountId);
        account.deposit(amount);
        saveAccounts(); 

        JOptionPane.showMessageDialog(null, "Deposit successful. New balance: " + account.getBalance());
    }

    private static void withdraw() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        String amountInput = JOptionPane.showInputDialog("Enter withdrawal amount:");
        double amount;
        try {
            amount = Double.parseDouble(amountInput);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid withdrawal amount.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
            return;
        }

        Account account = accounts.get(accountId);
        if (account.withdraw(amount)) {
            saveAccounts(); 
            JOptionPane.showMessageDialog(null, "Withdrawal successful. New balance: " + account.getBalance());
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient funds.");
        }
    }

    private static void viewMiniStatement() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        Account account = accounts.get(accountId);
        List<Transaction> transactions = account.getTransactions();

        StringBuilder statement = new StringBuilder("Mini Statement for Account ID: " + accountId + "\n");
        for (Transaction transaction : transactions) {
            statement.append(transaction.toString()).append("\n");
        }

        JOptionPane.showMessageDialog(null, statement.toString());
    }


    private static void checkBalance() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        Account account = accounts.get(accountId);
        JOptionPane.showMessageDialog(null, "Balance: " + account.getBalance());
    }

    private static void viewAccountDetails() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        Account account = accounts.get(accountId);

        String accountDetails = String.format(
                "Account ID: %s\n" +
                "Account Holder: %s\n" +
                "Email: %s\n" +
                "Phone Number: %s\n" +
                "Address: %s\n" +
                "NID Number: %s\n" +
                "Balance: %.2f",
                accountId,
                account.getAccountHolder(),
                account.getEmail(),
                account.getPhoneNumber(),
                account.getAddress(),
                account.getNidNumber(),
                account.getBalance()
        );

        JOptionPane.showMessageDialog(null, accountDetails);
    }

    private static void transferFunds() {
        String senderAccountId = JOptionPane.showInputDialog("Enter sender account ID:");
        if (!accounts.containsKey(senderAccountId)) {
            JOptionPane.showMessageDialog(null, "Sender account not found.");
            return;
        }

        String receiverAccountId = JOptionPane.showInputDialog("Enter receiver account ID:");
        if (!accounts.containsKey(receiverAccountId)) {
            JOptionPane.showMessageDialog(null, "Receiver account not found.");
            return;
        }

        String amountInput = JOptionPane.showInputDialog("Enter transfer amount:");
        double amount;
        try {
            amount = Double.parseDouble(amountInput);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid transfer amount.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
            return;
        }

        Account senderAccount = accounts.get(senderAccountId);
        Account receiverAccount = accounts.get(receiverAccountId);

        if (senderAccount.getBalance() < amount) {
            JOptionPane.showMessageDialog(null, "Insufficient funds.");
            return;
        }

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);
        saveAccounts(); 

        JOptionPane.showMessageDialog(null, "Transfer successful! Sender new balance: " + senderAccount.getBalance() +
                "\nReceiver new balance: " + receiverAccount.getBalance());
    }

    private static void editAccountDetails() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        Account account = accounts.get(accountId);

        JFrame editFrame = new JFrame("Edit Account Details");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel nameLabel = new JLabel("Account Holder Name:");
        JTextField nameField = new JTextField(account.getAccountHolder());
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(account.getEmail());
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField(account.getPhoneNumber());
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(account.getAddress());
        JLabel nidLabel = new JLabel("NID Number:");
        JTextField nidField = new JTextField(account.getNidNumber());

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            String newPhone = phoneField.getText();
            String newAddress = addressField.getText();
            String newNid = nidField.getText();

            if (!isValidEmail(newEmail)) {
                JOptionPane.showMessageDialog(editFrame, "Invalid email format.");
                return;
            }

            if (!isValidPhoneNumber(newPhone)) {
                JOptionPane.showMessageDialog(editFrame, "Invalid phone number. It should be 11 digits.");
                return;
            }

            if (!isValidNidNumber(newNid)) {
                JOptionPane.showMessageDialog(editFrame, "Invalid NID number. It should be 9 digits.");
                return;
            }

            account.setAccountHolder(newName);
            account.setEmail(newEmail);
            account.setPhoneNumber(newPhone);
            account.setAddress(newAddress);
            account.setNidNumber(newNid);
            saveAccounts(); 

            JOptionPane.showMessageDialog(editFrame, "Account details updated successfully.");
            editFrame.dispose();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(nidLabel);
        panel.add(nidField);
        panel.add(new JLabel());
        panel.add(saveButton);

        editFrame.add(panel);
        editFrame.setVisible(true);
    }

    private static void setPassword(String role) {
        String newPassword = JOptionPane.showInputDialog("Enter new password:");
        if (newPassword == null || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty.");
            return;
        }

        Authentication auth = new Authentication();
        auth.updatePassword(role, newPassword);
        JOptionPane.showMessageDialog(null, "Password updated successfully.");
    }

    private static void applyForLoan() {
        String accountId = JOptionPane.showInputDialog("Enter account ID:");
        if (!accounts.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Account not found.");
            return;
        }

        String amountInput = JOptionPane.showInputDialog("Enter loan amount:");
        double amount;
        try {
            amount = Double.parseDouble(amountInput);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid loan amount.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
            return;
        }

        Loan newLoan = new Loan(accountId, amount);
        loans.put(accountId, newLoan);
        saveLoans(); 

        JOptionPane.showMessageDialog(null, "Loan application submitted successfully.");
    }
    
	private static void notifyCustomer(String accountId, String message) {
        JOptionPane.showMessageDialog(null, "Notification for Account ID: " + accountId + "\n" + message);
    }
    
	private static void approveLoans() {
        if (loans.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No pending loans.");
            return;
        }

        StringBuilder loanDetails = new StringBuilder("Pending Loans:\n");
        for (Map.Entry<String, Loan> entry : loans.entrySet()) {
            if (entry.getValue().getStatus().equals("Pending")) {
                loanDetails.append(entry.getValue().toString()).append("\n");
            }
        }

        String accountId = JOptionPane.showInputDialog(loanDetails.toString() + "\nEnter account ID to approve or reject:");
        if (!loans.containsKey(accountId)) {
            JOptionPane.showMessageDialog(null, "Loan not found.");
            return;
        }

        Loan loan = loans.get(accountId);
        String action = JOptionPane.showInputDialog("Enter 'approve' to approve the loan or 'reject' to reject it:");
        if (action == null) {
            return;
        }

        if (action.equalsIgnoreCase("approve")) {
            loan.setStatus("Approved");
            Account account = accounts.get(accountId);
            account.deposit(loan.getAmount()); // Add the loan amount to the account balance
            account.getTransactions().add(new Transaction("Loan Approved", loan.getAmount(), new Date())); // Record the loan as a transaction
            saveAccounts(); // Save accounts after approval
            saveLoans(); // Save loans after approval
            JOptionPane.showMessageDialog(null, "Loan approved successfully. Amount added to the account.");
        } else if (action.equalsIgnoreCase("reject")) {
            loan.setStatus("Rejected");
            saveLoans(); // Save loans after rejection
            notifyCustomer(accountId, "Your loan application has been rejected.");
            JOptionPane.showMessageDialog(null, "Loan rejected. The customer will be notified.");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid action.");
        }
    }

   
    
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{11}");
    }

    private static boolean isValidNidNumber(String nidNumber) {
        return nidNumber.matches("\\d{9}");
    }

    private static void loadAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Map<String, Account> savedAccounts = (Map<String, Account>) ois.readObject();
                accounts.putAll(savedAccounts);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNTS_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadLoans() {
        File file = new File(LOANS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Map<String, Loan> savedLoans = (Map<String, Loan>) ois.readObject();
                loans.putAll(savedLoans);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveLoans() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LOANS_FILE))) {
            oos.writeObject(loans);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package myApp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import myLib.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IDGenerator idGenerator = new IDGenerator();
    private static final PasswordManager passwordManager = new PasswordManager();
    private static final Map<String, Account> accounts = new HashMap<>();
    private static final Map<String, Investment> investments = new HashMap<>();
    private static String currentUserId = null;
    private static String currentAccountId = null;

    public static void main(String[] args) {
        System.out.println("=== Welcome to MCASH Banking System ===");

        do {
            if (currentUserId == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        } while (true);
    }

    private static void showLoginMenu() {
        while (true) {
            System.out.println("\n--- Login Menu ---");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    loginUser();
                    if (currentUserId != null) {
                        return; // Exit login menu after successful login
                    }
                    break;
                case "3":
                    System.out.println("Thank you for using Simple Banking System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Select Account");
            System.out.println("3. Account Operations");
            System.out.println("4. Investment Operations");
            System.out.println("5. View All Accounts");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    selectAccount();
                    break;
                case "3":
                    if (currentAccountId != null) {
                        accountOperations();
                    } else {
                        System.out.println("Please select an account first.");
                    }
                    break;
                case "4":
                    if (currentAccountId != null) {
                        investmentOperations();
                    } else {
                        System.out.println("Please select an account first.");
                    }
                    break;
                case "5":
                    viewAllAccounts();
                    break;
                case "6":
                    currentUserId = null;
                    currentAccountId = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerUser() {
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            if (!Utility.validateString(username)) {
                System.out.println("Invalid username. Please try again.");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            if (!Utility.validateString(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }

            if (passwordManager.registerUser(username, password)) {
                System.out.println("User registered successfully!");
                break;
            } else {
                System.out.println("Username already exists. Please try again.");
            }
        }
    }

    private static void loginUser() {
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            if (!Utility.validateString(username)) {
                System.out.println("Invalid username. Please try again.");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            if (!Utility.validateString(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }

            if (passwordManager.authenticateUser(username, password)) {
                currentUserId = username; // Use the actual username as userId
                System.out.println("Login successful! Welcome " + username);
                break;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    private static void createAccount() {
        while (true) {
            System.out.println("\n--- Create Account ---");
            System.out.println("1. Checking Account");
            System.out.println("2. Savings Account");
            System.out.print("Choose account type: ");

            String choice = scanner.nextLine().trim();
            String accountId = idGenerator.generateAccountId();
            String passwordHash = "dummy_hash"; // In real app, would hash a password

            switch (choice) {
                case "1":
                    createCheckingAccount(accountId, passwordHash);
                    return;
                case "2":
                    createSavingsAccount(accountId, passwordHash);
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createCheckingAccount(String accountId, String passwordHash) {
        while (true) {
            System.out.print("Enter overdraft limit: $");
            String limitStr = scanner.nextLine().trim();

            if (!Utility.validateDouble(limitStr)) {
                System.out.println("Invalid amount. Please try again.");
                continue;
            }

            double overdraftLimit = Double.parseDouble(limitStr);
            CheckingAccount account = new CheckingAccount(accountId, currentUserId, overdraftLimit, passwordHash);
            accounts.put(accountId, account);
            System.out.println("Checking account created successfully! Account ID: " + accountId);
            break;
        }
    }

    private static void createSavingsAccount(String accountId, String passwordHash) {
        while (true) {
            System.out.print("Enter interest rate (%): ");
            String rateStr = scanner.nextLine().trim();

            if (!Utility.validateDouble(rateStr)) {
                System.out.println("Invalid rate. Please try again.");
                continue;
            }

            double interestRate = Double.parseDouble(rateStr);
            SavingsAccount account = new SavingsAccount(accountId, currentUserId, interestRate, passwordHash);
            accounts.put(accountId, account);
            System.out.println("Savings account created successfully! Account ID: " + accountId);
            break;
        }
    }

    private static void selectAccount() {
        List<Account> userAccounts = getUserAccounts();

        if (userAccounts.isEmpty()) {
            System.out.println("No accounts found. Please create an account first.");
            return;
        }

        while (true) {
            System.out.println("\n--- Your Accounts ---");
            for (int i = 0; i < userAccounts.size(); i++) {
                Account account = userAccounts.get(i);
                System.out.println((i + 1) + ". " + account.getAccountSummary());
            }
            System.out.print("Select account (1-" + userAccounts.size() + "): ");

            String choice = scanner.nextLine().trim();

            if (!Utility.validateInt(choice)) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }

            int accountIndex = Integer.parseInt(choice) - 1;
            if (accountIndex >= 0 && accountIndex < userAccounts.size()) {
                currentAccountId = userAccounts.get(accountIndex).getAccountId();
                System.out.println("Account selected: " + currentAccountId);
                break;
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private static void accountOperations() {
        Account account = accounts.get(currentAccountId);

        while (true) {
            System.out.println("\n--- Account Operations ---");
            System.out.println("Current Account: " + account.getAccountSummary());
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Balance");
            System.out.println("4. View Transaction History");
            if (account instanceof SavingsAccount) {
                System.out.println("5. Apply Interest");
            }
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    deposit(account);
                    break;
                case "2":
                    withdraw(account);
                    break;
                case "3":
                    System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
                    break;
                case "4":
                    viewTransactionHistory(account);
                    break;
                case "5":
                    if (account instanceof SavingsAccount) {
                        applyInterest((SavingsAccount) account);
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void deposit(Account account) {
        while (true) {
            System.out.print("Enter deposit amount: $");
            String amountStr = scanner.nextLine().trim();

            if (!Utility.validateDouble(amountStr)) {
                System.out.println("Invalid amount. Please try again.");
                continue;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                System.out.println("Amount must be positive. Please try again.");
                continue;
            }

            String transactionId = idGenerator.generateTransactionId();
            account.deposit(amount, transactionId);

            // Create and send notification
            Transaction transaction = new Transaction.Builder(
                    transactionId, currentAccountId, amount,
                    TransactionType.DEPOSIT, "Deposit to account"
            ).build();

            Notification notification = Notification.createFromTransaction(
                    idGenerator.generateTransactionId(), transaction, notificationType.IN_APP_SENT
            );
            notification.sendNotification();
            break;
        }
    }

    private static void withdraw(Account account) {
        while (true) {
            System.out.print("Enter withdrawal amount: $");
            String amountStr = scanner.nextLine().trim();

            if (!Utility.validateDouble(amountStr)) {
                System.out.println("Invalid amount. Please try again.");
                continue;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                System.out.println("Amount must be positive. Please try again.");
                continue;
            }

            String transactionId = idGenerator.generateTransactionId();
            account.withdraw(amount, transactionId);

            // Create and send notification
            Transaction transaction = new Transaction.Builder(
                    transactionId, currentAccountId, amount,
                    TransactionType.WITHDRAWAL, "Withdrawal from account"
            ).build();

            Notification notification = Notification.createFromTransaction(
                    idGenerator.generateTransactionId(), transaction, notificationType.IN_APP_SENT
            );
            notification.sendNotification();
            break;
        }
    }

    private static void applyInterest(SavingsAccount account) {
        String transactionId = idGenerator.generateTransactionId();
        account.applyInterest(transactionId);
    }

    private static void viewTransactionHistory(Account account) {
        List<Transaction> transactions = account.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n--- Transaction History ---");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getTransactionDetails());
        }
    }

    private static void investmentOperations() {
        while (true) {
            System.out.println("\n--- Investment Operations ---");
            System.out.println("1. Create Stock Investment");
            System.out.println("2. Create Bond Investment");
            System.out.println("3. View Investments");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createStockInvestment();
                    break;
                case "2":
                    createBondInvestment();
                    break;
                case "3":
                    viewInvestments();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createStockInvestment() {
        while (true) {
            try {
                System.out.print("Enter stock name: ");
                String name = scanner.nextLine().trim();
                if (!Utility.validateString(name)) {
                    System.out.println("Invalid name. Please try again.");
                    continue;
                }

                System.out.print("Enter ticker symbol: ");
                String ticker = scanner.nextLine().trim();
                if (!Utility.validateString(ticker)) {
                    System.out.println("Invalid ticker. Please try again.");
                    continue;
                }

                System.out.print("Enter purchase price: $");
                String priceStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(priceStr)) {
                    System.out.println("Invalid price. Please try again.");
                    continue;
                }
                double purchasePrice = Double.parseDouble(priceStr);

                System.out.print("Enter current price: $");
                String currentPriceStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(currentPriceStr)) {
                    System.out.println("Invalid price. Please try again.");
                    continue;
                }
                double currentPrice = Double.parseDouble(currentPriceStr);

                System.out.print("Enter quantity: ");
                String quantityStr = scanner.nextLine().trim();
                if (!Utility.validateInt(quantityStr)) {
                    System.out.println("Invalid quantity. Please try again.");
                    continue;
                }
                int quantity = Integer.parseInt(quantityStr);

                System.out.print("Enter dividend yield (as decimal, e.g., 0.05 for 5%): ");
                String dividendStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(dividendStr)) {
                    System.out.println("Invalid dividend yield. Please try again.");
                    continue;
                }
                double dividendYield = Double.parseDouble(dividendStr);

                String investmentId = idGenerator.generateInvestmentId();
                Stock stock = new Stock(investmentId, name, purchasePrice, currentPrice,
                        quantity, LocalDateTime.now(), currentUserId, ticker, dividendYield);

                investments.put(investmentId, stock);
                accounts.get(currentAccountId).getInvestments().add(stock);

                System.out.println("Stock investment created successfully! ID: " + investmentId);
                break;
            } catch (Exception e) {
                System.out.println("Error creating investment. Please try again.");
            }
        }
    }

    private static void createBondInvestment() {
        while (true) {
            try {
                System.out.print("Enter bond name: ");
                String name = scanner.nextLine().trim();
                if (!Utility.validateString(name)) {
                    System.out.println("Invalid name. Please try again.");
                    continue;
                }

                System.out.print("Enter issuer: ");
                String issuer = scanner.nextLine().trim();
                if (!Utility.validateString(issuer)) {
                    System.out.println("Invalid issuer. Please try again.");
                    continue;
                }

                System.out.print("Enter purchase price: $");
                String priceStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(priceStr)) {
                    System.out.println("Invalid price. Please try again.");
                    continue;
                }
                double purchasePrice = Double.parseDouble(priceStr);

                System.out.print("Enter current price: $");
                String currentPriceStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(currentPriceStr)) {
                    System.out.println("Invalid price. Please try again.");
                    continue;
                }
                double currentPrice = Double.parseDouble(currentPriceStr);

                System.out.print("Enter quantity: ");
                String quantityStr = scanner.nextLine().trim();
                if (!Utility.validateInt(quantityStr)) {
                    System.out.println("Invalid quantity. Please try again.");
                    continue;
                }
                int quantity = Integer.parseInt(quantityStr);

                System.out.print("Enter face value: $");
                String faceValueStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(faceValueStr)) {
                    System.out.println("Invalid face value. Please try again.");
                    continue;
                }
                double faceValue = Double.parseDouble(faceValueStr);

                System.out.print("Enter coupon rate (as decimal, e.g., 0.05 for 5%): ");
                String couponStr = scanner.nextLine().trim();
                if (!Utility.validateDouble(couponStr)) {
                    System.out.println("Invalid coupon rate. Please try again.");
                    continue;
                }
                double couponRate = Double.parseDouble(couponStr);

                System.out.print("Enter maturity date (YYYY-MM-DD): ");
                String dateStr = scanner.nextLine().trim();
                LocalDate maturityDate = LocalDate.parse(dateStr);

                String investmentId = idGenerator.generateInvestmentId();
                Bond bond = new Bond(investmentId, name, purchasePrice, currentPrice,
                        quantity, LocalDateTime.now(), currentUserId,
                        maturityDate, couponRate, faceValue, issuer);

                investments.put(investmentId, bond);
                accounts.get(currentAccountId).getInvestments().add(bond);

                System.out.println("Bond investment created successfully! ID: " + investmentId);
                break;
            } catch (Exception e) {
                System.out.println("Error creating investment. Please try again.");
            }
        }
    }

    private static void viewInvestments() {
        List<Investment> userInvestments = accounts.get(currentAccountId).getInvestments();

        if (userInvestments.isEmpty()) {
            System.out.println("No investments found.");
            return;
        }

        System.out.println("\n--- Your Investments ---");
        for (Investment investment : userInvestments) {
            investment.getInvestmentSummary();
            System.out.println();
        }
    }

    private static void viewAllAccounts() {
        List<Account> userAccounts = getUserAccounts();

        if (userAccounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("\n--- All Your Accounts ---");
        for (Account account : userAccounts) {
            System.out.println(account.getAccountSummary());
        }
    }

    private static List<Account> getUserAccounts() {
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getUserId().equals(currentUserId)) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }
}


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
    private static final Map<String, notificationType> userNotificationPreferences = new HashMap<>();
    private static String currentUserId = null;
    private static String currentAccountId = null;

    public static void main(String[] args) {
        // Clear screen and show welcome banner
        clearScreen();
        showWelcomeBanner();

        do {
            if (currentUserId == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        } while (true);
    }

    private static void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    private static void showWelcomeBanner() {
        System.out.println();
        System.out.println("             \033[1;36mMCASH BANKING SYSTEM\033[0m");
        System.out.println();
        System.out.println("            \033[3mYour Digital Banking Solution\033[0m");
        System.out.println();
    }

    private static void showLoginMenu() {
        while (true) {
            System.out.println("\n                      \033[1;34mLOGIN MENU\033[0m");
            System.out.println();
            System.out.println("  \033[1;32m[1]\033[0m Register New User");
            System.out.println("  \033[1;33m[2]\033[0m Login to Account");
            System.out.println("  \033[1;31m[3]\033[0m Exit System");
            System.out.print("\n\033[1;36m» Choose option: \033[0m");

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
                    showExitMessage();
                    System.exit(0);
                    break;
                default:
                    showError("Invalid option. Please try again.");
            }
        }
    }

    private static void showExitMessage() {
        System.out.println();
        System.out.println("           \033[1;32mThank you for using MCASH Banking!\033[0m");
        System.out.println();
        System.out.println("              \033[3mHave a wonderful day ahead!\033[0m");
        System.out.println();
    }

    private static void showError(String message) {
        System.out.println("\n\033[1;31m✗ " + message + "\033[0m");
        System.out.println();
    }

    private static void showSuccess(String message) {
        System.out.println("\n\033[1;32m✓ " + message + "\033[0m");
        System.out.println();
    }

    private static void showInfo(String message) {
        System.out.println("\n\033[1;34mℹ " + message + "\033[0m");
        System.out.println();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n                     \033[1;35mMAIN DASHBOARD\033[0m");
            System.out.println("  \033[1;36mWelcome back, " + currentUserId + "\033[0m");
            System.out.println();
            System.out.println("  \033[1;32m[1]\033[0m Create New Account");
            System.out.println("  \033[1;33m[2]\033[0m Select Account");
            System.out.println("  \033[1;34m[3]\033[0m Account Operations");
            System.out.println("  \033[1;35m[4]\033[0m Investment Operations");
            System.out.println("  \033[1;36m[5]\033[0m View All Accounts");
            System.out.println("  \033[1;37m[6]\033[0m Notification Settings");
            System.out.println("  \033[1;31m[7]\033[0m Logout");
            
            if (currentAccountId != null) {
                Account currentAccount = accounts.get(currentAccountId);
                System.out.println();
                System.out.println("\033[1;32mCurrent Account:\033[0m " + currentAccountId);
                System.out.println("\033[1;32mBalance:\033[0m $" + String.format("%,.2f", currentAccount.getBalance()));
            }
            
            System.out.print("\n\033[1;36m» Choose option: \033[0m");

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
                        showError("Please select an account first.");
                    }
                    break;
                case "4":
                    if (currentAccountId != null) {
                        investmentOperations();
                    } else {
                        showError("Please select an account first.");
                    }
                    break;
                case "5":
                    viewAllAccounts();
                    break;
                case "6":
                    notificationSettings();
                    break;
                case "7":
                    currentUserId = null;
                    currentAccountId = null;
                    showSuccess("Logged out successfully.");
                    return;
                default:
                    showError("Invalid option. Please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("\n                    \033[1;32mUSER REGISTRATION\033[0m");
        System.out.println();
        System.out.println("  \033[1;37m(Type 'back' to return to login menu)\033[0m");
        
        while (true) {
            System.out.print("\n\033[1;36mEnter username: \033[0m");
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("back")) {
                return; // Return to login menu
            }

            if (!Utility.validateString(username)) {
                showError("Invalid username. Please try again.");
                continue;
            }

            System.out.print("\033[1;36mEnter password: \033[0m");
            String password = scanner.nextLine().trim();

            if (password.equalsIgnoreCase("back")) {
                return; // Return to login menu
            }

            if (!Utility.validateString(password)) {
                showError("Invalid password. Please try again.");
                continue;
            }

            if (passwordManager.registerUser(username, password)) {
                showSuccess("User registered successfully! Welcome to MCASH Banking!");
                break;
            } else {
                showError("Username already exists. Please try again.");
            }
        }
    }

    private static void loginUser() {
        System.out.println("\n                      \033[1;33mUSER LOGIN\033[0m");
        System.out.println();
        System.out.println("  \033[1;37m(Type 'back' to return to login menu)\033[0m");
        
        while (true) {
            System.out.print("\n\033[1;36mEnter username: \033[0m");
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("back")) {
                return; // Return to login menu
            }

            if (!Utility.validateString(username)) {
                showError("Invalid username. Please try again.");
                continue;
            }

            System.out.print("\033[1;36mEnter password: \033[0m");
            String password = scanner.nextLine().trim();

            if (password.equalsIgnoreCase("back")) {
                return; // Return to login menu
            }

            if (!Utility.validateString(password)) {
                showError("Invalid password. Please try again.");
                continue;
            }

            if (passwordManager.authenticateUser(username, password)) {
                currentUserId = username; // Use the actual username as userId
                showSuccess("Login successful! Welcome back, " + username + "!");
                break;
            } else {
                showError("Invalid credentials. Please try again.");
            }
        }
    }

    private static void createAccount() {
        while (true) {
            System.out.println("\n                   \033[1;32mCREATE NEW ACCOUNT\033[0m");
            System.out.println();
            System.out.println("  \033[1;32m[1]\033[0m Checking Account (with overdraft)");
            System.out.println("  \033[1;33m[2]\033[0m Savings Account (with interest)");
            System.out.print("\n\033[1;36m» Choose account type: \033[0m");

            String choice = scanner.nextLine().trim();
            String accountId = idGenerator.generateAccountId();
            
            // Get account password and hash it properly
            String accountPassword = getAccountPassword();
            String passwordHash = passwordManager.hashPasswordForAccount(accountPassword);

            switch (choice) {
                case "1":
                    createCheckingAccount(accountId, passwordHash);
                    return;
                case "2":
                    createSavingsAccount(accountId, passwordHash);
                    return;
                default:
                    showError("Invalid option. Please try again.");
            }
        }
    }

    private static String getAccountPassword() {
        while (true) {
            System.out.print("Enter account password: ");
            String password = scanner.nextLine().trim();
            
            if (!Utility.validateString(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }
            
            System.out.print("Confirm account password: ");
            String confirmPassword = scanner.nextLine().trim();
            
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }
            
            return password;
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
            System.out.println("\n                 \033[1;34mACCOUNT OPERATIONS\033[0m");
            System.out.println("\033[1;36mAccount:\033[0m " + currentAccountId);
            System.out.println("\033[1;32mBalance:\033[0m $" + String.format("%,.2f", account.getBalance()));
            System.out.println();
            System.out.println("  \033[1;32m[1]\033[0m Make Deposit");
            System.out.println("  \033[1;33m[2]\033[0m Make Withdrawal");
            System.out.println("  \033[1;34m[3]\033[0m View Current Balance");
            System.out.println("  \033[1;35m[4]\033[0m View Transaction History");
            if (account instanceof SavingsAccount) {
                System.out.println("  \033[1;36m[5]\033[0m Apply Interest (Savings Only)");
            }
            System.out.println("  \033[1;31m[0]\033[0m Back to Main Menu");
            System.out.print("\n\033[1;36m» Choose option: \033[0m");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    deposit(account);
                    break;
                case "2":
                    withdraw(account);
                    break;
                case "3":
                    showAccountBalance(account);
                    break;
                case "4":
                    viewTransactionHistory(account);
                    break;
                case "5":
                    if (account instanceof SavingsAccount) {
                        applyInterest((SavingsAccount) account);
                    } else {
                        showError("Invalid option. Please try again.");
                    }
                    break;
                case "0":
                    return;
                default:
                    showError("Invalid option. Please try again.");
            }
        }
    }

    private static void showAccountBalance(Account account) {
        System.out.println("\n                     \033[1;32mACCOUNT BALANCE\033[0m");
        System.out.println();
        System.out.println("         Current Balance: \033[1;32m$" + String.format("%,.2f", account.getBalance()) + "\033[0m");
        System.out.println();
        
        System.out.print("\033[1;36m» Press Enter to continue...\033[0m");
        scanner.nextLine();
    }

    private static void deposit(Account account) {
        System.out.println("\n                      \033[1;32mMAKE DEPOSIT\033[0m");
        System.out.println("Current Balance: $" + String.format("%,.2f", account.getBalance()));
        System.out.println();
        
        while (true) {
            System.out.print("\033[1;36mEnter deposit amount: $\033[0m");
            String amountStr = scanner.nextLine().trim();

            if (!Utility.validateDouble(amountStr)) {
                showError("Invalid amount. Please try again.");
                continue;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showError("Amount must be positive. Please try again.");
                continue;
            }

            String transactionId = idGenerator.generateTransactionId();
            account.deposit(amount, transactionId);

            // Create and send notification using user's preferred notification type
            notificationType userPreference = userNotificationPreferences.getOrDefault(currentUserId, notificationType.IN_APP_SENT);
            Transaction transaction = new Transaction.Builder(
                    transactionId, currentAccountId, amount,
                    TransactionType.DEPOSIT, "Deposit to account"
            ).build();

            Notification notification = Notification.createFromTransaction(
                    idGenerator.generateTransactionId(), transaction, userPreference
            );
            notification.sendNotification();
            
            showSuccess("Deposit successful! Amount: $" + String.format("%.2f", amount));
            showInfo("New balance: $" + String.format("%.2f", account.getBalance()));
            break;
        }
    }

    private static void withdraw(Account account) {
        System.out.println("\n                    \033[1;33mMAKE WITHDRAWAL\033[0m");
        System.out.println("Current Balance: $" + String.format("%,.2f", account.getBalance()));
        if (account instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) account;
            System.out.println("Available (w/ overdraft): $" + String.format("%,.2f", account.getBalance() + checking.getOverdraftLimit()));
        }
        System.out.println();
        
        while (true) {
            System.out.print("\033[1;36mEnter withdrawal amount: $\033[0m");
            String amountStr = scanner.nextLine().trim();

            if (!Utility.validateDouble(amountStr)) {
                showError("Invalid amount. Please try again.");
                continue;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showError("Amount must be positive. Please try again.");
                continue;
            }

            String transactionId = idGenerator.generateTransactionId();
            account.withdraw(amount, transactionId);

            // Create and send notification using user's preferred notification type
            notificationType userPreference = userNotificationPreferences.getOrDefault(currentUserId, notificationType.IN_APP_SENT);
            Transaction transaction = new Transaction.Builder(
                    transactionId, currentAccountId, amount,
                    TransactionType.WITHDRAWAL, "Withdrawal from account"
            ).build();

            Notification notification = Notification.createFromTransaction(
                    idGenerator.generateTransactionId(), transaction, userPreference
            );
            notification.sendNotification();
            
            showSuccess("Withdrawal successful! Amount: $" + String.format("%.2f", amount));
            showInfo("New balance: $" + String.format("%.2f", account.getBalance()));
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
            System.out.println("\n                 \033[1;33mINVESTMENT OPERATIONS\033[0m");
            System.out.println();
            System.out.println("  \033[1;32m[1]\033[0m Create Stock Investment");
            System.out.println("  \033[1;33m[2]\033[0m Create Bond Investment");
            System.out.println("  \033[1;36m[3]\033[0m View My Investments");
            System.out.println("  \033[1;35m[4]\033[0m Update Investment Price");
            System.out.println("  \033[1;31m[0]\033[0m Back to Main Menu");
            System.out.print("\n\033[1;36m» Choose option: \033[0m");

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
                case "4":
                    updateInvestmentPrice();
                    break;
                case "0":
                    return;
                default:
                    showError("Invalid option. Please try again.");
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
                accounts.get(currentAccountId).addInvestment(stock);

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
                accounts.get(currentAccountId).addInvestment(bond);

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

    private static void updateInvestmentPrice() {
        List<Investment> userInvestments = accounts.get(currentAccountId).getInvestments();

        if (userInvestments.isEmpty()) {
            System.out.println("No investments found. Please create an investment first.");
            return;
        }

        while (true) {
            System.out.println("\n--- Update Investment Price ---");
            for (int i = 0; i < userInvestments.size(); i++) {
                Investment investment = userInvestments.get(i);
                System.out.println((i + 1) + ". " + investment.getName() + 
                    " (ID: " + investment.getInvestmentId() + 
                    ") - Current Price: $" + String.format("%.2f", investment.getCurrentPrice()));
            }
            System.out.print("Select investment to update (1-" + userInvestments.size() + "): ");

            String choice = scanner.nextLine().trim();

            if (!Utility.validateInt(choice)) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }

            int investmentIndex = Integer.parseInt(choice) - 1;
            if (investmentIndex >= 0 && investmentIndex < userInvestments.size()) {
                Investment selectedInvestment = userInvestments.get(investmentIndex);
                updateSelectedInvestmentPrice(selectedInvestment);
                break;
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private static void updateSelectedInvestmentPrice(Investment investment) {
        while (true) {
            System.out.println("\nUpdating price for: " + investment.getName());
            System.out.println("Current price: $" + String.format("%.2f", investment.getCurrentPrice()));
            System.out.print("Enter new current price: $");
            
            String priceStr = scanner.nextLine().trim();
            
            if (!Utility.validateDouble(priceStr)) {
                System.out.println("Invalid price. Please try again.");
                continue;
            }
            
            double newPrice = Double.parseDouble(priceStr);
            if (newPrice <= 0) {
                System.out.println("Price must be positive. Please try again.");
                continue;
            }
            
            double oldPrice = investment.getCurrentPrice();
            investment.updateCurrentPrice(newPrice);
            
            System.out.println("Price updated successfully!");
            System.out.println("Old price: $" + String.format("%.2f", oldPrice));
            System.out.println("New price: $" + String.format("%.2f", newPrice));
            System.out.println("Updated gain/loss: $" + String.format("%.2f", investment.getGainLoss()));
            break;
        }
    }
    
    private static void notificationSettings() {
        while (true) {
            System.out.println("\n                 \033[1;33mNOTIFICATION SETTINGS\033[0m");
            System.out.println();
            System.out.println("  \033[1;32m[1]\033[0m View Current Notification Preference");
            System.out.println("  \033[1;33m[2]\033[0m Update Notification Preference");
            System.out.println("  \033[1;36m[3]\033[0m Test Notification");
            System.out.println("  \033[1;31m[0]\033[0m Back to Main Menu");
            System.out.print("\n\033[1;36m» Choose option: \033[0m");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewNotificationPreference();
                    break;
                case "2":
                    updateNotificationPreference();
                    break;
                case "3":
                    testNotification();
                    break;
                case "0":
                    return;
                default:
                    showError("Invalid option. Please try again.");
            }
        }
    }

    private static void viewNotificationPreference() {
        notificationType currentPreference = userNotificationPreferences.getOrDefault(currentUserId, notificationType.IN_APP_SENT);
        
        System.out.println("\n              \033[1;36mCURRENT NOTIFICATION SETTINGS\033[0m");
        System.out.println();
        String preference = formatNotificationType(currentPreference);
        System.out.println("  \033[1;33mActive Preference:\033[0m " + preference);
        System.out.println();
        System.out.println("  This preference applies to transaction notifications");
        System.out.println();
    }

    private static void updateNotificationPreference() {
        while (true) {
            System.out.println("\n--- Update Notification Preference ---");
            System.out.println("1. SMS Notifications");
            System.out.println("2. Email Notifications");
            System.out.println("3. In-App Notifications");
            System.out.println("4. Web Notifications");
            System.out.println("0. Cancel");
            System.out.print("Choose notification type: ");

            String choice = scanner.nextLine().trim();
            notificationType newType = null;

            switch (choice) {
                case "1":
                    newType = notificationType.SMS_SENT;
                    break;
                case "2":
                    newType = notificationType.EMAIL_SENT;
                    break;
                case "3":
                    newType = notificationType.IN_APP_SENT;
                    break;
                case "4":
                    newType = notificationType.WEB_SENT;
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue;
            }

            userNotificationPreferences.put(currentUserId, newType);
            System.out.println("Notification preference updated to: " + formatNotificationType(newType));
            break;
        }
    }

    private static void testNotification() {
        notificationType currentPreference = userNotificationPreferences.getOrDefault(currentUserId, notificationType.IN_APP_SENT);
        
        String testNotificationId = idGenerator.generateTransactionId();
        Notification testNotification = new Notification(
            testNotificationId,
            currentUserId,
            "This is a test notification from MCASH Banking System",
            currentPreference
        );
        
        System.out.println("\n                   \033[1;32mTESTING NOTIFICATION\033[0m");
        System.out.println();
        String preference = formatNotificationType(currentPreference);
        System.out.println("  Sending test notification via " + preference + "...");
        System.out.println();
        
        testNotification.sendNotification();
        showSuccess("Test notification sent successfully!");
    }

    private static String formatNotificationType(notificationType type) {
        switch (type) {
            case SMS_SENT:
            case SMS_INVESTMENT:
                return "SMS";
            case EMAIL_SENT:
            case EMAIL_INVESTMENT:
                return "Email";
            case IN_APP_SENT:
            case IN_APP_INVESTMENT:
                return "In-App";
            case WEB_SENT:
            case WEB_INVESTMENT:
                return "Web";
            default:
                return type.toString();
        }
    }
}


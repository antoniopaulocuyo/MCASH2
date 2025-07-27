package myLib;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountId;
    protected double balance;
    protected String userId;
    protected String passwordHash;
    protected List<Transaction> transactions;
    protected List<Investment> investments;

    public Account(String userId, String accountId, String passwordHash) {
        this.userId = userId;
        this.accountId = accountId;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.investments = new ArrayList<>();
        this.passwordHash = passwordHash;
    }

    public void deposit(double amount, String transactionId) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Amount must be positive.");
            return;
        }

        this.balance += amount;
        Transaction transaction = new Transaction.Builder(
                transactionId,
                this.accountId,
                amount,
                TransactionType.DEPOSIT,
                "Deposit to account"
        ).build();
        addTransaction(transaction);
        System.out.println("Deposit successful. Amount: $" + amount);
    }


    public void withdraw(double amount, String transactionId) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Amount must be positive.");
            return;
        }

        if (amount > this.balance) {
            System.out.println("Insufficient funds. Available balance: $" + this.balance);
            return;
        }

        this.balance -= amount;
        Transaction transaction = new Transaction.Builder(
                transactionId,
                this.accountId,
                amount,
                TransactionType.WITHDRAWAL,
                "Withdrawal from account"
        ).build();
        addTransaction(transaction);
        System.out.println("Withdrawal successful. Amount: $" + amount);
        return;
    }

    public double getBalance() {
        return this.balance;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            this.transactions.add(transaction);
        }
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(this.transactions);
    }

    public abstract double calculateFees();

    public abstract String getAccountSummary();

    public String getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Investment> getInvestments() {
        return new ArrayList<>(investments);
    }
}
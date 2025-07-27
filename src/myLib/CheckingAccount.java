package myLib;

public class CheckingAccount extends Account {
    private double overdraftLimit;
    private double overdraftFee;

    public CheckingAccount(String accountNumber, String userId, double overdraftLimit, String passwordHash) {
        super(userId, accountNumber, passwordHash);
        this.overdraftLimit = overdraftLimit;
        this.overdraftFee = 35.0;
    }

    @Override
    public void withdraw(double amount, String transactionId) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Amount must be positive.");
            return;
        }

        double availableBalance = this.balance + this.overdraftLimit;

        if (amount > availableBalance) {
            System.out.println("Withdrawal exceeds available balance including overdraft limit. Available: $" + availableBalance);
            return;
        }

        this.balance -= amount;
        Transaction transaction = new Transaction.Builder(
                transactionId,
                this.accountId,
                amount,
                TransactionType.WITHDRAWAL,
                "Withdrawal from checking account"
        ).build();
        addTransaction(transaction);

        if (this.balance < 0) {
            Transaction overdraftTransaction = new Transaction.Builder(
                    transactionId,
                    this.accountId,
                    this.overdraftFee,
                    TransactionType.FEE,
                    "Overdraft fee"
            ).build();
            addTransaction(overdraftTransaction);
            this.balance -= this.overdraftFee;
            System.out.println("Withdrawal successful. Amount: $" + amount + " (Overdraft fee of $" + this.overdraftFee + " applied)");
        } else {
            System.out.println("Withdrawal successful. Amount: $" + amount);
        }
    }

    public boolean checkOverdraft() {
        return this.balance < 0;
    }

    @Override
    public double calculateFees() {
        if (checkOverdraft()) {
            return this.overdraftFee;
        }
        return 0.0;
    }

    @Override
    public String getAccountSummary() {
        return String.format("Checking Account [ID: %s] - Balance: $%.2f, Overdraft Limit: $%.2f, Overdraft Fee: $%.2f, In Overdraft: %s",
                this.accountId, this.balance, this.overdraftLimit, this.overdraftFee, checkOverdraft() ? "Yes" : "No");
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftFee() {
        return overdraftFee;
    }

    public void setOverdraftFee(double overdraftFee) {
        this.overdraftFee = overdraftFee;
    }
}

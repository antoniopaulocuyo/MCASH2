package myLib;

public class SavingsAccount extends Account {
    private Double interestRate;
    private Double minimumBalance;

    public SavingsAccount(String accountNumber, String userId, Double interestRate, String passwordHash) {
        super(userId, accountNumber, passwordHash);
        this.interestRate = interestRate;
        this.minimumBalance = 100.0;
    }

    public Double calculateInterest() {
        return this.balance * (this.interestRate / 100);
    }

    public void applyInterest(String transactionId) {
        Double interest = calculateInterest();
        if (interest > 0) {
            this.balance += interest;
            Transaction transaction = new Transaction.Builder(
                    transactionId,
                    this.accountId,
                    interest,
                    TransactionType.INTEREST,
                    "Interest applied to savings account"
            ).build();
            addTransaction(transaction);
            System.out.println("Interest applied: $" + interest);
        }
    }

    @Override
    public double calculateFees() {
        if (this.balance < this.minimumBalance) {
            return 5.0;
        }
        return 0.0;
    }

    @Override
    public String getAccountSummary() {
        return String.format("Savings Account [ID: %s] - Balance: $%.2f, Interest Rate: %.2f%%, Minimum Balance: $%.2f, Fees: $%.2f",
                this.accountId, this.balance, this.interestRate, this.minimumBalance, calculateFees());
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}

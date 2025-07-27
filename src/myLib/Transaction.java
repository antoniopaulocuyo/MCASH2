package myLib;

import java.time.LocalDateTime;
import java.util.Optional;

public class Transaction {
    private String transactionId;
    private String accountId1;
    private String accountId2;
    private double amount;
    private TransactionType type;
    private String description;
    private LocalDateTime timestamp;
    private String investmentId;

    // Transaction for Account Transfers
    public Transaction(Builder builder) {
        this.transactionId = builder.transactionId;
        this.accountId1 = builder.accountId1;
        this.amount = builder.amount;
        this.type = builder.type;
        this.description = builder.description;
        this.timestamp = LocalDateTime.now();

        //Optional Fields
        this.investmentId = builder.investmentId;
        this.accountId2 = builder.accountId2;
    }

    // Builder Method allows for Optional Fields
    public static class Builder {
        private final String transactionId;
        private final String accountId1;
        private final double amount;
        private final TransactionType type;
        private final String description;
        private  String accountId2;
        private String investmentId;

        public Builder(String transactionId, String accountId1,
                       double amount, TransactionType type, String description) {

            this.transactionId = transactionId;
            this.accountId1 = accountId1;
            this.amount = amount;
            this.type = type;
            this.description = description;
        }

        public Builder secondAccount(String accountId2) {
            this.accountId2 = accountId2;
            return this;
        }

        public Builder investmentId(String investmentId) {
            this.investmentId = investmentId;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    public Optional<String> getInvestmentId() {
        return Optional.ofNullable(investmentId);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId1() {
        return accountId1;
    }

    public String getAccountId2() {
        return accountId2;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionDetails() {
        if (investmentId != null) {
            return String.format("Investment Transaction [ID: %s] - Type: %s, %s $%.2f in investment %s for account %s at %s",
                    transactionId, type, description, amount, investmentId, accountId1, timestamp);
        } else if (accountId2 != null) {
            return String.format("Inter-Account Transaction [ID: %s] - Type: %s, %s $%.2f from account %s to account %s at %s",
                    transactionId, type, description, amount, accountId1, accountId2, timestamp);
        } else {
            return String.format("Unilateral Transaction [ID: %s] - Type: %s, %s $%.2f for account %s at %s",
                    transactionId, type, description, amount, accountId1, timestamp);
        }
    }
}

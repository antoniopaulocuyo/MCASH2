package myLib;

import java.time.LocalDateTime;

public abstract class Investment {
    protected String investmentId;
    protected String name;
    protected double purchasePrice;
    protected double currentPrice;
    protected int quantity;
    protected LocalDateTime purchaseDate;
    protected String userId;

    public Investment(String investmentId, String name, double purchasePrice,
                      double currentPrice, int quantity, LocalDateTime purchaseDate, String userId) {

        this.investmentId = investmentId;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.userId = userId;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    public String getName() {
        return name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public String getUserId() {
        return userId;
    }

    public void updateCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }


    public double getCurrentValue() {
        return currentPrice * quantity;
    }

    public double getGainLoss() {
        return (currentPrice - purchasePrice) * quantity;
    }

    public void sellInvestment(int quantity) {
        this.quantity -= quantity;
    }

    // PurchaseInvestment Automatically calculates new Average Purchase Price
    public void purchaseInvestment(int quantity, double newPurchasePrice) {
        double updatedPurchasePrice;

        updatedPurchasePrice = ((this.purchasePrice * this.quantity) + (newPurchasePrice * quantity)) /
                (quantity + this.quantity);

        this.purchasePrice = updatedPurchasePrice;
        this.quantity += quantity;

    }

    public abstract double calculateDividends();

    public abstract void getInvestmentSummary();


}

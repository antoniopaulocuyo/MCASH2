package myLib;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class Stock extends Investment {
    private final String ticker;
    private Double dividendYield;

    public Stock(
            String investmentId,
            String name,
            double purchasePrice,
            double currentPrice,
            int quantity,
            LocalDateTime purchaseDate,
            String userId,
            String ticker,
            Double dividendYield
    ) {
        super(
                investmentId,
                name,
                purchasePrice,
                currentPrice,
                quantity,
                purchaseDate,
                userId
        );
        this.ticker = ticker;
        this.dividendYield = dividendYield;
    }

    public void setDividendYield(Double dividendYield) {
        this.dividendYield = dividendYield;
    }

    @Override
    public double calculateDividends() {
        return currentPrice * dividendYield * quantity;
    }

    @Override
    public void getInvestmentSummary() {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        System.out.println("┌──────────────────────────────────────────────────────┐");
        System.out.printf("│ %-20s: %-30s │\n", "Stock", name + " (" + ticker + ")");
        System.out.printf("│ %-20s: %-30s │\n", "Investment ID", investmentId);
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-30s │\n", "Quantity", quantity + " shares");
        System.out.printf("│ %-20s: %-30s │\n", "Avg Purchase Price", "$" + df.format(purchasePrice));
        System.out.printf("│ %-20s: %-30s │\n", "Current Price", "$" + df.format(currentPrice));
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-30s │\n", "Total Value", "$" + df.format(getCurrentValue()));
        System.out.printf("│ %-20s: %-30s │\n", "Total Gain/Loss",
                (this.getGainLoss() >= 0 ? "+" : "") + "$" + df.format(this.getGainLoss()) +
                        " (" + df.format((currentPrice/purchasePrice - 1)*100) + "%)");
        System.out.printf("│ %-20s: %-30s │\n", "Dividend Yield", df.format(dividendYield*100) + "%");
        System.out.printf("│ %-20s: %-30s │\n", "Annual Dividends", "$" + df.format(this.calculateDividends()));
        System.out.println("└──────────────────────────────────────────────────────┘");
    }



}



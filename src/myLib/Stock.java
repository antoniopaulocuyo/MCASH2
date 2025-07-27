package myLib;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class Stock extends Investment {
    private final String ticker;
    private Double dividendYield;
    
    private static final String DECIMAL_FORMAT = "#,##0.00";
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

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
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);

        System.out.println("\n\033[1;36mStock Investment Summary\033[0m");
        System.out.println("\033[1;33mStock:\033[0m " + name + " (" + ticker + ")");
        System.out.println("\033[1;33mInvestment ID:\033[0m " + investmentId);
        System.out.println("\033[1;33mQuantity:\033[0m " + quantity + " shares");
        System.out.println("\033[1;33mAvg Purchase Price:\033[0m $" + df.format(purchasePrice));
        System.out.println("\033[1;33mCurrent Price:\033[0m $" + df.format(currentPrice));
        System.out.println("\033[1;33mTotal Value:\033[0m $" + df.format(getCurrentValue()));
        System.out.println("\033[1;33mTotal Gain/Loss:\033[0m " +
                (this.getGainLoss() >= 0 ? "+" : "") + "$" + df.format(this.getGainLoss()) +
                " (" + df.format((currentPrice/purchasePrice - 1)*PERCENTAGE_MULTIPLIER) + "%)");
        System.out.println("\033[1;33mDividend Yield:\033[0m " + df.format(dividendYield*PERCENTAGE_MULTIPLIER) + "%");
        System.out.println("\033[1;33mAnnual Dividends:\033[0m $" + df.format(this.calculateDividends()));
    }



}



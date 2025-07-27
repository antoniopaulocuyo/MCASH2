package myLib;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Bond extends Investment{
    private LocalDate maturityDate;
    private Double couponRate;
    private Double faceValue;
    private String issuer;

    public Bond(
            String investmentId,
            String name,
            double purchasePrice,
            double currentPrice,
            int quantity,
            LocalDateTime purchaseDate,
            String userId,
            LocalDate maturityDate,
            Double couponRate,
            Double faceValue,
            String issuer
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
        this.maturityDate = maturityDate;
        this.couponRate = couponRate;
        this.faceValue = faceValue;
        this.issuer = issuer;
    }

    private double calculateYieldToMaturity() {
        double yearsToMaturity = ChronoUnit.DAYS.between(LocalDate.now(), maturityDate) / 365.0;
        return (faceValue - currentPrice) / currentPrice / yearsToMaturity + couponRate;
    }

    @Override
    public double calculateDividends(){
        return faceValue * couponRate * quantity;
    }

    @Override
    public void getInvestmentSummary(){
        DecimalFormat df = new DecimalFormat("#,##0.00");
        long daysToMaturity = ChronoUnit.DAYS.between(LocalDate.now(), maturityDate);
        double ytm = calculateYieldToMaturity();

        System.out.println("┌──────────────────────────────────────────────────────┐");
        System.out.printf("│ %-20s: %-30s │\n", "Bond", name + " (" + issuer + ")");
        System.out.printf("│ %-20s: %-30s │\n", "Investment ID", investmentId);
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-30s │\n", "Quantity", quantity + " bonds");
        System.out.printf("│ %-20s: %-30s │\n", "Face Value", "$" + df.format(faceValue) + " per bond");
        System.out.printf("│ %-20s: %-30s │\n", "Purchase Price", "$" + df.format(purchasePrice));
        System.out.printf("│ %-20s: %-30s │\n", "Current Price", "$" + df.format(currentPrice));
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-30s │\n", "Maturity Date", maturityDate);
        System.out.printf("│ %-20s: %-30s │\n", "Days to Maturity", daysToMaturity);
        System.out.printf("│ %-20s: %-30s │\n", "Coupon Rate", df.format(couponRate*100) + "%");
        System.out.printf("│ %-20s: %-30s │\n", "Annual Coupons", "$" + df.format(calculateDividends()));
        System.out.printf("│ %-20s: %-30s │\n", "Yield to Maturity", df.format(ytm*100) + "%");
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-30s │\n", "Total Value", "$" + df.format(this.getCurrentValue()));
        System.out.printf("│ %-20s: %-30s │\n", "Total Gain/Loss",
                (this.getGainLoss() >= 0 ? "+" : "") + "$" + df.format(this.getGainLoss()));
        System.out.println("└──────────────────────────────────────────────────────┘");
    }

}


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
    
    private static final String DECIMAL_FORMAT = "#,##0.00";
    private static final double DAYS_PER_YEAR = 365.0;
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

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
        double yearsToMaturity = ChronoUnit.DAYS.between(LocalDate.now(), maturityDate) / DAYS_PER_YEAR;
        return (faceValue - currentPrice) / currentPrice / yearsToMaturity + couponRate;
    }

    @Override
    public double calculateDividends(){
        return faceValue * couponRate * quantity;
    }

    @Override
    public void getInvestmentSummary(){
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        long daysToMaturity = ChronoUnit.DAYS.between(LocalDate.now(), maturityDate);
        double ytm = calculateYieldToMaturity();

        System.out.println("\n\033[1;36mBond Investment Summary\033[0m");
        System.out.println("\033[1;33mBond:\033[0m " + name + " (" + issuer + ")");
        System.out.println("\033[1;33mInvestment ID:\033[0m " + investmentId);
        System.out.println("\033[1;33mQuantity:\033[0m " + quantity + " bonds");
        System.out.println("\033[1;33mFace Value:\033[0m $" + df.format(faceValue) + " per bond");
        System.out.println("\033[1;33mPurchase Price:\033[0m $" + df.format(purchasePrice));
        System.out.println("\033[1;33mCurrent Price:\033[0m $" + df.format(currentPrice));
        System.out.println("\033[1;33mMaturity Date:\033[0m " + maturityDate);
        System.out.println("\033[1;33mDays to Maturity:\033[0m " + daysToMaturity);
        System.out.println("\033[1;33mCoupon Rate:\033[0m " + df.format(couponRate*PERCENTAGE_MULTIPLIER) + "%");
        System.out.println("\033[1;33mAnnual Coupons:\033[0m $" + df.format(calculateDividends()));
        System.out.println("\033[1;33mYield to Maturity:\033[0m " + df.format(ytm*PERCENTAGE_MULTIPLIER) + "%");
        System.out.println("\033[1;33mTotal Value:\033[0m $" + df.format(this.getCurrentValue()));
        System.out.println("\033[1;33mTotal Gain/Loss:\033[0m " +
                (this.getGainLoss() >= 0 ? "+" : "") + "$" + df.format(this.getGainLoss()));
    }

}


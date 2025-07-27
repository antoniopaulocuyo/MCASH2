package myLib;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {
    private final AtomicLong transactionCounter = new AtomicLong(1);
    private final AtomicLong accountCounter = new AtomicLong(1);
    private final AtomicLong investmentCounter = new AtomicLong(1);
    private final AtomicLong userCounter = new AtomicLong(1);
    private final SecureRandom secureRandom = new SecureRandom();

    // Shared format for consistency
    private static final String ID_FORMAT = "%s-%d-%04d-%04d";

    public String generateTransactionId() {
        return String.format(ID_FORMAT,
                "TXN",
                Instant.now().toEpochMilli(),
                transactionCounter.getAndIncrement() % 10000,
                secureRandom.nextInt(10000)
        );
    }

    public String generateAccountId() {
        return String.format(ID_FORMAT,
                "ACC",
                System.nanoTime(),
                accountCounter.getAndIncrement() % 10000,
                secureRandom.nextInt(10000)
        );
    }

    public String generateInvestmentId() {
        return String.format(ID_FORMAT,
                "INV",
                Instant.now().toEpochMilli(),
                investmentCounter.getAndIncrement() % 10000,
                secureRandom.nextInt(10000)
        );
    }

    public String generateUserId() {
        return String.format(ID_FORMAT,
                "USR",
                System.currentTimeMillis(),
                userCounter.getAndIncrement() % 10000,
                secureRandom.nextInt(10000)
        );
    }
}


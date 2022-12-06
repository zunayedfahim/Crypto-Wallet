package CryptoWallet;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    String transactionId;
    String transactionType; // "IN" or "OUT"
    double amount;
    LocalDateTime dateTime;
    String currency;

    public Transaction(String transactionType, double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        dateTime = LocalDateTime.now();
        this.currency = "USD";

    }

    public Transaction(String transactionType, double amount, String currency) {
        this(transactionType, amount);
        this.currency = currency;
    }
}

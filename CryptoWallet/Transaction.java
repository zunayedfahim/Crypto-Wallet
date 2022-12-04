package CryptoWallet;
import java.time.LocalDateTime;

public class Transaction {
    String transactionId;
    String transactionType; // "IN" or "OUT"
    double amount;
    LocalDateTime dateTime;

    public Transaction(String transactionId, String transactionType, double amount) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        dateTime = LocalDateTime.now();
    }
}

package CryptoWallet;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    public String transactionId;
    public String transactionType; // "IN" or "OUT"
    public double amount;
    public LocalDateTime dateTime;
    public String currency;

    public Transaction(String transactionId, String transactionType, double amount, LocalDateTime dateTime, String currency) {
    	this.transactionId = transactionId;
    	this.transactionType = transactionType;
    	this.amount = amount;
    	this.dateTime = dateTime;
    	this.currency = currency;
    }

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

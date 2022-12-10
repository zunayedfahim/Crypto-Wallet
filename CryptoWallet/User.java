package CryptoWallet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    String userId;
    String fullName;
    String email;
    String username;
    String pass;
    String NID;
    String phoneNumber;
    double currentBalance;
    boolean isLoggedIn;
    Crypto[] holdedCrypto = {};
    Transaction[] transactionHistory = {};

    public User(String fullName, String email, String username, String pass, String NID, String phoneNumber) throws Exception {
        this.userId = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.pass = hashPassword(pass);
        

        // Check username
        char[] charArray = username.toCharArray();
        
        for(int i=0; i < charArray.length; i++){
            if(!Character.isLowerCase( charArray[i])) {
                throw new InvalidUsername("Only lowercase characters are allowed in username.");
            }
        }
        this.username = username;

        // Checks Email
        String regex = "^(.+)@(.+)$";  
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) {
            this.email = email;
        } else {
            throw new InvalidEmail("Invalid Email");
        }

        // Checks NID
        if(NID.matches("[0-9]+") && (NID.length()==10 || NID.length()==13)) {
            this.NID = NID;
        } else {
            throw new InvalidNID("Invalid NID");
        }

        // Checks Phone Number
        if(NID.matches("[0-9]+") && phoneNumber.length() == 11) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new InvalidPhoneNumber("Invalid Phone Number");
        }
    }

    public static String hashPassword(String pass) {
        try {
 
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(pass.getBytes());
 
            BigInteger no = new BigInteger(1, messageDigest);
 
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTransaction(Transaction t) {
        this.transactionHistory = Arrays.copyOf(this.transactionHistory, this.transactionHistory.length + 1);
        this.transactionHistory[this.transactionHistory.length - 1] = t;
    }

    public void buyCrypto(Crypto c) throws InterruptedException {

        // if the crypto is already in the holding
        for(int i=0; i<this.holdedCrypto.length; i++) {
            if(this.holdedCrypto[i].symbol.equals(c.symbol)) {
                this.holdedCrypto[i].totalValue += c.totalValue;
                this.holdedCrypto[i].holding += c.holding;
                Transaction t = new Transaction("IN", c.totalValue, c.symbol);
                this.addTransaction(t);
                System.out.println(c.symbol + " added to your holdings.");
                System.out.println("Returning to Dashboard ...");
                Thread.sleep(5000);
                Main.clearScreen();
                return;
            }
        }

        // if the crypto is not in the holding
        this.holdedCrypto = Arrays.copyOf(this.holdedCrypto, this.holdedCrypto.length + 1);
        this.holdedCrypto[this.holdedCrypto.length - 1] = c;
        Transaction t = new Transaction("IN", c.totalValue, c.symbol);
        this.addTransaction(t);
        System.out.println(c.symbol + " added to your holdings.");
        System.out.println("Returning to Dashboard ...");
        Thread.sleep(5000);
    }

    public void sellCrypto(String symbol) throws InvalidCrypto {
        if(this.holdedCrypto.length==0) {
            // if the user doesn't have the crypto in his holding
            throw new InvalidCrypto("You do not have this Crypto.");
        } else {
            Crypto[] anotherArray = new Crypto[this.holdedCrypto.length - 1];
            for (int i = 0, k = 0; i < this.holdedCrypto.length; i++) {
                if (this.holdedCrypto[i].symbol.equals(symbol)) {
                    Transaction t = new Transaction("OUT", this.holdedCrypto[i].totalValue, this.holdedCrypto[i].symbol);
                    this.addTransaction(t);
                    continue;
                }
                anotherArray[k++] = this.holdedCrypto[i];
            }
            this.holdedCrypto = anotherArray;
            System.out.println(symbol + " SOLD!");
            System.out.println("Returning to Dashboard ...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void swapCrypto(String fromCrypto, String toCrypto) throws InvalidCrypto, InterruptedException {
        double fromTotal = 0;
        double toValue = 0;
        for(int i=0;i<this.holdedCrypto.length;i++) {
            if(this.holdedCrypto[i].symbol.equals(fromCrypto)) {
                fromTotal = this.holdedCrypto[i].totalValue;
                for(int j=0; j<Crypto.cryptoInfo.length;j++) {
                    if(Crypto.cryptoInfo[j].symbol.equals(toCrypto)) {
                        toValue = Crypto.cryptoInfo[j].value;
                    }
                }
            }
        }
        double reqHolding = fromTotal / toValue;
        sellCrypto(fromCrypto);
        Crypto c = new Crypto(toCrypto, reqHolding);
        buyCrypto(c);

        
    }

    public double getTotalCryptoValue() {
        double sum = 0;
        for(int i=0; i<this.holdedCrypto.length; i++) {
            sum += this.holdedCrypto[i].totalValue;
        }
        return sum;
    }
      
    public void showHoldedCryptos() {
        System.out.println("Your Holdings:");
        System.out.println("Total Holdings Value: $" + this.getTotalCryptoValue());
        System.out.println("-----");
        for(int i=0; i<this.holdedCrypto.length; i++) {
            System.out.println("Crypto Currency: " + this.holdedCrypto[i].name);
            System.out.println("Total Holding: " + this.holdedCrypto[i].holding + " " + this.holdedCrypto[i].symbol);
            System.out.println("Change Percentage: " + this.holdedCrypto[i].changePercentage + "%");
            System.out.println("Total Value: $" + this.holdedCrypto[i].totalValue);
            System.out.println("-----");
        }
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History:");
        System.out.println("-----");
        for(int i=0; i<this.transactionHistory.length; i++) {
            System.out.println("Transaction ID: " + this.transactionHistory[i].transactionId);
            System.out.println("Transaction Type: " + this.transactionHistory[i].transactionType);
            System.out.println("Currency: " + this.transactionHistory[i].currency);
            System.out.println("Amount: $" + this.transactionHistory[i].amount);
            System.out.println("-----");
        }

    }
}


class FetchUsernamePass {
    String username;
    String pass;

    public FetchUsernamePass(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }
}
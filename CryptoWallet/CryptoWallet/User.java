package CryptoWallet;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    public String userId;
    public String fullName;
    public String email;
    public String username;
    public String pass;
    public String NID;
    public String phoneNumber;
    public double currentBalance;
    public boolean isLoggedIn;
    public Crypto[] holdedCrypto = {};
    public Transaction[] transactionHistory = {};

    public String addMoneyMedium;
    public double amount;
    public static String[] addMoneyMediums = {
		"card",
		"bkash",	
		"paypal"
	};

    

    public User(String userId, String fullName, String email, String username, String NID, String phoneNumber, double currentBalance) throws Exception {
        this.userId = userId;
        this.fullName = fullName;
        this.currentBalance = currentBalance;
        

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

    public User(String userId, String fullName, String email, String username, String pass, String NID, String phoneNumber, double currentBalance) throws Exception {
		this(userId, fullName, email, username, NID, phoneNumber, currentBalance);
		this.pass = hashPassword(pass);
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
        Database.addTransactionToDatabase(this.userId, t);
    }

    public void addCryptoToHoldings(Crypto c) {
    	this.holdedCrypto = Arrays.copyOf(this.holdedCrypto, this.holdedCrypto.length + 1);
        this.holdedCrypto[this.holdedCrypto.length - 1] = c;
        this.currentBalance -= c.totalValue;
    }

    public void deposit(double amount) {
    	System.out.println("amount is "+amount);
    	currentBalance += amount;
        Database.updateCurrentBalance(this.userId, this.currentBalance);
        Transaction t = new Transaction("IN", amount);
        this.addTransaction(t);
    }

    
    // withdraw
    public void withdraw(double amount) {
    	if(amount<=currentBalance){
    		currentBalance -= amount;
            Database.updateCurrentBalance(this.userId, this.currentBalance);
            Transaction t = new Transaction("OUT", amount); 
            this.addTransaction(t);   		
    	}else {
    		System.err.print("Insufficient Balance");
    	}
    }

    public void callMedium(String addMoneyMedium,double amount) {
    	if(Arrays.asList(addMoneyMediums).contains(addMoneyMedium)){
			this.addMoneyMedium = addMoneyMedium;
			this.amount= amount;
			deposit(amount);
            
		}else if (addMoneyMedium ==""){
            System.err.println("Medium of transaction is required");
		}else {
			System.err.println("Invalid Medium of transaction!");
		}
    }

    public void buyCrypto(Crypto c) throws InterruptedException, InsufficientBalance {

        if(this.currentBalance < c.totalValue) {
            throw new InsufficientBalance("You do not have sufficient balance.");
        }

        // if the crypto is already in the holding
        for(int i=0; i<this.holdedCrypto.length; i++) {
            if(this.holdedCrypto[i].symbol.equals(c.symbol)) {
                this.holdedCrypto[i].totalValue += c.totalValue;
                this.holdedCrypto[i].holding += c.holding;
                this.currentBalance -= c.totalValue;
                Database.updateCurrentBalance(this.userId, this.currentBalance);
                Transaction t = new Transaction("IN", c.totalValue, c.symbol);
                this.addTransaction(t);
                c.holding = this.holdedCrypto[i].holding;
                Database.updateRowInHoldings(this.userId, c);
                System.out.println(c.symbol + " added to your holdings.");
                return;
            }
        }

        // if the crypto is not in the holding
        this.holdedCrypto = Arrays.copyOf(this.holdedCrypto, this.holdedCrypto.length + 1);
        this.holdedCrypto[this.holdedCrypto.length - 1] = c;
        this.currentBalance -= c.totalValue;
        Database.updateCurrentBalance(this.userId, this.currentBalance);
        Transaction t = new Transaction("IN", c.totalValue, c.symbol);
        this.addTransaction(t);
        Database.addRowToHoldings(this.userId, c);
        System.out.println(c.symbol + " added to your holdings.");
        
    }

    public void sellCrypto(String symbol) throws InvalidCrypto {
        if(this.holdedCrypto.length==0) {
            // if the user doesn't have the crypto in his holding
            throw new InvalidCrypto("You do not have this Crypto.");
        } else {
            Crypto[] anotherArray = new Crypto[this.holdedCrypto.length - 1];
            for (int i = 0, k = 0; i < this.holdedCrypto.length; i++) {
                if (this.holdedCrypto[i].symbol.equals(symbol)) {
                    this.currentBalance += this.holdedCrypto[i].totalValue;
                    Database.updateCurrentBalance(this.userId, this.currentBalance);
                    Transaction t = new Transaction("OUT", this.holdedCrypto[i].totalValue, this.holdedCrypto[i].symbol);
                    this.addTransaction(t);
                    continue;
                }
                anotherArray[k++] = this.holdedCrypto[i];
            }
            this.holdedCrypto = anotherArray;
            Database.removeRowFromHoldings(this.userId, symbol);
            System.out.println(symbol + " SOLD!");
            return;
        }
    }

    public void swapCrypto(String fromCrypto, String toCrypto) throws InvalidCrypto, InterruptedException, InsufficientBalance {
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
        System.out.println("We have swapped your " + fromCrypto + " to " + toCrypto);
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

            LocalDateTime dateTime = this.transactionHistory[i].dateTime;
            DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");;
            String formattedString = dateTime.format(customFormat);
            System.out.println("Date & Time: " + formattedString);
            System.out.println("-----");
        }

    }

}


package CryptoWallet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    String fullName;
    String email;
    String username;
    String hashPass;
    int NID;
    String phoneNumber;
    double currentBalance;
    boolean isLoggedIn;
    Transaction[] transactionHistory = new Transaction[5];

    public User(String fullName, String email, String username, String pass, int NID, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.hashPass = hashPassword(pass);
        this.NID = NID;
        this.phoneNumber = phoneNumber;
    }

    private String hashPassword(String pass) {
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



}

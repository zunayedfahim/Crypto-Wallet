package CryptoWallet;

import java.util.Arrays;
import java.util.Scanner;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

class Database {

	public static FetchUser[] fetchUsers() {
        // return userId, username and passsword of every user.
		Connection con=null; //con-helps us to connect with db
		Statement st; //access all the db
		ResultSet rs; //helps to bring data and print
		
		String url="jdbc:ucanaccess://D:/NSU/4th Semester/CSE215/CSE215LAB/CryptoWalletProject/CryptoWallet/Database2.accdb";
		// Database2.accdb
		FetchUser[] users = {};
		
		try {
			con =DriverManager.getConnection(url);
			st =con.createStatement();
			String sql ="Select * from Users";
			rs =st.executeQuery(sql);
			
			//it will bring until there is data
			while(rs.next()) {
				String userId,username,pass;
				userId=rs.getString("userId");
				username=rs.getString("username");
				pass=rs.getString("pass");
				
				FetchUser user = new FetchUser(userId, username, pass);
				users = Arrays.copyOf(users, users.length + 1);
        		users[users.length - 1] = user;
				con.close();
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}

		
		return users;

    }

	public static User fetchUserDetails(String userId) {
        // fetch all the information from the user using userId 
        // create an object of the User class and pass all the information gathered from the database
        // return the user object
		Connection con=null; //con-helps us to connect with db
		Statement st, st2, st3; //access all the db
		ResultSet rs, rs2, rs3; //helps to bring data and print
		
		String url="jdbc:ucanaccess://D:/NSU/4th Semester/CSE215/CSE215LAB/CryptoWalletProject/CryptoWallet/Database2.accdb";
		User user = null;
		try {
			con = DriverManager.getConnection(url);
			String search = "SELECT * From Users WHERE userId="+userId;
			String searchTransaction = "SELECT * From TransactionHistory WHERE userId=" + userId;
			String searchHoldedCrypto = "SELECT * From HoldedCryptos WHERE userId=" + userId;
			st = con.createStatement();
			rs=st.executeQuery(search);
			st2 = con.createStatement();
			rs2=st2.executeQuery(searchTransaction);
			st3 = con.createStatement();
			rs3=st3.executeQuery(searchHoldedCrypto);
			
			if(rs.next()) {
				String fullName,email,username,NID,phoneNumber, cBalance;
				fullName=rs.getString("fullName");
				email=rs.getString("email");
				username=rs.getString("username");
				NID=rs.getString("NID");
				phoneNumber=rs.getString("phoneNumber");
				cBalance=rs.getString("currentBalance");
				double currentBalance = Double.parseDouble(cBalance);
				
				
				// create user object
				user = new User(userId, fullName, email, username, NID, phoneNumber, currentBalance);
				
			}
			else {
				System.out.println("No records found.");
			}
			// fetch from Transaction Table
			while(rs2.next()) {
				String transactionId=rs2.getString("transactionId");
				String transactionType=rs2.getString("transactionType");
				String a=rs2.getString("amount");
				String dt=rs2.getString("dateTime");
				String currency=rs2.getString("currency");
				
				LocalDateTime dateTime = LocalDateTime.parse(dt);
				double amount = Double.parseDouble(a);
				Transaction t = new Transaction(transactionId, transactionType, amount, dateTime, currency);
				user.addTransaction(t);
				
			}
			
			// fetch from Holdings Table
			while(rs3.next()) {
				String h=rs3.getString("holding");
				String symbol=rs3.getString("symbol");
				Double holding = Double.parseDouble(h);
				
				Crypto c = new Crypto(symbol, holding);
				user.addCryptoToHoldings(c);
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return user;
    }

	public static void addTransactionToDatabase(Transaction t) {
		// TODO: add new transaction row
	}
	
	public static void updateCurrentBalance(double amount) {
		// TODO: update currentBalance in user table
	}
	
	public static void removeRowFromHoldings(String userId, String symbol) {
		// remove the entire row from the table
	}
	
	// if the crypto is already in the holdings
	public static void updateRowInHoldings(String userId, Crypto c) {
		// find the specific row by userId and c.symbol
		// update totalValue and holding
	}
	
	// if the crypto is not in the holdings
	public static void addRowToHoldings(String userId, Crypto c) {
		// add row with given parameters.
	}
	
	
}


class FetchUser {
    String userId;
    String username;
    String password;

	FetchUser(String userId, String username, String password) {
		this.userId = userId;
		this.username = username;
		this.password = password;
	}
}
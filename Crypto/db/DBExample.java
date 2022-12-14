package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
public class DBExample {

	public static void main(String[] args) {
		
		String databaseURL="jdbc:ucanaccess://Database2.accdb";
		try {
		Connection connection= DriverManager.getConnection(databaseURL);
		
		System.out.println("Connected to MS Access database.");
		
		Scanner input = new Scanner(System.in);
		
		String id,name,email,uname,pass,nid,phno,hcrypto,cbalanace;
		System.out.println("Enter user ID: ");
		id = input.next();
		
		System.out.println("Enter full name: ");
		name = input.next();
		
		System.out.println("Enter email: ");
		email = input.next();
		
		System.out.println("Enter user name: ");
		uname = input.next();
		
		System.out.println("Enter pass: ");
		pass = input.next();
		
		System.out.println("Enter NID: ");
		nid = input.next();
		
		System.out.println("Enter Number: ");
		phno = input.next();
		
		System.out.println("Enter Current Balance: ");
		cbalanace = input.next();
		
		System.out.println("Enter uHolded crypto: ");
		hcrypto = input.next();
		
		
		String sql = "INSERT INTO Users (userId,fullName,email,username,pass,NID,phoneNumber,currentBalance,holdedCrypto) VALUES"
				+"('id','name','email','uname','pass','nid','phno','cbalanace','hcrypto')";
		
		Statement statement = connection.createStatement();
		int rows = statement.executeUpdate(sql);
		if (rows>0) {
			System.out.println("A new contact has been inserted.");
		}
		connection.close();
			
		}
		
		
	
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}




/*
String sql = "SELECT * FROM table1" ;

Statement statement = connection.createStatement();
ResultSet result = statement.executeQuery(sql);

while(result.next()) {
	int id = result.getInt("ID");
	String name = result.getString("Name");
	String address = result.getString("Address");
	
	System.out.println(id+ ", " + name+", "+address+", ");
	statement.close();*/






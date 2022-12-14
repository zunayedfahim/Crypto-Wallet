package db;
import java.util.Arrays;
import java.util.Scanner;
import java.sql.*;

class CryptoWallet {


	public static void main(String[] args) {

		FetchUser[] users = fetchUsers();
		for(int i=0; i<users.length; i++) {
			System.out.println(users[i].userId);
		}


		
		// CryptoWallet  c=new CryptoWallet() ;

		// Scanner input = new Scanner(System.in);
		
		// String id,name,email,uname,pass,nid,phno,hcrypto,cbalance;
		
		
		// System.out.println("1.Print all the record");
		// System.out.println("2.Register");
		// System.out.println("3.Search Record");
		// System.out.println("4.Update");
		// System.out.println("5.Select Record");
		// System.out.println("Enter choice: ");
		// int ch=input.nextInt();
		
		// switch(ch) {
		
		// case 1:
		// 	c.ConnectionFetch();
		// 	break;
		// case 2:
		// 	c.NewUser();
		// 	break;
		// case 3:
		// 	c.SearchRecord();
		// 	break;
		// case 4:
		// 	c.Update();
		// 	break;
		// case 5:
		// 	c.ReadARecord();
		// 	break;
		
		// }
		
	}
	
	
	public static Connection ConnectionFetch() {
		Connection con=null; //con-helps us to connect with db
		Statement st; //access all the db
		ResultSet rs; //helps to bring data and print
		
		String url="jdbc:ucanaccess://Database2.accdb";
		
		try {
			con =DriverManager.getConnection(url);
			st =con.createStatement();
			String sql ="Select * from Users";
			rs =st.executeQuery(sql);
			
			while(rs.next())//it will bring until there is data
			{
				String id,name,email,uname,pass,nid,phno,cbalance;
				id=rs.getString("userId");
				name=rs.getString("fullName");
				email=rs.getString("email");
				uname=rs.getString("username");
				pass=rs.getString("pass");
				nid=rs.getString("NID");
				phno=rs.getString("phoneNumber");
				cbalance=rs.getString("currentBalance");
				
				//System.out.println(id+" "+name+" "+email+" "+uname+" "+pass+" "+nid+" "+phno+" "+hcrypto+" "+cbalanace);
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return con;
	}
	

	public static FetchUser[] fetchUsers() {
        // return userId, username and passsword of every user.
		Connection con=null; //con-helps us to connect with db
		Statement st; //access all the db
		ResultSet rs; //helps to bring data and print
		
		String url="jdbc:ucanaccess://Database2.accdb";
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


	
	
	public void NewUser()
	{
		try {
			

			Scanner input = new Scanner(System.in);
			
			String id,name,email,uname,pass,nid,phno,hcrypto,cbalance;
			boolean logged;
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
			
			System.out.println("Enter Phone Number: ");
			phno = input.next();
			
			System.out.println("Enter Current Balance: ");
			cbalance = input.next();
			
			
			System.out.println("Enter Holded crypto: ");
			hcrypto = input.next();
			
			
			
			
			Connection con=ConnectionFetch();
			Statement st = con.createStatement();
			String Info="INSERT INTO Users ([userId],[fullName],[email],[username],[pass],[NID],[phoneNumber],[currentBalance],[holdedCrypto])VALUES"
					+ "('"+id+"','"+name+"','"+email+"','"+uname+"','"+pass+"','"+nid+"','"+phno+"','"+cbalance+"','"+hcrypto+"')";
			int a=st.executeUpdate(Info);
			
			System.out.println("A new Account has been created");
			
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	
	
	public void SearchRecord()
	{
		try {
			
			Scanner input = new Scanner(System.in);
			
			String id;
			
			System.out.println("Enter user id: ");
			id = input.next();
			
			Connection con=ConnectionFetch();
			Statement st = con.createStatement();
			String search="SELECT * From Users WHERE userId='"+id+"'";
			ResultSet rs=st.executeQuery(search);
			
			while(rs.next()) {
				String uid = rs.getString("userId");
				if(uid.equals(id)) {
					System.out.println("User found");
			
					
				}
				else {
					System.out.println("User not found");
				}
			}
		
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	public void Update() {
		try {
			Scanner input = new Scanner(System.in);
			String uid;
			System.out.println("Enter user id: ");
			uid = input.next();
			Connection con=ConnectionFetch();
			String search="SELECT * From Users WHERE userId= "+uid;
			Statement st = con.createStatement();
			ResultSet rs=st.executeQuery(search);
			
			if(rs.next()) {
				String id,name,email,uname,pass,nid,phno,hcrypto,cbalance;
				id=rs.getString("userId");
				name=rs.getString("fullName");
				email=rs.getString("email");
				uname=rs.getString("username");
				pass=rs.getString("pass");
				nid=rs.getString("NID");
				phno=rs.getString("phoneNumber");
				cbalance=rs.getString("currentBalance");
				hcrypto=rs.getString("holdedCrypto");
			
			Scanner in=new Scanner(System.in); 
			System.out.println("What do u want to update?");
			
			System.out.println("1. pass: ");
			System.out.println("2. Current Balance: ");
			System.out.println("3. Holded crypto: ");
			
			int ch = in.nextInt();
			
			
			switch(ch) {
			
			case 1:
				System.out.println("Enter new password: ");
				pass = in.next();
				PreparedStatement ps= con.prepareStatement("UPDATE Users SET pass=? WHERE userId=?");
				ps.setString(1,pass);
				ps.setString(2,id);
				ps.executeUpdate();
				ps.close();
				System.out.println("Your account has been updated");
				break;
			case 2:
				System.out.println("Enter new current balance: ");
				cbalance = in.next();
				PreparedStatement ps1= con.prepareStatement("UPDATE Users SET cbalance=? WHERE userId=?");
				ps1.setString(1,cbalance);
				ps1.setString(2,id);
				ps1.executeUpdate();
				ps1.close();
				System.out.println("Your account has been updated");
				break;
			case 3:
				System.out.println("Enter Holded Crypto: ");
				hcrypto = in.next();
				PreparedStatement ps2= con.prepareStatement("UPDATE Users SET hcrypto=? WHERE userId=?");
				ps2.setString(1,hcrypto);
				ps2.setString(2,id);
				ps2.executeUpdate();
				ps2.close();
				System.out.println("Your account has been updated");
				break;
			
			}
		}
		}
		
		
		
		
		
		/*System.out.println("Enter user ID: ");
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
		
		System.out.println("Enter Phone Number: ");
		phno = input.next();
		System.out.println("Enter new Current Balance: ");
		cbalance=input.next();
		Connection con=ConnectionFetch();
		PreparedStatement ps= con.prepareStatement("UPDATE Users SET currentBalance=? WHERE userId=?");
		ps.setString(1,cbalance);
		ps.setString(2,id);
		ps.executeUpdate();
		ps.close();
		System.out.println("Your account has been updated");*/
		
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
		

	
	
	public void ReadARecord() {
		try {
			
			Scanner input = new Scanner(System.in);
			String uid;
			System.out.println("Enter user id: ");
			uid = input.next();
			Connection con=ConnectionFetch();
			String search="SELECT * From Users WHERE userId= "+uid;
			Statement st = con.createStatement();
			ResultSet rs=st.executeQuery(search);
			
			if(rs.next()) {
				String id,name,email,uname,pass,nid,phno,hcrypto,cbalance;
				id=rs.getString("userId");
				name=rs.getString("fullName");
				email=rs.getString("email");
				uname=rs.getString("username");
				pass=rs.getString("pass");
				nid=rs.getString("NID");
				phno=rs.getString("phoneNumber");
				cbalance=rs.getString("currentBalance");
				hcrypto=rs.getString("holdedCrypto");
				
				System.out.println("Your user ID: "+id);
				System.out.println("Your full name: "+name);
				System.out.println("Your email: "+email);
				System.out.println("Your user name: "+uname);
				System.out.println("Your pass: "+pass);
				System.out.println("Your NID: "+nid);
				System.out.println("Your Phone Number: "+phno);
				System.out.println("Your new Current Balance: "+cbalance);
				System.out.println("Your Holded crypto: "+hcrypto);
			}
			else {
				System.out.println("No records found.");
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
}

	public static void fetchUserDetail(String userId) {
        // fetch all the information from the user using userId 
        // create an object of the User class and pass all the information gathered from the database
        // return the user object
		Connection con=null; //con-helps us to connect with db
		Statement st; //access all the db
		ResultSet rs; //helps to bring data and print
		
		String url="jdbc:ucanaccess://Database2.accdb";
		try {
			con =DriverManager.getConnection(url);
			con=ConnectionFetch();
			String search="SELECT * From Users WHERE userId= "+userId;
			st = con.createStatement();
			rs=st.executeQuery(search);
			
			if(rs.next()) {
				String id,name,email,uname,pass,nid,phno,cbalance;
				id=rs.getString("userId");
				name=rs.getString("fullName");
				email=rs.getString("email");
				uname=rs.getString("username");
				pass=rs.getString("pass");
				nid=rs.getString("NID");
				phno=rs.getString("phoneNumber");
				cbalance=rs.getString("currentBalance");
				// create user object
			}
			else {
				System.out.println("No records found.");
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
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
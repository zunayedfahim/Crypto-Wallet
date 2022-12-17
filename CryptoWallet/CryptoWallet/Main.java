package CryptoWallet;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.crypto.Data;



public class Main {


    public static void main(String args[]) throws Exception {


        // Welcome Note
        clearScreen();
        System.out.println("Welcome to Cryptox");
        System.out.print("Press - L to Login, R to Register or Q to Quit:");

        // Take Input
        Scanner takeInput = new Scanner(System.in);
        char input = takeInput.next().charAt(0);
        clearScreen();

        boolean valid_input = false;
        FetchUser[] users = Database.fetchUsers();

        while (!valid_input) {
            if(input=='L' || input=='l') {

                // Take input for login
                System.out.println("Login");
                System.out.print("Username:");
                String username = takeInput.next();
                Console console = System.console() ;
                char[] password = console.readPassword("Password: ");
                String pass = new String(password);
                User user = SignIn(username, pass, users);
                clearScreen();
                Dashboard(user);

            } else if(input=='R' || input=='r') {

                // Take input for signup
                Scanner in = new Scanner(System.in);
                System.out.println("Register");
                System.out.print("Full Name:");
                String fullName = in.nextLine();
                System.out.print("Email:");
                String email = in.next();
                System.out.print("Username:");
                String username = in.next();
                Console console = System.console() ;
                char[] password = console.readPassword("Password: ");
                String pass = new String(password);
                System.out.print("NID:");
                String NID = in.next();
                System.out.print("Phone Number:");
                String phoneNumber = in.next();
                String userId = String.valueOf(users.length + 1);
                User user = new User(userId, fullName, email, username, pass, NID, phoneNumber, 0.0);
                Database.addUserToDatabase(user);
                clearScreen();
                Dashboard(user);


            } else if(input=='Q' || input=='q') {
                QuitProgram();
            }
        }
        


    }

    public static void Dashboard(User user) {
        // username
        System.out.println("You are logged in as - " + user.username);

        // current balance
        System.out.println("Current Balance: " + user.currentBalance);
        System.out.println("-----");

        // buy/sell crypto 
        System.out.println("Press:");
        System.out.println("A. Add Money");
        System.out.println("W. Withdraw Money");
        System.out.println("B. Buy Crypto");
        System.out.println("S. Sell Crypto");
        System.out.println("P. Swap Crypto");
        System.out.println("H. Current Holdings");
        System.out.println("T. Transaction History");
        System.out.println("Q. Quit Program");

        Scanner inDashboard = new Scanner(System.in);
        char input = inDashboard.next().charAt(0);
        input = Character.toUpperCase(input);
        clearScreen();

        boolean valid_input = false;

        while (!valid_input) {
            if (input == 'A') {
                Scanner sc = new Scanner(System.in);
            	double addablemoney;
            	System.out.println("Supported Mediums to add money:\n1.\tCard\n2.\tbKash\n3.\tPaypal");
            	System.out.println("Enter Medium for adding money:");
            	String medium = sc.next();
            	medium = medium.toLowerCase();
            	if (medium =="#") {
            		Dashboard(user);
            	}
            	System.out.print("Enter Deposit amount: ");
            	addablemoney = sc.nextDouble();
            	try {
            		user.callMedium(medium, addablemoney);
                	System.out.println(addablemoney+" Tk is deposited to "+ user.username);
                	returnToDashboard(user);
            	}catch(Exception e) {
            		System.out.println(e);
                    returnToDashboard(user);
            	}
            } else if(input == 'W') {
                System.out.print("Enter amount to Withdraw: ");
            	double Wmoney = inDashboard.nextDouble();
            	user.withdraw(Wmoney);
            	System.out.println(Wmoney+" Tk is withdrawn from "+user.username);
                returnToDashboard(user);
            } else if(input == 'B') {
                // buy crypto
                System.out.println("Which crypto you want to buy?");
                Crypto.showCryptoInfo();
                System.out.println("# : Dashboard");
                System.out.print("Symbol: ");
                Scanner inBuy = new Scanner(System.in);
                String symbol = inBuy.next();
                symbol = symbol.toUpperCase();
                if(symbol=="#") {
                    Dashboard(user);
                }
                System.out.print("Amount: ");
                Double holding = inBuy.nextDouble();

                try {
                    Crypto c = new Crypto(symbol, holding);
                    user.buyCrypto(c);
                    returnToDashboard(user);
                } catch (Exception e) {
                    System.out.println(e);
                    returnToDashboard(user);
                }


            } else if(input == 'S') {
                if(user.holdedCrypto.length>0) {
                    System.out.println("Which crypto you want to sell?");
                    user.showHoldedCryptos();
                    System.out.println("# : Dashboard");
                    System.out.print("Symbol: ");
                    String symbol = inDashboard.next();
                    symbol = symbol.toUpperCase();
                    if(symbol=="#") {
                        Dashboard(user);
                    }
    
                    try {
                        user.sellCrypto(symbol);
                        returnToDashboard(user);
                    } catch (InvalidCrypto e) {
                        System.out.println(e);
                        returnToDashboard(user);
                    }
                } else {
                    System.out.println("You do not have any crypto in your holdings.");
                    returnToDashboard(user);
                }

            } else if(input == 'P') {
                Scanner inSwap = new Scanner(System.in);
                System.out.println("Which Crypto you want to swap?");
                user.showHoldedCryptos();
                System.out.print("Symbol: ");
                String fromCrypto = inSwap.next();
                fromCrypto = fromCrypto.toUpperCase();
                System.out.println("To which Crypto you want to swap?");
                Crypto.showCryptoInfo();
                System.out.print("Symbol: ");
                String toCrypto = inSwap.next();
                toCrypto = toCrypto.toUpperCase();
                try {
                    user.swapCrypto(fromCrypto, toCrypto);
                } catch (Exception e) {
                    System.out.println(e);
                    returnToDashboard(user);
                }
                returnToDashboard(user);
            } else if(input == 'H') {
                // holded cryptos 
                user.showHoldedCryptos();
                Scanner inHoldings = new Scanner(System.in);
                System.out.println("Press:");
                System.out.println("#: Dashboard");
                System.out.println("Q: Quit");
                char input1 = inHoldings.next().charAt(0);
                clearScreen();
                if(input1=='#') {
                    Dashboard(user);
                } else if(Character.toUpperCase(input1)=='Q') {
                    QuitProgram();
                }

            } else if(input == 'T') {
                // transaction history 
                user.showTransactionHistory();
                Scanner inTransaction = new Scanner(System.in);
                System.out.println("Press:");
                System.out.println("#: Dashboard");
                System.out.println("Q: Quit");
                char input1 = inTransaction.next().charAt(0);
                clearScreen();
                if(input1=='#') {
                    Dashboard(user);
                } else if(Character.toUpperCase(input1)=='Q') {
                    QuitProgram();
                }

            } else if(input == 'Q') {
                QuitProgram();
            }
        }

    }

    public static User SignIn(String username, String pass, FetchUser[] users) throws Exception {
        for(int i = 0; i < users.length; i++){
            if(username.equals(users[i].username)){
                String password = User.hashPassword(pass);
                if(password.equals(users[i].password)){
                    System.out.println("Sign in Successful");
                    return Database.fetchUserDetails(users[i].userId);
                } else {
                    throw new Exception("Password didn't match.");
                }
            }
        }
        throw new Exception("Login Failed");
    }

    public static void SignOut(User ob){
        ob.isLoggedIn = false;
        System.out.println("Logged out of " + ob.username);
    }

    public static void returnToDashboard(User user) {
        System.out.print("Returning to Dashboard ...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearScreen();
        Dashboard(user);
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void QuitProgram() {
        System.out.println("Thank You!");
        System.out.println("Terminating the Program.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearScreen();
        System.exit(0);
    }

    

}


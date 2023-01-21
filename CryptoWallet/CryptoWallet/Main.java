package CryptoWallet;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;


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
                User user = signIn(username, pass, users);
                clearScreen();
                dashboard(user);

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
                dashboard(user);


            } else if(input=='Q' || input=='q') {
                quitProgram();
            }
        }
        


    }

    public static void dashboard(User user) {
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

        Scanner indashboard = new Scanner(System.in);
        char input = indashboard.next().charAt(0);
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
                    clearScreen();
            		dashboard(user);
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
            	double Wmoney = indashboard.nextDouble();
            	user.withdraw(Wmoney);
            	System.out.println(Wmoney+" Tk is withdrawn from "+user.username);
                returnToDashboard(user);
            } else if(input == 'B') {
                // buy crypto
                System.out.println("Which crypto you want to buy?");
                Crypto.showCryptoInfo();
                System.out.println("# : dashboard");
                System.out.print("Symbol: ");
                Scanner inBuy = new Scanner(System.in);
                String symbol = inBuy.next();
                symbol = symbol.toUpperCase();
                if(symbol=="#") {
                    clearScreen();
                    dashboard(user);
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
                    System.out.println("# : dashboard");
                    System.out.print("Symbol: ");
                    String symbol = indashboard.next();
                    symbol = symbol.toUpperCase();
                    if(symbol=="#") {
                        clearScreen();
                        dashboard(user);
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
                clearScreen();
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
                System.out.println("#: dashboard");
                System.out.println("Q: Quit");
                char input1 = inHoldings.next().charAt(0);
                if(input1=='#') {
                    clearScreen();
                    dashboard(user);
                } else if(Character.toUpperCase(input1)=='Q') {
                    quitProgram();
                }

            } else if(input == 'T') {
                // transaction history 
                user.showTransactionHistory();
                Scanner inTransaction = new Scanner(System.in);
                System.out.println("Press:");
                System.out.println("#: dashboard");
                System.out.println("Q: Quit");
                char input1 = inTransaction.next().charAt(0);
                if(input1=='#') {
                    clearScreen();
                    dashboard(user);
                } else if(Character.toUpperCase(input1)=='Q') {
                    quitProgram();
                }

            } else if(input == 'Q') {
                quitProgram();
            }
        }

    }

    public static User signIn(String username, String pass, FetchUser[] users) throws Exception {
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

    public static void signOut(User user){
        user.isLoggedIn = false;
        System.out.println("Logged out of " + user.username);
    }

    public static void returnToDashboard(User user) {
        System.out.print("Returning to dashboard ...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearScreen();
        dashboard(user);
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void quitProgram() {
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


package CryptoWallet;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

// DUMMY FILE FOR TEST



public class Main {


    public static void main(String args[]) throws Exception {

        

        // // Welcome Note
        // System.out.println("Welcome to Cryptox");
        // System.out.print("Press - L to Login, R to Register or Q to Quit:");

        // // Take Input
        // Scanner takeInput = new Scanner(System.in);
        // char input = takeInput.next().charAt(0);

        // boolean valid_input = false;

        // while (!valid_input) {
        //     if(input=='L' || input=='l') {
        // ***
        // For Neha: fetch only username and pass from database and fill it in the array.
        // FetchUsernamePass[] users = {
        //     new FetchUsernamePass(userId, username, pass),
        //     new FetchUsernamePass(userId, username, pass),
        //     new FetchUsernamePass(userId, username, pass)
        // };
        // // ***
        //         // Take input for login
        //         System.out.println("Login");
        //         System.out.print("Username:");
        //         String username = takeInput.next();
        //         Console console = System.console() ;
        //         char[] password = console.readPassword("Password: ");
        //         String pass = new String(password);
        //         User user = SignIn(username, pass, users);
        //         // TODO: Redirect to Dashboard

        //     } else if(input=='R' || input=='r') {

        //         // Take input for signup
        //         Scanner in = new Scanner(System.in);
        //         System.out.print("Full Name:");
        //         String fullName = in.nextLine();
        //         System.out.print("Email:");
        //         String email = in.next();
        //         System.out.print("Username:");
        //         String username = in.next();
        //         Console console = System.console() ;
        //         char[] password = console.readPassword("Password: ");
        //         String pass = new String(password);
        //         System.out.print("NID:");
        //         String NID = in.next();
        //         System.out.print("Phone Number:");
        //         String phoneNumber = in.next();

        //         User user = new User(fullName, email, username, pass, NID, phoneNumber);
        //         SignUp(user);


        //     } else if(input=='Q' || input=='q') {
                    // QuitProgram();
        //     }
        // }
        

        User ob = new User("Zunayed", "fahim@gmail.com", "zunayed", "pass", "9165177958", "01842326680");

        // Transaction t = new Transaction("IN", 500);
        // ob.addTransaction(t);
        // System.out.println(ob.transactionHistory[0].currency);

        // Crypto c = new Crypto("BTC", 1.25);
        // ob.buyCrypto(c);
        
        // Crypto e = new Crypto("ETH", 2);
        // ob.buyCrypto(e);

        // ob.sellCrypto("BTC");
        // System.out.println(ob.holdedCrypto.length);

        Dashboard(ob);
    }

    public static User SignIn(String username, String pass, User[] users) throws Exception{
        for(int i = 0; i < users.length; i++){
            if(username.equals(users[i].username)){
                System.out.println(pass);
                String password = User.hashPassword(pass);
                if(password.equals(users[i].pass)){
                    users[i].isLoggedIn = true;
                    System.out.println("Sign in Successful");
                    // TODO: For Neha: fetch the exact user from databse
                    // create user object 
                    // fetch holded crypto and fill object.holdedCrypto
                    // fetch transactionHistory crypto and fill object.transactionHistory
                    return users[i];
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

    public static void SignUp(User user) {
        // ***
        // For Neha: Destructure the User object and push it to the database
        // ***
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
        System.out.println("H. Current Holdings");
        System.out.println("T. Transaction History");

        System.out.println("Q. Quit Program");

        Scanner inDashboard = new Scanner(System.in);
        char input = inDashboard.next().charAt(0);
        clearScreen();

        boolean valid_input = false;

        while (!valid_input) {
            if (input == 'A' || input == 'a') {
                // TODO: add money for HRIDI
            } else if(input == 'W' || input == 'w') {
                // TODO: withdraw money for HRIDI
            } else if(input == 'B' || input == 'b') {
                // buy crypto
                System.out.println("Which crypto you want to buy?");
                System.out.println("BTC : Bitcoin");
                System.out.println("ETH : Ethereum");
                System.out.println("DOGE : Dogecoin");
                System.out.println("DOT : Polkadot");
                System.out.println("LTC : Litecoin");
                System.out.println("# : Dashboard");
                System.out.print("Coin: ");
                Scanner inBuy = new Scanner(System.in);
                String symbol = inBuy.next();
                if(symbol=="#") {
                    Dashboard(user);
                }
                System.out.print("Amount: ");
                Double holding = inBuy.nextDouble();

                try {
                    Crypto c = new Crypto(symbol, holding);
                    user.buyCrypto(c);
                    clearScreen();
                    Dashboard(user);
                } catch (Exception e) {
                    System.out.println(e);
                    clearScreen();
                    Dashboard(user);
                }


            } else if(input == 'S' || input == 's') {
                if(user.holdedCrypto.length>0) {
                    System.out.println("Which crypto you want to sell?");
                    for(int i=0; i<user.holdedCrypto.length; i++) {
                        System.out.println(user.holdedCrypto[i].symbol + ": " + user.holdedCrypto[i].name);
                    }
                    System.out.println("# : Dashboard");
                    System.out.print("Coin: ");
                    String symbol = inDashboard.next();
                    if(symbol=="#") {
                        Dashboard(user);
                    }
    
                    try {
                        user.sellCrypto(symbol);
                        Dashboard(user);
                    } catch (InvalidCrypto e) {
                        System.out.println(e);
                        Dashboard(user);
                    }
                } else {
                    System.out.println("You do not have any crypto in your holdings.");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Dashboard(user);
                }

            } else if(input == 'H' || input == 'h') {
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

            } else if(input == 'T' || input == 't') {
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

            } else if(input == 'Q' || input == 'q') {
                QuitProgram();
            }
        }

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


        // For Neha: Update the user info to the database
        System.exit(0);
    }


}


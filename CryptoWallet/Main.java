package CryptoWallet;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

// DUMMY FILE FOR TEST



public class Main {

    public static User SignIn(String username, String pass, User[] users) throws Exception{
        for(int i = 0; i < users.length; i++){
            if(username.equals(users[i].username)){
                System.out.println(pass);
                String password = User.hashPassword(pass);
                if(password.equals(users[i].pass)){
                    users[i].isLoggedIn = true;
                    System.out.println("Sign in Successful");
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
    


    public static void main(String args[]) throws Exception {

        // ***
        // For Neha: fetch users from database and fill it in the array.
        User[] users = {
            new User("Tamim Iqbal","Tamim77@gmail.com", "a", "helloworld","1234567891","01997123456"),
            new User("Shakib hasan","Shakib71@gmail.com", "b", "hiworld","1234567891","01997143723"),
            new User("Sabbir Hossain","Sabbir1@gmail.com", "c", "holaworld","1234567891","01997739624")
        };
        // ***

        // Welcome Note
        System.out.println("Welcome to Cryptox");
        System.out.print("Press - L to Login, R to Register or Q to Quit:");

        // Take Input
        Scanner takeInput = new Scanner(System.in);
        char input = takeInput.next().charAt(0);

        boolean valid_input = false;

        while (!valid_input) {
            if(input=='L' || input=='l') {

                // Take input for login
                System.out.println("Login");
                System.out.print("Username:");
                String username = takeInput.next();
                Console console = System.console() ;
                char[] password = console.readPassword("Password: ");
                String pass = new String(password);
                SignIn(username, pass, users);
                input = takeInput.next().charAt(0);

            } else if(input=='R' || input=='r') {

                // Take input for signup
                Scanner in = new Scanner(System.in);
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

                User user = new User(fullName, email, username, pass, NID, phoneNumber);
                SignUp(user);
                input = takeInput.next().charAt(0);
            } else if(input=='Q' || input=='q') {
                System.out.println("Thank you.");
                valid_input = true;
            }
        }
        

        // User ob = new User("Zunayed", "fahim@gmail.com", "zunayed", "pass", "9165177958", "01842326680");

        // Transaction t = new Transaction("IN", 500);
        // ob.addTransaction(t);
        // System.out.println(ob.transactionHistory[0].currency);

        // Crypto c = new Crypto("BTC", 1.25);
        // ob.buyCrypto(c);
        
        // Crypto e = new Crypto("ETH", 2);
        // ob.buyCrypto(e);

        // ob.sellCrypto("BTC");
        // System.out.println(ob.holdedCrypto.length);
    }
}


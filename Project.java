import java.sql.*;
import java.util.Scanner;

public class Project {
    // you may change the following connection parameters
    private static String dbAddress = "jdbc:oracle:thin://@db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk";
    private static String dbUsername = "h052";
    private static String dbPassword = "fromaTwa";

    private static String YYYY = "0000";
    private static String MM = "00";
    private static String DD = "00";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        displayMainMenu();
    }

    private static void displayMainMenu() {
        System.out.println("The system time is now: " + YYYY + "-" + MM + "-" + DD);
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("-----------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Set System Date.");
        System.out.println("5. Quit the system......\n");
        
        System.out.print("Please enter your choice??..");
        String choice = sc.nextLine();
        System.out.println();

        switch (choice) {
            case "1":
                // System interface
                displaySystemInterface();
                break;
            case "2":
                // Customer interface
                displayCustomerInterface();
                break;
            case "3":
                // Bookstore interface
                displayBookstoreInterface();
                break;
            case "4":
                // Set System Date
                setSystemDate();
                break;
            case "5":
                // Quit the system
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                displayMainMenu();
                break;
        }

    }

    private static void displaySystemInterface() {
        System.out.println("<This is the System interface.>");
        System.out.println("-------------------------------");
        System.out.println("1. Create Table.");
        System.out.println("2. Delete Table.");
        System.out.println("3. Insert Data.");
        System.out.println("4. Set System Date.");
        System.out.println("5. Back to main menu.\n");

        System.out.print("Please enter your choice??..");
        String choice = sc.nextLine();
        System.out.println();

        switch (choice) {
            case "1":
                // Create Table
                break;
            case "2":
                // Delete Table
                break;
            case "3":
                // Insert Data
                break;
            case "4":
                // Set System Date
                break;
            case "5":
                // Back to main menu
                displayMainMenu();
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
        }
    }

    private static void displayCustomerInterface() {
        System.out.println("<This is the Customer interface.>");
        System.out.println("---------------------------------");
        System.out.println("1. Book Search.");
        System.out.println("2. Order Creation.");
        System.out.println("3. Order Altering.");
        System.out.println("4. Order Query.");
        System.out.println("5. Back to main menu.\n");

        System.out.print("Please enter your choice??..");
        String choice = sc.nextLine();
        System.out.println();

        switch (choice) {
            case "1":
                // Book Search
                bookSearch();
                break;
            case "2":
                // Order Creation
                orderCreation();
                break;
            case "3":
                // Order Altering
                orderAltering();
                break;
            case "4":
                // Order Query
                customerOrderQuery();
                break;
            case "5":
                // Back to main menu
                displayMainMenu();
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
        }
    }

    private static void bookSearch() {
        System.out.println("What do u want to search?");
        System.out.println("1 ISBN");
        System.out.println("2 Book Title");
        System.out.println("3 Author Name");
        System.out.println("4 Exit");
        System.out.print("Your choice??..");
        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                System.out.print("Input the ISBN: ");
                String ISBN = sc.nextLine();
                System.out.println();
                // search by ISBN
                break;
            case "2":
                System.out.print("Input the Book Title: ");
                String bookTitle = sc.nextLine();
                System.out.println();
                // search by book title
                break;
            case "3":
                System.out.print("Input the Author Name: ");
                String authorName = sc.nextLine();
                System.out.println();
                // search by author name
                break;
            case "4":
                displayCustomerInterface();
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                bookSearch();
                break;
        }
    }

    private static void orderCreation() {
        System.out.print("Please enter your customer ID??..");
        String customerID = sc.nextLine();
        System.out.println(">> What books do you want to order??");
        System.out.println(">> Input ISBN then the quantity.");
        System.out.println(">> You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
        System.out.print("Please input the book's ISBN: ");
        String ISBN = sc.nextLine();
        while (!ISBN.equals("F")) {
            if (ISBN.equals("L")) {
                System.out.println("ISBN             Number:");
                // TODO: display ordered list
            } else if (isValidISBN(ISBN)){
                System.out.print("Please input the quantity: ");
                String response = sc.nextLine();

                // Loop until a valid integer is entered
                while (!response.matches("\\d+")) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    System.out.print("Please input the quantity: ");
                    response = sc.nextLine();
                }

                int quantity = Integer.parseInt(response);
                // TODO: add to ordered list
            } else {
                System.out.println("Invalid ISBN. Please enter a valid ISBN.");
            }
            System.out.print("Please input the book's ISBN: ");
            ISBN = sc.nextLine();
        }
    }

    private static void orderAltering() {
        System.out.println("Please enter the orderId you want to change: ");
        String orderId = sc.nextLine();
        while (!isValidOrderID(orderId)) {
            System.out.println("Invalid orderID. Please enter a valid orderID.");
            System.out.println("Please enter the orderId you want to change: ");
            orderId = sc.nextLine();
        }
        // TODO: display order details

        System.out.print("Which book do you want to alter (input book no.): ");
        String bookNo = sc.nextLine();
        while (!bookNo.matches("\\d+")) {
            System.out.println("Invalid input. Please enter a valid integer.");
            System.out.print("Which book do you want to alter (input book no.): ");
            bookNo = sc.nextLine();
        }
        System.out.println("input add or remove");
        String choice = sc.nextLine();
        if (choice.equals("add")) {
            System.out.print("input the number: ");
            String number = sc.nextLine();
            while (!number.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a valid integer.");
                System.out.print("input the number: ");
                number = sc.nextLine();
            }

            // TODO: do the update


        } else if (choice.equals("remove")) {
            System.out.print("input the number: ");
            String number = sc.nextLine();
            while (!number.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a valid integer.");
                System.out.print("input the number: ");
                number = sc.nextLine();
            }
            // TODO: do the update
        }

    }

    private static void customerOrderQuery() {
        System.out.print("Please input customer ID: ");
        String customerID = sc.nextLine();
        System.out.print("Please input the year: ");
        String year = sc.nextLine();
        System.out.println();

        // TODO: display order details
        // “Order ID”, “Order Date”, “Books Ordered”, “Charge” and “Shipping Status”
        // Order: Results should be sorted in ascending order by “Order ID”
    }

    private static void displayBookstoreInterface() {
        System.out.println("<This is the Bookstore interface.>");
        System.out.println("----------------------------------");
        System.out.println("1. Order Update.");
        System.out.println("2. Order Query.");
        System.out.println("3. N most Popular Book Query.");
        System.out.println("4. Back to main menu.\n");

        System.out.print("Please enter your choice??..");
        String choice = sc.nextLine();
        System.out.println();

        switch (choice) {
            case "1":
                // Order Update
                orderUpdate();
                break;
            case "2":
                // Order Query
                bookstoreOrderQuery();
                break;
            case "3":
                // N most Popular Book Query
                nMostPopularBookQuery();
                break;
            case "4":
                // Back to main menu
                displayMainMenu();
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
        }
    }

    private static void orderUpdate() {
        System.out.print("Please input the order ID: ");
        String orderId = sc.nextLine();
        while (!isValidOrderID(orderId)) {
            System.out.println("Invalid orderID. Please enter a valid orderID.");
            System.out.print("Please enter the orderId you want to change: ");
            orderId = sc.nextLine();
        }
        // TODO: display order details if exists
    }

    private static void bookstoreOrderQuery() {
        System.out.print("Please input the Month for Order Query (e.g.2005-09): ");
        String yyyymm = sc.nextLine();
        while (!isValidYYYYMM(yyyymm)) {
            System.out.println("Invalid input. Please enter a valid YYYY-MM format.");
            System.out.print("Please input the Month for Order Query (e.g.2005-09): ");
            yyyymm = sc.nextLine();
        }
        // TODO: display order details with total charge
    }

    private static void nMostPopularBookQuery() {
        System.out.print("Please input the N popular books number: ");
        String N = sc.nextLine();
        while (!N.matches("\\d+")) {
            System.out.println("Invalid input. Please enter a valid integer.");
            System.out.print("Please input N: ");
            N = sc.nextLine();
        }
        // TODO: display N most popular books
    }

    private static void setSystemDate() {
        System.out.print("Please input the date (YYYYMMDD): ");
        String date = sc.nextLine();
        while (date.length() != 8 || !date.matches("\\d+") || !isLaterDate(date)) {
            System.out.println("Invalid input. Please enter a date later than " + YYYY + MM + DD);
            System.out.print("Please input the date (YYYYMMDD): ");
            date = sc.nextLine();
        }
        YYYY = date.substring(0, 4);
        MM = date.substring(4, 6);
        DD = date.substring(6, 8);
    }
    
    private static boolean isLaterDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        // check month
        if (month < 1 || month > 12) {
            return false;
        }
        // check day
        if (day < 1 || day > getDaysInMonth(year, month)) {
            return false;
        }
        return year > Integer.parseInt(YYYY) || 
               (year == Integer.parseInt(YYYY) && month > Integer.parseInt(MM)) || 
               (year == Integer.parseInt(YYYY) && month == Integer.parseInt(MM) && day > Integer.parseInt(DD));
    }

    private static int getDaysInMonth(int year, int month) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                return 31;
        }
    }

    private static Connection connectToDB()n{
        Connection con = null;
        try {
                Class.forName("oracle.jdbc.OracleDriver");
                con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e){
                System.out.println("[Error]: Java MySQL DB Driver not found!!");
                System.exit(0);
        } catch (SQLException e){
                System.out.println(e);
        }
        return con;
    }

    private static boolean isValidISBN(String ISBN) {
        // ISBN: 13 chars with format “X-XXXX-XXXX-X”, where X is a digit
        String pattern = "^\\d-[\\d]{4}-[\\d]{4}-\\d$";
        return ISBN.matches(pattern);
    }

    private static boolean isValidOrderID(String orderID) {
        // Check if the orderID is 8 characters long
        if (orderID.length() != 8) {
            return false;
        }
    
        // Check if the orderID consists of only digits
        if (!orderID.matches("\\d+")) {
            return false;
        }
    
        // Check if the orderID is greater than or equal to "00000000"
        if (orderID.compareTo("00000000") < 0) {
            return false;
        }
    
        return true;
    }

    private static boolean isValidYYYYMM(String yyyymm) {
        // Regular expression pattern for the month format: YYYY-MM
        String pattern = "^\\d{4}-\\d{2}$";
        return yyyymm.matches(pattern);
    }
}
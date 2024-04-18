package csci3170project;

import static csci3170project.Queries.*;
import static csci3170project.Utils.*;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Project {
  // you may change the following connection parameters to your db account
  private static String dbAddress = "jdbc:oracle:thin://@db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk";
  private static String dbUsername = "h052";
  private static String dbPassword = "fromaTwa";

  private static Connection connection = null;
  private static Statement statement = null;
  private static ResultSet resultSet = null;

  private static LocalDate date = LocalDate.of(0, 1, 1);
  private static String YYYY = "0000";
  private static String MM = "01";
  private static String DD = "01";
  private static Scanner sc = new Scanner(System.in);

  public static void main(String[] args) {

    try {
      // Load the Oracle JDBC driver
      Class.forName("oracle.jdbc.OracleDriver");
      // connectionnect to the database
      connection = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
      // Create a statement
      statement = connection.createStatement();

      initQueries(connection);
      displayMainMenu();
    } catch (ClassNotFoundException e) {
      System.err.println("Failed to load Oracle JDBC driver.");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("Failed to connection to the database.");
      e.printStackTrace();
    } finally {
      // Close the resources
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (connection != null) {
        try {
          connection.close();

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Main Menu
  private static void displayMainMenu() {
    System.out.println("The system time is now: " + YYYY + "-" + MM + "-" + DD);
    System.out.println("<This is the Book Ordering System.>");
    System.out.println("-----------------------------------");
    System.out.println("1. System interface.");
    System.out.println("2. Customer interface.");
    System.out.println("3. Bookstore interface.");
    System.out.println("4. Show System Date.");
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
        // Show System Date
        showSystemDate();
        break;
      case "5":
        // Quit the system
        break;
      default:
        System.out.println("Invalid choice. Please enter a valid option.");
        displayMainMenu();
        break;
    }

  }

  private static void showSystemDate() {
    System.out.println("The system time is now: " + YYYY + "-" + MM + "-" + DD);
    System.out.println();
    displayMainMenu();
  }

  // System Interface
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
        createTable();
        break;
      case "2":
        // Delete Table
        deleteTable();
        break;
      case "3":
        // Insert Data
        insertData();
        break;
      case "4":
        // Set System Date
        setSystemDate();
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

  private static void createTable() {
    try {
      // Create the table
      String createBook = """
              CREATE TABLE Book (
                  ISBN CHAR(13) PRIMARY KEY,
                  title VARCHAR(100) NOT NULL,
                  unit_price INT CHECK (unit_price >= 0),
                  no_of_copies INT CHECK (no_of_copies >= 0)
              )
          """;
      statement.executeUpdate(createBook);

      String createCustomer = """
              CREATE TABLE Customer (
                  customer_id VARCHAR(10) PRIMARY KEY,
                  name VARCHAR(50) NOT NULL,
                  shipping_address VARCHAR(200) NOT NULL,
                  credit_card_no CHAR(19)
              )
          """;
      statement.executeUpdate(createCustomer);

      String createOrder = """
              CREATE TABLE Orders (
                  order_id CHAR(8) PRIMARY KEY,
                  o_date DATE NOT NULL,
                  shipping_status CHAR(1) CHECK (shipping_status IN ('Y', 'N')),
                  charge INT CHECK (charge >= 0),
                  customer_id VARCHAR(10) NOT NULL,
                  FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
              )
          """;
      statement.executeUpdate(createOrder);

      String createOrdering = """
              CREATE TABLE Ordering (
                  order_id CHAR(8),
                  ISBN CHAR(13),
                  quantity INT CHECK (quantity >= 0),
                  FOREIGN KEY (order_id) REFERENCES Orders(order_id),
                  FOREIGN KEY (ISBN) REFERENCES Book(ISBN),
                  PRIMARY KEY (order_id, ISBN)
              )
          """;
      statement.executeUpdate(createOrdering);
      String createBookAuthor = """
              CREATE TABLE Book_author (
                  ISBN CHAR(13),
                  author_name VARCHAR(50) NOT NULL,
                  FOREIGN KEY (ISBN) REFERENCES Book(ISBN),
                  PRIMARY KEY (ISBN, author_name)
              )
          """;
      statement.executeUpdate(createBookAuthor);
      System.out.println("Table created successfully.\n");
      displaySystemInterface();
    } catch (SQLException e) {
      System.out.println("Failed to create the table.");
      e.printStackTrace();
    }
  }

  private static void deleteTable() {
    try {
      // Drop the table
      String dropBookAuthor = "DROP TABLE Book_author";
      statement.executeUpdate(dropBookAuthor);

      String dropOrdering = "DROP TABLE Ordering";
      statement.executeUpdate(dropOrdering);

      String dropOrder = "DROP TABLE Orders";
      statement.executeUpdate(dropOrder);

      String dropBook = "DROP TABLE Book";
      statement.executeUpdate(dropBook);

      String dropCustomer = "DROP TABLE Customer";
      statement.executeUpdate(dropCustomer);

      System.out.println("Table deleted successfully.\n");
      displaySystemInterface();
    } catch (SQLException e) {
      System.out.println("Failed to delete the table.");
      e.printStackTrace();
    }
  }

  private static void insertData() {
    System.out.println("Please enter the folder path");
    String path = sc.nextLine();
    try {
      System.out.print("Processing...");

      // Load the data
      try (BufferedReader br = new BufferedReader(new FileReader(path + "/book.txt"))) {
        String line;
        String sql = "INSERT INTO Book (ISBN, title, unit_price, no_of_copies) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          pstmt.setString(1, data[0]);
          pstmt.setString(2, data[1]);
          pstmt.setInt(3, Integer.parseInt(data[2]));
          pstmt.setInt(4, Integer.parseInt(data[3]));
          pstmt.executeUpdate();
        }
      }

      try (BufferedReader br = new BufferedReader(new FileReader(path + "/customer.txt"))) {
        String line;
        String sql = "INSERT INTO Customer (customer_id, name, shipping_address, credit_card_no) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          pstmt.setString(1, data[0]);
          pstmt.setString(2, data[1]);
          pstmt.setString(3, data[2]);
          pstmt.setString(4, data[3]);
          pstmt.executeUpdate();
        }
      }

      try (BufferedReader br = new BufferedReader(new FileReader(path + "/orders.txt"))) {
        String line;
        String sql = "INSERT INTO Orders (order_id, o_date, shipping_status, charge, customer_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          pstmt.setString(1, data[0]);
          pstmt.setDate(2, Date.valueOf(data[1]));
          pstmt.setString(3, data[2]);
          pstmt.setInt(4, Integer.parseInt(data[3]));
          pstmt.setString(5, data[4]);
          pstmt.executeUpdate();
        }
      }

      try (BufferedReader br = new BufferedReader(new FileReader(path + "/ordering.txt"))) {
        String line;
        String sql = "INSERT INTO Ordering (order_id, ISBN, quantity) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          pstmt.setString(1, data[0]);
          pstmt.setString(2, data[1]);
          pstmt.setInt(3, Integer.parseInt(data[2]));
          pstmt.executeUpdate();
        }
      }

      try (BufferedReader br = new BufferedReader(new FileReader(path + "/book_author.txt"))) {
        String line;
        String sql = "INSERT INTO Book_author (ISBN, author_name) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          pstmt.setString(1, data[0]);
          pstmt.setString(2, data[1]);
          pstmt.executeUpdate();
        }
      }

      System.out.println("Data is loaded!");
      displaySystemInterface();
    } catch (Exception e) {
      System.out.println("Failed to insert data.");
      e.printStackTrace();
    }
  }

  // https://www.javatpoint.com/java-string-to-date
  private static void setSystemDate() {
    System.out.print("Please input the date (YYYYMMDD): ");
    String date = sc.nextLine();
    while (date.length() != 8 || !date.matches("\\d+") || !isLaterDate(date)) {
      System.out.println("Invalid input. Please enter a date later than " + YYYY + "-" + MM + "-" + DD + ".");
      System.out.print("Please input the date (YYYYMMDD): ");
      date = sc.nextLine();
    }
    YYYY = date.substring(0, 4);
    MM = date.substring(4, 6);
    DD = date.substring(6, 8);

    try {
      resultSet = statement.executeQuery("SELECT MAX(o_date) FROM Orders");
      if (resultSet.next()) {
        String latestDate = resultSet.getString(1);
        System.out.println("Latest date in orders: " + latestDate.substring(0, 10));
        System.out.println("Today is " + YYYY + "-" + MM + "-" + DD);
        displaySystemInterface();
      }
    } catch (SQLException e) {
      System.out.println("Failed to get the latest date in orders.");
      e.printStackTrace();
    }
  }

  // Customer Interface
  private static void displayCustomerInterface() {
    System.out.println("<This is the Customer interface.>");
    System.out.println("---------------------------------");
    System.out.println("1. Book Search.");
    System.out.println("2. Order Creation.");
    System.out.println("3. Order Altering.");
    System.out.println("4. Order Query.");
    System.out.println("5. Back to main menu.\n");

    System.out.print("What is your choice??..");
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

    int choice;
    try {
      choice = readPosNum(sc.nextLine(), 4);
      if (choice == 4) {
        displayCustomerInterface();
        return;
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      bookSearch();
      return;
    }

    PreparedStatement ps = null;
    do {
      try {
        ps = switch (choice) {
          case 1:
            System.out.print("Input the ISBN: ");
            selectBookByISBN.setString(1, readISBN(sc.nextLine()));
            yield selectBookByISBN;

          case 2:
            System.out.print("Input the Book Title: ");
            selectBookByTitle.setString(1, readBookTitle(sc.nextLine()));
            yield selectBookByTitle;

          case 3:
            System.out.print("Input the Author Name: ");
            selectBookByAuthor.setString(1, readAuthor(sc.nextLine()));
            yield selectBookByAuthor;

          default:
            throw new IllegalArgumentException("Invalid choice. Please enter a valid option.");
        };
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (SQLException e) {
        System.err.println(
            "if parameterIndex does not correspond to a parameter marker in the SQL statement; if a database access error occurs or this method is called on a closed PreparedStatement");
        // Not user error
        e.printStackTrace();
        bookSearch();
        return;
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
        return;
      }
    } while (ps == null);

    try (var rs = ps.executeQuery()) {
      String curr_book = null;
      int i = 1;
      int j = 1;
      while (rs.next()) {
        if (!rs.getString("isbn").equals(curr_book)) {
          curr_book = rs.getString("isbn");
          System.out.printf("""

              Record %d
              ISBN: %s
              Book Title:%s
              Unit Price:%d
              No of Copies Available:%d
              Authors:
              1 :%s
              """, i++, curr_book, rs.getString("title"), rs.getInt("unit_price"),
              rs.getInt("no_of_copies"), rs.getString("author_name"));
          j = 1;
        } else
          System.out.printf("%d :%s\n", ++j, rs.getString("author_name"));
      }
      System.out.println();
    } catch (SQLException e) {
      System.out.println("Failed to search the book.");
      e.printStackTrace();
      bookSearch();
      return;
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return;
    }

    displayCustomerInterface();
    // switch (choice) {
    // case "1":
    // System.out.print("Input the ISBN: ");
    // String ISBN = sc.nextLine();
    // System.out.println();
    // // search by ISBN
    // try {
    // String sql = "SELECT * FROM Book WHERE ISBN = ?";
    // PreparedStatement pstmt = connection.prepareStatement(sql);
    // pstmt.setString(1, ISBN);
    // resultSet = pstmt.executeQuery();
    // int count = 1;
    // if (resultSet.next()) {
    // System.out.println("Record " + count++);
    // System.out.println("ISBN: " + resultSet.getString("ISBN"));
    // System.out.println("Book Title: " + resultSet.getString("title"));
    // System.out.println("Unit Price: " + resultSet.getInt("unit_price"));
    // System.out.println("No of Available: " + resultSet.getInt("no_of_copies"));
    // // search author name given ISBN
    // try {
    // String sql2 = "SELECT author_name FROM Book_author WHERE ISBN = ? ORDER BY
    // author_name ASC";
    // PreparedStatement pstmt2 = connection.prepareStatement(sql2);
    // pstmt2.setString(1, ISBN);
    // ResultSet resultSet2 = pstmt2.executeQuery();
    // System.out.println("Author Name: ");
    // int authorCount = 1;
    // while (resultSet2.next()) {
    // System.out.println(authorCount++ + ": " +
    // resultSet2.getString("author_name"));
    // }
    // // not sure what this line is doing
    // // System.out.println("Operation not allowed after ResultSet closed");
    // System.out.println();
    // } catch (SQLException e) {
    // System.out.println("Failed to search the author name.");
    // e.printStackTrace();
    // }
    // } else {
    // System.out.println("cannot query the book");
    // }
    // System.out.println();
    // displayCustomerInterface();
    // } catch (SQLException e) {
    // System.out.println("Failed to search the book by ISBN.");
    // e.printStackTrace();
    // }
    // break;
    // case "2":
    // System.out.print("Input the Book Title: ");
    // String bookTitle = sc.nextLine();
    // System.out.println();
    // // search by book title
    // break;
    // case "3":
    // System.out.print("Input the Author Name: ");
    // String authorName = sc.nextLine();
    // System.out.println();
    // // search by author name
    // break;
    // case "4":
    // displayCustomerInterface();
    // break;
    // default:
    // System.out.println("Invalid choice. Please enter a valid option.");
    // bookSearch();
    // break;
    // }
  }

  private static void orderCreation() {
    String customerID;
    while (true) {
      System.out.print("Please enter your customer ID??..");
      customerID = sc.nextLine();
      try {
        selectCustomerByID.setString(1, customerID);
        var rs = selectCustomerByID.executeQuery();
        if (rs.next()) {
          customerID = rs.getString(1);
          break;
        }
        System.out.println("Invalid Customer ID. Please enter a valid Customer ID.");
      } catch (SQLException e) {
        System.out.println("Failed to selectCustomerByID.");
        e.printStackTrace();
        orderCreation();
        return;
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
    }

    System.out.println(">> What books do you want to order??");
    System.out.println(">> Input ISBN then the quantity.");
    System.out.println(">> You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
    System.out.print("Please input the book's ISBN: ");
    var map = new LinkedHashMap<String, Integer>();
    String ISBN;
    while (true) {
      System.out.print("Please input the book's ISBN: ");
      ISBN = sc.nextLine();

      if (ISBN.equals("F") || ISBN.equals("f"))
        break;

      if (ISBN.equals("L") || ISBN.equals("l")) {
        System.out.println("ISBN          Number:");

        for (var entry : map.entrySet())
          System.out.printf("%s   %d\n", entry.getKey(), entry.getValue());
        continue;
      } else if (!isValidISBN(ISBN)) {
        System.out.println("Invalid ISBN. Please enter a valid ISBN.");
        continue;
      }

      if (!isValidISBN(ISBN)) {
        System.out.println("Invalid ISBN. Please enter a valid ISBN.");
        continue;
      }

      int maxQuant;
      try {
        checkCopiesAvailable.setString(1, ISBN);
        var rs = checkCopiesAvailable.executeQuery();
        if (!rs.next()) {
          System.out.println("Cannot find book.");
          continue;
        }

        if ((maxQuant = rs.getInt(1)) < 1) {
          System.out.println("No copies available.");
          continue;
        }
      } catch (SQLException sql) {
        System.out.println("Failed to check the copies available.");
        sql.printStackTrace();
        orderCreation();
        continue;
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }

      System.out.print("Please input the quantity: ");
      int quant = -1;
      do {
        try {
          quant = readPosNum(sc.nextLine(), maxQuant);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          System.out.println("The maximum copies available is " + maxQuant + ".");
          continue;
        }
      } while (quant == -1);

      map.put(ISBN, quant);
    }

    if (map.isEmpty()) {
      System.out.println("No book is ordered.");
      displayCustomerInterface();
      return;
    }

    // Find max order_id
    String nextOrderID = null;
    try (var rs = getMaxOrderID.executeQuery()) {
      nextOrderID = String.format("%08d", rs.next() ? rs.getInt(1) + 1 : 0);
    } catch (SQLException sql) {
      System.out.println("Failed to nextOrderID. " + nextOrderID);
      sql.printStackTrace();
      orderCreation();
      return;
    }

    try {
      insertOrders.setString(1, nextOrderID);
      insertOrders.setDate(2, Date.valueOf(readDate(YYYY + "-" + MM + "-" + DD)));
      insertOrders.setString(3, customerID);
      insertOrders.executeUpdate();

      for (var entry : map.entrySet()) {
        insertOrdering.setString(1, nextOrderID);
        insertOrdering.setString(2, entry.getKey());
        insertOrdering.setInt(3, entry.getValue());
        insertOrdering.executeUpdate();
      }

      // Maye use trigger? it is fine now too, so whatever..
      updateCharge.setString(1, nextOrderID);
      updateCharge.setString(2, nextOrderID);
      updateCharge.executeUpdate();
      displayCustomerInterface();
    } catch (SQLException sql) {
      System.out.println("Failed to insertOrders. " + nextOrderID);
      sql.printStackTrace();
      orderCreation();
      return;
    }
  }

  // 5.2.3 Order Altering
  private static void orderAltering() {
    String orderID = null;
    do {
      try {
        System.out.print("Please enter the orderId you want to change: ");
        var tmpOrderId = readOrderID(sc.nextLine());
        selectOrders.setString(1, tmpOrderId);
        var rs = selectOrders.executeQuery();
        if (rs.next()) {
          System.out.printf("order_id:%s  shipping:%s  charge=%d  customer_id=%s\n",
              orderID = tmpOrderId, rs.getString(1),
              rs.getInt(2), rs.getString(3));
          break;
        }
        System.out.println("Invalid orderID. Please enter a valid orderID.");
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (SQLException e) {
        System.out.println("Failed to selectOrders.");
        e.printStackTrace();
        return;
      }
    } while (orderID == null);

    var books = new ArrayList<Pair<String, Integer>>();
    try {
      var i = 1;
      selectOrdering.setString(1, orderID);
      var rs = selectOrdering.executeQuery();
      while (rs.next()) {
        books.add(new Pair<>(rs.getString(1), rs.getInt(2)));
        System.out.printf("book no: %d ISBN = %s quantity = %d\n",
            i++, rs.getString(1), rs.getInt(2));
      }

      if (i == 1) {
        System.out.println("No book found.");
        displayCustomerInterface();
        return;
      }
    } catch (SQLException e) {
      System.out.println("Failed to selectOrdering.");
      e.printStackTrace();
    }

    int bookNo = -1;
    do {
      System.out.print("Which book do you want to alter (input book no.): ");
      try {
        bookNo = readPosNum(sc.nextLine(), books.size()) - 1;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    } while (bookNo == -1);

    boolean isAdd = false;
    while (true) {
      try {
        System.out.println("input add or remove");
        isAdd = readAddOrRemove(sc.nextLine());
        break;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }

    // get upper limit of copies available if it is add, we can't buy more than
    // available
    int maxQuant = books.get(bookNo).b;
    if (isAdd)
      try {
        checkCopiesAvailable.setString(1, books.get(bookNo).a);
        var rs = checkCopiesAvailable.executeQuery();
        if (!rs.next()) {
          System.out.println("Cannot find book.");
          displayCustomerInterface();
          return;
        }

        if ((maxQuant = rs.getInt(1)) < 1) {
          System.out.println("No copies available.");
          displayCustomerInterface();
          return;
        }
      } catch (SQLException sql) {
        System.out.println("Failed to check the copies available.");
        sql.printStackTrace();
        return;
      }

    int quant = -1;
    do {
      try {
        System.out.print("Input the number: ");
        quant = readPosNum(sc.nextLine(), maxQuant);
        break;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        System.out.println("The maximum copies available is " + maxQuant + ".");
      }
    } while (quant == -1);

    try {
      updateOrdering.setInt(1, books.get(bookNo).b + quant * (isAdd ? 1 : -1));
      updateOrdering.setString(2, orderID);
      updateOrdering.setString(3, books.get(bookNo).a);
      updateOrdering.executeUpdate();

      updateOrders.setDate(1, Date.valueOf(date));
      updateOrders.setString(2, orderID);
      updateOrders.executeUpdate();

      updateCharge.setString(1, orderID);
      updateCharge.setString(2, orderID);
      updateCharge.executeUpdate();

      System.out.println("Update is ok!\nupdate done!!\nupdated charge");

      // Select and display order & ordering info at last
      var rs = selectOrders.executeQuery();

      // Impossible to be false
      if (rs.next()) {
        System.out.printf("order_id:%s  shipping:%s  charge=%d  customer_id=%s\n",
            orderID, rs.getString(1), rs.getInt(2), rs.getString(3));
      }

      var i = 1;
      rs = selectOrdering.executeQuery();
      while (rs.next()) {
        books.add(new Pair<>(rs.getString(1), rs.getInt(2)));
        System.out.printf("book no: %d ISBN = %s quantity = %d\n",
            i++, rs.getString(1), rs.getInt(2));
      }

      // Impossible to be true
      if (i == 1) {
        System.out.println("No book found.");
        displayCustomerInterface();
        return;
      }

    } catch (SQLException sql) {
      System.out.println("Failed to updateOrdering.");
      sql.printStackTrace();
    } catch (Exception e) {
      System.out.println("Unknown Error.");
      e.printStackTrace();
    }
    displayCustomerInterface();
  }

  // 5.2.4 Order Query
  private static void customerOrderQuery() {
    // Input: Customer ID
    String customerID = null;
    do {
      try {
        System.out.print("Please Input Customer ID: ");
        customerID = readCustomerID(sc.nextLine());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    } while (customerID == null);

    // Input: Year
    int year = -1;
    do {
      try {
        System.out.print("Please Input the Year: ");
        year = readPosNum(sc.nextLine(), 9999);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    } while (year == -1);

    // Get orders by c_id and year
    try {
      selectOrdersByCustomerID.setString(1, customerID);
      selectOrdersByCustomerID.setInt(2, year);
      var i = 1;
      var rs = selectOrdersByCustomerID.executeQuery();
      while (rs.next()) {
        System.out.printf("""

            Record : %d
            OrderID : %s
            OrderDate : %s
            charge : %d
            shipping status : %s
            """, i++, rs.getString(1), rs.getDate(2),
            rs.getInt(3), rs.getString(4));
      }

      // if i is 1, no order found
      if (i == 1)
        System.out.println("No order found.");

      displayCustomerInterface();
    } catch (SQLException e) {
      System.out.println("Failed to selectOrdersByCustomerID.");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Bookstore Interface
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
    try {
      // selectOrderShippingStausQuan.setString(1, orderId);
      // var rs = selectOrderShippingStausQuan.executeQuery();
      // if (rs.next()) {
      // String shipping_status = rs.getString(1);
      // int quan = rs.getInt(2);
      // System.out.printf("the Shipping status of %s is %s and %d books are
      // ordered\n", orderId, shipping_status, quan);
      // if (!shipping_status.equals("N")) {
      // System.out.println("The order has been shipped.\n");
      // displayBookstoreInterface();
      // return;
      // }

      // if (quan < 1) {
      // System.out.println("No book is ordered.");
      // } else {
      // System.out.print("Are you sure to update the shipping status? (Yes=Y) ");
      // String choice = sc.nextLine();
      // if (choice.equals("Y") || choice.equals("y")) {
      // updateOrderShippingStatus.setString(1, orderId);
      // updateOrderShippingStatus.executeUpdate();
      // System.out.println("Updated shipping status");
      // } else {
      // System.out.println("The order has not been updated.");
      // }
      // }
      // System.out.println();
      // } else {
      // System.out.println("Order not found.\n");
      // }
      // displayBookstoreInterface();
      selectOrderShippingStausQuan.setString(1, orderId);
      var rs = selectOrderShippingStausQuan.executeQuery();
      if (!rs.next()) {
        System.out.println("Order not found.\n");
        displayBookstoreInterface();
        return;
      }

      String shipping_status = rs.getString(1);
      int quan = rs.getInt(2);
      System.out.printf("the Shipping status of %s is %s and %d books are ordered\n", orderId, shipping_status, quan);
      if (!shipping_status.equals("N")) {
        System.out.println("The order has been shipped.\n");
        displayBookstoreInterface();
        return;
      }

      if (quan < 1) {
        System.out.println("No book is ordered.\n");
        displayBookstoreInterface();
        return;
      }

      System.out.print("Are you sure to update the shipping status? (Yes=Y) ");
      String choice = sc.nextLine();
      if (choice.equals("Y") || choice.equals("y")) {
        updateOrderShippingStatus.setString(1, orderId);
        updateOrderShippingStatus.executeUpdate();
        System.out.println("Updated shipping status\n");
      } else {
        System.out.println("The order has not been updated.\n");
      }
      displayBookstoreInterface();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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

  // Utility functions
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

  private static boolean isValidISBN(String ISBN) {
    // ISBN: 13 chars with format "X-XXXX-XXXX-X", where X is a digit
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

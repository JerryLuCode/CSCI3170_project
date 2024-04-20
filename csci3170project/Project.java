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
  private static String YYYY = "2000";
  private static String MM = "01";
  private static String DD = "01";
  private static boolean dateSet = false;
  private static Scanner sc = new Scanner(System.in);

  public static void main(String[] args) {

    try {
      // Load the Oracle JDBC driver
      Class.forName("oracle.jdbc.OracleDriver");
      // connectionnect to the database
      connection = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
      connection.setAutoCommit(false);

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
    try {
      var rs = selectMaxODate.executeQuery();

      if (rs.next() && dateSet == false) {
        var latestDate = rs.getString(1);
        YYYY = latestDate.substring(0, 4);
        MM = latestDate.substring(5, 7);
        DD = latestDate.substring(8, 10);
      }
    } catch (SQLException e) {
      System.out.println("Failed to get the latest date in orders.");
      // e.printStackTrace();
    }
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
        displaySystemInterface();
        break;
    }
  }

  private static void createTable() {
    try {
      try {
        createBookT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to create the table book.");
      }
      try {
        createCustomerT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to create the table customer.");
      }
      try {
        createOrdersT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to create the table orders.");
      }
      try {
        createOrderingT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to create the table ordering.");
      }
      try {
        createBookAuthorT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to create the table book_author.");
      }
      
      connection.commit();

      System.out.println("Table created successfully.\n");
      displaySystemInterface();
    } catch (SQLException sql) {
      try {
        connection.rollback();
      } catch (SQLException sql2) {
        sql2.printStackTrace();
      }
      System.out.println("Failed to create the table.");
      System.out.println();
      sql.printStackTrace();
      displaySystemInterface();
    }
  }

  private static void deleteTable() {
    try {
      // Drop the table
      try {
        dropBookAuthorT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to delete the table book_author.");
      }
      try {
        dropOrderingT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to delete the table ordering.");
      }
      try {
        dropOrderT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to delete the table orders.");
      }
      try {
        dropBookT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to delete the table book.");
      }
      try {
        dropCustomerT.executeUpdate();
      } catch (SQLException sql) {
        System.out.println("Failed to delete the table customer.");
      }
      connection.commit();

      System.out.println("Table deleted successfully.\n");
      displaySystemInterface();
    } catch (SQLException sql) {
      try {
        connection.rollback();
      } catch (SQLException sql2) {
        // sql2.printStackTrace();
      }
      System.out.println("Failed to delete the table.");
      sql.printStackTrace();
      displaySystemInterface();
    }
  }

  private static void insertData() {
    System.out.println("Please enter the folder path");
    String path = sc.nextLine();
    try {
      System.out.print("Processing...");

      // Load the data
      String line = null;
      try (var br = new BufferedReader(new FileReader(path + "/book.txt"))) {
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          insertBookData.setString(1, data[0]);
          insertBookData.setString(2, data[1]);
          insertBookData.setInt(3, Integer.parseInt(data[2]));
          insertBookData.setInt(4, Integer.parseInt(data[3]));
          insertBookData.executeUpdate();
        }
      } catch (SQLException sql) {
        System.out.println("Failed to insert book data: {" + line + "}");
        throw new SQLException("Failed to insert book_author data: {" + line + "}");
      }

      try (var br = new BufferedReader(new FileReader(path + "/customer.txt"))) {
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          insertBookAuthorData.setString(1, data[0]);
          insertBookAuthorData.setString(2, data[1]);
          insertBookAuthorData.setString(3, data[2]);
          insertBookAuthorData.setString(4, data[3]);
          insertBookAuthorData.executeUpdate();
        }
      } catch (SQLException sql) {
        System.out.println("Failed to insert customer data: {" + line + "}");
        throw new SQLException("Failed to insert book_author data: {" + line + "}");
      }

      try (var br = new BufferedReader(new FileReader(path + "/orders.txt"))) {
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          insertCustomerData.setString(1, data[0]);
          insertCustomerData.setDate(2, Date.valueOf(data[1]));
          insertCustomerData.setString(3, data[2]);
          insertCustomerData.setInt(4, Integer.parseInt(data[3]));
          insertCustomerData.setString(5, data[4]);
          insertCustomerData.executeUpdate();
        }
      } catch (SQLException sql) {
        System.out.println("Failed to insert orders data: {" + line + "}");
        throw new SQLException("Failed to insert book_author data: {" + line + "}");
      }

      try (var br = new BufferedReader(new FileReader(path + "/ordering.txt"))) {
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          insertOrdersData.setString(1, data[0]);
          insertOrdersData.setString(2, data[1]);
          insertOrdersData.setInt(3, Integer.parseInt(data[2]));
          insertOrdersData.executeUpdate();
        }
      } catch (SQLException sql) {
        System.out.println("Failed to insert ordering data: {" + line + "}");
        throw new SQLException("Failed to insert book_author data: {" + line + "}");
      }

      try (var br = new BufferedReader(new FileReader(path + "/book_author.txt"))) {
        while ((line = br.readLine()) != null) {
          String[] data = line.split("\\|");
          insertOrderingData.setString(1, data[0]);
          insertOrderingData.setString(2, data[1]);
          insertOrderingData.executeUpdate();
        }
      } catch (SQLException sql) {
        System.out.println("Failed to insert book_author data: {" + line + "}");
        throw new SQLException("Failed to insert book_author data: {" + line + "}");
      }

      connection.commit();

      System.out.println("Data is loaded!\n");
      displaySystemInterface();
    } catch (SQLException sql) {
      try {
        connection.rollback();
      } catch (SQLException sql2) {
        sql2.printStackTrace();
      }
      System.out.println("Failed to insert data.");
      System.out.println(sql.getMessage());
    } catch (Exception e) {
      try {
        connection.rollback();
      } catch (SQLException sql2) {
        sql2.printStackTrace();
      }
      System.out.println("Failed to insert data.");
      e.printStackTrace();
      displaySystemInterface();
    }
  }

  // https://www.javatpoint.com/java-string-to-date
  private static void setSystemDate() {
    System.out.print("Please input the date (YYYYMMDD): ");
    var date = sc.nextLine();
    while (date.length() != 8 || !date.matches("\\d+") || !isLaterDate(date, YYYY, MM, DD)) {
      System.out.println("Invalid input. Please enter a date later than " + YYYY + "-" + MM + "-" + DD + ".");
      System.out.print("Please input the date (YYYYMMDD): ");
      date = sc.nextLine();
    }
    YYYY = date.substring(0, 4);
    MM = date.substring(4, 6);
    DD = date.substring(6, 8);
    dateSet = true;

    try {
      var rs = selectMaxODate.executeQuery();

      if (rs.next()) {
        var latestDate = rs.getString(1);
        System.out.println("Latest date in orders: " + latestDate.substring(0, 10));
        System.out.println("Today is " + YYYY + "-" + MM + "-" + DD);
        displaySystemInterface();
      }
    } catch (SQLException e) {
      System.out.println("Failed to get the latest date in orders.");
      // e.printStackTrace();
      displaySystemInterface();
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
        displayCustomerInterface();
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
        switch (choice) {
          case 1:
            System.out.print("Input the ISBN: ");
            selectBookByISBN.setString(1, readISBN(sc.nextLine()));
            ps = selectBookByISBN;
            break;

          case 2:
            System.out.print("Input the Book Title: ");
            selectBookByTitle.setString(1, readBookTitle(sc.nextLine()));
            ps = selectBookByTitle;
            break;

          case 3:
            System.out.print("Input the Author Name: ");
            selectBookByAuthor.setString(1, readAuthor(sc.nextLine()));
            ps = selectBookByAuthor;
            break;

          default:
            throw new IllegalArgumentException("Invalid choice. Please enter a valid option.");
        }
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
          System.out.printf(
              "\nRecord %d\nISBN: %s\nBook Title:%s\nUnit Price:%d\nNo of Copies Available:%d\nAuthors:\n1 :%s\n", i++,
              curr_book, rs.getString("title"), rs.getInt("unit_price"),
              rs.getInt("no_of_copies"), rs.getString("author_name"));
          j = 1;
        } else
          System.out.printf("%d :%s\n", ++j, rs.getString("author_name"));
      }
      if (curr_book == null) {
        System.out.println("No book is found.");
      }
      System.out.println();
    } catch (SQLException e) {
      System.out.println("Failed to search the book.");
      e.printStackTrace();
      return;
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    displayCustomerInterface();
  }

  private static void orderCreation() {
    String customerID;
    while (true) {
      System.out.print("Please enter your customerID??");
      customerID = sc.nextLine();
      try {
        selectCustomerByID.setString(1, customerID);
        var rs = selectCustomerByID.executeQuery();
        if (rs.next()) {
          customerID = rs.getString(1);
          break;
        }
        System.out.println("Invalid CustomerID. Please enter a valid CustomerID.");
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
    System.out.println(">> Input ISBN and then the quantity.");
    System.out.println(">> You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
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

      System.out.print("Please input the quantity of the order: ");
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
    System.out.println();

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

      connection.commit();

      displayCustomerInterface();
    } catch (SQLException sql) {
      try {
        connection.rollback();
      } catch (SQLException e) {
        e.printStackTrace();
      }
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
        System.out.print("Please enter the OrderID that you want to change: ");
        var tmpOrderId = readOrderID(sc.nextLine());
        selectOrders.setString(1, tmpOrderId);
        var rs = selectOrders.executeQuery();
        if (rs.next()) {
          System.out.printf("order_id:%s  shipping:%s  charge=%d  customerId=%s\n",
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
      System.out.println("Which book do you want to alter (input book no.): ");
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

      connection.commit();

      System.out.println("Update is ok!\nupdate done!!\nupdated charge");

      // Select and display order & ordering info at last
      var rs = selectOrders.executeQuery();

      // Impossible to be false
      if (rs.next()) {
        System.out.printf("order_id:%s  shipping:%s  charge=%d  customerId=%s\n",
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

      System.out.println();
      displayCustomerInterface();
    } catch (SQLException sql) {
      System.out.println("Failed to updateOrdering.");
      sql.printStackTrace();
    } catch (Exception e) {
      System.out.println("Unknown Error.");
      e.printStackTrace();
    }
    try {
      connection.rollback();
    } catch (SQLException sql2) {
      sql2.printStackTrace();
    }
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
        System.out.printf("\n\nRecord : %d\nOrderID : %s\nOrderDate : %s\ncharge : %d\nshipping status : %s\n", i++,
            rs.getString(1), rs.getDate(2),
            rs.getInt(3), rs.getString(4));
      }

      // if i is 1, no order found
      if (i == 1)
        System.out.println("No order found.");

      System.out.println();
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

    System.out.print("What is your choice??..");
    String choice = sc.nextLine();

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
        displayBookstoreInterface();
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

    try {
      selectOrderShippingStausQuan.setString(1, orderId);
      var rs = selectOrderShippingStausQuan.executeQuery();
      if (!rs.next()) {
        System.out.println("Order not found.\n");
        displayBookstoreInterface();
        return;
      }

      String shipping_status = rs.getString(1);
      int quan = rs.getInt(2);
      System.out.printf("the Shipping status of %s is %s and %d books ordered\n", orderId, shipping_status, quan);
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
        connection.commit();
        System.out.println("Updated shipping status\n");
      } else {
        System.out.println("The order has not been updated.\n");
      }

      displayBookstoreInterface();
    } catch (SQLException sql) {
      try {
        connection.rollback();
      } catch (SQLException sql2) {
        sql2.printStackTrace();
      }
      sql.printStackTrace();
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

    try {
      var i = 1;
      var charge = 0;
      selectOrdersByMonth.setString(1, yyyymm.substring(0, 4));
      selectOrdersByMonth.setString(2, yyyymm.substring(5, 7));
      var rs = selectOrdersByMonth.executeQuery();
      while (rs.next()) {
        System.out.printf("\n\nRecord : %d\norder_id : %s\ncustomer_id : %s\ndate : %s\ncharge : %d\n", i++,
            rs.getString(1), rs.getString(2),
            rs.getDate(3), rs.getInt(4));
        charge += rs.getInt(4);
      }

      System.out.println("\n\nTotal charges of the month is " + charge);
      System.out.println();
      displayBookstoreInterface();
    } catch (SQLException sql) {
      sql.printStackTrace();
    }
  }

  private static void nMostPopularBookQuery() {
    System.out.print("Please input the N popular books number: ");
    String N = sc.nextLine();
    while (!N.matches("\\d+")) {
      System.out.println("Invalid input. Please enter a valid integer.");
      System.out.print("Please input N: ");
      N = sc.nextLine();
    }
    try {
      var check = selectNoOfBooks.executeQuery();
      ResultSet rs = null;
      check.next();
      int no_book = check.getInt(1);
      if (Integer.parseInt(N) > no_book) {
        rs = selectAllBook.executeQuery();
      } else {
        selectNMostPopularBook.setString(1, N);
        rs = selectNMostPopularBook.executeQuery();
      }
      System.out.println("ISBN            Title             copies");
      while (rs.next()) {
        System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3));
      }
      System.out.println();
      displayBookstoreInterface();
    } catch (SQLException sql) {
      sql.printStackTrace();
    }
  }
}

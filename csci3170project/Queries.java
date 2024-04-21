package csci3170project;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Queries {
  static PreparedStatement

  // 5.1.1 Create Table
  createBookT = null, createBookAuthorT = null, createCustomerT = null, createOrdersT = null, createOrderingT = null,

      // 5.1.2 Delete Table
      dropBookAuthorT = null, dropOrderingT = null, dropOrderT = null, dropBookT = null, dropCustomerT = null,

      // 5.1.3 Insert Data
      insertBookData = null, insertBookAuthorData = null, insertCustomerData = null, insertOrdersData = null,
      insertOrderingData = null,

      // 5.1.4 Set System Date
      selectMaxODate = null,

      // 5.2.1 Book Search
      selectBookByISBN = null,
      selectBookByTitle = null,
      selectBookByAuthor = null,

      // 5.2.2 Order Creation
      selectCustomerByID = null,
      checkCopiesAvailable = null,
      getMaxOrderID = null,
      insertOrders = null,
      insertOrdering = null,
      updateCharge = null,

      // 5.2.3 Order Altering
      selectOrders = null,
      selectOrdering = null,
      updateOrders = null,
      updateOrdering = null,
      updateNoOfCopies = null,

      // 5.2.4 Order Query
      selectOrdersByCustomerID = null,

      // 5.3.1 Order Update
      selectOrderShippingStausQuan = null,
      updateOrderShippingStatus = null,

      // 5.3.2 Order Query
      selectOrdersByMonth = null,

      // 5.3.3 N Most Popular Book Query
      selectNoOfBooks = null,
      selectAllBook = null,
      selectNMostPopularBook = null;

  public static void main(String[] args) {

    try {
      Class.forName("oracle.jdbc.OracleDriver");
      var conn = DriverManager.getConnection("jdbc:oracle:thin://@db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk",
          "h052", "fromaTwa");
      initQueries(conn);

      // TODO:: Unit Test
      selectBookByISBN.setString(1, "1-1234-1234-2"); // Not Found
      selectBookByISBN.setString(1, "1-1234-1234-1"); // Found

    } catch (ClassNotFoundException e) {
      System.err.println("Failed to load Oracle JDBC driver.");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("Failed to connection to the database.");
      e.printStackTrace();
    }
  }

  static void initQueries(Connection conn) throws SQLException {

    // 5.1.1 Create Table
    createBookT = conn.prepareStatement(
        "CREATE TABLE Book ( ISBN CHAR(13) PRIMARY KEY, title VARCHAR(100) NOT NULL, unit_price INT CHECK (unit_price >= 0), no_of_copies INT CHECK (no_of_copies >= 0) ) ");

    createCustomerT = conn.prepareStatement(
        "CREATE TABLE Customer ( customer_id VARCHAR(10) PRIMARY KEY, name VARCHAR(50) NOT NULL, shipping_address VARCHAR(200) NOT NULL, credit_card_no CHAR(19) ) ");

    createOrdersT = conn.prepareStatement(
        "CREATE TABLE Orders ( order_id CHAR(8) PRIMARY KEY, o_date DATE NOT NULL, shipping_status CHAR(1) CHECK (shipping_status IN ('Y', 'N')), charge INT CHECK (charge >= 0), customer_id VARCHAR(10) NOT NULL, FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ) ");

    createOrderingT = conn.prepareStatement(
        "CREATE TABLE Ordering ( order_id CHAR(8), ISBN CHAR(13), quantity INT CHECK (quantity >= 0), FOREIGN KEY (order_id) REFERENCES Orders(order_id), FOREIGN KEY (ISBN) REFERENCES Book(ISBN), PRIMARY KEY (order_id, ISBN) ) ");

    createBookAuthorT = conn.prepareStatement(
        "CREATE TABLE Book_author ( ISBN CHAR(13), author_name VARCHAR(50) NOT NULL, FOREIGN KEY (ISBN) REFERENCES Book(ISBN), PRIMARY KEY (ISBN, author_name) ) ");

    // 5.1.2 Delete Table
    dropBookAuthorT = conn.prepareStatement("DROP TABLE Book_author");
    dropOrderingT = conn.prepareStatement("DROP TABLE Ordering");
    dropOrderT = conn.prepareStatement("DROP TABLE Orders");
    dropBookT = conn.prepareStatement("DROP TABLE Book");
    dropCustomerT = conn.prepareStatement("DROP TABLE Customer");

    // 5.1.3 Insert Data
    insertBookData = conn.prepareStatement(
        "INSERT INTO Book (ISBN, title, unit_price, no_of_copies) VALUES (?, ?, ?, ?)");
    insertBookAuthorData = conn.prepareStatement(
        "INSERT INTO Customer (customer_id, name, shipping_address, credit_card_no) VALUES (?, ?, ?, ?)");
    insertCustomerData = conn.prepareStatement(
        "INSERT INTO Orders (order_id, o_date, shipping_status, charge, customer_id) VALUES (?, ?, ?, ?, ?)");
    insertOrdersData = conn.prepareStatement(
        "INSERT INTO Ordering (order_id, ISBN, quantity) VALUES (?, ?, ?)");
    insertOrderingData = conn.prepareStatement(
        "INSERT INTO Book_author (ISBN, author_name) VALUES (?, ?)");

    // 5.1.4 Set System Date
    selectMaxODate = conn.prepareStatement("SELECT MAX(o_date) FROM Orders");

    // 5.2 Customer Interface
    /*
     * 5.2.1 Book Search
     * a. Query by ISBN
     * Input: ISBN
     * 
     * b. Query by Book Title
     * Input: Book Title
     * (Partial book title is supported. Both the wild cards "%" and "_" should be
     * supported. In the results, exact matches (if any) should be displayed first.
     * E.g. if the input is "ABC%", then "ABC Book" may be the output. The meaning
     * of exact matches is illustrated as follows. If the input is "%ABC%", "%ABC"
     * or "ABC%", then the exact match of this input is "ABC". For simplicity, we
     * assume that there is no exact match if the wild card "%" occursin the middle
     * of the input (e.g. "A%BC", "%A%BC" and "%A%BC%"). )
     * 
     * c. Query by Author Name
     * Input: Author Name
     * (Partial author name is supported. The description is similar as above. Note
     * that the above input refers to a single author. This author is not restricted
     * to the first author of the book. For example, this author may be the first
     * author. He/she may be the second author. )
     * 
     * For each of the above query, the output and the order are described as
     * follows.
     * 
     * Output: "Book Title", "ISBN", "Unit Price", "No of Copies Available" and "A
     * List of Authors"
     * 
     * Order: Results should be sorted in ascending order by "Book Title" and then
     * in
     * ascending order by "ISBN". In each book, the author should be sorted in
     * ascending order by "Author Name".
     */
    selectBookByISBN = conn.prepareStatement(
        "SELECT title, book.isbn, unit_price, no_of_copies, author_name FROM book, book_author WHERE book.isbn = ? and book.isbn = book_author.isbn ORDER BY title, book.isbn, author_name ");
    selectBookByTitle = conn.prepareStatement(
        "SELECT title, book.isbn, unit_price, no_of_copies, author_name FROM book, book_author WHERE title LIKE ? and book.isbn = book_author.isbn ORDER BY title, book.isbn, author_name ");
    selectBookByAuthor = conn.prepareStatement(
        "SELECT title, book.isbn, unit_price, no_of_copies, author_name FROM book, book_author WHERE book.isbn IN ( SELECT isbn FROM book_author WHERE author_name LIKE ? ) and book.isbn = book_author.isbn ORDER BY title, book.isbn, author_name ");

    /*
     * 5.2.2. Order Creation
     * to create a new order
     * 
     * Input: Customer ID, Books to be Ordered (i.e. a list of books with quantities
     * to be ordered)
     * 
     * Action: check if the book copies are available ("No of Copies Available"
     * greater
     * than or equal to the requested number). Then create an order and insert it
     * into
     * the database. "Order ID" to be assigned is equal to one plus the current
     * greatest
     * Order ID stored in the database. "Order Date" to be assigned is equal to the
     * system date. "Shipping Status" is initialized to "N".
     * 
     * (NOTE: "Order ID", "Order Date", "Charge" and "Shipping Status" are assigned
     * automatically. This action should create an order with at least one book to
     * be
     * ordered with quantity at least 1.)
     */

    selectCustomerByID = conn.prepareStatement("SELECT customer_id FROM customer WHERE customer_id = ? ");
    // just return no_of_copies
    // checkCopiesAvailable = conn.prepareStatement(" // SELECT no_of_copies // FROM
    // book // WHERE isbn = ? // ");

    // return no_of_copies - sum of all ordering's quantity
    checkCopiesAvailable = conn.prepareStatement(
        "SELECT no_of_copies FROM book WHERE book.isbn = ? GROUP BY book.isbn, no_of_copies");

    getMaxOrderID = conn.prepareStatement("SELECT MAX(order_id) FROM orders ");

    insertOrders = conn.prepareStatement(
        "INSERT INTO orders (order_id, o_date, shipping_status, customer_id) VALUES (?, ?, 'N', ?) ");

    insertOrdering = conn.prepareStatement("INSERT INTO ordering (order_id, isbn, quantity) VALUES (?, ?, ?) ");

    updateCharge = conn.prepareStatement(
        "UPDATE orders SET charge = (SELECT SUM(total) +(CASE WHEN SUM(total) > 0 THEN 10 ELSE 0 END) AS TOTAL FROM (SELECT order_id, SUM(unit_price + 10)*quantity AS total FROM book INNER JOIN ordering ON book.isbn = ordering.isbn WHERE order_id = ? GROUP BY order_id, quantity) GROUP BY order_id) WHERE order_id = ?");

    /*
     * 5.2.3. Order Altering
     * to add or drop copies of books to or from an order
     * 
     * Input: Order ID
     * 
     * Action: After Order ID is inputted, a list of books ordered in the order will
     * be
     * displayed. Allow the user to select one of them. Also, allow the user to
     * choose
     * one of the following actions.
     * 
     * a. Add Copies of Book
     * Input: No of Copies to be Added
     * 
     * Action: to add one or more copied (specified by the user) of a book in
     * the current order. If "Shipping Status" is "N" and the no of copies to be
     * added is smaller than or equal to the "No of Copies Available" of the book,
     * then this operation is successful, the "Quantity" in the order is incremented
     * by the requested amount and the "No of Copies Available" is decremented
     * by the requested amount. Otherwise, return the correspondence error
     * message such as "The books in the order are shipped" or other relevant
     * messages.
     * 
     * b. Remove Copies of Book
     * Input: No of Copies to be Removed
     * 
     * Action: to remove one or more copies (specified by the user) of a book in the
     * current
     * order. If "Shipping Status" is "N", then this operation is successful, the
     * "Quantity"
     * is decremented by the request amount and the "No of Copies Available" is
     * incremented
     * by the request amount. Otherwise, return the correspondence error message
     * such as
     * "The books in the order are shipped" or other relevant messages. (NOTE: Even
     * though
     * "Quantity" is decremented to 0, we do not need to remove the record of book
     * ordered,
     * which can allow the customer to add copies of the book later.)
     * 
     * Each of the above operations should update "Order Date" to be the current
     * system date. (NOTE: Please remember to update "Charge" in the order. In this
     * project, we assume the user will not add or delete any book in the order.)
     */

    selectOrders = conn
        .prepareStatement("SELECT shipping_status, charge, customer_id FROM orders WHERE order_id = ? ");
    selectOrdering = conn.prepareStatement("SELECT isbn, quantity FROM ordering WHERE order_id = ? ");
    updateOrders = conn.prepareStatement("UPDATE orders SET o_date = ? WHERE order_id = ? ");
    updateOrdering = conn.prepareStatement("UPDATE ordering SET quantity = ? WHERE order_id = ? and isbn = ? ");
    updateNoOfCopies = conn.prepareStatement("UPDATE book SET no_of_copies = ? WHERE isbn = ? ");

    /*
     * 5.2.4. Order Query
     * to query a list of orders made by a customer in a particular year.
     * 
     * Input: Customer ID, Year
     * 
     * Output: "Order ID", "Order Date", "Books Ordered", "Charge" and
     * "Shipping Status"
     * 
     * Order: Results should be sorted in ascending order by "Order ID".
     */

    // Note: EXTRACT()!? first time hear...
    selectOrdersByCustomerID = conn.prepareStatement(
        "SELECT order_id, o_date, charge, shipping_status FROM orders WHERE customer_id = ? and EXTRACT(YEAR FROM o_date) = ? ORDER BY order_id ");

    // 5.3.1. Order Update
    selectOrderShippingStausQuan = conn.prepareStatement(
        "SELECT shipping_status, sum(quantity) FROM orders, ordering WHERE orders.order_id = ? and orders.order_id = ordering.order_id GROUP BY orders.order_id, shipping_status ");

    updateOrderShippingStatus = conn.prepareStatement("UPDATE orders SET shipping_status = 'Y' WHERE order_id = ? ");

    // 5.3.2. Order Query
    selectOrdersByMonth = conn.prepareStatement(
        "SELECT order_id, customer_id, o_date, charge FROM orders WHERE EXTRACT(YEAR FROM o_date) = ? and EXTRACT(MONTH FROM o_date) = ? ORDER BY order_id ");

    // 5.3.3 N Most Popular Book Query
    selectNoOfBooks = conn.prepareStatement(
        "SELECT COUNT(*) FROM ( SELECT O.ISBN, B.TITLE, SUM(O.QUANTITY) AS NO_OF_COPIES FROM ORDERING O JOIN BOOK B ON O.ISBN = B.ISBN GROUP BY O.ISBN, B.TITLE ORDER BY ISBN DESC ) ");

    selectAllBook = conn.prepareStatement(
        "SELECT ISBN, TITLE, NO_OF_COPIES FROM ( SELECT O.ISBN, B.TITLE, SUM(O.QUANTITY) AS NO_OF_COPIES FROM ORDERING O JOIN BOOK B ON O.ISBN = B.ISBN GROUP BY O.ISBN, B.TITLE ORDER BY ISBN DESC ) WHERE NO_OF_COPIES > 0 ORDER BY NO_OF_COPIES DESC ");

    selectNMostPopularBook = conn.prepareStatement(
        "SELECT B2.ISBN, B2.TITLE, B2.NO_OF_COPIES FROM ( SELECT A.*, ROW_NUMBER() OVER (ORDER BY A.NO_OF_COPIES DESC) AS ROW_NUM FROM ( SELECT O.ISBN, B.TITLE, SUM(O.QUANTITY) AS NO_OF_COPIES FROM ORDERING O JOIN BOOK B ON O.ISBN = B.ISBN GROUP BY O.ISBN, B.TITLE ORDER BY ISBN DESC ) A )B2, ( SELECT N.NO_OF_COPIES FROM ( SELECT A.*, ROW_NUMBER() OVER (ORDER BY A.NO_OF_COPIES DESC) AS ROW_NUM FROM ( SELECT O.ISBN, B.TITLE, SUM(O.QUANTITY) AS NO_OF_COPIES FROM ORDERING O JOIN BOOK B ON O.ISBN = B.ISBN GROUP BY O.ISBN, B.TITLE ORDER BY ISBN DESC ) A ) N WHERE N.ROW_NUM = ? ) N2 WHERE B2.NO_OF_COPIES >= N2.NO_OF_COPIES AND B2.NO_OF_COPIES > 0 ORDER BY B2.NO_OF_COPIES DESC ");
  }
}


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Queries {
    static PreparedStatement
    selectBookByISBN = null,
    selectBookByTitle = null,
    selectBookByAuthor = null,

    checkCopiesAvailable = null,
    insertOrders = null,
    insertOrdering = null,

    selectOrders = null,
    selectOrdering = null,
    updateOrders = null,
    updateOrdering = null,

    selectOrdersByCustomerID = null
    ;

    public static void main(String[] args) {

      try {
          Class.forName("oracle.jdbc.OracleDriver");
          var conn = DriverManager.getConnection("jdbc:oracle:thin://@db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk", "h052", "fromaTwa");
          init(conn);
          
          // TODO:: Unit Test


      } catch (ClassNotFoundException e) {
          System.err.println("Failed to load Oracle JDBC driver.");
          e.printStackTrace();
      } catch (SQLException e) {
          System.out.println("Failed to connection to the database.");
          e.printStackTrace();
      }
  }

    static void init(Connection conn) throws SQLException {

// 5.2 Customer Interface
/*
  5.2.1 Book Search
  a. Query by ISBN
  Input: ISBN

  b. Query by Book Title
  Input: Book Title
  (Partial book title is supported. Both the wild cards "%" and "_" should be
  supported. In the results, exact matches (if any) should be displayed first.
  E.g. if the input is "ABC%", then "ABC Book" may be the output. The meaning
  of exact matches is illustrated as follows. If the input is "%ABC%", "%ABC"
  or "ABC%", then the exact match of this input is "ABC". For simplicity, we
  assume that there is no exact match if the wild card "%" occursin the middle
  of the input (e.g. "A%BC", "%A%BC" and "%A%BC%"). )

  c. Query by Author Name
  Input: Author Name
  (Partial author name is supported. The description is similar as above. Note
  that the above input refers to a single author. This author is not restricted
  to the first author of the book. For example, this author may be the first
  author. He/she may be the second author. )

For each of the above query, the output and the order are described as follows.

Output: "Book Title", "ISBN", "Unit Price", "No of Copies Available" and "A
List of Authors"

Order: Results should be sorted in ascending order by "Book Title" and then in
ascending order by "ISBN". In each book, the author should be sorted in
ascending order by "Author Name".
*/
        selectBookByISBN = conn.prepareStatement("""
          SELECT title, isbn, unit_price, no_of_copies, author
            FROM books, book_author
            WHERE isbn = ? and books.isbn = book_author.isbn
            ORDER BY title, isbn, author_name
          """);
        selectBookByTitle = conn.prepareStatement("""
          SELECT title, isbn, unit_price, no_of_copies, author
            FROM books, book_author
            WHERE title = ? and books.isbn = book_author.isbn
            ORDER BY title, isbn, author_name
          """);
        selectBookByAuthor = conn.prepareStatement("""
          SELECT title, isbn, unit_price, no_of_copies, author
            FROM books, book_author
            WHERE author_name = ? and books.isbn = book_author.isbn
            ORDER BY title, isbn, author_name
          """);

        /*
5.2.2. Order Creation
  to create a new order

  Input: Customer ID, Books to be Ordered (i.e. a list of books with quantities to be ordered)

  Action: check if the book copies are available ("No of Copies Available" greater
  than or equal to the requested number). Then create an order and insert it into
  the database. "Order ID" to be assigned is equal to one plus the current greatest
  Order ID stored in the database. "Order Date" to be assigned is equal to the
  system date. "Shipping Status" is initialized to "N".

  (NOTE: "Order ID", "Order Date", "Charge" and "Shipping Status" are assigned
  automatically. This action should create an order with at least one book to be
  ordered with quantity at least 1.)
*/
        checkCopiesAvailable = conn.prepareStatement("""
          SELECT no_of_copies
            FROM books
            WHERE isbn = ?
          """);
          
        insertOrders = conn.prepareStatement("""
          INSERT INTO orders (order_id, o_date, shipping_status, charge, customer_id)
            VALUES (?, ?, "N", ?, ?)
          """);

        insertOrdering = conn.prepareStatement("""
          INSERT INTO orders (order_id, isbn, quantity)
            VALUES (?, ?, ?)
          """);
      
/*
5.2.3. Order Altering
to add or drop copies of books to or from an order

Input: Order ID

Action: After Order ID is inputted, a list of books ordered in the order will be
displayed. Allow the user to select one of them. Also, allow the user to choose
one of the following actions.

  a. Add Copies of Book
  Input: No of Copies to be Added

  Action: to add one or more copied (specified by the user) of a book in
  the current order. If "Shipping Status" is "N" and the no of copies to be
  added is smaller than or equal to the "No of Copies Available" of the book,
  then this operation is successful, the "Quantity" in the order is incremented
  by the requested amount and the "No of Copies Available" is decremented
  by the requested amount. Otherwise, return the correspondence error
  message such as "The books in the order are shipped" or other relevant
  messages.

  b. Remove Copies of Book
  Input: No of Copies to be Removed

  Action: to remove one or more copies (specified by the user) of a book in the current
  order. If "Shipping Status" is "N", then this operation is successful, the "Quantity" 
  is decremented by the request amount and the "No of Copies Available" is incremented 
  by the request amount. Otherwise, return the correspondence error message such as 
  "The books in the order are shipped" or other relevant messages. (NOTE: Even though
  "Quantity" is decremented to 0, we do not need to remove the record of book ordered,
  which can allow the customer to add copies of the book later.)

Each of the above operations should update "Order Date" to be the current
system date. (NOTE: Please remember to update "Charge" in the order. In this
project, we assume the user will not add or delete any book in the order.)
         */
        
         selectOrders = conn.prepareStatement("""
          SELECT shipping_status, charge, customer_id
            FROM orders
            WHERE order_id = ?
          """);
          selectOrdering = conn.prepareStatement("""
           SELECT isbn, quantity
             FROM ordering
             WHERE order_id = ?
           """);
        updateOrders = conn.prepareStatement("""
          UPDATE orders 
            SET o_date = ?
            WHERE order_id = ?
          """);
        updateOrdering = conn.prepareStatement("""
          UPDATE ordering 
            SET no_of_copies = ?
            WHERE order_id = ?
          """);
        
/*
5.2.4. Order Query
to query a list of orders made by a customer in a particular year.

  Input: Customer ID, Year

  Output: "Order ID", "Order Date", "Books Ordered", "Charge" and "Shipping Status"

  Order: Results should be sorted in ascending order by "Order ID".
*/

// Note: EXTRACT()!? first time hear...
selectOrdersByCustomerID = conn.prepareStatement("""
  SELECT order_id, o_date, charge, shipping_status
    FROM orders
    WHERE customer_id = ? and EXTRACT(YEAR FROM o_date) = ?
    ORDER BY order_id
  """);
    }


}

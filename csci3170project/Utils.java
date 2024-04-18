package csci3170project;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Utils {

  // Unit Test
  public static void main(String args[]) {
    try {
      System.out.println(readISBN(""));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readISBN("1"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readISBN("1-1234-1234-1"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readBookTitle(
          "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readBookTitle("Correct Book Title"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readBookTitle("Correct Book Title 2 % _"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readAuthor("123456789012345678901234567890123456789012345678901234567890"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readAuthor("Corrent Author"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readCustomerID("12345678901"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readCustomerID("CorrectID"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readCustomerName("123456789012345678901234567890123456789012345678901234567890"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readCustomerName("Corrent Customer Nume"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readShippingAddress("A, B, C, D"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readCreditCardNo("1"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readCreditCardNo("1234-1234-1234-1234"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readOrderID("12345678901"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readOrderID("abc"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readOrderID("123"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readDate("10241024"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readDate("1248-16-32"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readPosNum("1", 2));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      System.out.println(readPosNum("a", 2));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  // Read Functions
  // Book-related
  public static String readISBN(String str) throws IllegalArgumentException {
    if (Pattern.matches("^\\d-(\\d{4}-){2}\\d$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid ISBN Format: " + str);
  }

  public static String readBookTitle(String str) throws IllegalArgumentException {
    if (Pattern.matches("^[\\w%_ ]{1,100}$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Book Title Format: " + str);
  }

  public static String readAuthor(String str) throws IllegalArgumentException {
    if (Pattern.matches("^[\\w ]{1,50}$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Author Format: " + str);
  }

  // Customer-related
  public static String readCustomerID(String str) throws IllegalArgumentException {
    if (Pattern.matches("^\\w{1,10}$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Customer ID Format: " + str);
  }

  public static String readCustomerName(String str) throws IllegalArgumentException {
    if (Pattern.matches("^[\\w ]{1,50}$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Customer Name Format: " + str);
  }

  public static String readShippingAddress(String str) throws IllegalArgumentException {
    if (Pattern.matches("^[\\w, ]{1,200}$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Shipping Address Format: " + str);
  }

  public static String readCreditCardNo(String str) throws IllegalArgumentException {
    if (Pattern.matches("^(\\d{4}-){3}\\d{4}$", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Credit Card No. Format: " + str);
  }

  // Order-related
  public static String readOrderID(String str) throws IllegalArgumentException {
    if (Pattern.matches("^\\d{1,8}", str))
      return str;
    else
      throw new IllegalArgumentException("Invalid Order ID Format: " + str);
  }

  // Date-related
  public static LocalDate readDate(String str) throws IllegalArgumentException {
    try {
      return LocalDate.parse(str);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid Date Format: " + str);
    }
  }

  // General
  public static int readPosNum(String str, int max) {
    try {
      int i = Integer.parseInt(str);
      if (i > max)
        throw new IllegalArgumentException("Invalid Option: " + str);
      return i;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid Number Format: " + str);
    }
  }

  public static boolean readAddOrRemove(String str) {
    try {
      return switch (str.toLowerCase().charAt(0)) {
        case 'a':
          yield true;
        case 'r':
          yield false;
        default:
          throw new IllegalArgumentException("Invalid Option: " + str);
      };
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid Number Format: " + str);
    }
  }

  public static class Pair<T, U> {
    public T a;
    public U b;

    public Pair(T a, U b) {
      this.a = a;
      this.b = b;
    }
  }
}

package oracle.common;

/**
 * Represents an exception specific to Oracle.
 * This exception is thrown when an error occurs during command execution or task processing.
 */
public class OracleException extends Exception {
  /**
   * Constructs an OracleException with a specified error message.
   *
   * @param message The detail message describing the cause of the exception.
   */
  public OracleException(String message) {
    super(message);
  }
}
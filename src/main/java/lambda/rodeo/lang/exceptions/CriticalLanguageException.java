package lambda.rodeo.lang.exceptions;

public class CriticalLanguageException extends RuntimeException {

  public CriticalLanguageException() {
  }

  public CriticalLanguageException(String message) {
    super(message);
  }

  public CriticalLanguageException(String message, Throwable cause) {
    super(message, cause);
  }

  public CriticalLanguageException(Throwable cause) {
    super(cause);
  }

  public CriticalLanguageException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

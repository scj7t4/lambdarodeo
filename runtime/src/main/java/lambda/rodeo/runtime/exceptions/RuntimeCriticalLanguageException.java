package lambda.rodeo.runtime.exceptions;

public class RuntimeCriticalLanguageException extends RuntimeException {

  public RuntimeCriticalLanguageException() {
  }

  public RuntimeCriticalLanguageException(String message) {
    super(message);
  }

  public RuntimeCriticalLanguageException(String message, Throwable cause) {
    super(message, cause);
  }

  public RuntimeCriticalLanguageException(Throwable cause) {
    super(cause);
  }

  public RuntimeCriticalLanguageException(String message, Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

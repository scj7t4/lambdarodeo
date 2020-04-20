package lambda.rodeo.lang.exception;

public class TypeException extends RuntimeException {

  public TypeException() {
  }

  public TypeException(String s) {
    super(s);
  }

  public TypeException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public TypeException(Throwable throwable) {
    super(throwable);
  }
}

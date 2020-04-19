package lambda.rodeo.lang.types;

public class IntType implements Type {

  public static final IntType INSTANCE = new IntType();

  private IntType() {
  }

  @Override
  public String toString() {
    return "int";
  }
}

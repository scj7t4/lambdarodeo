package lambda.rodeo.lang.asmmodels;

import java.util.function.Supplier;
import lambda.rodeo.lang.types.Atom;

public class BasicModule1 {
  private BasicModule1() {}

  public Supplier<Object> someFunc() {
    return () -> Atom.NULL;
  }
}

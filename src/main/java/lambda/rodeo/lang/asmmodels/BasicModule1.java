package lambda.rodeo.lang.asmmodels;

import java.util.function.Supplier;
import lambda.rodeo.lang.functions.Result;
import lambda.rodeo.lang.types.Atom;

public class BasicModule1 {
  private BasicModule1() {}

  public static Result someFunc() {
    return Atom.NULL;
  }
}

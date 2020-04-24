package lambda.rodeo.lang.asmmodels;

import lambda.rodeo.lang.functions.Result;
import lambda.rodeo.lang.types.Atom;

public class BasicModule1 {

  private BasicModule1() {
  }

  public static Result someFunc(Atom toot) {
    return toot;
  }
}

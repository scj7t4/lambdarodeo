package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.lang.functions.Result;
import lambda.rodeo.lang.types.Atom;

public class BasicModule1 {

  private BasicModule1() {
  }

  public static BigInteger someFunc(BigInteger a, BigInteger b) {
    return a;
  }
}

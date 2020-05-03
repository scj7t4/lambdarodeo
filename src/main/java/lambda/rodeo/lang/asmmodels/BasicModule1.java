package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.lang.types.Atom;

public class BasicModule1 {

  private BasicModule1() {
  }

  public static BigInteger func2(Atom a1, BigInteger a2) {
    return new BigInteger("2");
  }


  public static BigInteger someFunc() {
    BigInteger cheetos = func2(Atom.NULL, BigInteger.ONE);
    return cheetos;
  }
}

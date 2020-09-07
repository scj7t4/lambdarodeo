package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;

public class BasicModule1 {
  public static String fibonacci(BigInteger y, BigInteger x) {
    return y + "" + BigInteger.valueOf(2L);
  }
}

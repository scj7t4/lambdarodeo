package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.lang.types.Atom;

public class BasicModule1 {

  private BasicModule1() {
  }

  public static BigInteger twoptwo() {
    return new BigInteger("2").add(new BigInteger("2"));
  }


  public static BigInteger callAndAdd() {
    return new BigInteger("2").add(twoptwo());
  }
}

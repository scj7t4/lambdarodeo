package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lambda.rodeo.runtime.types.Atom;

public class BasicModule1 {

  public static BigInteger testFn(Atom arg1, BigInteger arg2) {
    if(new AtomMatcher(new Atom("ok")).matches(arg1)
        && new IntMatcher(new BigInteger("69420")).matches(arg2)) {
      return BigInteger.ONE;
    }
    return BigInteger.ZERO;
  }
}

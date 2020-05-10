package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lambda.rodeo.runtime.types.Atom;

public class BasicModule1 {

  public static final IntMatcher INT_MATCHER = new IntMatcher(new BigInteger("1"));
  public static final IntMatcher INT_MATCHER1 = new IntMatcher(new BigInteger("0"));

  public static BigInteger fibonacci() {
    return multiArgFn(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3));
  }

  public static BigInteger multiArgFn(BigInteger v1, BigInteger v2, BigInteger v3) {
    return v1;
  }
}

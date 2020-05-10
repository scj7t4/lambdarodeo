package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lambda.rodeo.runtime.types.Atom;

public class BasicModule1 {

  public static BigInteger someFunc(BigInteger arg1) {
    if (new IntMatcher(new BigInteger("1")).matches(arg1)) {
      return new BigInteger("1");
    }
    if (new IntMatcher(new BigInteger("0")).matches(arg1)) {
      return new BigInteger("0");
    }
    throw new RuntimeCriticalLanguageException("Pattern didn't match blah blah");
  }
}

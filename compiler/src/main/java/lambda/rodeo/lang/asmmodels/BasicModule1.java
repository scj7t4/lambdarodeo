package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lambda.rodeo.runtime.types.Atom;

public class BasicModule1 {

  public static BigInteger testFn(Atom arg1, BigInteger arg2) {
    throw new RuntimeCriticalLanguageException("No pattern cases matched");
  }
}

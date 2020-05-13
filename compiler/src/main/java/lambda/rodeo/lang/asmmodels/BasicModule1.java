package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.function.Supplier;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lambda.rodeo.runtime.types.Atom;

public class BasicModule1 {
  public static Supplier<String> fibonacci(String y) {
    return () -> y;
  }
}

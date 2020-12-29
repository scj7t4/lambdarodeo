package lambda.rodeo.runtime.lambda;

import java.util.function.Function;

public interface Lambda1<T, T2> extends Function<T, T2>, Axiom<Lambda1<T, T2>> {

  default Lambda1<T, T2> apply() {
    return this;
  }
}

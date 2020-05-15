package lambda.rodeo.runtime.lambda;

import java.util.function.Supplier;

public interface Lambda0<T> extends Supplier<T> {
  @Override
  default T get() {
    return apply();
  }

  T apply();
}

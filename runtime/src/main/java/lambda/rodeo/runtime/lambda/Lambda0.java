package lambda.rodeo.runtime.lambda;

import java.util.function.Supplier;

public interface Lambda0<T> extends Supplier<T> {
  @Override
  default T get() {
    return apply();
  }

  T apply();

  static <T> Lambda0<T> make(T value) {
    return new Lambda0Impl<>(value);
  }
}

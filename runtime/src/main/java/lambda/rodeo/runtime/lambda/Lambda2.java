package lambda.rodeo.runtime.lambda;

import java.util.function.BiFunction;

public interface Lambda2<T, U, V> extends BiFunction<T, U, V> {

  V apply(T arg1, U arg2);
}

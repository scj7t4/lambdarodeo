package lambda.rodeo.runtime.execution;

import lambda.rodeo.runtime.lambda.Axiom;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.types.Atom;

public class Trampoline<T> implements Lambda0<T> {
  private final Lambda0<?> bounce;

  private Trampoline(Lambda0<?> bounce) {
    this.bounce = bounce;
  }

  public static <T> Trampoline<T> make(Lambda0<?> bounce) {
    return new Trampoline<>(bounce);
  }

  @SuppressWarnings("unchecked")
  public T exec() {
    Object applied = bounce.apply();
    if (applied instanceof Trampoline) {
      return ((Trampoline<T>) applied).exec();
    } else {
      return (T) applied;
    }
  }

  @Override
  public T apply() {
    return exec();
  }

  public static Object trampoline(Object bouncer) {
    while (bouncer instanceof Lambda0 && !(bouncer instanceof Axiom)) {
      bouncer = ((Lambda0<?>) bouncer).get();
    }
    return bouncer;
  }
}

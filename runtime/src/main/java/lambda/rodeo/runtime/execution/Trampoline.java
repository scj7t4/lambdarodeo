package lambda.rodeo.runtime.execution;

import lambda.rodeo.runtime.lambda.Lambda0;

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
}

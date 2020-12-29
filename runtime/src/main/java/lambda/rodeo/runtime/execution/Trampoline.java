package lambda.rodeo.runtime.execution;

import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Quine;
import lambda.rodeo.runtime.lambda.Value;
import lombok.EqualsAndHashCode;

/**
 * Trampoline is a shorthand for Lambda0&lt;Lambda0&lt;Lambda0...&lt;T&gt;.
 */
@EqualsAndHashCode
public class Trampoline<T> implements Lambda0<T> {

  private final Lambda0<?> bounce;

  private Trampoline(Lambda0<?> bounce) {
    this.bounce = bounce;
  }

  /**
   * Make sure when invoking this your Lambda0 is a Lambda0 of T at some point!!
   *
   * @param bounce The inner lambda
   * @param <T> The type that the inner lambda eventually returns.
   * @return A trampoline that wrapps the outer lambda.
   */
  public static <T> Lambda0<T> make(Lambda0<?> bounce) {
    return new Trampoline<>(bounce);
  }

  @SuppressWarnings("unchecked")
  public T exhaust() {
    return (T) exhaust(bounce);
  }

  public Lambda0<T> bounce() {
    @SuppressWarnings("unchecked")
    T value = (T) bounce.get();
    if (value instanceof Lambda0) {
      return Trampoline.make((Lambda0<?>) value);
    } else {
      return Value.of(value);
    }
  }

  @Override
  public T apply() {
    return exhaust();
  }

  /**
   * Given an object that may be a trampoline (IE, Lambda0&lt;Lambda0&lt;Lambda0...&lt;T&gt;
   * repeatedly call get() until you reach an Axiom (a call to get will return the same object) or
   * you get a non-Lambda0.
   *
   * @param bouncer The object to collapse.
   * @return The result of the last call to get.
   */
  public static Object exhaust(Object bouncer) {
    while (bouncer instanceof Lambda0 && !(bouncer instanceof Quine)) {
      bouncer = ((Lambda0<?>) bouncer).get();
    }
    return bouncer;
  }

  public static <T> Lambda0<T> maybeBounce(Lambda0<T> maybeTrampoline) {
    if (maybeTrampoline instanceof Trampoline) {
      return ((Trampoline<T>) maybeTrampoline).bounce();
    } else {
      return maybeTrampoline;
    }
  }
}

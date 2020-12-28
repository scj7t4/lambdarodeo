package lambda.rodeo.runtime.lambda;

public class Lambda0Impl<T> implements Lambda0<T> {
  private final T contents;

  Lambda0Impl(T contents) {
    this.contents = contents;
  }

  public T apply() {
    return contents;
  }
}

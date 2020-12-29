package lambda.rodeo.runtime.fn;

import lambda.rodeo.runtime.lambda.Lambda0;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

public class StringFunctions {
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class ConcatString implements Lambda0<String> {
    private final Lambda0<?> left;
    private final Lambda0<?> right;

    @Override
    public String apply() {
      return "" + left.get() + right.get();
    }

    @Override
    public String toString() {
      return left.toString() + "+" + right.toString();
    }
  }

  public static Lambda0<String> makeConcat(Lambda0<?> left, Lambda0<?> right) {
    return new ConcatString(left, right);
  }
}

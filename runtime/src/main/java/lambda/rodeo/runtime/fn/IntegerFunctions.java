package lambda.rodeo.runtime.fn;

import static lambda.rodeo.runtime.execution.Trampoline.maybeBounce;

import java.math.BigInteger;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Value;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

public class IntegerFunctions {

  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IntegerAdd implements Lambda0<BigInteger> {

    private final Lambda0<BigInteger> left;
    private final Lambda0<BigInteger> right;


    @Override
    public BigInteger apply() {
      return left.get().add(right.get());
    }

    @Override
    public String toString() {
      return "(" + left.toString() + " + " + right.toString() + ")";
    }
  }

  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IntegerSubtract implements Lambda0<BigInteger> {

    private final Lambda0<BigInteger> left;
    private final Lambda0<BigInteger> right;


    @Override
    public BigInteger apply() {
      return left.get().subtract(right.get());
    }

    @Override
    public String toString() {
      return "(" + left.toString() + " - " + right.toString() + ")";
    }
  }

  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IntegerDivide implements Lambda0<BigInteger> {

    private final Lambda0<BigInteger> left;
    private final Lambda0<BigInteger> right;


    @Override
    public BigInteger apply() {
      return left.get().divide(right.get());
    }

    @Override
    public String toString() {
      return "(" + left.toString() + " / " + right.toString() + ")";
    }
  }

  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IntegerMultiply implements Lambda0<BigInteger> {

    private final Lambda0<BigInteger> left;
    private final Lambda0<BigInteger> right;


    @Override
    public BigInteger apply() {
      return left.get().multiply(right.get());
    }

    @Override
    public String toString() {
      return "(" + left.toString() + " * " + right.toString() + ")";
    }
  }

  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IntegerNegate implements Lambda0<BigInteger> {

    private final Lambda0<BigInteger> item;


    @Override
    public BigInteger apply() {
      return item.get().negate();
    }

    @Override
    public String toString() {
      return "(-" + item.toString() + ")";
    }
  }

  public static Lambda0<BigInteger> makeAdd(Lambda0<BigInteger> left, Lambda0<BigInteger> right) {
    if (left instanceof Value && right instanceof Value) {
      return Value.of(left.get().add(right.get()));
    }
    return new IntegerAdd(left, right);
  }

  public static Lambda0<BigInteger> makeSubtract(Lambda0<BigInteger> left,
      Lambda0<BigInteger> right) {
    if (left instanceof Value && right instanceof Value) {
      return Value.of(left.get().subtract(right.get()));
    }
    return new IntegerSubtract(left, right);
  }

  public static Lambda0<BigInteger> makeDivide(Lambda0<BigInteger> left,
      Lambda0<BigInteger> right) {
    if (left instanceof Value && right instanceof Value) {
      return Value.of(left.get().divide(right.get()));
    }
    return new IntegerDivide(left, right);
  }

  public static Lambda0<BigInteger> makeMultiply(Lambda0<BigInteger> left,
      Lambda0<BigInteger> right) {
    if (left instanceof Value && right instanceof Value) {
      return Value.of(left.get().multiply(right.get()));
    }
    return new IntegerMultiply(left, right);
  }

  public static Lambda0<BigInteger> makeNegate(Lambda0<BigInteger> item) {
    if (item instanceof Value) {
      return Value.of(item.get().negate());
    }
    return new IntegerNegate(item);
  }
}

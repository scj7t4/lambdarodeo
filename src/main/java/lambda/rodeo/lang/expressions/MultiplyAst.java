package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import lambda.rodeo.lang.exception.TypeException;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.ValueHolder;
import lombok.ToString;

@ToString
public class MultiplyAst implements ExpressionAst {

  private final Type type;
  private final ValueHolder<?> valueHolder;

  public MultiplyAst(ExpressionAst lhs, ExpressionAst rhs) {
    if(AstUtils.bothIntType(lhs, rhs)) {
      type = lhs.getType();
      @SuppressWarnings("unchecked")
      ValueHolder<BigInteger> lhsVh = (ValueHolder<BigInteger>) lhs.getValueHolder();
      @SuppressWarnings("unchecked")
      ValueHolder<BigInteger> rhsVh = (ValueHolder<BigInteger>) rhs.getValueHolder();
      valueHolder = new BigIntegerMultiplyValueHolder(lhsVh, rhsVh);
    } else {
      throw new TypeException("Cannot multiply types " + lhs.getType() + " and " + rhs.getType());
    }
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public ValueHolder<?> getValueHolder() {
    return valueHolder;
  }

  public static class BigIntegerMultiplyValueHolder implements ValueHolder<BigInteger> {
    private final ValueHolder<BigInteger> lhs;
    private final ValueHolder<BigInteger> rhs;

    public BigIntegerMultiplyValueHolder(ValueHolder<BigInteger> lhs,
        ValueHolder<BigInteger> rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public BigInteger getValue() {
      return lhs.getValue().multiply(rhs.getValue());
    }

    @Override
    public String toString() {
      return "(" + lhs.toString() + " * " + rhs.toString() + ")";
    }
  }
}

package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import lambda.rodeo.lang.exception.TypeException;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.ValueHolder;

public class AddAst implements ExpressionAst {

  private final Type type;
  private final ValueHolder<?> valueHolder;

  public AddAst(ExpressionAst lhs, ExpressionAst rhs) {
    if(AstUtils.bothIntType(lhs, rhs)) {
      type = lhs.getType();
      @SuppressWarnings("unchecked")
      ValueHolder<BigInteger> lhsVh = (ValueHolder<BigInteger>) lhs.getValueHolder();
      @SuppressWarnings("unchecked")
      ValueHolder<BigInteger> rhsVh = (ValueHolder<BigInteger>) rhs.getValueHolder();
      valueHolder = new BigIntegerAddValueHolder(lhsVh, rhsVh);
    } else {
      throw new TypeException("Cannot add types " + lhs.getType() + " and " + rhs.getType());
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

  public static class BigIntegerAddValueHolder implements ValueHolder<BigInteger> {
    private final ValueHolder<BigInteger> lhs;
    private final ValueHolder<BigInteger> rhs;

    public BigIntegerAddValueHolder(ValueHolder<BigInteger> lhs,
        ValueHolder<BigInteger> rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public BigInteger getValue() {
      return lhs.getValue().add(rhs.getValue());
    }

    @Override
    public String toString() {
      return "(" + lhs.toString() + " + " + rhs.toString() + ")";
    }
  }
}

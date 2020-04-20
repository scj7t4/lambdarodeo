package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import lambda.rodeo.lang.exception.TypeException;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.ToString;

@ToString
public class MultiplyAst implements ExpressionAst {

  private final Type type;
  private final Computable<?> computable;

  public MultiplyAst(ExpressionAst lhs, ExpressionAst rhs) {
    if(AstUtils.bothIntType(lhs, rhs)) {
      type = lhs.getType();
      @SuppressWarnings("unchecked")
      Computable<BigInteger> lhsVh = (Computable<BigInteger>) lhs.getComputable();
      @SuppressWarnings("unchecked")
      Computable<BigInteger> rhsVh = (Computable<BigInteger>) rhs.getComputable();
      computable = new BigIntegerMultiplyComputable(lhsVh, rhsVh);
    } else {
      throw new TypeException("Cannot multiply types " + lhs.getType() + " and " + rhs.getType());
    }
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Computable<?> getComputable() {
    return computable;
  }

  public static class BigIntegerMultiplyComputable implements Computable<BigInteger> {
    private final Computable<BigInteger> lhs;
    private final Computable<BigInteger> rhs;

    public BigIntegerMultiplyComputable(Computable<BigInteger> lhs,
        Computable<BigInteger> rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public BigInteger compute(Scope scope) {
      return lhs.compute(scope).multiply(rhs.compute(scope));
    }

    @Override
    public String toString() {
      return "(" + lhs.toString() + " * " + rhs.toString() + ")";
    }
  }
}
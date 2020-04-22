package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import lambda.rodeo.lang.exception.TypeException;
import lambda.rodeo.lang.statements.Scope;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.ToString;

@ToString
public class AddAst implements ExpressionAst {

  private final Type type;
  private final Computable<?> computable;

  public AddAst(ExpressionAst lhs, ExpressionAst rhs) {
    if(AstUtils.bothIntType(lhs, rhs)) {
      type = lhs.getType();
      @SuppressWarnings("unchecked")
      Computable<BigInteger> lhsVh = (Computable<BigInteger>) lhs.getComputable();
      @SuppressWarnings("unchecked")
      Computable<BigInteger> rhsVh = (Computable<BigInteger>) rhs.getComputable();
      computable = new BigIntegerAddComputable(lhsVh, rhsVh);
    } else {
      throw new TypeException("Cannot add types " + lhs.getType() + " and " + rhs.getType());
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

  public static class BigIntegerAddComputable implements Computable<BigInteger> {
    private final Computable<BigInteger> lhs;
    private final Computable<BigInteger> rhs;

    public BigIntegerAddComputable(Computable<BigInteger> lhs,
        Computable<BigInteger> rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public BigInteger compute(Scope scope) {
      return lhs.compute(scope).add(rhs.compute(scope));
    }

    @Override
    public String toString() {
      return "(" + lhs.toString() + " + " + rhs.toString() + ")";
    }
  }
}

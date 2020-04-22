package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import lambda.rodeo.lang.exception.TypeException;
import lambda.rodeo.lang.statements.Scope;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.ToString;

@ToString
public class UnaryMinusAst implements ExpressionAst {

  private final Type type;
  private final Computable<?> computable;

  public UnaryMinusAst(ExpressionAst operand) {
    if (AstUtils.isIntType(operand)) {
      type = operand.getType();
      @SuppressWarnings("unchecked")
      Computable<BigInteger> opVh = (Computable<BigInteger>) operand.getComputable();
      computable = new BigIntegerUnaryMinusComputable(opVh);
    } else {
      throw new TypeException("Cannot negate type " + operand.getType());
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

  public static class BigIntegerUnaryMinusComputable implements Computable<BigInteger> {

    private final Computable<BigInteger> op;

    public BigIntegerUnaryMinusComputable(Computable<BigInteger> op) {
      this.op = op;
    }

    @Override
    public BigInteger compute(Scope scope) {
      return op.compute(scope).negate();
    }

    @Override
    public String toString() {
      return "( -" + op.toString() + ")";
    }
  }
}

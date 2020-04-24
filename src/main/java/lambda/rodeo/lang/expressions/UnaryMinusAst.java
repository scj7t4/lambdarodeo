package lambda.rodeo.lang.expressions;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.statements.Scope;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.ToString;

@ToString
public class UnaryMinusAst implements ExpressionAst {

  private final Type type;
  private final Computable<?> computable;

  public UnaryMinusAst(ExpressionAst operand, TypeScope typeScope,
      CompileContext compileContext) {
    if (AstUtils.isAnyUndefined(typeScope, operand)) {
      type = Atom.UNDEFINED_VAR;
      computable = Atom.UNDEFINED_VAR.toComputable();
    } else if (AstUtils.isIntType(operand, typeScope)) {
      type = operand.getType(typeScope);
      @SuppressWarnings("unchecked")
      Computable<BigInteger> opVh = (Computable<BigInteger>) operand.getComputable();
      computable = new BigIntegerUnaryMinusComputable(opVh);
    } else {
      throw new TypeException("Cannot negate type " + operand.getType(typeScope));
    }
  }

  @Override
  public Type getType(TypeScope typeScope) {
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

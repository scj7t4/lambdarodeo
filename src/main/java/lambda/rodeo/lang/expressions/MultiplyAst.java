package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.ToString;

@ToString
public class MultiplyAst implements ExpressionAst {

  private final Type type;

  public MultiplyAst(ExpressionAst lhs, ExpressionAst rhs, TypeScope typeScope,
      CompileContext compileContext) {
    if (AstUtils.isAnyUndefined(typeScope, lhs, rhs)) {
      type = Atom.UNDEFINED_VAR;
    } else if (AstUtils.bothIntType(lhs, rhs, typeScope)) {
      type = lhs.getType(typeScope);
    } else {
      throw new TypeException(
          "Cannot multiply types " + lhs.getType(typeScope) + " and " + rhs.getType(typeScope));
    }
  }

  @Override
  public Type getType(TypeScope typeScope) {
    return type;
  }
}

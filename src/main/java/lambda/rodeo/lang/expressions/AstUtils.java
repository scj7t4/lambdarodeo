package lambda.rodeo.lang.expressions;

import java.util.Objects;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.Type;
import lombok.Getter;

@Getter
public class AstUtils {

  public static boolean bothIntType(ExpressionAst lhs, ExpressionAst rhs, TypeScope typeScope) {
    return isIntType(lhs)
        && isIntType(rhs);
  }

  public static boolean isIntType(ExpressionAst operand) {
    return Objects.equals(operand.getType(), IntType.INSTANCE);
  }

  public static boolean isAnyUndefined(ExpressionAst... expressionAsts) {
    for (ExpressionAst expr : expressionAsts) {
      Type type = expr.getType();
      if (Objects.equals(Atom.UNDEFINED_VAR, type)) {
        return true;
      }
    }
    return false;
  }
}

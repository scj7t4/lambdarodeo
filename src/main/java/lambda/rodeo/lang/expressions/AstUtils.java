package lambda.rodeo.lang.expressions;

import java.util.Objects;
import lambda.rodeo.lang.types.IntType;
import lombok.Getter;

@Getter
public class AstUtils {

  public static boolean bothIntType(ExpressionAst lhs, ExpressionAst rhs) {
    return isIntType(lhs)
        && isIntType(rhs);
  }

  public static boolean isIntType(ExpressionAst operand) {
    return Objects.equals(operand.getType(), IntType.INSTANCE);
  }
}

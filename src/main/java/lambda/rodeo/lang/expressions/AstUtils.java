package lambda.rodeo.lang.expressions;

import java.util.Objects;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.ValueHolder;
import lombok.Getter;

@Getter
public class AstUtils {

  public static boolean bothIntType(ExpressionAst lhs, ExpressionAst rhs) {
    return Objects.equals(lhs.getType(), IntType.INSTANCE)
        && Objects.equals(rhs.getType(), IntType.INSTANCE);
  }
}

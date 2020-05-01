package lambda.rodeo.lang.expressions;

import java.util.Objects;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.Type;
import lombok.Getter;

@Getter
public class AstUtils {

  public static boolean bothIntType(Type lhs, Type rhs) {
    return isIntType(lhs)
        && isIntType(rhs);
  }

  public static boolean isIntType(Type type) {
    return Objects.equals(type, IntType.INSTANCE);
  }

  public static boolean isAnyUndefined(Type... types) {
    for (Type type : types) {
      if (Objects.equals(Atom.UNDEFINED_VAR, type)) {
        return true;
      }
    }
    return false;
  }
}

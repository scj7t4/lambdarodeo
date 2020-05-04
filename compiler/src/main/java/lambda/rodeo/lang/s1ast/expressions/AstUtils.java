package lambda.rodeo.lang.s1ast.expressions;

import java.util.Objects;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.IntType;
import lambda.rodeo.runtime.types.Type;
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
      if (Objects.equals(Atom.UNDEFINED, type)) {
        return true;
      }
    }
    return false;
  }
}

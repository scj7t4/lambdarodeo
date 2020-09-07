package lambda.rodeo.lang.s1ast.expressions;

import java.util.Objects;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Getter;

@Getter
public class AstUtils {

  public static boolean bothIntType(CompileableType lhs, CompileableType rhs) {
    return isIntType(lhs)
        && isIntType(rhs);
  }


  public static boolean isIntType(LambdaRodeoType type) {
    return Objects.equals(type, IntType.INSTANCE);
  }

  public static boolean isIntType(CompileableType type) {
    return Objects.equals(type, IntType.INSTANCE);
  }

  public static boolean isAnyUndefined(LambdaRodeoType... types) {
    for (LambdaRodeoType type : types) {
      if (Objects.equals(CompileableAtom.UNDEFINED, type)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAnyUndefined(CompileableType... types) {
    for (CompileableType type : types) {
      if (Objects.equals(CompileableAtom.UNDEFINED, type)) {
        return true;
      }
    }
    return false;
  }
}

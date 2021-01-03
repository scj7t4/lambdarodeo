package lambda.rodeo.lang.s2typed.type;

import java.util.Objects;
import lambda.rodeo.lang.types.CompileableType;

public interface S2TypedVar {

  String getName();
  CompileableType getType();

  public static boolean s2TypedVarEquals(S2TypedVar self, Object other) {
    if (other instanceof S2TypedVar) {
      return Objects.equals(((S2TypedVar) other).getName(), self.getName())
          && Objects.equals(((S2TypedVar) other).getType(), self.getType());
    }
    return false;
  }
}

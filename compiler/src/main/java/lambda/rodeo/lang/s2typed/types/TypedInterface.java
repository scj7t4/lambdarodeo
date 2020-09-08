package lambda.rodeo.lang.s2typed.types;

import java.util.List;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class TypedInterface {
  @NonNull
  private final InterfaceAst from;

  @NonNull
  private final List<S2TypedVar> members;
}

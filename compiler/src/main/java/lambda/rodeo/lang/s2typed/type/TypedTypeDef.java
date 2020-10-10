package lambda.rodeo.lang.s2typed.type;

import lambda.rodeo.lang.s1ast.type.TypeDef;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedTypeDef {
  private final TypeDef from;
  private final CompileableType type;

}

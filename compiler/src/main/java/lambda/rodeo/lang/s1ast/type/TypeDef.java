package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s2typed.type.TypedTypeDef;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypeDef {

  private final String identifier;
  private final LambdaRodeoType type;

  public TypedTypeDef toTypedTypeDef(
      TypedModuleScope typedModuleScope,
      S2CompileContext compileContext) {
    return TypedTypeDef.builder()
        .from(this)
        .type(type.toCompileableType(typedModuleScope, compileContext))
        .build();
  }
}

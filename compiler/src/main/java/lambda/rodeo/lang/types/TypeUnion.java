package lambda.rodeo.lang.types;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypeResolver;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class TypeUnion implements LambdaRodeoType {

  private final Set<LambdaRodeoType> unions;

  @Override
  public CompileableType toCompileableType(TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return CompileableTypeUnion.builder()
        .type(this)
        .unions(unions.stream()
            .map(union -> union.toCompileableType(typeResolver, compileContext))
            .collect(Collectors.toSet()))
        .build();
  }

}

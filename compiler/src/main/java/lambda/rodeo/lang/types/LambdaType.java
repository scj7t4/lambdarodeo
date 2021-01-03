package lambda.rodeo.lang.types;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypeResolver;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class LambdaType implements LambdaRodeoType {

  private final List<? extends LambdaRodeoType> args;
  private final LambdaRodeoType returnType;

  @Override
  public CompileableLambdaType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return CompileableLambdaType.builder()
        .from(this)
        .args(args.stream()
            .map(arg -> arg.toCompileableType(typeResolver,
                compileContext
            ))
            .collect(Collectors.toList())
        )
        .returnType(returnType.toCompileableType(typeResolver,
            compileContext
        ))
        .build();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("<lambda>(");
    for (int i = 0; i < args.size(); i++) {
      LambdaRodeoType arg = args.get(i);
      sb.append(arg);
      if (i < args.size() - 1) {
        sb.append(",");
      }
    }
    sb.append(")=>").append(returnType);
    return sb.toString();
  }
}

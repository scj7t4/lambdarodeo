package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LRInterface;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class InterfaceAst implements AstNode, LambdaRodeoType {

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @NonNull
  private final List<TypedVar> members;

  public LRInterface toTypedInterface(TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    return LRInterface.builder()
        .from(this)
        .members(members.stream()
            .map(typedVar -> typedVar.toS2TypedVar(typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .build();
  }

  @Override
  public CompileableType toCompileableType(
      TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    return LRInterface.builder()
        .from(this)
        .members(this.members.stream()
            .map(member -> member.toS2TypedVar(typedModuleScope, compileContext))
            .collect(Collectors.toList())
        )
        .build();
  }
}

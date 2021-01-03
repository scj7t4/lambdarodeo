package lambda.rodeo.lang.s1ast.type;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.CompileableInterface;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class InterfaceAst implements AstNode, LambdaRodeoType {

  @EqualsAndHashCode.Exclude
  private final int startLine;
  @EqualsAndHashCode.Exclude
  private final int endLine;
  @EqualsAndHashCode.Exclude
  private final int characterStart;

  @NonNull
  private final LinkedHashSet<TypedVar> members;

  @Override
  public CompileableType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {
    List<S2TypedVar> collectedMembers = this.members.stream()
        .map(member -> member.toS2TypedVar(typeResolver, compileContext))
        .collect(Collectors.toList());
    return CompileableInterface.builder()
        .from(this)
        .members(new LinkedHashSet<>(collectedMembers))
        .build();
  }
}

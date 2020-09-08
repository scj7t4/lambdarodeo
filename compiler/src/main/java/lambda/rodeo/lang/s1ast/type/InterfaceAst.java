package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.s2typed.types.TypedInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class InterfaceAst implements AstNode {
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @NonNull
  private final String name;

  @NonNull
  private final List<TypedVar> members;

  public TypedInterface toTypedInterface() {
    return TypedInterface.builder()
        .from(this)
        .members(members.stream()
          .map(TypedVar::toS2TypedVar)
          .collect(Collectors.toList()))
        .build();
  }
}

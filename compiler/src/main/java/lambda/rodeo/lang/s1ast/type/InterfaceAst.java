package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.CompileableInterface;
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

  @Override
  public CompileableType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return CompileableInterface.builder()
        .from(this)
        .members(this.members.stream()
            .map(member -> member.toS2TypedVar(typeResolver, compileContext))
            .collect(Collectors.toList())
        )
        .build();
  }

  /**
   * Converts this interface to a generic one.
   *
   * The parser will produce the interface as a normal interface, because resolving the referenced
   * types is not done immediately. This will mark this interface as generic and provide hinting for
   * the types to be bound.
   *
   * @param generics Types that are generic, and their minimum typing.
   * @return This interface as a generic.
   */
  public GenericInterfaceAst genericInterfaceAst(List<TypedVar> generics) {
    return GenericInterfaceAst.builder()
        .genericParams(generics)
        .characterStart(characterStart)
        .startLine(startLine)
        .endLine(endLine)
        .members(members)
        .build();
  }
}

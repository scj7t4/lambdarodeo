package lambda.rodeo.lang.types;

import java.util.Optional;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.expressions.AtomAst;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class DefinedType implements LambdaRodeoType, AstNode {

  @EqualsAndHashCode.Exclude
  private final int startLine;
  @EqualsAndHashCode.Exclude
  private final int endLine;
  @EqualsAndHashCode.Exclude
  private final int characterStart;
  private final String declaration;

  @Override
  public CompileableType toCompileableType(
      TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    Optional<CompileableType> maybeType = typedModuleScope.getTypeTarget(declaration)
        .map(type -> type.toCompileableType(typedModuleScope, compileContext));
    if (maybeType.isEmpty()) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.undefinedIdentifier(this, declaration));
      return AtomAst.undefinedAtomExpression().getType();
    }
    return maybeType.get();
  }

  @Override
  public String toString() {
    return "Type<" + declaration + ">";
  }
}

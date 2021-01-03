package lambda.rodeo.lang.types;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.expressions.AtomAst;
import lambda.rodeo.lang.s1ast.type.TypeDef;
import lambda.rodeo.lang.scope.TypeResolver;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

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
  @NonNull
  @Builder.Default
  private final List<LambdaRodeoType> genericBindings = Collections.emptyList();

  @Override
  public CompileableType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {
    List<CompileableType> compileableGenerics = this.genericBindings.stream()
        .map(type -> type.toCompileableType(typeResolver,
            compileContext))
        .collect(Collectors.toList());

    Optional<TypeDef> maybeTypeDef = typeResolver
        .getTypeTarget(declaration);

    if (maybeTypeDef.isEmpty()) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.undefinedIdentifier(this, declaration));
      return AtomAst.undefinedAtomExpression().getType();
    }
    TypeDef typeDef = maybeTypeDef.get();
    TypeResolver innerScope = typeDef
        .bindGenerics(typeResolver, compileableGenerics, compileContext);

    return typeDef.getType()
        .toCompileableType(innerScope, compileContext);
  }

  @Override
  public String toString() {
    return "Type<" + declaration + ">";
  }
}

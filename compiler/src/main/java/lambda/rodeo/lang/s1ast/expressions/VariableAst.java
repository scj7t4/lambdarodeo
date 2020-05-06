package lambda.rodeo.lang.s1ast.expressions;


import static lambda.rodeo.runtime.types.Atom.UNDEFINED;

import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s2typed.expressions.TypedVariable;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class VariableAst implements ExpressionAst {

  private final String name;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedExpression toTypedExpression(
      TypeScope typeScope,
      TypedModuleScope typedModuleScope, CompileContext compileContext) {
    Optional<Entry> entry = typeScope.get(name);
    Type type = entry
        .map(Entry::getType)
        .orElse(UNDEFINED);
    //TODO: Fix context...
    if (Objects.equals(UNDEFINED, type)) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedIdentifier(name, this));
      return AtomAst.builder()
          .atom(UNDEFINED)
          .build()
          .toTypedExpression(typeScope, typedModuleScope, compileContext);
    } else {
      return TypedVariable.builder()
          .scopeEntry(entry.get())
          .variableAst(this)
          .build();
    }
  }
}
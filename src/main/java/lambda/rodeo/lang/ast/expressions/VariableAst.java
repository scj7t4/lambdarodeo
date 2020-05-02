package lambda.rodeo.lang.ast.expressions;


import static lambda.rodeo.lang.types.Atom.UNDEFINED_VAR;

import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.typed.expressions.TypedVariable;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.types.TypeScope.Entry;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VariableAst implements ExpressionAst {

  private final String name;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedExpression toTypedExpressionAst(
      TypeScope typeScope,
      CompileContext compileContext) {
    Optional<Entry> entry = typeScope.get(name);
    Type type = entry
        .map(Entry::getType)
        .orElse(UNDEFINED_VAR);
    //TODO: Fix context...
    if (Objects.equals(UNDEFINED_VAR, type)) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedVariableError(name, this));
      return AtomAst.builder()
          .atom(UNDEFINED_VAR)
          .build()
          .toTypedExpressionAst(typeScope, compileContext);
    } else {
      return TypedVariable.builder()
          .scopeEntry(entry.get())
          .variableAst(this)
          .build();
    }
  }
}

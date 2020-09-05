package lambda.rodeo.lang.s1ast.expressions;


import static lambda.rodeo.runtime.types.Atom.UNDEFINED;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedVariable;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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
      TypedModuleScope typedModuleScope, ToTypedFunctionContext compileContext) {
    Optional<Entry> entry = typeScope.get(name).findFirst();
    CompileableType type = entry
        .map(TypeScope.Entry::getType)
        .orElse(UNDEFINED);
    if (Objects.equals(UNDEFINED, type)) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedIdentifier(this, name));
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

  @Override
  public Set<String> getReferencedVariables() {
    return Set.of(name);
  }
}

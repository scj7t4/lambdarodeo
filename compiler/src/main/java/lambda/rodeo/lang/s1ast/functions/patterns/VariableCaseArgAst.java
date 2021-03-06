package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.VariableTypedCaseArg;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.types.CompileableAtom;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class VariableCaseArgAst implements CaseArgAst {

  private final String identifier;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedCaseArg toTypedCaseArg(TypeScope initialTypeScope,
      TypeResolver typeResolver,
      ToTypedFunctionContext compileContext) {
    return initialTypeScope.get(identifier)
        .map(entry -> VariableTypedCaseArg.builder()
            .caseArgAst(this)
            .referencedArgEntry(entry)
            .build())
        .map(x -> (TypedCaseArg) x)
        .findFirst()
        .orElseGet(() -> {

          compileContext
              .getCompileErrorCollector()
              .collect(CompileError.undefinedIdentifier(this, identifier));

          return (TypedCaseArg) AtomCaseArgAst
              .builder()
              .atom(CompileableAtom.UNDEFINED)
              .build();
        });
  }
}

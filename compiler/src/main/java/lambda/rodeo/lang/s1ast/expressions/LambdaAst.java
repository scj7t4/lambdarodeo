package lambda.rodeo.lang.s1ast.expressions;

import static lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst.getTypedStatements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.expressions.TypedLambda;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LambdaType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class LambdaAst implements ExpressionAst {

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @NonNull
  private final List<TypedVar> arguments;

  @NonNull
  private final List<StatementAst> statements;

  @Override
  public TypedLambda toTypedExpression(
      TypeScope scope,
      TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {

    // The scope inside the lambda starts out empty:
    TypeScope lambdaScope = TypeScope.EMPTY;

    // We take the variables we reference from the enclosing scope an declare those first
    List<Entry> scopeArgs = new ArrayList<>();
    for (String var : getReferencedVariables()) {
      Entry scopeEntry = scope.get(var)
          .findFirst()
          .orElseThrow(() -> new UnsupportedOperationException(
              "Variable '" + var + "' is missing from scope?"));
      scopeArgs.add(scopeEntry);
      lambdaScope = lambdaScope.declare(var, scopeEntry.getType());
    }

    // Then we add the arguments of the function itself:
    for (TypedVar typedVar : arguments) {
      lambdaScope = lambdaScope.declare(typedVar.getName(), typedVar.getType().toCompileableType());
    }

    // Now we produce a list of the typed statements.
    List<TypedStatement> typedStatements = getTypedStatements(
        lambdaScope,
        typedModuleScope,
        compileContext,
        statements);

    List<? extends LambdaRodeoType> argTypes = arguments.stream()
        .map(TypedVar::getType)
        .collect(Collectors.toList());

    CompileableType returnType = typedStatements
        .get(typedStatements.size() - 1)
        .getTypedExpression()
        .getType();

    TypedFunction lambdaFn = FunctionAst.builder()
        .functionSignature(FunctionSigAst.builder()
            .arguments(Stream.concat(
                scopeArgs.stream().map(entry -> TypedVar.builder()
                    .name(entry.getName())
                    .type(entry.getType().getType())
                    .build()),
                getArguments().stream())
                .collect(Collectors.toList()))
            .startLine(getStartLine())
            .endLine(getEndLine())
            .characterStart(getCharacterStart())
            .declaredReturnType(returnType.getType())
            .name("lambda$" + compileContext.allocateLambda())
            .build())
        .functionBodyAst(FunctionBodyAst.builder()
            .patternCases(
                Collections.singletonList(
                    PatternCaseAst.builder()
                        .caseArgs(Collections.emptyList())
                        .startLine(getStartLine())
                        .endLine(getEndLine())
                        .characterStart(getCharacterStart())
                        .statements(getStatements())
                        .build()
                )
            )
            .build())
        .lambda(true)
        .build()
        .toTypedFunctionAst(TypeScope.EMPTY, typedModuleScope, compileContext.getCompileContext());

    return TypedLambda.builder()
        .expr(this)
        .typedModuleScope(typedModuleScope)
        .scopeArgs(scopeArgs)
        .type(LambdaType.builder()
            .args(argTypes)
            .returnType(returnType.getType())
            .build()
            .toCompileableType())
        .typedFunction(lambdaFn)
        .build();
  }

  @Override
  public Set<String> getReferencedVariables() {
    // The type scope will be the declared args, plus the args in the scope used by the
    // statements, but not those declared in the scope x_x
    Set<String> assignedVars = statements.stream()
        .map(StatementAst::getAssignment)
        .filter(Objects::nonNull)
        .flatMap(assignment -> assignment.newDeclarations().stream())
        .collect(Collectors.toSet());
    Set<String> arguments = getArguments().stream()
        .map(TypedVar::getName)
        .collect(Collectors.toSet());
    return statements.stream()
        .flatMap(statement -> statement.getReferencedVariables().stream())
        .filter(referencedVar -> !assignedVars.contains(referencedVar))
        .filter(referencedVar -> !arguments.contains(referencedVar))
        .collect(Collectors.toSet());
  }
}

package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionSigContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.type.TypeExpressionFactory;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s1ast.type.TypedVarFactory;

public class FunctionSigAstFactory extends LambdaRodeoBaseVisitor<FunctionSigAst> {

  private final S1CompileContext compileContext;
  private final FunctionSigAst ast;

  public FunctionSigAstFactory(FunctionSigContext ctx, S1CompileContext compileContext) {
    this.compileContext = compileContext;
    ast = visit(ctx);
  }

  @Override
  public FunctionSigAst visitFunctionSig(FunctionSigContext ctx) {
    TypeExpressionFactory returnTypeFactory = new TypeExpressionFactory(
        ctx
            .returnType()
            .typeExpression());

    List<TypedVar> arguments = ctx
        .functionArgs()
        .typedVar()
        .stream()
        .map(TypedVarFactory::new)
        .map(TypedVarFactory::toAst)
        .collect(Collectors.toList());

    checkForDuplicateArgNames(arguments);

    return FunctionSigAst.builder()
        .characterStart(ctx.getStart().getCharPositionInLine())
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .name(ctx.functionName().getText())
        .arguments(arguments)
        .declaredReturnType(returnTypeFactory.toAst())
        .build();
  }

  private void checkForDuplicateArgNames(List<TypedVar> arguments) {
    Map<String, List<TypedVar>> namingCount = new HashMap<>();

    for (TypedVar var : arguments) {
      String name = var.getName();
      namingCount.computeIfAbsent(name, key -> new ArrayList<>()).add(var);
    }

    namingCount.values().stream()
        .filter(typedVars -> typedVars.size() > 1)
        .map(Collection::stream)
        .flatMap(dupes -> dupes.skip(1))
        .forEach(dupe -> compileContext.getCompileErrorCollector().collect(
            CompileError.identifierAlreadyDeclaredInScope(dupe, dupe.getName())
        ));
  }

  public FunctionSigAst toAst() {
    return ast;
  }
}

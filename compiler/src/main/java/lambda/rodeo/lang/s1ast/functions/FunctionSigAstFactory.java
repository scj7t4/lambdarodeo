package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionArgsContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionNameContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.FunctionSigContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ReturnTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FunctionSigAstFactory extends LambdaRodeoBaseListener {

  private final S1CompileContext compileContext;
  private FunctionSigAst.FunctionSigAstBuilder builder = FunctionSigAst.builder();
  private final List<TypedVar> arguments = new ArrayList<>();
  private LambdaRodeoType returnType;

  public FunctionSigAstFactory(FunctionSigContext ctx, S1CompileContext compileContext) {
    this.compileContext = compileContext;
    builder.arguments(arguments);
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  @Override
  public void enterFunctionSig(FunctionSigContext ctx) {
    builder.startLine(ctx.getStart().getLine());
    builder.endLine(ctx.getStop().getLine());
    builder.characterStart(ctx.getStart().getLine());
  }

  @Override
  public void enterFunctionName(FunctionNameContext ctx) {
    builder = builder.name(ctx.getText());
  }


  @Override
  public void enterTypedVar(TypedVarContext ctx) {
    arguments.add(new TypedVarFactory(ctx).toAst());
  }

  @Override
  public void exitFunctionArgs(FunctionArgsContext ctx) {
    Map<String, List<TypedVar>> namingCount = new HashMap<>();

    for(TypedVar var : arguments) {
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

  @Override
  public void enterReturnType(ReturnTypeContext ctx) {
    TypeExpressionContext typeExpressionContext = ctx.typeExpression();
    returnType = new TypeFactory(typeExpressionContext).toAst();
  }

  public FunctionSigAst toAst() {
    return builder
        .declaredReturnType(returnType)
        .build();
  }
}

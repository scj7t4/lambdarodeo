package lambda.rodeo.lang.s1ast.functions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StringTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.IntType;
import lambda.rodeo.runtime.types.LambdaType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lambda.rodeo.runtime.types.StringType;

public class TypeFactory extends LambdaRodeoBaseVisitor<LambdaRodeoType> {

  private final LambdaRodeoType ast;

  public TypeFactory(TypeExpressionContext ctx) {
    ast = visit(ctx);
  }

  public LambdaRodeoType toAst() {
    return ast;
  }

  @Override
  public LambdaRodeoType visitIntType(IntTypeContext ctx) {
    return IntType.INSTANCE;
  }

  @Override
  public LambdaRodeoType visitAtom(AtomContext ctx) {
    String atomIdentifier = ctx.IDENTIFIER().getText();
    return new Atom(atomIdentifier);
  }

  @Override
  public LambdaRodeoType visitLambdaTypeExpression(LambdaTypeExpressionContext ctx) {
    List<LambdaRodeoType> types = ctx.typeExpression().stream()
        .map(this::visit)
        .collect(Collectors.toList());
    return LambdaType.builder()
        .args(types.subList(0, types.size() - 1))
        .returnType(types.get(types.size() - 1))
        .build();
  }

  @Override
  public LambdaRodeoType visitStringType(StringTypeContext ctx) {
    return StringType.INSTANCE;
  }
}

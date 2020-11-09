package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.DefinedTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.InterfaceDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StringTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.DefinedType;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lambda.rodeo.lang.types.LambdaType;
import lambda.rodeo.lang.types.StringType;

public class TypeExpressionFactory extends LambdaRodeoBaseVisitor<LambdaRodeoType> {

  private final LambdaRodeoType ast;
  private final CollectsErrors compileContext;

  public TypeExpressionFactory(TypeExpressionContext ctx,
      CollectsErrors compileContext) {
    this.compileContext = compileContext;
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
    return new CompileableAtom(atomIdentifier);
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

  @Override
  public LambdaRodeoType visitDefinedType(DefinedTypeContext ctx) {
    return DefinedType.builder()
        .characterStart(ctx.getStart().getCharPositionInLine())
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .declaration(ctx.identifier().getText())
        .build();
  }

  @Override
  public LambdaRodeoType visitInterfaceDef(InterfaceDefContext ctx) {
    InterfaceAstFactory interfaceAstFactory = new InterfaceAstFactory(ctx, compileContext);
    return interfaceAstFactory.getAst();
  }
}

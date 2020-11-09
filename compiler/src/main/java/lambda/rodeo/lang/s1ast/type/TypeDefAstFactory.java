package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeDefContext;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.types.LambdaRodeoType;

public class TypeDefAstFactory extends LambdaRodeoBaseVisitor<TypeDef> {

  private final TypeDef ast;
  private final CollectsErrors compileContext;

  public TypeDefAstFactory(TypeDefContext context,
      CollectsErrors compileContext) {
    this.compileContext = compileContext;
    ast = visit(context);
  }

  public TypeDef toAst() {
    return ast;
  }

  @Override
  public TypeDef visitTypeDef(TypeDefContext ctx) {
    String identifier = ctx.IDENTIFIER().getText();
    LambdaRodeoType type = new TypeExpressionFactory(ctx.typeExpression(), compileContext).toAst();
    return TypeDef.builder()
        .identifier(identifier)
        .type(type)
        .build();
  }
}

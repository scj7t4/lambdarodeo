package lambda.rodeo.lang.s1ast.type;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeDefContext;
import lambda.rodeo.lang.types.LambdaRodeoType;

public class TypeDefAstFactory extends LambdaRodeoBaseVisitor<TypeDef> {

  private final TypeDef ast;

  public TypeDefAstFactory(TypeDefContext context) {
    ast = visit(context);
  }

  public TypeDef toAst() {
    return ast;
  }

  @Override
  public TypeDef visitTypeDef(TypeDefContext ctx) {
    String identifier = ctx.IDENTIFIER().getText();
    LambdaRodeoType type = new TypeExpressionFactory(ctx.typeExpression()).toAst();
    return TypeDef.builder()
        .identifier(identifier)
        .type(type)
        .build();
  }
}

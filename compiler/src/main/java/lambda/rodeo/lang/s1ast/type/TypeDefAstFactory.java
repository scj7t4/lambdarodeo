package lambda.rodeo.lang.s1ast.type;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.GenericDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeDefContext;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.types.AnyType;
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
    GenericDefContext genericDefContext = ctx.genericDef();
    LambdaRodeoType type = new TypeExpressionFactory(ctx.typeExpression(), compileContext).toAst();

    List<TypedVar> generics = Collections.emptyList();
    if (genericDefContext != null) {
      generics = genericDefContext.monoGenericDef()
          .stream()
          .map(def -> {
            LambdaRodeoType minimum;
            if (def.typeExpression() != null) {
              TypeExpressionFactory expressionFactory = new TypeExpressionFactory(
                  def.typeExpression(),
                  compileContext);
              minimum = expressionFactory.toAst();
            } else {
              minimum = AnyType.INSTANCE;
            }
            return TypedVar.builder()
                .characterStart(def.getStart().getCharPositionInLine())
                .startLine(def.getStart().getLine())
                .endLine(def.getStop().getLine())
                .name(def.IDENTIFIER().getText())
                .type(minimum)
                .build();
          })
          .collect(Collectors.toList());
    }

    return TypeDef.builder()
        .identifier(identifier)
        .type(type)
        .generics(generics)
        .build();
  }
}

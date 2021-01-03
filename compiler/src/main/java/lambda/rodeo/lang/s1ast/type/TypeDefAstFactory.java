package lambda.rodeo.lang.s1ast.type;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.GenericDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.MonoGenericDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
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

    Map<String, LambdaRodeoType> generics = Collections.emptyMap();
    if (genericDefContext != null) {
       generics = genericDefContext.monoGenericDef()
          .stream()
          .collect(Collectors.toMap(
              def -> def.IDENTIFIER().getText(),
              MonoGenericDefContext::typeExpression))
          .entrySet()
          .stream()
          .collect(Collectors.toMap(
              Entry::getKey,
              entry -> {
                if (entry.getValue() != null) {
                  TypeExpressionFactory typeExpressionFactory = new TypeExpressionFactory(
                      entry.getValue(), compileContext);
                  return typeExpressionFactory.toAst();
                } else {
                  return AnyType.INSTANCE;
                }
              }
          ));
    }

    LambdaRodeoType type = new TypeExpressionFactory(ctx.typeExpression(), compileContext).toAst();
    return TypeDef.builder()
        .identifier(identifier)
        .type(type)
        .generics(generics)
        .build();
  }
}

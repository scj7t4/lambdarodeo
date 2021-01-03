package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.DefinedTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.InterfaceDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.LambdaTypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.StringTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptAtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptCompoundContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptDefinedContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptIntContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptInterfaceDefContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptLambdaContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptParenContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeOptStringContext;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.DefinedType;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lambda.rodeo.lang.types.LambdaType;
import lambda.rodeo.lang.types.StringType;
import lambda.rodeo.lang.types.TypeUnion;

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
  public LambdaRodeoType visitTypeOptInt(TypeOptIntContext ctx) {
    return IntType.INSTANCE;
  }

  @Override
  public LambdaRodeoType visitTypeOptAtom(TypeOptAtomContext ctx) {
    String atomIdentifier = ctx.atom().IDENTIFIER().getText();
    return new CompileableAtom(atomIdentifier);
  }

  @Override
  public LambdaRodeoType visitTypeOptLambda(TypeOptLambdaContext branch) {
    LambdaTypeExpressionContext ctx = branch.lambdaTypeExpression();
    List<LambdaRodeoType> types = ctx.typeExpression().stream()
        .map(this::visit)
        .collect(Collectors.toList());
    return LambdaType.builder()
        .args(types.subList(0, types.size() - 1))
        .returnType(types.get(types.size() - 1))
        .build();
  }

  @Override
  public LambdaRodeoType visitTypeOptString(TypeOptStringContext ctx) {
    return StringType.INSTANCE;
  }

  @Override
  public LambdaRodeoType visitTypeOptDefined(TypeOptDefinedContext branch) {
    DefinedTypeContext ctx = branch.definedType();
    List<LambdaRodeoType> genericBindings = ctx.typeExpression().stream()
        .map(expr -> {
          TypeExpressionFactory factory = new TypeExpressionFactory(expr, compileContext);
          return factory.toAst();
        })
        .collect(Collectors.toList());

    return DefinedType.builder()
        .characterStart(ctx.getStart().getCharPositionInLine())
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .declaration(ctx.identifier().getText())
        .genericBindings(genericBindings)
        .build();
  }

  @Override
  public LambdaRodeoType visitTypeOptInterfaceDef(TypeOptInterfaceDefContext branch) {
    InterfaceDefContext ctx = branch.interfaceDef();
    InterfaceAstFactory interfaceAstFactory = new InterfaceAstFactory(ctx, compileContext);
    return interfaceAstFactory.getAst();
  }

  @Override
  public LambdaRodeoType visitTypeOptParen(TypeOptParenContext ctx) {
    TypeExpressionFactory inner = new TypeExpressionFactory(ctx.typeExpression(), compileContext);
    return inner.toAst();
  }

  @Override
  public LambdaRodeoType visitTypeOptCompound(TypeOptCompoundContext ctx) {
    Set<LambdaRodeoType> unions = ctx.typeExpression()
        .stream()
        .map(expr -> {
          TypeExpressionFactory inner = new TypeExpressionFactory(expr, compileContext);
          return inner.toAst();
        })
        .collect(Collectors.toSet());
    return TypeUnion.builder()
        .unions(unions)
        .build();
  }
}

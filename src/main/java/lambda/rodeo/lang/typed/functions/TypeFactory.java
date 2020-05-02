package lambda.rodeo.lang.typed.functions;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeExpressionContext;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.Type;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TypeFactory extends LambdaRodeoBaseListener {

  private Type ast;

  public TypeFactory(TypeExpressionContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public Type toAst() {
    return ast;
  }

  @Override
  public void enterIntType(IntTypeContext ctx) {
    ast = IntType.INSTANCE;
  }

  @Override
  public void enterAtom(AtomContext ctx) {
    String atomIdentifier = ctx.IDENTIFIER().getText();
    ast = new Atom(atomIdentifier);
  }
}

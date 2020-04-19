package lambda.rodeo.lang;

import lambda.rodeo.lang.antlr.LambdaRodeoBaseListener;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.AtomContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.IntTypeContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypedVarContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.VarNameContext;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TypedVarAstFactory extends LambdaRodeoBaseListener {

  private TypedVarAst.TypedVarAstBuilder builder = TypedVarAst.builder();

  public TypedVarAstFactory(TypedVarContext ctx) {
    ParseTreeWalker.DEFAULT.walk(this, ctx);
  }

  public TypedVarAst toAst() {
    return builder.build();
  }

  @Override
  public void enterVarName(VarNameContext ctx) {
    builder = builder.name(ctx.getText());
  }

  @Override
  public void enterIntType(IntTypeContext ctx) {
    builder.type(IntType.INSTANCE);
  }

  @Override
  public void enterAtom(AtomContext ctx) {
    String atomIdentifier = ctx.IDENTIFIER().getText();
    builder.type(new Atom(atomIdentifier));
  }
}

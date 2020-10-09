package lambda.rodeo.lang.s1ast.expressions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ObjectContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ObjectExprContext;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ObjectMemberContext;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.expressions.ObjectAst.ObjectAstMember;

public class ObjectAstFactory {

  public ObjectAstFactory(ObjectContext ctx, S1CompileContext compileContext) {
    List<ObjectExprContext> objectExprContexts = ctx.objectExpr();

    List<ObjectAstMember> entries = new ArrayList<>();
    for(ObjectExprContext objectExprContext : objectExprContexts) {
      ObjectMemberContext objectMemberContext = objectExprContext.objectMember();
      String identifier = objectMemberContext.IDENTIFIER().getText();
      ExprContext expr = objectMemberContext.expr();
      ExpressionAstFactory factory = new ExpressionAstFactory(expr, compileContext);
      ExpressionAst expressionAst = factory.toAst();
      entries.add(ObjectAstMember.builder()
          .identifier(identifier)
          .expression(expressionAst)
          .build());
    }
  }

  public ExpressionAst toAst() {
    return null;
  }
}

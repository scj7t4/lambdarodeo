package lambda.rodeo.lang.typed.expressions;

import lambda.rodeo.lang.ast.expressions.CompileableExpr;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.types.Type;

public interface TypedExpressionAst extends CompileableExpr {

  Type getType();

  ExpressionAst getExpr();
}

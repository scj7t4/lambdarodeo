package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.types.Type;

public interface TypedExpressionAst extends CompileableExpr {

  Type getType();

  ExpressionAst getExpr();
}

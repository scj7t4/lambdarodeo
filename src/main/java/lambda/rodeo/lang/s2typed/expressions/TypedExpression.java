package lambda.rodeo.lang.s2typed.expressions;

import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.types.Type;

public interface TypedExpression {

  Type getType();

  ExpressionAst getExpr();

  CompileableExpr toCompileableExpr();
}

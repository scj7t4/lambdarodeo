package lambda.rodeo.lang.s2typed.expressions;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.types.CompileableType;

public interface TypedExpression {

  CompileableType getType();

  ExpressionAst getExpr();

  CompileableExpr toCompileableExpr(CollectsErrors compileContext);
}

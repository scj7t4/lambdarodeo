package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.s2typed.expressions.TypedExpression;

public interface CompileableExpr extends CompileableExpression {

  TypedExpression getTypedExpression();
}

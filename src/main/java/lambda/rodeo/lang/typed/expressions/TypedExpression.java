package lambda.rodeo.lang.typed.expressions;

import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.types.Type;

public interface TypedExpression {

  Type getType();

  ExpressionAst getExpr();

  CompileableExpr toCompileableExpr(
      CompileableModuleScope compileableModuleScope);
}

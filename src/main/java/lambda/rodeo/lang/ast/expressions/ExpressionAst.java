package lambda.rodeo.lang.ast.expressions;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.expressions.TypedExpression;

public interface ExpressionAst extends AstNode {

  TypedExpression toTypedExpressionAst(TypeScope scope, CompileContext compileContext);

}

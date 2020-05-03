package lambda.rodeo.lang.ast.expressions;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.typed.expressions.TypedExpression;

/**
 * Type representing an expression. Expressions are segments of the language the represent
 * manipulating data.
 */
public interface ExpressionAst extends AstNode {

  TypedExpression toTypedExpression(TypeScope scope, CompileContext compileContext);

}

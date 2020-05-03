package lambda.rodeo.lang.s1ast.expressions;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypedModuleScope;

/**
 * Type representing an expression. Expressions are segments of the language the represent
 * manipulating data.
 */
public interface ExpressionAst extends AstNode {

  TypedExpression toTypedExpression(TypeScope scope,
      TypedModuleScope typedModuleScope, CompileContext compileContext);

}

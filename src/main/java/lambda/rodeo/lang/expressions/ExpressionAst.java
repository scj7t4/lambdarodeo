package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.statements.TypeScope;

public interface ExpressionAst extends AstNode {

  TypedExpressionAst toTypedExpressionAst(TypeScope scope, CompileContext compileContext);

}

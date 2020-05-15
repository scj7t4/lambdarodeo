package lambda.rodeo.lang.s1ast.statements;

import java.util.Set;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedAssignment;
import lambda.rodeo.runtime.types.Type;

public interface AssigmentAst extends AstNode {

  TypeScope scopeAfter(TypeScope before, ToTypedFunctionContext compileContext, Type typeAssignFrom);

  TypedAssignment toTypedAssignmentAst(TypeScope after);

  Set<String> newDeclarations();
}

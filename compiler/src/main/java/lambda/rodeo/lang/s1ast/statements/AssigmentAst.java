package lambda.rodeo.lang.s1ast.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedAssignment;
import lambda.rodeo.lang.scope.TypeScopeImpl;
import lambda.rodeo.runtime.types.Type;

public interface AssigmentAst {

  TypeScope scopeAfter(TypeScope before, CompileContext compileContext, Type typeAssignFrom);

  TypedAssignment toTypedAssignmentAst(TypeScope after);
}

package lambda.rodeo.lang.ast.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.Type;

public interface AssigmentAst {

  TypeScope scopeAfter(TypeScope before, CompileContext compileContext, Type typeAssignFrom);

  TypedAssignmentAst toTypedAssignmentAst(TypeScope after);
}

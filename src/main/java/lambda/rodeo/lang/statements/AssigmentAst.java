package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.Type;
import org.objectweb.asm.MethodVisitor;

public interface AssigmentAst {

  TypeScope scopeAfter(TypeScope before, CompileContext compileContext, Type typeAssignFrom);

  TypedAssignmentAst toTypedAssignmentAst(TypeScope after);
}

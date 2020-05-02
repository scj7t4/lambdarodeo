package lambda.rodeo.lang.typed.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import org.objectweb.asm.MethodVisitor;

public interface TypedAssignmentAst {
  void compile(MethodVisitor methodVisitor, CompileContext compileContext);
}

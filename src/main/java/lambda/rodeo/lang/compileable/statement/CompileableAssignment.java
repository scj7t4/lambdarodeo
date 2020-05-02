package lambda.rodeo.lang.compileable.statement;

import lambda.rodeo.lang.compilation.CompileContext;
import org.objectweb.asm.MethodVisitor;

public interface CompileableAssignment {
  void compile(MethodVisitor methodVisitor, CompileContext compileContext);
}

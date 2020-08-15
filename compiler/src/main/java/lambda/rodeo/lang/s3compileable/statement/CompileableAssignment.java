package lambda.rodeo.lang.s3compileable.statement;

import lambda.rodeo.lang.compilation.S1CompileContext;
import org.objectweb.asm.MethodVisitor;

public interface CompileableAssignment {
  void compile(MethodVisitor methodVisitor, S1CompileContext compileContext);
}

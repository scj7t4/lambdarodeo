package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.S1CompileContext;
import org.objectweb.asm.MethodVisitor;

public interface Compileable {

  void compile(MethodVisitor methodVisitor, S1CompileContext compileContext);
}

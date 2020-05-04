package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import org.objectweb.asm.MethodVisitor;

public interface Compileable {

  void compile(MethodVisitor methodVisitor, CompileContext compileContext);
}

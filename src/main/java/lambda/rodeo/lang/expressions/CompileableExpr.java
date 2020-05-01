package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.compilation.CompileContext;
import org.objectweb.asm.MethodVisitor;

public interface CompileableExpr {

  void compile(MethodVisitor methodVisitor, CompileContext compileContext);
}

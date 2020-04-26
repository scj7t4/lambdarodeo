package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.Type;
import org.objectweb.asm.MethodVisitor;

public interface ExpressionAst {

  Type getType();

  void compile(MethodVisitor methodVisitor,
      CompileContext compileContext);

}

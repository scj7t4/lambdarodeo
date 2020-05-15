package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import org.objectweb.asm.ClassWriter;

public interface LambdaLiftable {

  void lambdaLift(ClassWriter cw, CompileContext compileContext, String internalModuleName);
}

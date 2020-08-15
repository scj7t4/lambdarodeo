package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S1CompileContextImpl;
import lambda.rodeo.lang.compilation.S2CompileContext;
import org.objectweb.asm.ClassWriter;

public interface LambdaLiftable {

  void lambdaLift(ClassWriter cw, S2CompileContext compileContext, String internalModuleName);
}

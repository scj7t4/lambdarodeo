package lambda.rodeo.lang.scope;

import lambda.rodeo.lang.types.CompileableType;
import org.objectweb.asm.MethodVisitor;

public interface Entry {

  String getName();

  CompileableType getType();

  void compileLoad(MethodVisitor methodVisitor);

  void compileStore(MethodVisitor methodVisitor);
}

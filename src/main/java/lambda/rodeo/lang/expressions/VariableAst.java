package lambda.rodeo.lang.expressions;


import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import org.objectweb.asm.MethodVisitor;

@Builder
public class VariableAst implements ExpressionAst {

  private final String name;
  private final Type type;
  private final int index;

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    methodVisitor.visitVarInsn(ALOAD, index);
  }
}

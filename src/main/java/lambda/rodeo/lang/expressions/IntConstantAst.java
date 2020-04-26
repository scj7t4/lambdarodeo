package lambda.rodeo.lang.expressions;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import org.objectweb.asm.MethodVisitor;

@Builder
public class IntConstantAst implements ExpressionAst {

  private final String literal;

  @Override
  public Type getType(TypeScope typeScope) {
    return IntType.INSTANCE;
  }

  @Override
  public void compile(MethodVisitor methodVisitor, TypeScope typeScope) {
    methodVisitor.visitTypeInsn(NEW, "java/math/BigInteger"); // Start creating the new type
    methodVisitor.visitInsn(DUP); // Duplicate the new object on the stack
    methodVisitor.visitLdcInsn(literal); // Initialize a string constant for the the value...
    methodVisitor.visitMethodInsn(
        INVOKESPECIAL,
        "java/math/BigInteger",
        "<init>",
        "(Ljava/lang/String;)V",
        false); // Invoke the ctor... it maybe pops the object off the stack, that's why
    // we dup'd earlier??
  }
}

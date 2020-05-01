package lambda.rodeo.lang.expressions;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.IntType;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class IntConstantAst implements ExpressionAst, CompileableExpr {

  private final String literal;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpressionAst toTypedExpressionAst(TypeScope typeScope, CompileContext compileContext) {
    return SimpleTypedExpressionAst
        .builder()
        .type(IntType.INSTANCE)
        .compileableExpr(this::compile)
        .build();
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
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

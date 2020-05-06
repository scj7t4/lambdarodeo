package lambda.rodeo.lang.s1ast.expressions;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.IntType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class IntConstantAst implements ExpressionAst, Compileable {

  private final String literal;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public SimpleTypedExpression toTypedExpression() {
    return SimpleTypedExpression
        .builder()
        .expr(this)
        .type(IntType.INSTANCE)
        .toCompileable(() -> this)
        .build();
  }

  @Override
  public SimpleTypedExpression toTypedExpression(TypeScope typeScope,
      TypedModuleScope typedModuleScope, CompileContext compileContext) {
    return toTypedExpression();
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
package lambda.rodeo.lang.s1ast.expressions;
import static org.objectweb.asm.Opcodes.BASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.T_BYTE;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.IntType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
      TypedModuleScope typedModuleScope, ToTypedFunctionContext compileContext) {
    return toTypedExpression();
  }

  @Override
  public Set<String> getReferencedVariables() {
    return Collections.emptySet();
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      S1CompileContext compileContext) {
    byte[] asBytes = new BigInteger(literal).toByteArray();

    methodVisitor.visitTypeInsn(NEW, "java/math/BigInteger"); // Start creating the new type
    methodVisitor.visitInsn(DUP); // Duplicate the new object on the stack
    methodVisitor.visitLdcInsn(asBytes.length);
    methodVisitor.visitIntInsn(NEWARRAY, T_BYTE);
    for(int i = 0; i < asBytes.length; i++) {
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitLdcInsn(i);
      methodVisitor.visitIntInsn(BIPUSH, asBytes[i]);
      methodVisitor.visitInsn(BASTORE);
    }
    methodVisitor.visitMethodInsn(
        INVOKESPECIAL,
        Type.getInternalName(BigInteger.class),
        "<init>",
        "([B)V",
        false);
  }
}

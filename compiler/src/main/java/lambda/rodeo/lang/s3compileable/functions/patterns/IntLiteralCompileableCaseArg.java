package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.TypeReference.NEW;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.IntLiteralCaseArgAst;
import lambda.rodeo.lang.s2typed.functions.patterns.IntLiteralTypedCaseArg;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class IntLiteralCompileableCaseArg implements CompileableCaseArg {
  private final IntLiteralTypedCaseArg typedCaseArg;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext, int argIndex) {
    String intMatcher = Type.getInternalName(IntMatcher.class);
    methodVisitor.visitTypeInsn(NEW, intMatcher);
    methodVisitor.visitInsn(DUP);
    String intInternal = Type.getInternalName(BigInteger.class);
    methodVisitor.visitTypeInsn(NEW, intInternal);
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitLdcInsn(typedCaseArg.getCaseArgAst().getValue());
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        intInternal,
        "<init>",
        "(Ljava/lang/String;)V", false);
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        intMatcher,
        "<init>",
        "(Ljava/math/BigInteger;)V",
        false);
    methodVisitor.visitVarInsn(ALOAD, argIndex);
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        intMatcher,
        "matches",
        "(Ljava/lang/Object;)Z",
        false);
  }
}

package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.TypeReference.NEW;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.VariableCaseArgAst;
import lambda.rodeo.lang.s2typed.functions.patterns.VariableTypedCaseArg;
import lambda.rodeo.runtime.patterns.matchers.EqualMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class VariableCompileableCaseArg implements CompileableCaseArg {
  private final VariableTypedCaseArg typedCaseArg;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext, int argIndex) {
    String eqMatcher = Type.getInternalName(EqualMatcher.class);
    methodVisitor.visitTypeInsn(NEW, eqMatcher);
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitVarInsn(ALOAD, typedCaseArg.getReferencedArgEntry().getIndex());
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        eqMatcher,
        "<init>",
        "(Ljava/lang/Object;)V",
        false);
    methodVisitor.visitVarInsn(ALOAD, argIndex);
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        eqMatcher,
        "matches",
        "(Ljava/lang/Object;)Z",
        false);
  }
}

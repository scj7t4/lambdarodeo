package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.TypeReference.NEW;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.AtomTypedCaseArg;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.types.Atom;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class AtomCompileableCaseArg implements CompileableCaseArg {
  private final AtomTypedCaseArg typedCaseArg;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext, int argIndex) {
    Label label0 = new Label();
    final String atomMatcherInternal = Type.getInternalName(AtomMatcher.class);
    methodVisitor.visitTypeInsn(NEW, atomMatcherInternal);
    methodVisitor.visitInsn(DUP);
    final String atomInternal = Type.getInternalName(Atom.class);
    methodVisitor.visitTypeInsn(NEW, atomInternal);
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitLdcInsn(typedCaseArg.getCaseArgAst().getAtom());
    methodVisitor.visitMethodInsn(
        INVOKESPECIAL,
        atomInternal,
        "<init>",
        "(Ljava/lang/String;)V",
        false);
    methodVisitor.visitMethodInsn(
        INVOKESPECIAL,
        atomMatcherInternal,
        "<init>",
        "(Llambda/rodeo/runtime/types/Atom;)V",
        false);
    methodVisitor.visitVarInsn(ALOAD, argIndex);
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        atomMatcherInternal,
        "matches",
        "(Ljava/lang/Object;)Z",
        false);
  }
}

package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.TypeReference.NEW;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.AtomTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.runtime.patterns.matchers.AtomMatcher;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
import lambda.rodeo.runtime.types.Atom;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class AtomCompileableCaseArg implements CompileableCaseArg {
  @NonNull
  private final AtomTypedCaseArg typedCaseArg;
  @NonNull
  private final TypedStaticPattern staticPattern;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext,
      CompileableCaseArgContext caseArgContext) {
    final String atomMatcherInternal = Type.getInternalName(AtomMatcher.class);
    methodVisitor.visitFieldInsn(GETSTATIC,
        caseArgContext.getInternalModuleName(),
        staticPattern.getMatcherIdentifier(),
        Type.getDescriptor(AtomMatcher.class));
    methodVisitor.visitVarInsn(ALOAD, caseArgContext.getArgIndex());
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        atomMatcherInternal,
        "matches",
        "(Ljava/lang/Object;)Z",
        false);
  }

  @Override
  public void declareMatcherField(ClassWriter classWriter) {
    FieldVisitor fieldVisitor = classWriter.visitField(
        ACC_PUBLIC | ACC_FINAL | ACC_STATIC,
        "INT_MATCHER",
        Type.getDescriptor(AtomMatcher.class),
        null,
        null);
    fieldVisitor.visitEnd();
  }

  @Override
  public void matcherInit(MethodVisitor methodVisitor, CompileContext compileContext,
      String internalModuleName) {
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
    methodVisitor.visitFieldInsn(PUTSTATIC,
        internalModuleName,
        staticPattern.getMatcherIdentifier(),
        Type.getDescriptor(AtomMatcher.class));
  }

}

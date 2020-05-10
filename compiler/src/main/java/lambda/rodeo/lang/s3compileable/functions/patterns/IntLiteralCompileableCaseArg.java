package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTSTATIC;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.IntLiteralTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.runtime.patterns.matchers.IntMatcher;
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
public class IntLiteralCompileableCaseArg implements CompileableCaseArg {

  @NonNull
  private final IntLiteralTypedCaseArg typedCaseArg;
  @NonNull
  private final TypedStaticPattern staticPattern;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext,
      CompileableCaseArgContext caseArgContext) {
    String intMatcher = Type.getInternalName(IntMatcher.class);
    methodVisitor.visitFieldInsn(GETSTATIC,
        caseArgContext.getInternalModuleName(),
        staticPattern.getMatcherIdentifier(),
        Type.getDescriptor(IntMatcher.class));
    methodVisitor.visitVarInsn(ALOAD, caseArgContext.getArgIndex());
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        intMatcher,
        "matches",
        "(Ljava/lang/Object;)Z",
        false);
  }

  @Override
  public void declareMatcherField(ClassWriter classWriter) {
    FieldVisitor fieldVisitor = classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC,
        staticPattern.getMatcherIdentifier(),
        Type.getDescriptor(IntMatcher.class),
        null,
        null);
    fieldVisitor.visitEnd();
  }

  @Override
  public void matcherInit(
      MethodVisitor methodVisitor,
      CompileContext compileContext, String internalModuleName) {
    String type = Type.getInternalName(IntMatcher.class);
    methodVisitor.visitTypeInsn(NEW, type);
    methodVisitor.visitInsn(DUP);
    String intInternal = Type.getInternalName(BigInteger.class);
    methodVisitor.visitTypeInsn(NEW, intInternal);
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitLdcInsn(typedCaseArg.getCaseArgAst().getValue());
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        intInternal,
        "<init>",
        "(Ljava/lang/String;)V",
        false);
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        type,
        "<init>",
        "(Ljava/math/BigInteger;)V",
        false);
    methodVisitor.visitFieldInsn(PUTSTATIC,
        internalModuleName,
        staticPattern.getMatcherIdentifier(),
        Type.getDescriptor(IntMatcher.class));

  }
}

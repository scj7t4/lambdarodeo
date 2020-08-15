package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.VariableTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.runtime.patterns.matchers.EqualMatcher;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class VariableCompileableCaseArg implements CompileableCaseArg {
  @NonNull
  private final VariableTypedCaseArg typedCaseArg;
  @NonNull
  private final TypedStaticPattern staticPattern;

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext,
      CompileableCaseArgContext caseArgContext) {
    String eqMatcher = Type.getInternalName(EqualMatcher.class);
    methodVisitor.visitTypeInsn(NEW, eqMatcher);
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitVarInsn(ALOAD, typedCaseArg.getReferencedArgEntry().getIndex());
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        eqMatcher,
        "<init>",
        "(Ljava/lang/Object;)V",
        false);
    methodVisitor.visitVarInsn(ALOAD, caseArgContext.getArgIndex());
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        eqMatcher,
        "matches",
        "(Ljava/lang/Object;)Z",
        false);
  }

  @Override
  public void declareMatcherField(ClassWriter classWriter) {
    // Can't be declared statically
  }

  @Override
  public void matcherInit(MethodVisitor methodVisitor, S1CompileContext compileContext,
      String internalModuleName) {
    // Can't be declared statically
  }
}

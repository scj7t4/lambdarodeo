package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.util.List;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedObject;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRObjectSetter;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class CompileableObject implements CompileableExpr {

  @Builder
  @Getter
  public static class CompileableObjectEntry {
    private final CompileableExpr expr;
    private final String identifier;
  }

  @NonNull
  private final TypedObject from;

  @NonNull
  private final List<CompileableObjectEntry> members;

  @Override
  public TypedObject getTypedExpression() {
    return from;
  }

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(LRObject.class),
        "create",
        "()Llambda/rodeo/runtime/types/LRObject;",
        false);
    for (CompileableObjectEntry entry : members) {
      methodVisitor.visitLdcInsn(entry.getIdentifier());
      entry.getExpr().compile(methodVisitor, compileContext);
      entry.getExpr().getTypedExpression().getType().provideRuntimeType(methodVisitor);
      methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
          Type.getInternalName(LRObjectSetter.class),
          "set",
          "(Ljava/lang/String;Ljava/lang/Object;Llambda/rodeo/runtime/types/LRType;)Llambda/rodeo/runtime/types/LRObjectSetter;",
          false);
    }
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        Type.getInternalName(LRObjectSetter.class),
        "done",
        "()Llambda/rodeo/runtime/types/LRObject;",
        false);

  }
}

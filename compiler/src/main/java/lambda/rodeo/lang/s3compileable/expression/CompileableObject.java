package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.List;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedObject;
import lambda.rodeo.lang.s2typed.expressions.TypedObject.TypedObjectMember;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRObjectSetter;
import lambda.rodeo.runtime.types.LRObjectSetterImpl;
import lambda.rodeo.runtime.types.LRType;
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
  public static class CompileableObjectMember {

    @NonNull
    private final CompileableExpr expr;
    @NonNull
    private final String identifier;
    private final TypedObjectMember from;
  }

  @NonNull
  private final TypedObject from;

  @NonNull
  private final List<CompileableObjectMember> members;

  @Override
  public TypedObject getTypedExpression() {
    return from;
  }

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(LRObject.class),
        "create",
        FunctionDescriptorBuilder.args().returns(LRObject.class),
        false);
    for (CompileableObjectMember entry : members) {
      methodVisitor.visitLdcInsn(entry.getIdentifier());
      entry.getExpr().compile(methodVisitor, compileContext);
      entry.getExpr().getTypedExpression().getType().provideRuntimeType(methodVisitor);
      methodVisitor.visitMethodInsn(INVOKEINTERFACE,
          Type.getInternalName(LRObjectSetter.class),
          "set",
          FunctionDescriptorBuilder
              .args(String.class, Object.class, LRType.class)
              .returns(LRObjectSetterImpl.class),
          true);
    }
    if (!members.isEmpty()) {
      methodVisitor.visitMethodInsn(INVOKEINTERFACE,
          Type.getInternalName(LRObjectSetter.class),
          "done",
          FunctionDescriptorBuilder
              .args()
              .returns(LRObject.class),
          true);
    }
  }
}

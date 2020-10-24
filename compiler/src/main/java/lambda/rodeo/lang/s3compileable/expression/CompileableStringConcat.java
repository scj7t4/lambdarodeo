package lambda.rodeo.lang.s3compileable.expression;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedStringConcat;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Builder
@Getter
public class CompileableStringConcat implements CompileableExpr {
  private final TypedStringConcat typedExpression;
  private final CompileableExpr lhs;
  private final CompileableExpr rhs;

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    lhs.compile(methodVisitor, compileContext);
    rhs.compile(methodVisitor, compileContext);
    String invokeDescriptor = "("
        + lhs.getTypedExpression().getType().getDescriptor()
        + rhs.getTypedExpression().getType().getDescriptor()
        + ")" + typedExpression.getType().getDescriptor();
    methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants",
        invokeDescriptor,
        new Handle(Opcodes.H_INVOKESTATIC,
            "java/lang/invoke/StringConcatFactory",
            "makeConcatWithConstants",
            FunctionDescriptorBuilder.args(
                MethodHandles.Lookup.class,
                String.class,
                MethodType.class,
                String.class,
                Object[].class
            ).returns(CallSite.class),
            false),
        "\u0001\u0001");
  }
}

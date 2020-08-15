package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedStringConcat;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Builder
@Getter
public class CompileableStringConcat implements CompileableExpr {
  private final TypedStringConcat typedExpression;
  private final CompileableExpr lhs;
  private final CompileableExpr rhs;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
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
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;",
            false),
        "\u0001\u0001");
  }
}

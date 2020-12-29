package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedStringConcat;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.fn.StringFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
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
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(StringFunctions.class), "makeConcat",
        FunctionDescriptorBuilder.args(Lambda0.class, Lambda0.class)
            .returns(Lambda0.class), false);
  }
}

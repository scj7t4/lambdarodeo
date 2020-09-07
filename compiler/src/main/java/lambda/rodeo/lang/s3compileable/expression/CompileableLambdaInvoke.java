package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;

import java.util.List;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedLambdaInvoke;
import lambda.rodeo.lang.types.CompileableLambdaType;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class CompileableLambdaInvoke implements CompileableExpr, LambdaLiftable {
  private final TypedLambdaInvoke typedExpression;
  private final List<CompileableExpr> args;
  private final CompileableExpr invokeTarget;

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    // Load the lambda object onto the stack:
    invokeTarget.compile(methodVisitor, compileContext);
    // Then load all its args:
    for(CompileableExpr arg : args) {
      arg.compile(methodVisitor, compileContext);
    }
    // Then invoke the lambda:
    CompileableLambdaType lambda = typedExpression.getLambda();
    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
        Type.getInternalName(lambda.functionalRep()),
        "apply",
        lambda.getGenericFunctionDescriptor(),
        true);
    // And check to make sure it returns what it says it would.
    methodVisitor.visitTypeInsn(CHECKCAST, lambda.getReturnType().getInternalName());
  }


  @Override
  public void lambdaLift(ClassWriter cw, S2CompileContext compileContext, String internalModuleName) {
    if(invokeTarget instanceof LambdaLiftable) {
      ((LambdaLiftable) invokeTarget).lambdaLift(cw, compileContext, internalModuleName);
    }
  }
}

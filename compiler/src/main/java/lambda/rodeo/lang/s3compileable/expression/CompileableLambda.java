package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedLambda;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class CompileableLambda implements CompileableExpr, LambdaLiftable {
  private final TypedLambda typedExpression;
  private final CompileableFunction lambdaFunction;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    // Bunch of invoke dynamic stuff here...
  }

  @Override
  public void lambdaLift(ClassWriter cw, CompileContext compileContext, String internalModuleName) {
    lambdaFunction.compile(cw, compileContext, internalModuleName);
    lambdaFunction.lambdaLift(cw, compileContext, internalModuleName);
  }
}

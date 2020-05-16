package lambda.rodeo.lang.s3compileable.expression;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedLambdaInvoke;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class CompileableLambdaInvoke implements CompileableExpr {
  private final TypedLambdaInvoke typedExpression;
  private final List<CompileableExpr> args;
  private final CompileableExpr invokeTarget;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    for(CompileableExpr arg : args) {
      arg.compile(methodVisitor, compileContext);
    }
    invokeTarget.compile(methodVisitor, compileContext);
    //TODO: Now call the apply method on this lambda:
  }
}

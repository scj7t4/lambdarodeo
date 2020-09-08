package lambda.rodeo.lang.s3compileable.statement;

import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpression;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.s3compileable.expression.LambdaLiftable;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableStatement implements CompileableExpression {

  private final TypedStatement typedStatement;
  private final CompileableTypeScope beforeTypeScope;
  private final CompileableTypeScope afterTypeScope;
  private final CompileableExpr compileableExpr;
  private final CompileableAssignment compileableAssignment;


  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    compileableExpr.compile(methodVisitor,
        compileContext); // So this should hopefully mean that the result of the
    // computation is on the top of the stack...
    if (compileableAssignment != null) {
      compileableAssignment.compile(methodVisitor, compileContext);
    }
  }

  public void lambdaLift(ClassWriter cw, S2CompileContext compileContext, String internalJavaName) {
    if(compileableExpr instanceof LambdaLiftable) {
      ((LambdaLiftable) compileableExpr).lambdaLift(cw, compileContext, internalJavaName);
    }
  }
}

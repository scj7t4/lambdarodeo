package lambda.rodeo.lang.s3compileable.statement;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableStatement implements Compileable {

  private final TypedStatement typedStatement;
  private final CompileableTypeScope beforeTypeScope;
  private final CompileableTypeScope afterTypeScope;
  private final CompileableExpr compileableExpr;
  private final CompileableAssignment compileableAssignment;


  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    compileableExpr.compile(methodVisitor,
        compileContext); // So this should hopefully mean that the result of the
    // computation is on the top of the stack...
    if (compileableAssignment != null) {
      compileableAssignment.compile(methodVisitor, compileContext);
    }
  }
}
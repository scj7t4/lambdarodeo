package lambda.rodeo.lang.compileable.statement;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.typed.statements.TypedStatement;
import lambda.rodeo.lang.types.CompileableTypeScope;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
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

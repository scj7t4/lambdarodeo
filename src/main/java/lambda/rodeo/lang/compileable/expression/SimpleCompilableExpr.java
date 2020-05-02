package lambda.rodeo.lang.compileable.expression;


import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class SimpleCompilableExpr<T extends Compileable & TypedExpression>
    implements CompileableExpr {
  T typedExpression;

  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    typedExpression.compile(methodVisitor, compileContext);
  }
}

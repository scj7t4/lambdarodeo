package lambda.rodeo.lang.s3compileable.expression;


import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class SimpleCompilableExpr implements CompileableExpr {

  @NonNull
  TypedExpression typedExpression;
  @NonNull
  private final Compileable compileable;

  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    compileable.compile(methodVisitor, compileContext);
  }
}

package lambda.rodeo.lang.compileable.expression;


import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
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

  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    compileable.compile(methodVisitor, compileContext);
  }
}
